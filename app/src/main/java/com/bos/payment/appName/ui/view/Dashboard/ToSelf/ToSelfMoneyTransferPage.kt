package com.bos.payment.appName.ui.view.Dashboard.ToSelf

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.idfcPayout.AOPPayOutReq
import com.bos.payment.appName.data.model.idfcPayout.AOPPayOutRes
import com.bos.payment.appName.data.model.justpaymodel.CheckBankDetailsModel
import com.bos.payment.appName.data.model.justpaymodel.ToSelfTransferSlabMaxMin
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialRes
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsReq
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceReq
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceRes
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceRes
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.repository.PayoutRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.data.viewModelFactory.PayoutViewModelFactory
import com.bos.payment.appName.databinding.ActivityToSelfMoneyTransferPageBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.Dashboard.activity.JustPeDashboard.Companion.QRBimap
import com.bos.payment.appName.ui.view.Dashboard.activity.JustPeDashboard.Companion.vpa
import com.bos.payment.appName.ui.view.Dashboard.dmt.DMTRechargeSuccessfulPage
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.ui.viewmodel.PayoutViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils
import com.bos.payment.appName.utils.Utils.animateTextSize
import com.bos.payment.appName.utils.Utils.generateQrBitmap
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.google.gson.Gson

class ToSelfMoneyTransferPage : AppCompatActivity() {
    lateinit var binding : ActivityToSelfMoneyTransferPageBinding
    private var mStash: MStash? = null
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    private lateinit var viewModel1: PayoutViewModel
    lateinit var pd : ProgressDialog
    var totalamoutForWithdraw: Double = 0.0
    var walletAmount: Double = 0.0
    var mainBalance: Double = 0.0

    var holdername : String? = ""
    var AccountNumber : String? = ""
    var IFSC : String? = ""
    var selleridentifier : String? = ""
    var mobilenumber : String ?= ""
    var emailid : String ?= ""
    var accounttype : String ?= ""
    var merchantcode : String ?= ""
    var registrationID : String ?= ""
    var isSlabMatched = false
    private var serviceCharge: Double = 0.0
    lateinit var dialog: Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityToSelfMoneyTransferPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mStash = MStash.getInstance(this)
        viewModel1 = ViewModelProvider(this, PayoutViewModelFactory(PayoutRepository(RetrofitClient.apiAllPayoutAPI)))[PayoutViewModel::class.java]
        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(
            GetAllAPIServiceRepository(RetrofitClient.apiAllInterface)
        )
        )[GetAllApiServiceViewModel::class.java]
        pd = ProgressDialog(this)

        getBankDetails(mStash!!.getStringValue(Constants.RegistrationId, "").toString())

        setClickListner()

    }


    override fun onResume() {
        super.onResume()
        getAllWalletBalance(false)
    }



    fun setClickListner(){

        binding.main.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            binding.main.getWindowVisibleDisplayFrame(r)
            val screenHeight = binding.main.rootView.height
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.20) {
                // Keyboard is open
                if (binding.remarks.hasFocus()) {
                    binding.scrollview.post {
                        binding.scrollview.smoothScrollTo(0, binding.scrollview.bottom)
                    }
                }
            }
        }

        binding.back.setOnClickListener {
            finish()
        }


        binding.etAmount.filters= arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val newInput = dest.toString().substring(0, dstart) +
                    source.subSequence(start, end) +
                    dest.toString().substring(dend)

            val regex = Regex("^\\d{0,7}(\\.\\d{0,2})?$") // up to 7 digits before decimal, 2 after
            if (newInput.matches(regex)) {
                null // accept input
            } else {
                ""   // reject invalid characters
            }
        })


        var lastValidAmount = ""

        binding.etAmount.addTextChangedListener(object : TextWatcher {
            private var editing = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (editing) return
                editing = true

                val text = s?.toString()?.trim() ?: ""

                // 🧠 Handle empty or just "." input safely
                if (text.isEmpty() || text == ".") {
                    lastValidAmount = ""
                    binding.confirmbutton.visibility = View.GONE
                    editing = false
                    return
                }

                val enteredPrice = text.toDoubleOrNull()

                // 🧱 If invalid number typed
                if (enteredPrice == null) {
                    binding.etAmount.setText(lastValidAmount)
                    binding.etAmount.setSelection(binding.etAmount.text.length)
                    binding.confirmbutton.visibility = View.GONE
                    editing = false
                    return
                }

                // 🚫 If exceeds wallet amount → revert and show error
                if (enteredPrice > walletAmount) {
                    binding.etAmount.error = "Enter amount ≤ ₹$walletAmount"
                    binding.etAmount.setText(lastValidAmount)
                    binding.etAmount.setSelection(binding.etAmount.text.length)
                    binding.confirmbutton.visibility = View.GONE
                    editing = false
                    return
                }

                // ✅ Valid input → save, show button, and animate
                lastValidAmount = text
                binding.confirmbutton.visibility = View.VISIBLE

                // 💫 Animate text size like Paytm
                val length = text.length
                val targetSize = when {
                    length <= 3 -> 60f
                    length <= 5 -> 45f
                    length <= 7 -> 35f
                    else -> 28f
                }
                animateTextSize(binding.etAmount, targetSize)

                // Keep cursor at end
                binding.etAmount.setSelection(text.length)
                editing = false
            }
        })


        binding.confirmbutton.setOnClickListener {
           /* if (binding.etAmount.text.toString().isEmpty()) {
                binding.etAmount.requestFocus()
                toast("Please enter your amount")
            }
            else{
                getAllWalletBalance()
            }*/
            transactionValidation()
        }


    }


    private fun transactionValidation() {
        val transferAmountText = binding.etAmount.text.toString().trim()
        if (binding.etAmount.text.toString().isEmpty()) {
            binding.etAmount.requestFocus()
            toast("Please enter your amount")
        }
        else {
            getAllApiPayoutCommercialCharge(transferAmountText)
        }


    }


    private fun getAllApiPayoutCommercialCharge(transferAmt: String) {
        //F0112 for payout
        val getPayoutCommercialReq = GetPayoutCommercialReq(
            merchant = mStash!!.getStringValue(Constants.RegistrationId, ""),
            productId = "F0112",
            modeofPayment = "NEFT"
        )
        Log.d("getAllGsonFromAPI", Gson().toJson(getPayoutCommercialReq))
        getAllApiServiceViewModel.getAllApiPayoutCommercialCharge(getPayoutCommercialReq)
            .observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    Log.d("CommercialResp",Gson().toJson(response))
                                    getAllApiPayoutCommercialChargeRes(response, transferAmt)
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


    private fun getAllApiPayoutCommercialChargeRes(response: GetPayoutCommercialRes, transferAmt: String) {
        if (response.isSuccess == true)
        {
            try {
                val rechargeAmountValue = transferAmt.toDoubleOrNull() ?: 0.0
                var slablist = response.data
                var matchMaxMinSlabAmount : MutableList<ToSelfTransferSlabMaxMin> = mutableListOf()

                for (i in response.data.indices) {
                    val min = response.data[i].slabName
                        ?.substringAfter(": ") // Get the part after "With Slab: "
                        ?.substringBefore("-") // Get the part before "-"
                        ?.replace(",", "")     // Remove commas
                        ?.toDoubleOrNull()     // Convert to Double
                        ?: 0.0

                    // Extract the maximum value
                    val max = response.data[i].slabName
                        ?.substringAfter("-")  // Get the part after "-"
                        ?.replace(",", "")     // Remove commas
                        ?.toDoubleOrNull()     // Convert to Double
                        ?: 0.0

                    matchMaxMinSlabAmount.add(ToSelfTransferSlabMaxMin(min,max))
                }

                Log.d("SlibList", Gson().toJson(matchMaxMinSlabAmount))

                var validateRange= matchMaxMinSlabAmount(rechargeAmountValue,matchMaxMinSlabAmount)

                if(validateRange){
                    for (i in response.data.indices) {
                        val gst = 18.0 // Fixed GST rate of 18%
                        // Get characters between ":" and "-" and convert to Double
                        // Extract the minimum value
                        val min = response.data[i].slabName
                            ?.substringAfter(": ") // Get the part after "With Slab: "
                            ?.substringBefore("-") // Get the part before "-"
                            ?.replace(",", "")     // Remove commas
                            ?.toDoubleOrNull()     // Convert to Double
                            ?: 0.0

                        // Extract the maximum value
                        val max = response.data[i].slabName
                            ?.substringAfter("-")  // Get the part after "-"
                            ?.replace(",", "")     // Remove commas
                            ?.toDoubleOrNull()     // Convert to Double
                            ?: 0.0

                        Log.d(TAG, "getAllApiPayoutCommercialChargeRes: $min, $max")


                        if (rechargeAmountValue in min..max) {
                            isSlabMatched = true   // Mark that a matching slab was found
                            serviceCharge = response.data[i].serviceCharge?.toDoubleOrNull() ?: 0.0
                            Log.d("serviceCharge", serviceCharge.toString())
                            serviceChargeCalculation(serviceCharge, gst, transferAmt, response, min, max)
                            break
                        }


                    }
                }


            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            pd.dismiss()
            Toast.makeText(this, response.returnMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    fun matchMaxMinSlabAmount(amount: Double, slabs: MutableList<ToSelfTransferSlabMaxMin>): Boolean {
        // 🔍 Try to find a matching slab where amount lies between min and max
        val matchingSlab = slabs.find { amount in it.min..it.max }

        return if (matchingSlab != null) {
            true // ✅ Amount is valid in some range
        } else {
            // ❌ No matching slab found → find closest range
            val minVal = slabs.minOfOrNull { it.min } ?: 0.0
            val maxVal = slabs.maxOfOrNull { it.max } ?: 0.0

            Toast.makeText(
                this,
                "Please enter an amount between ₹$minVal and ₹$maxVal",
                Toast.LENGTH_LONG
            ).show()
            false
        }
    }


    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun serviceChargeCalculation(serviceCharge: Double, gstRate: Double, rechargeAmount: String, response: GetPayoutCommercialRes, min: Double, max: Double): Double {
        val rechargeAmountValue = rechargeAmount.toDoubleOrNull() ?: 0.0

        var serviceChargeWithGst = 0.0
        var slabLimit: String? = null

        if (mStash!!.getStringValue(Constants.RegistrationId, "")!!.isNotEmpty()) {
            slabLimit = when {
                rechargeAmountValue <= max -> max.toString()
                else -> {
                    Toast.makeText(this, "No valid slab found for the amount: $rechargeAmountValue", Toast.LENGTH_SHORT).show()
                    null
                }
            }
        }

        else {
            toast("No valid slab found for the amount: $rechargeAmountValue")
        }

        for (i in response.data.indices) {
            if (slabLimit != null) {
                Log.d("ServiceType", "${response.data[i].serviceType} - Index $i")
                serviceChargeWithGst = when (response.data[i].serviceType) {
                    "Amount" -> {
                        val serviceChargeGst = serviceCharge * (gstRate / 100)
                        mStash?.setStringValue(Constants.serviceCharge, String.format("%.2f", serviceCharge))
                        mStash?.setStringValue(Constants.serviceChargeGST, String.format("%.2f", serviceChargeGst))
                        serviceCharge + serviceChargeGst
                    }
                    "Percentage" -> {
                        val serviceInAmount = rechargeAmountValue * (serviceCharge / 100)
                        val serviceWithGst = serviceInAmount * (gstRate / 100)
                        mStash?.setStringValue(Constants.serviceCharge, String.format("%.2f", serviceInAmount))
                        mStash?.setStringValue(Constants.serviceChargeGST, String.format("%.2f", serviceWithGst))
                        serviceInAmount + serviceWithGst
                    }
                    else -> 0.0
                }
                break
            }
        }


        val actualAmount = serviceChargeWithGst + rechargeAmountValue

        Log.d("serviceChargeWithGst", String.format("%.2f", serviceChargeWithGst))
        Log.d("TotalRechargeAmount", String.format("%.2f", actualAmount))
        Log.d("totalAmountWithGst", String.format("%.2f", serviceChargeWithGst))

        mStash?.apply {
            setStringValue(Constants.actualAmountServiceChargeWithGST, String.format("%.2f", actualAmount))
            setStringValue(Constants.serviceChargeWithGST, String.format("%.2f", serviceChargeWithGst))
            setStringValue(Constants.totalTransaction, String.format("%.2f", actualAmount))
        }

        binding.confirmbutton.visibility=View.GONE
        openDialogForPayout(serviceChargeWithGst,rechargeAmountValue)

        return serviceChargeWithGst
    }


    fun getBankDetails(retailerCode: String){
        val requestForBankDetails = CheckBankDetailsModel(reatilerCode =  retailerCode)
        Log.d("bankdetailereq", Gson().toJson(requestForBankDetails))
        getAllApiServiceViewModel.getBankDetails(requestForBankDetails).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response ->

                                if(response.isSuccess!!){
                                    var getdata = response.data
                                    Log.d("BankdetailsResponse", Gson().toJson(response))
                                    if(getdata!=null){
                                        mStash!!.setStringValue(Constants.SettlementAccountName, getdata[0]!!.settlementAccountName)
                                        mStash!!.setStringValue(Constants.SettlementAccountNumber, getdata[0]!!.settlementAccountNumber)
                                        mStash!!.setStringValue(Constants.SettlementAccountIfsc, getdata[0]!!.settlementAccountIfsc)
                                        mStash!!.setStringValue(Constants.SellerIdentifier, getdata[0]!!.sellerIdentifier)
                                        mStash!!.setStringValue(Constants.BankMobileNumber, getdata[0]!!.mobileNumber)
                                        mStash!!.setStringValue(Constants.EmailId, getdata[0]!!.emailId)
                                        mStash!!.setStringValue(Constants.BankAccountType, getdata[0]!!.accountType)
                                        mStash!!.setStringValue(Constants.CreatedBy, getdata[0]!!.createdBy)
                                        mStash!!.setStringValue(Constants.ISQRCodeActivate, getdata[0]!!.isQRCodeActivate)
                                        mStash!!.setStringValue(Constants.ISQRCodeGenerated, getdata[0]!!.isQRCodeGenerated)
                                        mStash!!.setStringValue(Constants.VPAid, getdata[0]!!.vpaid)

                                        binding.holdername.text= getdata[0]!!.settlementAccountName
                                        binding.accountnumber.text= getdata[0]!!.settlementAccountNumber
                                        binding.ifsccode.text= getdata[0]!!.settlementAccountIfsc
                                        binding.selleridentifier.text=getdata[0]!!.sellerIdentifier
                                        binding.mobilenumber.text= getdata[0]!!.mobileNumber
                                        binding.emailid.text= getdata[0]!!.emailId
                                        binding.accountType.text= getdata[0]!!.accountType

                                    }

                                }
                                else{

                                }

                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        pd.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        pd.dismiss()
                    }
                }
            }
        }

    }


    private fun getAllWalletBalance(check:Boolean) {
        runIfConnected {
            val walletBalanceReq = GetBalanceReq(
                parmUser = mStash!!.getStringValue(Constants.RegistrationId, ""),
                flag = "CreditBalance"
            )

            getAllApiServiceViewModel.getWalletBalance(walletBalanceReq)
                .observe(this) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        getAllWalletBalanceRes(response,check)
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


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun getAllWalletBalanceRes(response: GetBalanceRes ,check:Boolean) {
        if (response.isSuccess == true) {
            walletAmount = (response.data[0].result!!.toDoubleOrNull() ?: 0.0)

            totalamoutForWithdraw = binding.etAmount.text.toString()  !!.toDoubleOrNull() ?: 0.0

            binding.walletamt.text = "Available  wallet amount is ₹$walletAmount"

            Log.d("actualBalance", "main = $mainBalance")

        } else {
            toast(response.returnMessage.toString())
        }

    }


    private fun getMerchantBalance(mainBalance: Double) {
        val getMerchantBalanceReq = GetMerchantBalanceReq(
            parmUser = mStash!!.getStringValue(Constants.MerchantId, ""),
            flag = "DebitBalance"
        )
        Log.d("getAllGsonFromAPI", Gson().toJson(getMerchantBalanceReq))

        getAllApiServiceViewModel.getAllMerchantBalance(getMerchantBalanceReq)
            .observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    getAllMerchantBalanceRes(response, mainBalance)
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



    private fun getAllMerchantBalanceRes(response: GetMerchantBalanceRes, mainBalance: Double) {
        if (response.isSuccess == true) {
            Log.d(TAG, "getAllMerchantBalanceRes: ${response.data[0].debitBalance}")
            mStash!!.setStringValue(Constants.merchantBalance, response.data[0].debitBalance)
            Log.d("creditamt", mStash!!.getStringValue(Constants.toBeCreditedAmt, "") !!)

            val totalAmount = mStash!!.getStringValue(Constants.totalTransaction, "")?.toDoubleOrNull() ?: 0.0

            val merchantBalance = response.data[0].debitBalance?.toDoubleOrNull() ?: 0.0

            Log.d("balanceCheck", "MainBal = $mainBalance, merchantBal = $merchantBalance,totalAmount = $totalAmount, Status = ${totalAmount <= mainBalance && totalAmount <= merchantBalance}")

            if (totalAmount <= merchantBalance) {
                getTransferAmountToAgentWithCal(binding.etAmount.text.toString())
            }
            else {
                pd.dismiss()
                Toast.makeText(this, "Yor merchant balance is low, please contact with your admin.", Toast.LENGTH_LONG).show()
            }

        }
        else {
            pd.dismiss()
            Toast.makeText(this, response.returnMessage.toString(), Toast.LENGTH_SHORT).show()
        }

    }


    private fun getTransferAmountToAgentWithCal(rechargeAmount: String) {
        try {
            val currentDateTime = Utils.getCurrentDateTime()
            val bankAccountNo = binding.accountnumber.text.toString().trim()

            val transferAmountToAgentsReq = TransferAmountToAgentsReq(
                transferFrom = mStash!!.getStringValue(Constants.RegistrationId, "") ?: "0",
                transferTo = "Admin",
                transferAmt = mStash!!.getStringValue(Constants.totalTransaction, "") ?: "0",
                remark = binding.remarks.text.toString().trim(),
                transferFromMsg = "Your Account is debited by ${rechargeAmount ?: "0"} Rs. Due to Pay Out on number ${bankAccountNo ?: ""}.",
                transferToMsg = "Your Account is credited by ${rechargeAmount ?: "0"} Rs. Due to Pay Out on number ${bankAccountNo ?: ""}.",
                amountType = "Payout",
                actualTransactionAmount = rechargeAmount ?: "0",
                transIpAddress = mStash!!.getStringValue(Constants.deviceIPAddress, "") ?: "0.0.0.0",
                parmUserName = mStash!!.getStringValue(Constants.RegistrationId, "") ?: "0",
                merchantCode = mStash!!.getStringValue(Constants.MerchantId, "") ?: "0",
                servicesChargeAmt = mStash!!.getStringValue(Constants.serviceCharge, "") ?: "0.00",
                servicesChargeGSTAmt ="0.00",
                servicesChargeWithoutGST = mStash!!.getStringValue(Constants.serviceCharge, "") ?: "0.00",
                customerVirtualAddress = "",
                retailerCommissionAmt ="0.00",
                retailerId = "",
                paymentMode = "NEFT",
                depositBankName = binding.ifsccode.text.toString().trim(),
                branchCodeChecqueNo = "",
                apporvedStatus = "Approved",
                registrationId = mStash!!.getStringValue(Constants.RegistrationId, "") ?: "0",
                benfiid = "",
                accountHolder =  binding.holdername.text.toString().trim(),
                flag = "Y"
            )

            Log.d("getAllGsonFromAPI", Gson().toJson(transferAmountToAgentsReq))
            getAllApiServiceViewModel.getTransferAmountToAgents(transferAmountToAgentsReq)
                .observe(this) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        Log.d("AdminTransferResp",Gson().toJson(response))
                                        if(response.isSuccess!!){
                                            sendAllPayoutAmount()
                                        }
                                    }
                                }
                            }

                            ApiStatus.ERROR -> {
                                pd.dismiss()
                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
                            }

                            ApiStatus.LOADING -> {
                                pd.show()
                            }

                        }
                    }
                }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            pd.dismiss()
            Toast.makeText(this, e.message.toString() + " " + e.localizedMessage?.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun sendAllPayoutAmount() {
       var ToBeCreditedAmt = mStash!!.getStringValue(Constants.toBeCreditedAmt, "")
        val aopPayOutReq = AOPPayOutReq(
            accountNumber = binding.accountnumber.text.toString().trim(),
            amount = ToBeCreditedAmt,
            transactionType = "NEFT",
            beneficiaryIFSC = binding.ifsccode.text.toString().trim(),
            beneficiaryName = binding.holdername.text.toString().trim(),
            emailID = binding.emailid.text.toString().trim(),
            mobileNo = binding.mobilenumber.text.toString().trim(),
            registrationID = mStash?.getStringValue(Constants.MerchantId, "")
        )

        Log.d("getAllGsonFromAPI", Gson().toJson(aopPayOutReq))
        viewModel1.sendAllPayoutAmount(aopPayOutReq).observe(this) { resource ->
            resource?.let {
                when(it.apiStatus){
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("getAllGsonFromAPIResp", Gson().toJson(response))
                                sendAllPayoutAmountRes(response)
                            }
                        }
                    }
                    ApiStatus.ERROR -> {
                        pd.dismiss()
                        val message = it.message ?: "Something went wrong, please try again."
                        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    }
                    ApiStatus.LOADING -> {
                        pd.show()
                    }
                }
            }
        }
    }


    private fun sendAllPayoutAmountRes(response: AOPPayOutRes) {
        if (response.statuss == "SUCCESS"){
            pd.dismiss()
        } else {
            Toast.makeText(this, response.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("SetTextI18n")
    fun openDialogForPayout(serviceCharge: Double, transferAmount: Double) {
        val dialog = Dialog(this@ToSelfMoneyTransferPage, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.payoutscreen)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        dialog.setCanceledOnTouchOutside(false)

        val transferamttxt = dialog.findViewById<TextView>(R.id.actualamt)
        val serviceChargetxt = dialog.findViewById<TextView>(R.id.servicecharge)
        val creditedamt = dialog.findViewById<TextView>(R.id.creditamt)
        val serviceChargeamount = dialog.findViewById<TextView>(R.id.servicescharge)
        val gstamount = dialog.findViewById<TextView>(R.id.gstamount)

        val cancel = dialog.findViewById<ImageView>(R.id.cancel)
        val done = dialog.findViewById<LinearLayout>(R.id.Proceedbtn)
        val viewBreakLayout = dialog.findViewById<LinearLayout>(R.id.viewbreaklayout)
        val detailsgstserviceslayout = dialog.findViewById<LinearLayout>(R.id.chargesdetailslayout)


        transferamttxt.text = "$transferAmount"
        serviceChargetxt.text = "$serviceCharge"

       var servicechargeamount = mStash!!.getStringValue(Constants.serviceCharge, "") ?: "0.00"
       var servicechargeGst = mStash!!.getStringValue(Constants.serviceChargeGST, "") ?: "0.00"


        serviceChargeamount.text = "$servicechargeamount"
        gstamount.text = "$servicechargeGst"

        val toBeCreditedAmt = transferAmount - serviceCharge
        creditedamt.text = String.format("%.2f", toBeCreditedAmt)

        var checkView : Boolean = false

        viewBreakLayout.setOnClickListener {
            if(checkView){
                detailsgstserviceslayout.visibility = View.GONE
                checkView= false
            }else{
                detailsgstserviceslayout.visibility = View.VISIBLE
                checkView= true
            }
        }


        done.setOnClickListener {
            if(toBeCreditedAmt>0){
                mStash?.setStringValue(Constants.toBeCreditedAmt, String.format("%.2f", toBeCreditedAmt))
                getMerchantBalance(walletAmount)
            }
            else{
                Toast.makeText(this,
                    "Transfer amount must be greater than the service charge.",
                    Toast.LENGTH_LONG
                ).show()
            }

        }

        cancel.setOnClickListener {
            dialog.dismiss()
            binding.confirmbutton.visibility=View.VISIBLE
        }

        dialog.setOnDismissListener {
            binding.confirmbutton.visibility=View.VISIBLE
        }

        dialog.show() // ✅ REQUIRED
    }


}