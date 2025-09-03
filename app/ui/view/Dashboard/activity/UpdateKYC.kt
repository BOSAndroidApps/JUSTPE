package com.bos.payment.appName.ui.view.Dashboard.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.data.model.kyc.ReteriveAgentKYCReq
import com.bos.payment.appName.data.model.kyc.ReteriveAgentKYCRes
import com.bos.payment.appName.data.model.kyc.UpdateKYCReq
import com.bos.payment.appName.data.model.kyc.UpdateKYCRes
import com.bos.payment.appName.data.model.stateDistrict.GetStateRes
import com.bos.payment.appName.data.repository.ServiceChangeRepository
import com.bos.payment.appName.data.viewModelFactory.ServiceChangeViewModelFactory
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.viewmodel.ServiceChangeViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.hideKeyboard
import com.bos.payment.appName.utils.Utils.toast
import com.bos.payment.appName.utils.imageCompressor.Compressor
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityUpdateKycBinding
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.Calendar
import java.util.GregorianCalendar

class UpdateKYC : AppCompatActivity() {
    private lateinit var bin: ActivityUpdateKycBinding
    private lateinit var viewModel: ServiceChangeViewModel
    private var mStash: MStash? = null
    private var mCapturedImageURI: Uri? = null
    private var flag: Int = 0
    private val myCalender = Calendar.getInstance()
    private val UPDATE_REQ_CODE = 267
    private val requestOption: RequestOptions? = null
    private lateinit var pd: androidx.appcompat.app.AlertDialog

    private var builder: AlertDialog.Builder? = null
    private val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        view.maxDate = System.currentTimeMillis()
        myCalender.add(Calendar.YEAR, -18)
        myCalender[Calendar.YEAR] = year
        myCalender[Calendar.MONTH] = monthOfYear
        myCalender[Calendar.DAY_OF_MONTH] = dayOfMonth
        val minAdultAge: Calendar = GregorianCalendar()
        val minAdultAge1: Calendar = GregorianCalendar()
        minAdultAge.add(Calendar.YEAR, -18)
        minAdultAge1.add(Calendar.YEAR, -61)
        when {
            minAdultAge.before(myCalender) -> {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.min_age_person),
                    Toast.LENGTH_LONG
                ).show()
                bin.dobKYC.setText("")
            }

            minAdultAge1.after(myCalender) -> {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.max_age_person),
                    Toast.LENGTH_LONG
                ).show()
                bin.dobKYC.setText("")
            }

            else -> {
                bin.dobKYC.let { Utils.updateLabel(it, myCalender, "Update KYC") }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityUpdateKycBinding.inflate(layoutInflater)
        setContentView(bin.root)
        initView()
        dropDown()
//        validateRegistrationID()
        stateListAPI()
        btnListener()

    }

    private fun stateListAPI() {
        viewModel.getState().observe(this) { resource ->
            resource.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { response ->
                            response.body()?.let { stateList ->
                                // Process the state list here
                                for (state in stateList) {
                                    // Assuming each state is an object with properties
                                    val stateName = state.stateName
                                    val stateId = state.RID
                                    Log.d("StateList", "State ID: $stateId, Name: $stateName")
                                }
                                getStateRes(stateList)
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        // Handle error
                        Log.e("StateListAPI", "Error fetching state list")
                    }

                    ApiStatus.LOADING -> {
                        // Handle loading state
                        Log.d("StateListAPI", "Loading state list...")
                    }
                }
            }
        }
    }

    // Assuming getStateRes is a method to handle the list data
    private fun getStateRes(stateList: List<GetStateRes>) {
        // Implementation here
    }

    private fun dropDown() {
        /****************************************  Vendor_Name_Agency_spinner   */
//        Constants.state_adapter = ArrayAdapter(
//            this,
//            R.layout.spinner_item_selected,
//            Constants.stateName
//        )
//        Constants.state_adapter!!.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
//        bin.stateKYC.adapter = Constants.state_adapter
//
//        bin.stateKYC.onItemSelectedListener = object :
//            AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
//                if (pos > 0) {
//                    try {
//                        vendorNameTxt = parent.getItemAtPosition(pos).toString()
//                        Log.d("vendor_name_txt", vendorNameTxt!!)
////                        if (MCons.IS_EDIT_MODE != 0) {
////                            binding.vendorNameAgencyNameSpinner.setBackgroundResource(R.drawable.spinner_bg)
////                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(this@UpdateKYC, "error:86", Toast.LENGTH_SHORT).show()
//                        e.printStackTrace()
//                    }
//                } else {
//                    vendorNameTxt = null
//                }
//            }
//
//            override fun onNothingSelected(adapterView: AdapterView<*>?) {
//                // Handle case where nothing is selected, if needed
//            }
//        }
//        Constants.stateAdapter.notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        pd = PD(this@UpdateKYC)
        builder = AlertDialog.Builder(this)
        mStash = MStash.getInstance(this)!!
//        bin.mainManu.title.text = "Update KYC Details"
        viewModel = ViewModelProvider(
            this,
            ServiceChangeViewModelFactory(ServiceChangeRepository(RetrofitClient.apiAllInterface))
        )[ServiceChangeViewModel::class.java]

        bin.registrationID.setText(mStash!!.getStringValue(Constants.RegistrationId, ""))
//        val textWatcher: TextWatcher = object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
////                toast("text is edited and onTextChangedListener is called.")
//            }
//
//            override fun afterTextChanged(s: Editable) {
//                validateRegistrationID()
//            }
//        }
//        bin.registrationID.addTextChangedListener(textWatcher)

//        val imageUrl = mStash!!.getStringValue(Constants.CompanyLogo, "")
//        Picasso.get()
//            .load(imageUrl)
////            .placeholder(R.drawable.placeholder)  // Optional: placeholder while loading
//            .error(R.drawable.ic_error)        // Optional: error image if load fails
//            .into(bin.mainManu.dashboardImage)
    }

    private fun validateRegistrationID() {

        val reteriveAgentKYCReq =
            ReteriveAgentKYCReq(
                RegistrationID = mStash!!.getStringValue(Constants.RegistrationId, ""),
                CompanyCode = mStash!!.getStringValue(Constants.CompanyCode,""))
        viewModel.reteriveData(reteriveAgentKYCReq).observe(this) { resource ->
            resource.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            pd.dismiss()
//                            Log.d(TAG, "validateRegistrationID:2222 ${users.body()}")
                            users.body().let { it1 -> reteriveDataRes(it1?.get(0)) }
                        }
                    }

                    ApiStatus.ERROR -> {
                        pd.dismiss()
//                        toast("something went wrong")
                    }

                    ApiStatus.LOADING -> {
                        pd.show()
//                        toast("please loading")
                    }
                }
            }

        }
    }

    private fun reteriveDataRes(response: ReteriveAgentKYCRes?) {
//        if (!response!!.AgentType.isNullOrBlank()) {
        if (response!!.Status == true) {
            pd.dismiss()
            bin.agentType.setText(response.AgentType.toString())
            bin.referenceID.setText(response.RefrenceID.toString())
            bin.referenceTypeKYC.setText(response.RefrenceType.toString())
            bin.agentNameKYC.setText(response.AgencyName.toString())
            bin.panCardNoKYC.setText(response.PanCardNumber.toString())
            bin.firstNameKYC.setText(response.FirstName.toString())
            bin.lastNameKYC.setText(response.LastName.toString())
            bin.dobKYC.setText(response.DOB.toString())
            bin.emailIdKYC.setText(response.EmailID.toString())
            bin.mobileNoKYC.setText(response.MobileNo.toString())
            bin.alternateNoKYC.setText(response.AlternateMobileNo.toString())
            bin.permanentAddKYC.setText(response.PermanentAddress.toString())
            bin.businessTypeKYC.setText(response.BusinessType.toString())
            bin.officeAddKYC.setText(response.OfficeAddress.toString())
            bin.stateKYC.setText(response.State.toString())
            bin.districtKYC.setText(response.District.toString())
            bin.cityKYC.setText(response.City.toString())
            bin.pinCodeKYC.setText(response.Pincode.toString())
            bin.aadharNoKYC.setText(response.AddharCardNo.toString())
            bin.gstNoKYC.setText(response.GSTNO.toString())
            bin.websiteKYC.setText(response.WebSite.toString())
            bin.accountHolderNameKYC.setText(response.AccountHolderName.toString())
            bin.backNameKYC.setText(response.BankName.toString())
            bin.branchNameKYC.setText(response.BranchName.toString())
            bin.accountNoKYC.setText(response.AccountNo.toString())
            bin.accountTypeKYC.setText(response.AccountType.toString())
            bin.ifdcCodeKYC.setText(response.IFSCCode.toString())
            if (response.UploadPanCard != null) {
                requestOption?.let {
                    Utils.setImgGlideLib(
                        response.UploadPanCard!!, bin.panImage, this,
                        it
                    )
                }
            }
            if (response.UploadAddharCardFront != null) {
                requestOption?.let {
                    Utils.setImgGlideLib(
                        response.UploadAddharCardFront!!, bin.aadharFrontImage, this,
                        it
                    )
                }
            }
            if (response.UploadAddharCardBack != null) {
                requestOption?.let {
                    Utils.setImgGlideLib(
                        response.UploadAddharCardBack!!, bin.aadharBackImage, this,
                        it
                    )
                }
            }
            Constants.uploadImage = response.UploadPhoto.toString()
            Log.d("uploadImage", Constants.uploadImage)

            if (response.UploadPhoto != null) {
                requestOption?.let {
                    Utils.setImgGlideLib(
                        response.UploadPhoto!!, bin.photoImage, this,
                        it
                    )

                }
            }
            if (response.UploadOtherProof != null) {
                requestOption?.let {
                    Utils.setImgGlideLib(
                        response.UploadOtherProof!!, bin.otherImage, this,
                        it
                    )

                }
            }


            mStash!!.setStringValue(Constants.ReferenceId, response.RefrenceID.toString())
            mStash!!.setStringValue(Constants.ReferenceType, response.RefrenceType.toString())
            mStash!!.setStringValue(Constants.PanCardNo, response.PanCardNumber.toString())
            mStash!!.setStringValue(Constants.FirstName, response.FirstName.toString())
            mStash!!.setStringValue(Constants.LastName, response.LastName.toString())
            mStash!!.setStringValue(Constants.DOB, response.DOB.toString())
            mStash!!.setStringValue(Constants.EmailID, response.EmailID.toString())
            mStash!!.setStringValue(Constants.MobileNumber, response.MobileNo.toString())
            mStash!!.setStringValue(
                Constants.AlternateMobileNumber,
                response.AlternateMobileNo.toString()
            )
            mStash!!.setStringValue(
                Constants.PermanentAddress,
                response.PermanentAddress.toString()
            )
            mStash!!.setStringValue(Constants.BusinessType, response.BusinessType.toString())
            mStash!!.setStringValue(Constants.OfficeAddress, response.OfficeAddress.toString())
            mStash!!.setStringValue(Constants.Status, response.Status.toString())
            mStash!!.setStringValue(Constants.State, response.State.toString())
            mStash!!.setStringValue(Constants.District, response.District.toString())
            mStash!!.setStringValue(Constants.City, response.City.toString())
            mStash!!.setStringValue(Constants.PinCode, response.Pincode.toString())
            mStash!!.setStringValue(Constants.AadharCardNo, response.AddharCardNo.toString())
            mStash!!.setStringValue(Constants.GSTNo, response.GSTNO.toString())
            mStash!!.setStringValue(Constants.Website, response.WebSite.toString())
            mStash!!.setStringValue(
                Constants.AccountHolderName,
                response.AccountHolderName.toString()
            )
            mStash!!.setStringValue(Constants.BankName, response.BankName.toString())
            mStash!!.setStringValue(Constants.BranchName, response.BranchName.toString())
            mStash!!.setStringValue(Constants.AccountNumber, response.AccountNo.toString())
            mStash!!.setStringValue(Constants.AccountType, response.AccountType.toString())
            mStash!!.setStringValue(Constants.IFSCCODE, response.IFSCCode.toString())
            mStash!!.setStringValue(Constants.PanCardPic, response.UploadPanCard.toString())
            mStash!!.setStringValue(
                Constants.AadharFront,
                response.UploadAddharCardFront.toString()
            )
            mStash!!.setStringValue(Constants.AadharBack, response.UploadAddharCardBack.toString())
            mStash!!.setStringValue(Constants.Photo, response.UploadPhoto.toString())

            toast(response.message.toString())
        } else {
            Toast.makeText(this, response.message.toString(), Toast.LENGTH_SHORT).show()
//            toast(response.message.toString())
        }
    }

    private fun btnListener() {
        bin.proceedBtn.setOnClickListener {
            validationKYC()
        }
        bin.tvBtnCancel.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }

        bin.mainManu.dashboardImage.setOnClickListener {
            startActivity(Intent(this@UpdateKYC, DashboardActivity::class.java))

        }
        bin.dobKYC.setOnClickListener {
            hideKeyboard(this)
            DatePickerDialog(
                this, date, myCalender[Calendar.YEAR],
                myCalender[Calendar.MONTH],
                myCalender[Calendar.DAY_OF_MONTH]
            ).show()
        }
        bin.panImageBtn.setOnClickListener {
            flag = Constants.OWNER_PAN_CARD
            Log.d(TAG, "btnListener: " + "panImageBtn")
            show_image_upload_option()
        }
        bin.aadharFrontImageBtn.setOnClickListener {
            flag = Constants.OWNER_AADHAR_FRONT
            show_image_upload_option()
        }
        bin.aadharBackImageBtn.setOnClickListener {
            flag = Constants.OWNER_AADHAR_BACK
            show_image_upload_option()
        }
        bin.photoImageBtn.setOnClickListener {
            flag = Constants.OWNER_PHOTO
            show_image_upload_option()
        }
        bin.otherImageBtn.setOnClickListener {
            flag = Constants.OTHER_PHOTO
            show_image_upload_option()
        }
    }

    private fun validationKYC() {
        if (bin.registrationID.text.isNullOrBlank()) {
            toast("Enter registration ID")
            bin.registrationID.requestFocus()
        } else if (bin.agentType.text.isNullOrBlank()) {
            toast("Enter agent type")
            bin.agentType.requestFocus()
        } else if (bin.referenceID.text.isNullOrBlank()) {
            toast("Enter reference Id")
            bin.referenceID.requestFocus()
        } else if (bin.referenceTypeKYC.text.isNullOrBlank()) {
            toast("Enter reference type")
            bin.referenceTypeKYC.requestFocus()
        } else if (bin.agentNameKYC.text.isNullOrBlank()) {
            toast("Enter agent name")
            bin.agentNameKYC.requestFocus()
        } else if (bin.panCardNoKYC.text.isNullOrBlank()) {
            toast("Enter pan card Number")
            bin.panCardNoKYC.requestFocus()
        } else if (bin.firstNameKYC.text.isNullOrBlank()) {
            toast("Enter first name")
            bin.firstNameKYC.requestFocus()
        } else if (bin.dobKYC.text.isNullOrBlank()) {
            toast("Enter your DOB")
            bin.dobKYC.requestFocus()
        } else if (bin.emailIdKYC.text.isNullOrBlank()) {
            toast("Enter your Email")
            bin.emailIdKYC.requestFocus()
        } else if (bin.mobileNoKYC.text.isNullOrBlank()) {
            toast("Enter your mobile no")
            bin.mobileNoKYC.requestFocus()
        } else if (bin.permanentAddKYC.text.isNullOrBlank()) {
            toast("Enter your permanent Address")
            bin.permanentAddKYC.requestFocus()
        } else if (bin.businessTypeKYC.text.isNullOrBlank()) {
            toast("Enter business type")
            bin.businessTypeKYC.requestFocus()
        } else if (bin.officeAddKYC.text.isNullOrBlank()) {
            toast("Enter office address")
            bin.officeAddKYC.requestFocus()
        } else if (bin.stateKYC.text.isNullOrBlank()) {
            toast("Select your State")
            bin.stateKYC.requestFocus()
        } else if (bin.districtKYC.text.isNullOrBlank()) {
            toast("Select your district")
            bin.districtKYC.requestFocus()
        } else if (bin.cityKYC.text.isNullOrBlank()) {
            toast("Select your city")
            bin.cityKYC.requestFocus()
        } else if (bin.pinCodeKYC.text.length != 6) {
            toast("Enter your pin Code")
            bin.pinCodeKYC.requestFocus()
        } else if (bin.aadharNoKYC.text.length != 12) {
            toast("Enter your aadhar No")
            bin.aadharNoKYC.requestFocus()
        } else {
//            updateKYCApiCalling()
            showUpdateKycDetails(this@UpdateKYC)
        }
    }

    private fun updateKYCApiCalling() {
        this.runIfConnected {
            val updateKYCReq = UpdateKYCReq(
                RegistrationID = mStash!!.getStringValue(Constants.RegistrationId, ""),
                AEPSOnboardStatus = "Pass",
                EncryptedPass = "13475",
                HoldAmt = "0",
                District = mStash!!.getStringValue(Constants.District, ""),
                EmpCode = "7889",
                TransactionPin = "1122",
                RefrenceID = mStash!!.getStringValue(Constants.ReferenceId, ""),
                RefrenceType = mStash!!.getStringValue(Constants.ReferenceType, ""),
                AgentPassword = "bos123",
//            UploadPanCard = mStash!!.getStringValue(Constants.PanCardPic, ""),
                UploadPanCard = Utils.getBytesFromBitmap(
                    Utils.getBitmapFromDrawable(
                        bin.panImage,
                        this
                    )
                ).toString(),
                UploadAddharCardFront = mStash!!.getStringValue(Constants.AadharFront, "")
                    ?.ifEmpty {
                        Utils.getBytesFromBitmap(
                            Utils.getBitmapFromDrawable(
                                bin.aadharFrontImage,
                                this
                            )
                        )
                            .toString()
                    },
//            UploadAddharCardFront = Utils.getBytesFromBitmap(Utils.getBitmapFromDrawable(bin.aadharFrontImage, this)).toString(),
//            UploadAddharCardBack = mStash!!.getStringValue(Constants.AadharBack, ""),
                UploadAddharCardBack = Utils.getBytesFromBitmap(
                    Utils.getBitmapFromDrawable(
                        bin.aadharBackImage,
                        this
                    )
                ).toString(),
                UploadOtherProof = Utils.getBytesFromBitmap(
                    Utils.getBitmapFromDrawable(
                        bin.otherImage,
                        this
                    )
                ).toString(),
//            UploadPhoto = mStash!!.getStringValue(Constants.Photo, ""),
                UploadPhoto = Utils.getBytesFromBitmap(
                    Utils.getBitmapFromDrawable(
                        bin.photoImage,
                        this
                    )
                ).toString(),
                UpdatedBy = "",
                RegistrationDate = "21rwf",
                AgentType = mStash!!.getStringValue(Constants.AgentType, ""),
                AgencyName = mStash!!.getStringValue(Constants.AgentName, ""),
                FirstName = mStash!!.getStringValue(Constants.FirstName, ""),
                EmailID = mStash!!.getStringValue(Constants.EmailID, ""),
//            DOB = mStash!!.getStringValue(Constants.DOB, ""),
                DOB = bin.dobKYC.text.toString().trim(),
                AlternateMobileNo = bin.alternateNoKYC.text.toString().trim(),
                PermanentAddress = bin.permanentAddKYC.text.toString().trim(),
                State = mStash!!.getStringValue(Constants.State, ""),
                AddharCardNo = mStash!!.getStringValue(Constants.AadharCardNo, ""),
                WebSite = mStash!!.getStringValue(Constants.Website, ""),
                PanCardNumber = mStash!!.getStringValue(Constants.PanCardNo, ""),
                MobileNo = bin.mobileNoKYC.text.toString().trim(),
                OfficeAddress = mStash!!.getStringValue(Constants.OfficeAddress, ""),
                City = mStash!!.getStringValue(Constants.City, ""),
                LastName = bin.lastNameKYC.text.toString().trim(),
                Pincode = mStash!!.getStringValue(Constants.PinCode, ""),
                BusinessType = mStash!!.getStringValue(Constants.BusinessType, ""),
//            GSTNO = mStash!!.getStringValue(Constants.GSTNo, ""),
                GSTNO = bin.gstNoKYC.text.toString().trim(),
//            AccountHolderName = mStash!!.getStringValue(Constants.AccountHolderName, ""),
                AccountHolderName = bin.accountHolderNameKYC.text.toString().trim(),
//            BankName = mStash!!.getStringValue(Constants.BankName, ""),
                BankName = bin.backNameKYC.text.toString().trim(),
//            BranchName = mStash!!.getStringValue(Constants.BranchName, ""),
                BranchName = bin.backNameKYC.text.toString().trim(),
//            AccountType = mStash!!.getStringValue(Constants.AccountType, ""),
                AccountType = bin.accountTypeKYC.text.toString().trim(),
//            IFSCCode = mStash!!.getStringValue(Constants.IFSCCODE, ""),
                IFSCCode = bin.ifdcCodeKYC.text.toString().trim(),
                AccountNo = bin.accountNoKYC.text.toString().trim(),
                CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
            )

            Log.d(TAG, "updateKYCApiCalling: ${updateKYCReq}")
            Log.d("gspRegistration", Gson().toJson(updateKYCReq))

            viewModel.updateKYC(
                updateKYCReq
            ).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                pd.dismiss()
                                Log.d(TAG, "updateKYCApiCalling: ${users.body()}")
                                Log.d(TAG, "updateKYCApiCalling: ${users.message()}")
                                users.body()?.let { it1 -> updateKYCRes(it1) }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                            toast("Something went wrong")
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }
                    }
                }
            }
        }
    }

    private fun updateKYCRes(response: UpdateKYCRes) {
        if (!response.RegistrationId.isNullOrBlank()) {
            toast(response.message.toString())
            startActivity(Intent(this@UpdateKYC, DashboardActivity::class.java))
        } else {
            toast(response.message.toString())
        }
    }


    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            when (requestCode) {
                1 ->                     //camera
                    if (resultCode == Activity.RESULT_OK) {
                        try {
                            val selectedImagePath = Utils.getRealPathFromURI(
                                mCapturedImageURI,
                                this@UpdateKYC
                            )
                            Log.i("selectedImagePath", selectedImagePath!!)
                            val file = File(selectedImagePath)
                            Constants.compressedBitmap = null
                            Constants.compressedBitmap = Compressor(this@UpdateKYC)
                                .setQuality(60)
                                .setMaxWidth(640)
                                .setMaxHeight(480)
                                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                .setDestinationDirectoryPath(
                                    Environment.getExternalStoragePublicDirectory(
                                        Environment.DIRECTORY_PICTURES
                                    ).absolutePath
                                )
                                .compressToBitmap(file)
                            val stream = ByteArrayOutputStream()
                            Constants.compressedBitmap?.compress(
                                Bitmap.CompressFormat.JPEG,
                                100,
                                stream
                            )
                            setImage()

                        } catch (e: IOException) {
                            e.printStackTrace()
                            Log.d(TAG, "btnListener: ${e.printStackTrace()}")

                        }
                    }

                2 ->                     //gallery
                    // Enable if permission granted
                    if (resultCode == Activity.RESULT_OK) {
                        if (data != null) {
                            try {
                                val selectedImage = data.data
                                Log.i("Selected Image", "" + selectedImage)
                                val file = File(Utils.getPath(this@UpdateKYC, selectedImage!!)!!)
                                Constants.compressedBitmap = null
                                Constants.compressedBitmap = Compressor(this@UpdateKYC)
                                    .setQuality(60)
                                    .setMaxWidth(640)
                                    .setMaxHeight(480)
                                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                                    .setDestinationDirectoryPath(
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_PICTURES
                                        ).absolutePath
                                    )
                                    .compressToBitmap(file)
                                val stream = ByteArrayOutputStream()
                                Constants.compressedBitmap?.compress(
                                    Bitmap.CompressFormat.JPEG,
                                    100,
                                    stream
                                )
                                Log.d(TAG, "btnListener: " + "setImage()")
                                Log.d(TAG, "btnListener: ${Constants.compressedBitmap}")
                                setImage()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                UPDATE_REQ_CODE -> when (resultCode) {
                    Activity.RESULT_OK -> {
                        Log.d("TAG", "RESULT_CANCELED" + "Result Ok")
                    }

                    Activity.RESULT_CANCELED -> {
                        Log.d("TAG", "RESULT_CANCELED" + "Result Cancelled")
                        //  handle user's rejection  }
                    }
//                    ActivityResult.RESULT_IN_APP_UPDATE_FAILED -> {
//                        //if you want to request the update again just call checkUpdate()
//                        Log.d("TAG", "" + "Update Failure")
//                        //  handle update failure
//                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setImage() {
        Log.d(TAG, "btnListener: ${flag} ${Constants.OWNER_PAN_CARD}")
        Log.d(TAG, "btnListener: ${Constants.compressedBitmap}")
        if (flag == Constants.OWNER_PAN_CARD) {
            Utils.check_error_msg(bin.panImageBtn)
            bin.panImage.setImageBitmap(Constants.compressedBitmap)
        }
        if (flag == Constants.OWNER_AADHAR_FRONT) {
            Utils.check_error_msg(bin.aadharFrontImageBtn)
            bin.aadharFrontImage.setImageBitmap(Constants.compressedBitmap)
        }
        if (flag == Constants.OWNER_AADHAR_BACK) {
            Utils.check_error_msg(bin.aadharBackImageBtn)
            bin.aadharBackImage.setImageBitmap(Constants.compressedBitmap)
        }
        if (flag == Constants.OWNER_PHOTO) {
            Utils.check_error_msg(bin.photoImageBtn)
            bin.photoImage.setImageBitmap(Constants.compressedBitmap)
        }
        if (flag == Constants.OTHER_PHOTO) {
            Utils.check_error_msg(bin.otherImageBtn)
            bin.otherImage.setImageBitmap(Constants.compressedBitmap)
        }
    }


    private fun show_image_upload_option() {
        hideKeyboard(this)
        builder!!.setView(null)
        builder!!.setTitle("Upload Pictures Option")
        builder!!.setMessage("How do you want to set your picture?")
        builder!!.setPositiveButton("Gallery",
            DialogInterface.OnClickListener { arg0, arg1 ->
                val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(i, 2)
                Log.d(TAG, "btnListener: " + "Gallery")

            })
        builder!!.setNegativeButton("Camera",
            DialogInterface.OnClickListener { arg0, arg1 ->
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "Image File name")
                mCapturedImageURI =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                val intentPicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intentPicture.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    mCapturedImageURI
                )
                startActivityForResult(intentPicture, 1)
            })
        builder!!.show()
    }

    private fun showUpdateKycDetails(activity: Activity) {
        val builder = MaterialAlertDialogBuilder(activity)
        builder.setTitle("Need Permissions")
        builder.setMessage("Are you sure want to update the KYC")
        builder.setPositiveButton("Yes") { dialog, which ->
            updateKYCApiCalling()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }


}