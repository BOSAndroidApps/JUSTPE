package com.bos.payment.appName.ui.view.Dashboard.ToSelf

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.idfcPayout.AOPPayOutReq
import com.bos.payment.appName.data.model.idfcPayout.AOPPayOutRes
import com.bos.payment.appName.data.model.justpaymodel.CheckBankDetailsModel
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialRes
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsReq
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
        getAllWalletBalance()
    }



    fun setClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.confirmbutton.setOnClickListener {
            transactionValidation()
        }

    }

    private fun transactionValidation() {
        if (binding.etAmount.text.toString().isEmpty()) {
            binding.etAmount.requestFocus()
            toast("Please enter your amount")
        } else {
            val transferAmountText = binding.etAmount.text.toString().trim()
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

                    var servicecharge = response.data[i].serviceCharge!!.toDoubleOrNull() ?: 0.0

                    if (rechargeAmountValue in min..max) {
                        isSlabMatched = true   // Mark that a matching slab was found
                        serviceCharge = response.data[i].serviceCharge?.toDoubleOrNull() ?: 0.0
                        Log.d("serviceCharge", serviceCharge.toString())
                        serviceChargeCalculation(serviceCharge, gst, transferAmt, response, min, max)
                        break

                       /* var totalamount = rechargeAmountValue + servicecharge

                        mStash?.setStringValue(Constants.serviceCharge, String.format("%.2f", servicecharge))

                        Log.d("totalamt",": $totalamount")

                        if(totalamount<=mainBalance){
                            getTransferAmountToAgentWithCal(totalamount.toString())
                        }
                        else{
                            Toast.makeText(this, "Wallet balance is low. VBal = $mainBalance,  totalAmt = $totalamount", Toast.LENGTH_LONG).show()
                        }*/
                    }

                    //if no matching slab was found, show an error message
                    if (!isSlabMatched) {
                        isSlabMatched = false
                        Toast.makeText(this, "No matching slab found for the amount: $rechargeAmountValue", Toast.LENGTH_SHORT).show()
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


    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun serviceChargeCalculation(serviceCharge: Double, gstRate: Double, rechargeAmount: String, response: GetPayoutCommercialRes, min: Double, max: Double):
            Double {
        val rechargeAmountValue = rechargeAmount.toDoubleOrNull() ?: 0.0
        var totalServiceChargeWithGst = 0.0
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
        }else {
            toast("No valid slab found for the amount: $rechargeAmountValue")

        }

        for (i in response.data.indices) {
            if (slabLimit != null) {
                Log.d("ServiceType", "${response.data[i].serviceType} - Index $i")
                serviceChargeWithGst = when (response.data[i].serviceType) {
                    "Amount" -> {
                        val serviceChargeGst = serviceCharge * (gstRate / 100)
                        mStash?.setStringValue(Constants.serviceCharge, String.format("%.2f", serviceCharge))
                        serviceCharge + serviceChargeGst
                    }
                    "Percentage" -> {
                        val serviceInAmount = rechargeAmountValue * (serviceCharge / 100)
                        val serviceWithGst = serviceInAmount * (gstRate / 100)
                        mStash?.setStringValue(Constants.serviceCharge, String.format("%.2f", serviceInAmount))
                        serviceInAmount + serviceWithGst
                    }
                    else -> 0.0
                }
                break
            }
        }

        if (serviceChargeWithGst == 0.0) {
            Toast.makeText(this, "No matching slab found for the entered amount", Toast.LENGTH_SHORT).show()
            return 0.0
        }

        val actualAmount = serviceChargeWithGst + rechargeAmountValue
        Log.d("serviceChargeWithGst", String.format("%.2f", serviceChargeWithGst))
        Log.d("TotalRechargeAmount", String.format("%.2f", actualAmount))
        Log.d("totalAmountWithGst", String.format("%.2f", serviceChargeWithGst))

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

    private fun getAllWalletBalance() {
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
                                        getAllWalletBalanceRes(response)
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
    private fun getAllWalletBalanceRes(response: GetBalanceRes) {
        if (response.isSuccess == true) {
            mainBalance = (response.data[0].result!!.toDoubleOrNull() ?: 0.0)
            totalamoutForWithdraw = binding.etAmount.text.toString()  !!.toDoubleOrNull() ?: 0.0

            if(totalamoutForWithdraw<= mainBalance){
               // hitApiForSendMoneyToAdmin(totalamoutForWithdraw)
            }
            else{
                pd.dismiss()
                Toast.makeText(this, "Wallet balance is low. VBal = $mainBalance,  totalAmt = $totalamoutForWithdraw", Toast.LENGTH_LONG).show()
            }

            binding.walletamt.text = "You can send money up to ₹$mainBalance only"

            Log.d("actualBalance", "main = $mainBalance")

        } else {
            toast(response.returnMessage.toString())
        }

    }


    private fun getTransferAmountToAgentWithCal(rechargeAmount: String) {
        try {
            val currentDateTime = Utils.getCurrentDateTime()
            val bankAccountNo = binding.accountnumber.text.toString().trim()

            val transferAmountToAgentsReq = TransferAmountToAgentsReq(
                transferFrom = mStash!!.getStringValue(Constants.RegistrationId, "") ?: "0",
                transferTo = "Admin",
                transferAmt = rechargeAmount,
                remark = "Payout",
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
                depositBankName = "",
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
                                        //getTransferAmountToAgentWithCalRes(response)
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
            Toast.makeText(
                this,
                e.message.toString() + " " + e.localizedMessage?.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun sendAllPayoutAmount() {
        val aopPayOutReq = AOPPayOutReq(
            accountNumber = binding.accountnumber.text.toString().trim(),
            amount = binding.etAmount.text.toString().trim(),
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



}