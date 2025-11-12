package com.bos.payment.appName.ui.view.makepayment.fragment

import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.constant.ConstantClass.saveImageToCache
import com.bos.payment.appName.data.model.makepaymentnew.RaiseMakePaymentReq
import com.bos.payment.appName.data.model.makepaymentnew.ReferenceIDGenerateReq
import com.bos.payment.appName.data.model.supportmanagement.AddCommentReq
import com.bos.payment.appName.data.model.transactionreportsmodel.RaiseTicketReq
import com.bos.payment.appName.data.model.transactionreportsmodel.ReportListReq
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.databinding.FragmentRaiseMakePaymentBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.makepayment.MakePaymentActivity
import com.bos.payment.appName.ui.view.supportmanagement.ChatToAdminActivity.Companion.commentId
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.Constants.BankName
import com.bos.payment.appName.utils.Constants.isInternetAvailable
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.google.gson.Gson
import com.google.type.DateTime
import org.bouncycastle.jcajce.provider.asymmetric.GOST
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RaiseMakePayment : Fragment() {
    lateinit var binding:FragmentRaiseMakePaymentBinding
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    lateinit var pd: ProgressDialog
    private var mStash: MStash? = null
    lateinit var referenceID : String
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    var displayBankNameList : MutableList<String?> = arrayListOf()
    var displaybankvalueList : MutableList<String?> = arrayListOf()
    var transferMode : MutableList<String?> = arrayListOf()
    var photoUri: Uri? = null
    var imagepath: String? = ""
    var branchvalue: String = ""


    companion object {

    }

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Handle the photoUri, e.g., show image in ImageView
            binding.receiptPhoto.visibility = View.VISIBLE
            binding.cameraicon.visibility = View.GONE
            binding.receiptPhoto.setImageURI(photoUri)
            binding.clicktosealphoto1.text="Re-Send"
            imagepath = saveImageToCache(requireContext(),photoUri!!,"ReceiptPhoto")!!.absolutePath
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRaiseMakePaymentBinding.inflate(layoutInflater, container, false)

        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface))
        )[GetAllApiServiceViewModel::class.java]

        mStash = MStash.getInstance(requireContext())
        pd = ProgressDialog(requireContext())

        setTransferModepinner()
        hitApiForReferenceID()
        hitapiforBankList()
        setonclicklistner()

        return binding.root
    }

    private fun setonclicklistner(){

        binding.depositbanklist.onItemSelectedListener = object : OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(displaybankvalueList.isNotEmpty()){
                    branchvalue = displaybankvalueList[position]!!.trim()
                    hitapiforBankBranchName(branchvalue)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        binding.transfermode.onItemSelectedListener = object : OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var mode = binding.transfermode.selectedItem.toString().trim()
                if(mode.equals("Cash Deposit",ignoreCase = true)){
                    binding.transactionidlayout.visibility=View.GONE
                    binding.uploadpiclayout.visibility=View.GONE
                }
                else{
                        binding.transactionidlayout.visibility=View.VISIBLE
                        binding.uploadpiclayout.visibility=View.VISIBLE
                    }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        binding.amountetxtve.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val input = s.toString()

                if (input.contains(".")) {
                    val decimalIndex = input.indexOf(".")
                    val digitsAfterDecimal = input.length - decimalIndex - 1

                    if (digitsAfterDecimal > 2) {
                        // Trim extra digits
                        val trimmed = input.substring(0, decimalIndex + 3)
                        binding.amountetxtve.setText(trimmed)
                        binding.amountetxtve.setSelection(trimmed.length) // move cursor to end
                    }
                }
            }
        })

        binding.clicktosealphoto1.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        binding.confirmbutton.setOnClickListener {
            if (!isInternetAvailable(requireContext())) {
                Toast.makeText(requireContext(), "No internet connection", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val amountToBeTransfer = binding.amountetxtve.text.toString().trim()
            val transactionId = binding.transactionIdetv.text.toString().trim()
            val branchCode = binding.branchcodetxt.text.toString().trim()
            val remarks = binding.remarksetxtev.text.toString().trim()
            val transferMode = binding.transfermode.selectedItem.toString()

            // ✅ Validate branch code
            if (branchCode.isEmpty()) {
                Toast.makeText(requireContext(), "Branch code not found", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Validate amount or transaction based on transfer mode
            if (transferMode.equals(Constants.CashDeposit, ignoreCase = true)) {
                if (amountToBeTransfer.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter paid amount", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } else {
                if (transactionId.isEmpty()) {
                    Toast.makeText(requireContext(), "Please enter transaction ID", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (imagepath.isNullOrBlank()) {
                    Toast.makeText(requireContext(), "Please upload receipt photo", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // ✅ Validate remarks
            if (remarks.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter remarks", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ All validations passed — proceed with API call
            HitApiForTransferAmountToAdminAccount()
        }


    }

    private fun HitApiForTransferAmountToAdminAccount(){
        val transferMode = binding.transfermode.selectedItem.toString()
        var userCode = mStash!!.getStringValue(Constants.RegistrationId, "").toString()
        var adminCode = mStash!!.getStringValue(Constants.AdminCode, "").toString()
        val currentDateTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

        if (transferMode.equals(Constants.CashDeposit, ignoreCase = true)) {
            val request = RaiseMakePaymentReq(
                Mode = "ADD",
                RID = "",
                RefrenceID = referenceID,
                PaymentMode = binding.transfermode.selectedItem.toString().trim(),
                PaymentDate = currentDateTime,
                DepositBankName = branchvalue,
                BranchCode_ChecqueNo = binding.branchcodetxt.text.toString().trim(),
                Remarks = binding.remarksetxtev.text.toString().trim(),
                TransactionID = binding.transactionIdetv.text.toString().trim(),
                DocumentPath = "",
                RecordDateTime = currentDateTime,
                UpdatedBy = userCode,
                UpdatedOn = currentDateTime,
                ApprovedBy = "",
                ApprovedDateTime = "",
                ApporvedStatus = "Pending",
                RegistrationId = userCode,
                ApporveRemakrs = "",
                Amount = binding.amountetxtve.text.toString().trim(),
                CompanyCode = "",
                BeneId = "",
                AccountHolder = "",
                PaymentType = "Makepement",
                Flag = "Y",
                AdminCode = adminCode,
                imagefile1 = null
            )

            Log.d("RaiseMakePaymentReq", Gson().toJson(request))

            getAllApiServiceViewModel.RaisMakePaymentReq(request).observe(requireActivity()) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    Log.d("RaiseMakePaymentResp", Gson().toJson(response))
                                    pd.dismiss()
                                    if (response.isSuccess!!) {
                                        clearData()
                                        Toast.makeText(requireContext(), response.returnMessage, Toast.LENGTH_SHORT).show()

                                    } else {
                                        Toast.makeText(requireContext(), response.returnMessage, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }

                    }
                }
            }

        }
        else{
            val imageFile1 = saveImageToCache(requireContext(),photoUri!!, "image1.jpg")

            val request = RaiseMakePaymentReq(
                Mode = "ADD",
                RID = "",
                RefrenceID = referenceID,
                PaymentMode = binding.transfermode.selectedItem.toString().trim(),
                PaymentDate = currentDateTime,
                DepositBankName = branchvalue,
                BranchCode_ChecqueNo = binding.branchcodetxt.text.toString().trim(),
                Remarks = binding.remarksetxtev.text.toString().trim(),
                TransactionID = binding.transactionIdetv.text.toString().trim(),
                DocumentPath = imageFile1!!.absolutePath.toString(),
                RecordDateTime = currentDateTime,
                UpdatedBy = userCode,
                UpdatedOn = currentDateTime,
                ApprovedBy = "",
                ApprovedDateTime = "",
                ApporvedStatus = "Pending",
                RegistrationId = userCode,
                ApporveRemakrs = "",
                Amount = binding.amountetxtve.text.toString().trim(),
                CompanyCode = "",
                BeneId = "",
                AccountHolder = "",
                PaymentType = "Makepement",
                Flag = "Y",
                AdminCode = adminCode,
                imagefile1 = imageFile1
            )

            Log.d("RaiseMakePaymentReq", Gson().toJson(request))

            getAllApiServiceViewModel.RaisMakePaymentReq(request).observe(requireActivity()) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    Log.d("RaiseMakePaymentResp", Gson().toJson(response))
                                    pd.dismiss()
                                    if (response.isSuccess!!) {
                                        clearData()
                                        hitApiForReferenceID()
                                        Toast.makeText(requireContext(), response.returnMessage, Toast.LENGTH_SHORT).show()

                                    } else {
                                        Toast.makeText(requireContext(), response.returnMessage, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }

                    }
                }
            }
        }


    }

    private fun setTransferModepinner(){
        transferMode.clear()
        transferMode.add("Cash Deposit")
        transferMode.add("UPI")
        transferMode.add("NEFT")
        transferMode.add("RTGS")
        transferMode.add("IMPS")
        transferMode.add("QR")
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, transferMode)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.transfermode.adapter = adapter
    }

    private fun setBankInSpinner(){
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, displayBankNameList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.depositbanklist.adapter = adapter

        displaybankvalueList.forEachIndexed { index, item ->
            val bankName = MakePaymentActivity.BankName ?: ""
            val bankAccount = MakePaymentActivity.BankAccountNumber ?: ""

            if (item.toString().contains(bankName, ignoreCase = true) || item.toString().contains(bankAccount, ignoreCase = true)) {
                binding.depositbanklist.setSelection(index)
                return@forEachIndexed // stop once a match is found
            }
        }

    }

    fun hitApiForReferenceID(){

        var req = ReferenceIDGenerateReq(
            seriesCode= "SR00000004",

        )

        getAllApiServiceViewModel.getReferenceIdReq(req).observe(requireActivity()) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("referenceIDResp", Gson().toJson(response))
                                if(response.isSuccess!!){
                                    referenceID = response.data!!.seriesCode!!
                                } else{
                                    Toast.makeText(requireContext(),response.returnMessage, Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        pd.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        pd.show()
                    }

                }
            }
        }

    }

    private fun hitapiforBankList() {

            val bankReq = ReportListReq(
                pParmFlag = Constants.AdminBank ,
                pParmFlag1 = mStash!!.getStringValue(Constants.AdminCode, ""),
                pParmFlag2 = ""
            )
            Log.d("BankReq",Gson().toJson(bankReq))
            getAllApiServiceViewModel.sendForReportListReq(bankReq)
                .observe(requireActivity()) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        Log.d("BankResp",Gson().toJson(response))
                                        if(response.isSuccess!!){
                                            pd.dismiss()
                                            displayBankNameList.clear()
                                            displaybankvalueList.clear()

                                            response.data!!.forEach { unit->
                                                displayBankNameList.add(unit!!.displayText)
                                                displaybankvalueList.add(unit!!.displayValue)
                                            }

                                            if( response.data.size==displayBankNameList.size){
                                                setBankInSpinner()
                                            }

                                        }
                                    }
                                }
                            }

                            ApiStatus.ERROR -> {
                                pd.dismiss()
                            }

                            ApiStatus.LOADING -> {
                                pd.show()
                            }
                        }
                    }
                }

    }

    private fun hitapiforBankBranchName(bankdisplayvalue: String) {

        val bankReq = ReportListReq(
            pParmFlag = Constants.BranchNamee ,
            pParmFlag1 = bankdisplayvalue,
            pParmFlag2 = ""
        )
        Log.d("bankbranchnamereq",Gson().toJson(bankReq))
        getAllApiServiceViewModel.sendForReportListReq(bankReq)
            .observe(requireActivity()) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    Log.d("bankbranchnameresp",Gson().toJson(response))
                                    if(response.isSuccess!!){
                                        pd.dismiss()
                                        if(response.data!!.isNotEmpty()){
                                            binding.branchcodetxt.text = response.data[0]!!.displayText
                                        }
                                    }
                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }
                    }
                }
            }

    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickCameraForUploadDocument()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE_FRONT)
        }
    }

    fun clickCameraForUploadDocument() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", photoFile)
        cameraLauncher.launch(photoUri!!)
    }

    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    private fun clearData(){
        binding.amountetxtve.text.clear()
        binding.remarksetxtev.text.clear()
        binding.transactionIdetv.text.clear()
        binding.depositbanklist.setSelection(0)
        binding.transfermode.setSelection(0)
        binding.amountetxtve.text.clear()
        binding.receiptPhoto.visibility = View.GONE
        binding.cameraicon.visibility = View.VISIBLE
        imagepath = ""
        photoUri=null
        binding.clicktosealphoto1.text="Send"
    }



}