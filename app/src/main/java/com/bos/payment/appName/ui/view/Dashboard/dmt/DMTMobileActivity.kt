package com.bos.payment.appName.ui.view.Dashboard.dmt

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bos.payment.appName.R
import com.bos.payment.appName.adapter.ReceiptAdapter
import com.bos.payment.appName.constant.ConstantClass
import com.bos.payment.appName.constant.CustomDateUtils
import com.bos.payment.appName.data.model.dmt.bankList.DMTBankListReq
import com.bos.payment.appName.data.model.dmt.bankList.DMTBankListRes
import com.bos.payment.appName.data.model.dmt.fetchBenificary.Data
import com.bos.payment.appName.data.model.dmt.fetchBenificary.FetchBeneficiaryReq
import com.bos.payment.appName.data.model.dmt.fetchBenificary.FetchBeneficiaryRes
import com.bos.payment.appName.data.model.dmt.queryRemitters.QueryRemitterReq
import com.bos.payment.appName.data.model.dmt.queryRemitters.QueryRemitterRes
import com.bos.payment.appName.data.model.dmt.registerBeneficiary.RegisterBaneficiaryReq
import com.bos.payment.appName.data.model.dmt.registerBeneficiary.RegisterBaneficiaryRes
import com.bos.payment.appName.data.model.dmt.registerRemitters.RegisterRemitterReq
import com.bos.payment.appName.data.model.dmt.registerRemitters.RegisterRemitterRes
import com.bos.payment.appName.data.model.dmt.transaction.TransactStatusReq
import com.bos.payment.appName.data.model.dmt.transaction.TransactStatusRes
import com.bos.payment.appName.data.model.dmt.transaction.TransactionReq
import com.bos.payment.appName.data.model.dmt.transaction.TransactionRes
import com.bos.payment.appName.data.model.dmt.transactionOtp.TransactionSendOtpReq
import com.bos.payment.appName.data.model.dmt.transactionOtp.TransactionSendOtpRes
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetAPIServiceChargeReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetAPIServiceChargeRes
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialRes
import com.bos.payment.appName.data.model.recharge.payout.PayoutAmountRes
import com.bos.payment.appName.data.model.recharge.payout.payoutBos.PayoutAppReq
import com.bos.payment.appName.data.model.recharge.payout.payoutStatus.PayoutStatusReq
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsReq
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsRes
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsWithCalculationRes
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceReq
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceRes
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceRes
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.repository.MoneyTransferRepository
import com.bos.payment.appName.data.repository.PayoutRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.data.viewModelFactory.MoneyTransferViewModelFactory
import com.bos.payment.appName.data.viewModelFactory.PayoutViewModelFactory
import com.bos.payment.appName.databinding.ActivityDmtmobileBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.ui.viewmodel.MoneyTransferViewModel
import com.bos.payment.appName.ui.viewmodel.PayoutViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import java.util.Calendar
import java.util.GregorianCalendar

class DMTMobileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDmtmobileBinding
    private val context: Context = this@DMTMobileActivity
    private lateinit var fetchListData: ArrayList<Data>
    private var mStash: MStash? = null
    private var doubleBackToExitPressedOnce: Boolean = false
    private var mobileNo: String? = ""
    private var bankList_txt: String = ""
    private var transferMode_txt: String = ""
    private val myCalender = Calendar.getInstance()
    private lateinit var viewModel: MoneyTransferViewModel
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    private lateinit var viewModel1: PayoutViewModel
    private lateinit var pd: AlertDialog
    private lateinit var bankList: ArrayList<DMTBankListRes>
    private lateinit var timer11: CountDownTimer
    private var serviceCharge: Double = 0.0
    var isSlabMatched = false // Flag to check if any slab matches


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
                binding.dobOwner.setText("")
            }

            minAdultAge1.after(myCalender) -> {
                Toast.makeText(
                    applicationContext,
                    resources.getString(R.string.max_age_person),
                    Toast.LENGTH_LONG
                ).show()
                binding.dobOwner.setText("")
            }

            else -> {
                binding.dobOwner.let { Utils.updateLabel(it, myCalender, "Update KYC") }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityDmtmobileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        setAdapter()
        btnListener()


    }

    private fun setAdapter() {
        Constants.getAllBankListAdapter = ArrayAdapter<String>(
            this, R.layout.support_simple_spinner_dropdown_item, Constants.bankListName!!
        )
        Constants.getAllBankListAdapter!!.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        binding.bankSp.adapter = Constants.getAllBankListAdapter
        binding.bankSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>, view: View?, pos: Int, id: Long
            ) {
                if (pos > 0) {
                    try {
                        try {
                            bankList_txt =
                                (Constants.bankListNameMap?.get(parent.getItemAtPosition(pos))
                                    ?: "").toString()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        toast(bankList_txt)
                        // If you need to perform further actions with selectedAllOperator, do it here.
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    bankList_txt = ""
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where nothing is selected, if needed
            }
        }

        // Notify the adapter that the data set has changed (if data is dynamically added/removed)
        Constants.getAllBankListAdapter!!.notifyDataSetChanged()
    }

    //
    private fun showUpdateKycDetails(activity: Activity) {
        val builder = MaterialAlertDialogBuilder(activity)
        builder.setTitle("Need Permissions")
        builder.setMessage("Are you sure want to transfer balance")
        builder.setPositiveButton("Yes") { dialog, which ->

            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun btnListener() {
        binding.tvBtnGo.setOnClickListener { validation() }
        binding.tvBtnChangeNumber.setOnClickListener {
            binding.llAddRecipient.visibility = View.GONE
            binding.llIfMobileNumberExist.visibility = View.GONE
            binding.llRecipientDetails.visibility = View.GONE
            binding.llTransferAmount.visibility = View.GONE
            binding.llRegister.visibility = View.GONE
            binding.tvBtnAddRecipient.visibility = View.GONE
            changeNumber()
        }
        binding.tvBtnRegisterCancel.setOnClickListener {
            changeNumber()
            changeNumber2()
        }
        binding.transactionCancelBtn.setOnClickListener {
            binding.llTransferAmount.visibility = View.GONE
        }
        binding.transactionGoBtn.setOnClickListener {
            getAllWalletBalance()

//            val walletBalance =
//                mStash!!.getStringValue(Constants.retailerMainVirtualAmount, "")!!.toDoubleOrNull()
//            val totalTransaction =
//                mStash!!.getStringValue(Constants.totalTransaction, "")!!.toDoubleOrNull()
//
//            if (walletBalance != null && totalTransaction != null) {
//                if (walletBalance > totalTransaction) {
//                    getTransferAmountToAgentWithCal(binding.transferAmount.text.toString())
//                } else {
//                    toast("Wallet balance is low. Contact the BOS Center.")
//                }
//            } else {
//                toast("Invalid wallet balance or transaction amount.")
//            }

//            val walletBalance = mStash!!.getStringValue(Constants.retailerMainVirtualAmount, "")
//            if (mStash!!.getStringValue(Constants.totalTransaction, "")!! < walletBalance.toString()){

//            }else {
//                toast("Wallet balance is low Contact to the Bos Center")
//            }

//            getPayoutAmountTransaction()

//            if (mStash!!.getStringValue(Constants.serviceChargeWithGST, "").toString() > "0.00") {
//                Log.d("serviceChargeWithGST", mStash!!.getStringValue(Constants.serviceChargeWithGST, "").toString())
//                getAllServiceChargeAPI()
//            } else {
//                getAllCommissionChargeAPI()
//            }
//            showUpdateKycDetails(this@DMTMobileActivity)
        }
        binding.proceedBtn.setOnClickListener {
//            getTransferAmountToAgentWithCal(binding.transferAmount.text.toString())
            transactionValidation()
        }
        binding.dobOwner.setOnClickListener {
            Utils.hideKeyboard(this)
            DatePickerDialog(
                this,
                date,
                myCalender[Calendar.YEAR],
                myCalender[Calendar.MONTH],
                myCalender[Calendar.DAY_OF_MONTH]
            ).show()
        }

//        binding.tvBtnRegisterGo.setOnClickListener { registerRemitters() }
        binding.tvBtnAddRecipientGo.setOnClickListener { registerBeneficiary() }
        binding.etRegisterDOB.setOnClickListener {
            CustomDateUtils.showDatePickerDialogMaxDt(context,
                CustomDateUtils.dateFormatServer11,
                object : CustomDateUtils.ISC_String {
                    override fun onClicked(string: String?) {
                        binding.etRegisterDOB.setText(string)
                    }
                })
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.tvBtnAddRecipientCancel.setOnClickListener {
            binding.llAddRecipient.visibility = View.GONE
            changeNumber3()
        }
        binding.tvBtnAddRecipient.setOnClickListener {
            binding.llAddRecipient.visibility = View.VISIBLE
            getAllBankList()
            binding.bankAccountNo.requestFocus()
            //            CustomAlertDialog.viewRecipientDialog(
            //                context
            //            )
        }
    }

    private fun transactionValidation() {
        if (binding.transferAmount.text.toString().isEmpty()) {
            binding.transferAmount.requestFocus()
            toast("Please enter your amount")
        } else {
//            getPayoutAmountTransaction()
//            showUpdateKycDetails(this)
            val transferAmountText = binding.transferAmount.text.toString().trim()
            getTransactionSendOtp()
//            getAllServiceCharge(transferAmountText)
            getAllApiPayoutCommercialCharge(transferAmountText)

        }

    }

    private fun getAllApiPayoutCommercialCharge(rechargeAmount: String) {
        this.runIfConnected {
            val getPayoutCommercialReq = GetPayoutCommercialReq(
                merchant = mStash!!.getStringValue(Constants.RegistrationId, ""),
                productId = "F0112",
                modeofPayment = "IMPS"
            )
            Log.d(TAG, "getAllApiPayoutCommercialCharge: " + Gson().toJson(getPayoutCommercialReq))
            getAllApiServiceViewModel.getAllApiPayoutCommercialCharge(getPayoutCommercialReq)
                .observe(this) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                pd.dismiss()
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        getAllApiPayoutCommercialChargeRes(response, rechargeAmount)
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

    private fun getAllApiPayoutCommercialChargeRes(response: GetPayoutCommercialRes, rechargeAmount: String) {
        if (response.isSuccess == true) {
            binding.serviceChargeLayout.visibility = View.VISIBLE
            binding.commissionLayout.visibility = View.VISIBLE
            binding.amountPayLayout.visibility = View.VISIBLE
            try {
                val rechargeAmountValue = rechargeAmount.toDoubleOrNull() ?: 0.0
                val TDSTax = 5.0 // Fixed TDS rate of 5%

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
                        serviceChargeCalculation(
                            serviceCharge,
                            gst,
                            rechargeAmount,
                            response,
                            min,
                            max
                        )
                        break
                    }
                }
                //if no matching slab was found, show an error message
                if (!isSlabMatched) {
                    isSlabMatched = false
                    binding.serviceChargeLayout.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "No matching slab found for the amount: $rechargeAmountValue",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            pd.dismiss()
            Toast.makeText(this, response.returnMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getAllWalletBalance() {
        this.runIfConnected {
            val walletBalanceReq = GetBalanceReq(
                parmUser = mStash!!.getStringValue(Constants.RegistrationId, ""),
                flag = "CreditBalance"
            )
            getAllApiServiceViewModel.getWalletBalance(walletBalanceReq).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
//                        pd.dismiss()
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
            val mainBalance = (response.data[0].result!!.toDoubleOrNull() ?: 0.0)
            getMerchantBalance(mainBalance)

            Log.d("actualBalance", "main = $mainBalance")

            val totalAmount =
                mStash!!.getStringValue(Constants.totalTransaction, "")?.toDoubleOrNull() ?: 0.0

        } else {
            toast(response.returnMessage.toString())
        }
    }

    private fun getMerchantBalance(mainBalance: Double) {
        val getMerchantBalanceReq = GetMerchantBalanceReq(
            parmUser = mStash!!.getStringValue(Constants.MerchantId, ""),
            flag = "DebitBalance"
        )
        getAllApiServiceViewModel.getAllMerchantBalance(getMerchantBalanceReq)
            .observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
//                        pd.dismiss()
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

            val totalAmount =
                mStash!!.getStringValue(Constants.totalTransaction, "")?.toDoubleOrNull() ?: 0.0
            val merchantBalance = response.data[0].debitBalance?.toDoubleOrNull() ?: 0.0

            Log.d(
                "balanceCheck",
                "MainBal = $mainBalance, merchantBal = $merchantBalance,totalAmount = $totalAmount, Status = ${totalAmount <= mainBalance && totalAmount <= merchantBalance}"
            )

            if (totalAmount <= mainBalance && totalAmount <= merchantBalance) {
                getTransferAmountToAgentWithCal(binding.transferAmount.text.toString())
            } else {
                pd.dismiss()
                Toast.makeText(
                    this,
                    "Wallet balance is low. VBal = $mainBalance, MBal = $merchantBalance, totalAmt = $totalAmount",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            pd.dismiss()
            Toast.makeText(this, response.returnMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getTransferAmountToAgentWithCal(rechargeAmount: String) {
        try {
            val currentDateTime = Utils.getCurrentDateTime()
            val retailerCommission =
                mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "")
            val retailerCommissionWithoutTDS = retailerCommission!!.toIntOrNull() ?: 0
            val bankAccountNo = binding.bankAccountNo.text.toString().trim()
            val transferAmountToAgentsReq = TransferAmountToAgentsReq(
                transferFrom = mStash!!.getStringValue(Constants.RegistrationId, ""),
                transferTo = "Admin",
                transferAmt = mStash!!.getStringValue(Constants.totalTransaction, ""),
                remark = "Payout",
                transferFromMsg = "Your Account is debited by $rechargeAmount Rs. Due to Pay Out on number ${bankAccountNo}.",
                transferToMsg = "Your Account is credited by $rechargeAmount Rs. Due to Pay Out on number ${bankAccountNo}.",
                amountType = "Payout",
//                actualTransactionAmount = Integer.valueOf(rechargeAmount),
                actualTransactionAmount = rechargeAmount,
                transIpAddress = mStash!!.getStringValue(Constants.deviceIPAddress, ""),
                parmUserName = mStash!!.getStringValue(Constants.RegistrationId, ""),
                merchantCode = mStash!!.getStringValue(Constants.MerchantId, ""),
                servicesChargeAmt = mStash!!.getStringValue(Constants.serviceCharge, "") ?: "0",
                servicesChargeGSTAmt = mStash!!.getStringValue(Constants.serviceChargeWithGST, "")
                    ?: "0",
                servicesChargeWithoutGST = (mStash!!.getStringValue(Constants.serviceCharge, "")!!
                    .toIntOrNull() ?: 0).toString(),
                customerVirtualAddress = "",
                retailerCommissionAmt = (mStash!!.getStringValue(
                    Constants.retailerCommissionWithoutTDS,
                    ""
                )!!.toIntOrNull() ?: 0).toString(),
                retailerId = "",
                paymentMode = "IMPS",
                depositBankName = "",
                branchCodeChecqueNo = "",
                apporvedStatus = "Approved",
                registrationId = mStash!!.getStringValue(Constants.RegistrationId, ""),
                benfiid = "",
                accountHolder = "",
                flag = "Y"
            )
            Log.d("mobileRechargeReq", Gson().toJson(transferAmountToAgentsReq))
            getAllApiServiceViewModel.getTransferAmountToAgents(transferAmountToAgentsReq)
                .observe(this) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
//                                 pd.dismiss()
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        getTransferAmountToAgentWithCalRes(response)
                                    }
                                }
                            }

                            ApiStatus.ERROR -> {
                                pd.dismiss()
                                Toast.makeText(
                                    this,
                                    "${it.message} try again later",
                                    Toast.LENGTH_SHORT
                                ).show()
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

    private fun getTransferAmountToAgentWithCalRes(response: TransferAmountToAgentsRes) {
        if (response.isSuccess == true) {
//            pd.dismiss()
            transactionAmountFromDMTAPI()
//            getPayoutAmountTransaction()
//            toast(response.message.toString())
        } else {
            pd.dismiss()
            toast(response.returnMessage.toString())
        }
    }

    private fun getPayoutAmountTransaction() {
        val payoutAppReq = PayoutAppReq(
            registrationID = mStash!!.getStringValue(Constants.MerchantId, ""),
            accountName = mStash!!.getStringValue(Constants.bankOwnerName, ""),
            accountNumber = binding.bankAccountNo.text.toString().trim(),
            ifscCode = mStash!!.getStringValue(Constants.ifscCode, ""),
            amount = binding.transferAmount.text.toString().trim(),
            txnNote = "RK"
        )
        Log.d("payoutAppReq", Gson().toJson(payoutAppReq))
        viewModel1.getPayoutAmountTransaction(payoutAppReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
//                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                getPayoutAmountTransactionRes(response)
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        pd.dismiss()
                        toast(it.message.toString())
                    }

                    ApiStatus.LOADING -> {
                        pd.show()
                    }
                }
            }
        }
    }

    private fun getPayoutAmountTransactionRes(response: PayoutAmountRes) {
        if (response.statuss == true || response.statuss == false) {
//            sendPayoutAmountResStatus(response.data!![0].payoutRef.toString())

            pd.dismiss()
            // UTR received, process the response and move to the success page
            val intent = Intent(this, DMTRechargeSuccessfulPage::class.java).apply {

                putExtra("transactionId", response.data!![0].payoutRef.toString())
//                putExtra("operatorName", response.operatorname.toString())
//                putExtra("mobileNumber", mobileNo.toString())
                putExtra("upiId", intent.getStringExtra("upiId")) // Fix: Use correct Intent value
                putExtra("amount", binding.transferAmount.text.toString())
                putExtra(
                    "payeeName",
                    intent.getStringExtra("payeeName")
                ) // Fix: Use correct Intent value
//                putExtra("totalAmount", totalAmountNet.toString())
                putExtra("referenceId", response.data!![0].payoutRef.toString())
//                putExtra("operationId", body.data?.operatorid.toString())
//                    putExtra("dateAndTime", response.daterefunded.toString())
//                    putExtra("tdsTax", response.tds.toString())
                putExtra("Status", response.statuss.toString())
                putExtra("message", response.message.toString())
//                putExtra("utrNo", response.data!![0].utr.toString())
                putExtra("utrNo", response.data!![0].payoutRef.toString())
                putExtra("customerBankName", mStash!!.getStringValue(Constants.bankOwnerName, ""))
                putExtra("bankIFSC", mStash!!.getStringValue(Constants.ifscCode, ""))
                putExtra("bankAccountNO", mStash!!.getStringValue(Constants.bankAccount, ""))


//                putExtra("totalAmount", mStash!!.getStringValue(Constants.totalTransaction, ""))

                //service and commission charge
//                putExtra("tdsTax", mStash!!.getStringValue(Constants.tds, "0.00"))
//                putExtra(
//                    "retailerCommissionWithoutTDS",
//                    mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "0.00")
//                )
//                putExtra(
//                    "customerCommissionWithoutTDS",
//                    mStash!!.getStringValue(Constants.customerCommissionWithoutTDS, "0.00")
//                )
                putExtra("serviceCharge", mStash!!.getStringValue(Constants.serviceCharge, "0.00"))
                putExtra(
                    "serviceChargeWithGST",
                    mStash!!.getStringValue(Constants.serviceChargeWithGST, "0.00")
                )
                putExtra(
                    "totalTransaction",
                    mStash!!.getStringValue(Constants.totalTransaction, "0.00")
                )
            }
            startActivity(intent)
            toast("Transaction Successful")
        } else {
            Toast.makeText(this, response.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

//    private fun getTransferAmountToAgentWithCal(rechargeAmount: String) {
//        val currentDateTime = Utils.getCurrentDateTime()
//        val transferAmountToAgentsWithCalculationReq = TransferAmountToAgentsWithCalculationReq(
//            Agentid = mStash!!.getStringValue(Constants.RegistrationId, ""),
//            APIName = "Payout Api",
//            Category = "Payout",
//            OperatorCode = "Payout",
//            Amount = Integer.valueOf(rechargeAmount),
//            CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, ""),
//            TransIpAddress = mStash!!.getStringValue(Constants.deviceIPAddress, ""),
//            AmountType = "Payout",
//            Remark = "Payout",
//            TransactionDate = currentDateTime,
//            MobileNo = binding.etMobileNumber.text.toString().trim()
//        )
//        Log.d("mobileRechargeReq", Gson().toJson(transferAmountToAgentsWithCalculationReq))
//        viewModel.getTransferAmountToAgentWithCal(transferAmountToAgentsWithCalculationReq)
//            .observe(this) { resource ->
//                resource?.let {
//                    when (it.apiStatus) {
//                        ApiStatus.SUCCESS -> {
////                        pd.dismiss()
//                            it.data?.let { users ->
//                                users.body()?.let { response ->
//                                    getTransferAmountToAgentWithCalRes(response)
//                                }
//                            }
//                        }
//
//                        ApiStatus.ERROR -> {
//                            pd.dismiss()
//                            toast(it.message.toString())
//                        }
//
//                        ApiStatus.LOADING -> {
//                            pd.show()
//                        }
//                    }
//                }
//            }
//
//    }

    private fun getTransferAmountToAgentWithCalRes(response: TransferAmountToAgentsWithCalculationRes?) {
        if (response!!.Status == true) {
            pd.dismiss()
//            transactionAmountFromDMTAPI()
//            getPayoutAmountTransaction()
//            toast(response.message.toString())
        } else {
            pd.dismiss()
            toast(response.message.toString())
        }
    }

    private fun getAllServiceCharge(rechargeAmount: String) {
        val getAPIServiceChargeReq = GetAPIServiceChargeReq(
//            APIName = mStash!!.getStringValue(Constants.APIName, ""),
//            APIName = "DMT Api",
////            Category = mStash!!.getStringValue(Constants.APIName, ""),
//            Category = "DMT",
//            Code = "DMT",
            APIName = "Payout Api",
            Category = "Payout",
            Code = "Payout",
            CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
        )

        Log.d("getAllServiceCharge", Gson().toJson(getAPIServiceChargeReq))

        viewModel.getAllAPIServiceCharge(getAPIServiceChargeReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response ->
//                                getAllServiceChargeApiRes(response, rechargeAmount)
                            }
                        }
                    }

                    ApiStatus.ERROR -> pd.dismiss()
                    ApiStatus.LOADING -> pd.show()
                }
            }
        }
    }

    @SuppressLint("DefaultLocale", "SetTextI18n", "SuspiciousIndentation")
    private fun getAllServiceChargeApiRes(
        response: GetAPIServiceChargeRes?, rechargeAmount: String
    ) {
        response?.let {
            if (it.Status == true) {
                binding.commissionLayout.visibility = View.VISIBLE
                binding.amountPayLayout.visibility = View.VISIBLE

                val rechargeAmountValue = rechargeAmount.toDoubleOrNull() ?: 0.0
                val TDSTax = 5.0 // Fixed TDS rate of 5%

                // Retailer Commission Calculation
                val retailerCommissionWithTDS = calculateCommissionWithTDS(
                    commission = response.RetailerCommission?.toDoubleOrNull() ?: 0.0,
                    commissionType = response.RetailerCommissionType,
                    amount = rechargeAmountValue,
                    tdsTax = TDSTax,
                    isRetailer = true,
                    isDistributer = false,
                    isMDistributer = false
                )
                val subDisCommissionWithTDS = calculateCommissionWithTDS(
                    commission = response.SubDisCommission?.toDoubleOrNull() ?: 0.0,
                    commissionType = response.SubDisCommissionType,
                    amount = rechargeAmountValue,
                    tdsTax = TDSTax,
                    isRetailer = false,
                    isDistributer = true,
                    isMDistributer = false
                )
                val mDisCommissionWithTDS = calculateCommissionWithTDS(
                    commission = response.DisCommission?.toDoubleOrNull() ?: 0.0,
                    commissionType = response.DisCommissionType,
                    amount = rechargeAmountValue,
                    tdsTax = TDSTax,
                    isRetailer = false,
                    isDistributer = false,
                    isMDistributer = true
                )
                // Customer Commission Calculation
                val customerCommissionWithTDS = calculateCommissionWithTDS(
                    commission = response.CustomerCommission?.toDoubleOrNull() ?: 0.0,
                    commissionType = response.CustomerCommissionType,
                    amount = rechargeAmountValue,
                    tdsTax = TDSTax,
                    isRetailer = false,
                    isMDistributer = true,
                    isDistributer = false
                )

                // Service Charge with GST Calculation
                val gst = 18.0 // Fixed GST rate of 18%
                val serviceCharge = response.ServiceCharge?.toDoubleOrNull() ?: 0.0
//                val totalServiceChargeWithGst =
//                    serviceChargeCalculation(serviceCharge, gst, rechargeAmount, response)

                // Total Recharge Amount Calculation
//                val totalRechargeAmount = rechargeAmountValue +
////                        retailerCommissionWithTDS +
////                        customerCommissionWithTDS +
////                        mDisCommissionWithTDS +
////                        subDisCommissionWithTDS +
//                        totalServiceChargeWithGst

                // Store the total transaction in mStash
//                mStash!!.setStringValue(
//                    Constants.totalTransaction, String.format("%.2f", totalRechargeAmount)
//                )
//                binding.totalTransferText.setText(String.format("%.2f", totalRechargeAmount))
//                Log.d("Total Recharge Amount", String.format("%.2f", totalRechargeAmount))
            } else {
                toast(response.message.toString())
            }
        }
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun calculateCommissionWithTDS(
        commission: Double,
        commissionType: String?,
        amount: Double,
        tdsTax: Double,
        isRetailer: Boolean,
        isMDistributer: Boolean,
        isDistributer: Boolean
    ): Double {
        val commissionWithTDS = when (commissionType) {
            "Percentage" -> {
                // Calculate commission as a percentage of the amount
                val commissionAmount = amount * (commission / 100)
                val commissionTDS = commissionAmount * (tdsTax / 100)
                val finalCommission = commissionAmount - commissionTDS

                if (isRetailer) {
                    mStash!!.setStringValue(
                        Constants.retailerCommissionWithoutTDS,
                        String.format("%.2f", finalCommission)
                    )
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
//                    binding.retailerCommissionText.setText(String.format("%.2f", commissionAmount))
//                    binding.retailerCommissionWithoutTDs.setText(String.format("%.2f", finalCommission))
//                    binding.tdsChargetext.setText(String.format("%.2f", commissionTDS))
//                    binding.retailerCommissionName.text = "Retailer Commission %"
                } else if (isDistributer) {
                    mStash!!.setStringValue(
                        Constants.adminCommissionWithoutTDS, String.format("%.2f", finalCommission)
                    )
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
                } else if (isMDistributer) {
                    mStash!!.setStringValue(
                        Constants.mDistributerCommissionWithoutTDS,
                        String.format("%.2f", finalCommission)
                    )
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
                } else {
                    mStash!!.setStringValue(
                        Constants.customerCommissionWithoutTDS,
                        String.format("%.2f", finalCommission)
                    )
//                    binding.customerCommissionText.setText(String.format("%.2f", commissionAmount))
//                    binding.customerCommissionWithoutTDS.setText(
//                        String.format(
//                            "%.2f",
//                            finalCommission
//                        )
//                    )
//                    binding.customerCommissionName.text = "Customer Commission %"

                }

                commissionAmount
            }

            "Amount" -> {
                // Fixed commission in amount
                val commissionTDS = commission * (tdsTax / 100)
                val finalCommission = commission - commissionTDS

                if (isRetailer) {
                    mStash!!.setStringValue(
                        Constants.retailerCommissionWithoutTDS,
                        String.format("%.2f", finalCommission)
                    )
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
//                    binding.retailerCommissionText.setText(String.format("%.2f", commission))
//                    binding.retailerCommissionWithoutTDs.setText(
//                        String.format(
//                            "%.2f",
//                            finalCommission
//                        )
//                    )
//                    binding.tdsChargetext.setText(String.format("%.2f", commissionTDS))
//                    binding.retailerCommissionName.text = "Retailer Commission RS"
                } else if (isDistributer) {
                    mStash!!.setStringValue(
                        Constants.adminCommissionWithoutTDS, String.format("%.2f", finalCommission)
                    )
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
                } else if (isMDistributer) {
                    mStash!!.setStringValue(
                        Constants.mDistributerCommissionWithoutTDS,
                        String.format("%.2f", finalCommission)
                    )
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
                } else {
                    mStash!!.setStringValue(
                        Constants.customerCommissionWithoutTDS,
                        String.format("%.2f", finalCommission)
                    )
//                    binding.customerCommissionText.setText(String.format("%.2f", commission))
//                    binding.customerCommissionWithoutTDS.setText(
//                        String.format(
//                            "%.2f",
//                            finalCommission
//                        )
//                    )
//                    binding.customerCommissionName.text = "Customer Commission Rs"
                }

                commission
            }

            else -> 0.0
        }

        return commissionWithTDS
    }

    @SuppressLint("DefaultLocale", "SetTextI18n")
    private fun serviceChargeCalculation(
        serviceCharge: Double,
        gstRate: Double,
        rechargeAmount: String,
//        response: List<GetAPIServiceChargeRes>,
        response: GetPayoutCommercialRes,
        min: Double,
        max: Double
    ): Double {
        val rechargeAmountValue = rechargeAmount.toDoubleOrNull() ?: 0.0
        var totalServiceChargeWithGst = 0.0
        var serviceChargeWithGst = 0.0
        var slabLimit: String? = null
        binding.serviceChargeLayout.visibility = View.VISIBLE

        if (mStash!!.getStringValue(Constants.RegistrationId, "")!!.isNotEmpty()) {
            slabLimit = when {
                rechargeAmountValue <= max -> max.toString()
                else -> {
                    Toast.makeText(
                        this,
                        "No valid slab found for the amount: $rechargeAmountValue",
                        Toast.LENGTH_SHORT
                    ).show()
                    null
                }
            }
        } else {
            toast("No valid slab found for the amount: $rechargeAmountValue")

        }

        for (i in response.data.indices) {
//            if (response.data[i].slabName?.contains(slabLimit.toString()) == true) {
            if (slabLimit != null) {
                Log.d("ServiceType", "${response.data[i].serviceType} - Index $i")
                serviceChargeWithGst = when (response.data[i].serviceType) {
                    "Amount" -> {
                        binding.serviceChargeName.text = "Service Charge Rs"
                        val serviceChargeGst = serviceCharge * (gstRate / 100)
                        mStash?.setStringValue(
                            Constants.serviceCharge,
                            String.format("%.2f", serviceCharge)
                        )
                        serviceCharge + serviceChargeGst
                    }

                    "Percentage" -> {
                        binding.serviceChargeName.text = "Service Charge %"
                        val serviceInAmount = rechargeAmountValue * (serviceCharge / 100)
                        val serviceWithGst = serviceInAmount * (gstRate / 100)
                        mStash?.setStringValue(
                            Constants.serviceCharge,
                            String.format("%.2f", serviceInAmount)
                        )
                        serviceInAmount + serviceWithGst
                    }

                    else -> 0.0
                }
                break
//                totalServiceChargeWithGst += serviceChargeWithGst
            }
        }

        if (serviceChargeWithGst == 0.0) {
            Toast.makeText(
                this,
                "No matching slab found for the entered amount",
                Toast.LENGTH_SHORT
            ).show()
            return 0.0
        }

        val actualAmount = serviceChargeWithGst + rechargeAmountValue
        mStash?.apply {
            setStringValue(
                Constants.actualAmountServiceChargeWithGST,
                String.format("%.2f", actualAmount)
            )
            setStringValue(
                Constants.serviceChargeWithGST,
                String.format("%.2f", serviceChargeWithGst)
            )
            setStringValue(Constants.totalTransaction, String.format("%.2f", actualAmount))
        }

        binding.apply {
            gstWithServiceCharge.setText(String.format("%.2f", serviceChargeWithGst))
            serviceChargeTotal.setText(String.format("%.2f", serviceCharge))
            totalTransferText.setText(String.format("%.2f", actualAmount))
        }

        Log.d("TotalRechargeAmount", String.format("%.2f", actualAmount))
        Log.d("totalAmountWithGst", String.format("%.2f", serviceChargeWithGst))

        return serviceChargeWithGst
    }
//
//    @SuppressLint("DefaultLocale", "SetTextI18n")
//    private fun serviceChargeCalculation(
//        serviceCharge: Double,
//        gstRate: Double,
//        rechargeAmount: String,
//        response: GetAPIServiceChargeRes
//    ): Double {
//        val rechargeAmountValue = rechargeAmount.toDoubleOrNull() ?: 0.0
//
//        val totalAmountWithGst = when (response.ServiceType) {
//            "Amount" -> {
//                // Service charge is a fixed amount
//                binding.serviceChargeName.text = "Service Charge Rs"
//                val serviceChargeGst = serviceCharge * (gstRate / 100)
////                val serviceWithGST = serviceCharge + serviceChargeGst
////                binding.gstWithServiceCharge.setText(String.format("%.2f", totalAmountWithGst))
//                mStash!!.setStringValue(
//                    Constants.serviceCharge, String.format("%.2f", serviceCharge)
//                )
//
//                serviceCharge + serviceChargeGst
////                mStash.setStringValue(Constants.serviceCharge, )
//            }
//
//            "Percentage" -> {
//                // Service charge is a percentage of the recharge amount
//                binding.serviceChargeName.text = "Service Charge %"
//                val serviceInAmount = rechargeAmountValue * (serviceCharge / 100)
//                val serviceWithGst = serviceInAmount * (gstRate / 100)
//                binding.gstWithServiceCharge.setText(String.format("%.2f", serviceWithGst))
//
//                mStash!!.setStringValue(
//                    Constants.serviceCharge, String.format("%.2f", serviceInAmount)
//                )
//
//                serviceInAmount + serviceWithGst
//
//            }
//
//            else -> 0.0
//        }
//
//        val actualAmount = totalAmountWithGst + rechargeAmountValue
//        mStash!!.setStringValue(
//            Constants.actualAmountServiceChargeWithGST, String.format("%.2f", actualAmount)
//        )
//        // Display service charge and total amount with GST in the UI
//        mStash!!.setStringValue(
//            Constants.serviceChargeWithGST, String.format("%.2f", totalAmountWithGst)
//        )
//        binding.gstWithServiceCharge.setText(String.format("%.2f", totalAmountWithGst))
//        binding.serviceCharge.setText(String.format("%.2f", serviceCharge))
////        binding.totalTransferText.setText(
////            String.format("%.2f", totalAmountWithGst))
//
//        Log.d("totalAmountWithGst", totalAmountWithGst.toString())
//
//        // Return the total service charge (with GST) to include in the final transaction
//        return totalAmountWithGst
//    }


//
//    private fun getPayoutAmountTransaction() {
//        val payoutAppReq = PayoutAppReq(
//            registrationID = mStash!!.getStringValue(Constants.MerchantId, ""),
//            accountName = mStash!!.getStringValue(Constants.bankOwnerName,""),
//            accountNumber = mStash!!.getStringValue(Constants.bankAccount,""),
//            ifscCode = mStash!!.getStringValue(Constants.ifscCode,""),
//            amount = binding.transferAmount.text.toString().trim(),
//            txnNote = "Rk"
//        )
//        Log.d("payoutAppReq", Gson().toJson(payoutAppReq))
//        viewModel1.getPayoutAmountTransaction(payoutAppReq).observe(this) { resource ->
//            resource?.let {
//                when (it.apiStatus) {
//                    ApiStatus.SUCCESS -> {
////                        pd.dismiss()
//                        it.data?.let { users ->
//                            users.body()?.let { response ->
//                                getPayoutAmountTransactionRes(response)
//                            }
//                        }
//                    }
//
//                    ApiStatus.ERROR -> {
//                        pd.dismiss()
//                        toast(it.message.toString())
//                    }
//
//                    ApiStatus.LOADING -> {
//                        pd.show()
//                    }
//                }
//            }
//        }
//    }
//
//    private fun getPayoutAmountTransactionRes(response: PayoutAmountRes) {
//        if (response.data!![0].status == "Processed") {
////            sendPayoutAmountResStatus(response.data!![0].payoutRef.toString())
//
//            pd.dismiss()
//            // UTR received, process the response and move to the success page
//            val intent = Intent(this, DMTRechargeSuccessfulPage::class.java).apply {
//
//                putExtra("transactionId", response.data!![0].payoutRef.toString())
////                putExtra("operatorName", response.operatorname.toString())
//                putExtra("mobileNumber", mobileNo.toString())
//                putExtra("upiId", intent.getStringExtra("upiId")) // Fix: Use correct Intent value
//                putExtra("amount", binding.transferAmount.text.toString())
//                putExtra(
//                    "payeeName",
//                    intent.getStringExtra("payeeName")
//                ) // Fix: Use correct Intent value
////                putExtra("totalAmount", totalAmountNet.toString())
//                putExtra("referenceId", response.data!![0].payoutRef.toString())
////                putExtra("operationId", body.data?.operatorid.toString())
////                    putExtra("dateAndTime", response.daterefunded.toString())
////                    putExtra("tdsTax", response.tds.toString())
//                putExtra("Status", response.status.toString())
//                putExtra("message", response.message.toString())
////                putExtra("utrNo", response.data!![0].utr.toString())
//                putExtra("utrNo", response.data!![0].payoutRef.toString())
//                putExtra("customerBankName", mStash!!.getStringValue(Constants.bankOwnerName,""))
//                putExtra("bankIFSC", mStash!!.getStringValue(Constants.ifscCode,""))
//                putExtra("bankAccountNO", mStash!!.getStringValue(Constants.bankAccount,""))
//
//
////                putExtra("totalAmount", mStash!!.getStringValue(Constants.totalTransaction, ""))
//
//                //service and commission charge
////                putExtra("tdsTax", mStash!!.getStringValue(Constants.tds, "0.00"))
////                putExtra(
////                    "retailerCommissionWithoutTDS",
////                    mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "0.00")
////                )
////                putExtra(
////                    "customerCommissionWithoutTDS",
////                    mStash!!.getStringValue(Constants.customerCommissionWithoutTDS, "0.00")
////                )
//                putExtra("serviceCharge", mStash!!.getStringValue(Constants.serviceCharge, "0.00"))
//                putExtra(
//                    "serviceChargeWithGST",
//                    mStash!!.getStringValue(Constants.serviceChargeWithGST, "0.00")
//                )
//                putExtra(
//                    "totalTransaction",
//                    mStash!!.getStringValue(Constants.totalTransaction, "0.00")
//                )
//            }
//            startActivity(intent)
//            toast("Transaction Successful")
//        } else {
//            toast(response.message.toString())
//        }
//    }

    private fun sendPayoutAmountResStatus(referenceId: String) {
        val payoutStatusReq = PayoutStatusReq(
            txnRefranceID = referenceId,
            registrationID = mStash!!.getStringValue(Constants.MerchantId, "")
        )
        Log.d("payoutStatusReq", "" + Gson().toJson(payoutStatusReq))

        // Show progress dialog initially
        pd.show()

        // Start a countdown timer for a maximum of 30 seconds with 3-second intervals
        timer11 = object : CountDownTimer(5 * 60000, 3000) { // 5 min duration, 3 seconds interval
            override fun onTick(millisUntilFinished: Long) {
                // Call the status API every 3 seconds
                viewModel1.getPayoutStatus(payoutStatusReq)
                    .observe(this@DMTMobileActivity) { resource ->
                        resource?.let {
                            when (it.apiStatus) {
                                ApiStatus.SUCCESS -> {
                                    it.data?.let { users ->
                                        users.body()?.let { response ->
                                            Log.d(
                                                "payoutStatusReq",
                                                "4444444     " + Gson().toJson(response)
                                            )

                                            // Check if UTR is received
                                            if (response.data!![0].utr != null) {
                                                timer11.cancel()
                                                pd.dismiss() // Dismiss progress dialog

                                                sendAllAmountStatusRes(response) // Process the result
//                                            cancel() // Stop the timer once UTR is received

//                                            {"registrationID":"BOS-2197","txnRefranceID":"20241023220141PBOS2197"}
                                            }
                                        }
                                    }
                                }

                                ApiStatus.ERROR -> {
                                    pd.dismiss() // Dismiss progress dialog on error
                                    toast("Something went wrong")
//                                cancel() // Stop the timer on error
                                }

                                ApiStatus.LOADING -> {
                                    pd.show()
                                }
                            }
                        }
                    }
            }

            override fun onFinish() {
                // After 30 seconds, dismiss the dialog if UTR is still not received
                pd.dismiss()
                toast("UTR not received within 5 minutes. Please try again.")
            }
        }

        // Start the countdown timer
        timer11.start()
    }

    private fun sendAllAmountStatusRes(response: PayoutAmountRes) {
        if (response.data != null) {
            pd.dismiss()
            // UTR received, process the response and move to the success page
            val intent = Intent(this, DMTRechargeSuccessfulPage::class.java).apply {

                putExtra("transactionId", response.data!![0].payoutRef.toString())
//                putExtra("operatorName", response.operatorname.toString())
                putExtra("mobileNumber", mobileNo.toString())
                putExtra("upiId", intent.getStringExtra("upiId")) // Fix: Use correct Intent value
                putExtra("amount", binding.transferAmount.text.toString())
                putExtra(
                    "payeeName", intent.getStringExtra("payeeName")
                ) // Fix: Use correct Intent value
//                putExtra("totalAmount", totalAmountNet.toString())
                putExtra("referenceId", response.data!![0].payoutRef.toString())
//                putExtra("operationId", body.data?.operatorid.toString())
//                    putExtra("dateAndTime", response.daterefunded.toString())
//                    putExtra("tdsTax", response.tds.toString())
                putExtra("Status", response.status.toString())
                putExtra("message", response.message.toString())
                putExtra("utrNo", response.data!![0].utr.toString())
                putExtra("customerBankName", mStash!!.getStringValue(Constants.bankOwnerName, ""))
                putExtra("bankIFSC", mStash!!.getStringValue(Constants.ifscCode, ""))
                putExtra("bankAccountNO", mStash!!.getStringValue(Constants.bankAccount, ""))


//                putExtra("totalAmount", mStash!!.getStringValue(Constants.totalTransaction, ""))

                //service and commission charge
                putExtra("tdsTax", mStash!!.getStringValue(Constants.tds, "0.00"))
                putExtra(
                    "retailerCommissionWithoutTDS",
                    mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "0.00")
                )
                putExtra(
                    "customerCommissionWithoutTDS",
                    mStash!!.getStringValue(Constants.customerCommissionWithoutTDS, "0.00")
                )
                putExtra("serviceCharge", mStash!!.getStringValue(Constants.serviceCharge, "0.00"))
                putExtra(
                    "serviceChargeWithGST",
                    mStash!!.getStringValue(Constants.serviceChargeWithGST, "0.00")
                )
                putExtra(
                    "totalTransaction", mStash!!.getStringValue(Constants.totalTransaction, "0.00")
                )

//                    putExtra("CustomerCharge", response.customercharge.toString())
//                    putExtra("BCShare", response.bcShare.toString())
//                    putExtra("GST", response.gst.toString())
//                    putExtra("NetCommission", response.netcommission.toString())
//                    putExtra("TDS", response.tds.toString())
            }
            startActivity(intent)
            toast("Transaction Successful")
        } else {
            toast(response.message.toString())
        }
    }


    private fun transactionAmountFromDMTAPI() {
        this.runIfConnected {
            val referenceID: String = ConstantClass.generateRandomNumber()
            val transactionReq = TransactionReq(
                MobileNumber = mobileNo,
                Pincode = "110045",
                Address = "Delhi",
                DOB = "20-08-200",
                BeneID = mStash!!.getStringValue(Constants.beneId, ""),
                TxnType = "IMPS",
                Amount = binding.transferAmount.text.toString().trim(),
                Stateresp = mStash!!.getStringValue(Constants.stateRespDMT, ""),
                OTP = binding.otp.text.toString(),
                RegistrationID = mStash!!.getStringValue(Constants.MerchantId, ""),

                )
            Log.d("transactionReq", Gson().toJson(transactionReq))
            viewModel.transactionAmountFromDMT(transactionReq).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    getAllTransactionRes(response, referenceID)
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

    private fun getAllTransactionRes(response: TransactionRes, referenceID: String) {
        if (response.status == true) {
            pd.dismiss()
            // UTR received, process the response and move to the success page
            val intent = Intent(this, DMTRechargeSuccessfulPage::class.java).apply {

                putExtra("transactionId", response.ackno.toString())
//                putExtra("operatorName", response.operatorname.toString())
                putExtra("mobileNumber", mobileNo.toString())
                putExtra("upiId", intent.getStringExtra("upiId")) // Fix: Use correct Intent value
                putExtra("amount", binding.transferAmount.text.toString())
                putExtra(
                    "payeeName", intent.getStringExtra("payeeName")
                ) // Fix: Use correct Intent value
//                putExtra("totalAmount", totalAmountNet.toString())
                putExtra("referenceId", response.txnReferanceID.toString())
//                putExtra("operationId", body.data?.operatorid.toString())
//                    putExtra("dateAndTime", response.daterefunded.toString())
//                    putExtra("tdsTax", response.tds.toString())
                putExtra("Status", response.status.toString())
                putExtra("message", response.message.toString())
                putExtra("utrNo", response.utr.toString())
                putExtra("customerBankName", mStash!!.getStringValue(Constants.bankOwnerName, ""))
                putExtra("bankIFSC", mStash!!.getStringValue(Constants.ifscCode, ""))
                putExtra("bankAccountNO", response.accountNumber.toString())


//                putExtra("totalAmount", mStash!!.getStringValue(Constants.totalTransaction, ""))

                //service and commission charge
                putExtra("tdsTax", mStash!!.getStringValue(Constants.tds, "0.00"))
                putExtra(
                    "retailerCommissionWithoutTDS",
                    mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "0.00")
                )
                putExtra(
                    "customerCommissionWithoutTDS",
                    mStash!!.getStringValue(Constants.customerCommissionWithoutTDS, "0.00")
                )
                putExtra("serviceCharge", mStash!!.getStringValue(Constants.serviceCharge, "0.00"))
                putExtra(
                    "serviceChargeWithGST",
                    mStash!!.getStringValue(Constants.serviceChargeWithGST, "0.00")
                )
                putExtra(
                    "totalTransaction", mStash!!.getStringValue(Constants.totalTransaction, "0.00")
                )

//                    putExtra("CustomerCharge", response.customercharge.toString())
//                    putExtra("BCShare", response.bcShare.toString())
//                    putExtra("GST", response.gst.toString())
//                    putExtra("NetCommission", response.netcommission.toString())
//                    putExtra("TDS", response.tds.toString())
            }
            startActivity(intent)
            Toast.makeText(this, response.remarks.toString(), Toast.LENGTH_LONG).show()
//            toast("Transaction Successful")
        } else {
            toast(response.message.toString())
        }

//        if (response.status == true) {
////            transactionStatusAPICalling(referenceID)
//            toast(response.message.toString())
//        } else {
//            toast(response.message.toString())
//        }
    }

    private fun transactionStatusAPICalling(referenceID: String) {
        val transactStatusReq = TransactStatusReq(
            referenceid = referenceID,
            RegistrationID = mStash!!.getStringValue(Constants.MerchantId, "")
        )

        viewModel.getAllTransactionStatus(transactStatusReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response -> getAllTransactionStatusRes(response) }
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

    @SuppressLint("DefaultLocale")
    private fun getAllTransactionStatusRes(response: TransactStatusRes) {
        if (response.status == true) {

            val totalAmount = listOf(
                response.amount?.toDoubleOrNull() ?: 0.0,
                response.bcShare?.toDoubleOrNull() ?: 0.0,
                response.customercharge?.toDoubleOrNull() ?: 0.0,
                response.gst?.toDoubleOrNull() ?: 0.0,
                response.netcommission?.toDoubleOrNull() ?: 0.0,
                response.tds?.toDoubleOrNull() ?: 0.0
            ).sum()

            // Format the total amount to show in rupees and paise (2 decimal places)
            val totalAmountNet = String.format("%.2f", totalAmount)

            val intent = Intent(this, DMTRechargeSuccessfulPage::class.java).apply {

                putExtra("transactionId", response.txnStatus.toString())
//                putExtra("operatorName", response.operatorname.toString())
                putExtra("mobileNumber", mobileNo.toString())
                putExtra("amount", response.amount.toString())
//                putExtra("totalAmount", totalAmountNet.toString())
                putExtra("referenceId", response.referenceid.toString())
//                putExtra("operationId", body.data?.operatorid.toString())
                putExtra("dateAndTime", response.daterefunded.toString())
                putExtra("tdsTax", response.tds.toString())
                putExtra("Status", response.status.toString())
                putExtra("message", response.message.toString())

//                putExtra("totalAmount", mStash!!.getStringValue(Constants.totalTransaction, ""))

                //service and commission charge
                putExtra("tdsTax", mStash!!.getStringValue(Constants.tds, "0.00"))
                putExtra(
                    "retailerCommissionWithoutTDS",
                    mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "0.00")
                )
                putExtra(
                    "customerCommissionWithoutTDS",
                    mStash!!.getStringValue(Constants.customerCommissionWithoutTDS, "0.00")
                )
                putExtra("serviceCharge", mStash!!.getStringValue(Constants.serviceCharge, "0.00"))
                putExtra(
                    "serviceChargeWithGST",
                    mStash!!.getStringValue(Constants.serviceChargeWithGST, "0.00")
                )
                putExtra(
                    "totalTransaction", mStash!!.getStringValue(Constants.totalTransaction, "0.00")
                )

                putExtra("CustomerCharge", response.customercharge.toString())
                putExtra("BCShare", response.bcShare.toString())
                putExtra("GST", response.gst.toString())
                putExtra("NetCommission", response.netcommission.toString())
                putExtra("TDS", response.tds.toString())
            }
            startActivity(intent)
        } else {
            toast(response.message ?: "Transaction failed")
        }
    }

    private fun getAllBankList() {
        val dmtBankListReq = DMTBankListReq(
            RegistrationId = mStash!!.getStringValue(Constants.MerchantId, ""),
            CompanyCode = "CMP1347"
        )
        Log.d("getAllBankList", Gson().toJson(dmtBankListReq))
        viewModel.getAllDMTBankList(dmtBankListReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response -> getAllBankListRes(response) }
                        }
                    }

                    ApiStatus.ERROR -> {
                        Toast.makeText(this, "Error fetching bank list", Toast.LENGTH_SHORT).show()
                        pd.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        pd.dismiss()
                    }
                }
            }
        }
    }

    private fun getAllBankListRes(response: List<DMTBankListRes>) {
        if (response.isNotEmpty() && response[0].Status == true) {
            if (Constants.bankListName == null) {
                Constants.bankListName = ArrayList()
            }
            Constants.bankListName!!.clear()
            Constants.bankListName!!.add("Select your bank name")
            response.forEach { bankListRes ->
                Constants.bankListName!!.add(bankListRes.BankName.toString())

                Constants.bankListNameMap!![bankListRes.BankName.toString()] =
                    Integer.valueOf(bankListRes.BankId.toString())
//                Constants.bankListNameMapForGettingbankListName!![bankListRes.BankId!!.toInt()] =
//                    bankListRes.BankName.toString()
//                    Constants.bankListName!!.add(bankListRes.BankName.toString())
//                    Constants.bankListId!!.add(bankListRes.BankId.toString())
            }
            Constants.getAllBankListAdapter!!.notifyDataSetChanged()
        } else {
            toast(response[0].message.toString())
        }
    }

    private fun initView() {
        pd = PD(this)
        fetchListData = ArrayList()
        bankList = ArrayList()
        mStash = MStash.getInstance(this)
        Constants.bankListName = ArrayList()
        Constants.bankListName!!.add("Select your bank name")
        Constants.bankListNameMap = HashMap()

        viewModel = ViewModelProvider(this, MoneyTransferViewModelFactory(MoneyTransferRepository(RetrofitClient.apiAllAPIService)))[MoneyTransferViewModel::class.java]

        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface)))[GetAllApiServiceViewModel::class.java]

        viewModel1 = ViewModelProvider(
            this, PayoutViewModelFactory(PayoutRepository(RetrofitClient.apiInterface))
        )[PayoutViewModel::class.java]

        binding.llAddCustomer.visibility = View.GONE
        binding.llRegister.visibility = View.GONE
        binding.tvBtnAddRecipient.visibility = View.GONE
    }



    private fun registerRemitters() {
        val fullName = binding.etRegisterCustomerName.text.toString().trim()
        var nameParts: Array<String> = arrayOf()
        try {
            nameParts = fullName.split(" ").toTypedArray()
        } catch (e: Exception) {
        }
        if (binding.etRegisterMobileNumber.text == null || binding.etRegisterMobileNumber.text.length < 10) {
            Toast.makeText(context, "Please enter valid mobile number", Toast.LENGTH_SHORT).show()
        } else if (binding.etRegisterOTP.text == null || binding.etRegisterOTP.text.length < 6) {
            Toast.makeText(context, "Please enter valid OTP", Toast.LENGTH_SHORT).show()
        } else if (binding.etRegisterCustomerName.text.isEmpty()) {
            Toast.makeText(context, "Please enter customer name", Toast.LENGTH_SHORT).show()
        }/*else if (nameParts[1].length == 0){
            Toast.makeText(context, "Please enter complete customer name", Toast.LENGTH_SHORT).show()
        }*/ else if (binding.etRegisterDOB.text == null) {
            Toast.makeText(context, "Please enter DOB", Toast.LENGTH_SHORT).show()
        } else if (binding.etRegisterAddress.text == null) {
            Toast.makeText(context, "Please enter address", Toast.LENGTH_SHORT).show()
        } else if (binding.etRegisterLandmark.text == null) {
            Toast.makeText(context, "Please enter landmark", Toast.LENGTH_SHORT).show()
        } else if (binding.etRegisterPincode.text == null || binding.etRegisterPincode.text.length < 6) {
            Toast.makeText(context, "Please enter valid pinCode", Toast.LENGTH_SHORT).show()
        } else {
            getRegister()
        }
    }


    private fun registerBeneficiary() {
        if (binding.etAddReceiptBenficiaryName.text == null) {
            Toast.makeText(context, "Please enter Beneficiary name", Toast.LENGTH_SHORT).show()
        } else if (bankList_txt == "0") {
            toast("Select your bank name")
        } else if (binding.etAddReceiptIFSCCode.text == null) {
            Toast.makeText(context, "Please enter IFSC code", Toast.LENGTH_SHORT).show()
        } else if (binding.etAddReceiptBankAccountNumber.text == null) {
            Toast.makeText(context, "Please enter bank account number", Toast.LENGTH_SHORT).show()
        } else {
            getRegisterBeneficiary()
        }
    }

    private fun getRegisterBeneficiary() {

        mStash!!.setStringValue(Constants.PinCodeDMT, binding.pinCode.text.toString().trim())
        mStash!!.setStringValue(Constants.addressOwnerDMT, binding.address.text.toString().trim())
        mStash!!.setStringValue(Constants.dobOwnerDMT, binding.dobOwner.text.toString().trim())
        val registerBaneficiaryReq = RegisterBaneficiaryReq(
            MobileNumber = mobileNo,
            BeneName = binding.etAddReceiptBenficiaryName.text.toString().trim(),
            BankID = bankList_txt,
            AccountNumber = binding.etAddReceiptBankAccountNumber.text.toString().trim(),
            IfscCode = binding.etAddReceiptIFSCCode.text.toString().trim(),
            DOB = binding.dobOwner.text.toString().trim(),
            Address = binding.address.text.toString().trim(),
            Pincode = binding.pinCode.text.toString().trim(),
            RegistrationID = mStash!!.getStringValue(Constants.MerchantId, "")
        )
        viewModel.registerBeneficiary(registerBaneficiaryReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response -> getRegisterBeneficiaryRes(response) }
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

    private fun getRegisterBeneficiaryRes(response: RegisterBaneficiaryRes) {
        if (response.status == true) {
            binding.llAddRecipient.visibility = View.GONE
            toast(response.message.toString())
        } else {
            toast(response.message.toString())
        }
    }

    private fun changeNumber() {
        binding.llRegister.visibility = View.GONE
        binding.etMobileNumber.setBackgroundResource(R.drawable.bg_button_plane)
        binding.etMobileNumber.isClickable = true
        binding.etMobileNumber.isFocusable = true
        binding.etMobileNumber.isFocusableInTouchMode = true
        binding.tvBtnGo.alpha = 1f
        binding.tvBtnGo.isClickable = true
        binding.tvBtnChangeNumber.alpha = 0.5f
        binding.tvBtnChangeNumber.isClickable = false
    }

    private fun changeNumber2() {
        binding!!.etRegisterMobileNumber.setText("")
        binding!!.etRegisterOTP.setText("")
        binding!!.etRegisterCustomerName.setText("")
        binding!!.etRegisterDOB.setText("")
        binding!!.etRegisterAddress.setText("")
        binding!!.etRegisterLandmark.setText("")
        binding!!.etRegisterCity.setText("")
        binding!!.etRegisterState.setText("")
        binding!!.etRegisterPincode.setText("")
    }

    private fun changeNumber3() {
        binding!!.etAddReceiptBenficiaryName.setText("")
        binding!!.etAddReceiptIFSCCode.setText("")
        binding!!.etAddReceiptBankAccountNumber.setText("")
    }

    private fun validation() {
        if (binding.etMobileNumber.text == null || binding.etMobileNumber.text.length < 10) {
            Toast.makeText(context, "Please enter valid mobile number", Toast.LENGTH_SHORT).show()
        } else {
            mobileNo = binding.etMobileNumber.text.toString().trim()
            getDMTDetailsByMobileNumber(mobileNo!!, "true")
        }
    }

    private fun getDMTDetailsByMobileNumber(StrMobileNumber: String, StrAppType: String) {
        this.runIfConnected {
            val queryRemitterReq = QueryRemitterReq(
                MobileNumber = StrMobileNumber,
                Lattitude = "81.128",
                Longitude = "87.45",
                RegistrationID = mStash!!.getStringValue(Constants.MerchantId, ""),
            )
            Log.d("queryRemitterReq", Gson().toJson(queryRemitterReq))
            viewModel.getDMTDetailsByMobileNumber(queryRemitterReq).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    getQueryDMTDetailsRes(response, StrMobileNumber)
                                }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                            toast(it.message.toString())
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getQueryDMTDetailsRes(response: QueryRemitterRes?, mobileNo: String) {
        if (response!!.status == true) {


            if (response.responseCode.toString() == "1") {
                getFetchBeneficiaryDetails(mobileNo)
                binding.llIfMobileNumberExist.visibility = View.VISIBLE
                binding.tvBtnAddRecipient.visibility = View.GONE
                binding.llRegister.visibility = View.GONE
//            binding.tvCustomerName.text = response.data!!.fname + " " + response.data!!.lname
//            binding.tvCustomerName.text = response.data!!.fname.toString()
                binding.tvCustomerMobile.text = response.data!!.mobile.toString()
                binding.tvCustomerLimit.text = response.data!!.limit.toString()

                Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()
            } else if (response.responseCode.toString() == "2") {
                toast(msg = "Please Contact to the Admin For E-KYC.!!")
//                Toast.makeText(this, "Please Contact to the Admin For E-KYC.!!", Toast.LENGTH_LONG).show()
            } else {

            }

            binding.llIfMobileNumberExist.setOnClickListener {
//                val intent = Intent(context, DMTCustomerDetailsActivity::class.java)
//                    .putExtra("AttendanceViewModel", response)
//                startActivity(intent)
//                startActivity(
//                    Intent(context, DMTCustomerDetailsActivity::class.java)
//                        .putExtra("AttendanceViewModel",response)
//                )
            }
//            getAllBankList()

        } else {
//            mStash!!.setStringValue(Constants.clientStateResponse, response.stateresp.toString())
            binding.llRegister.visibility = View.GONE
            binding.tvBtnAddRecipient.visibility = View.GONE
            binding.tvBtnAddCustomer.setOnClickListener {
                binding.llAddCustomer.visibility = View.VISIBLE
            }
            binding.etRegisterMobileNumber.setText(
                binding.etMobileNumber.getText().toString().trim()
            )
            Toast.makeText(context, response.message, Toast.LENGTH_SHORT).show()

        }
        binding.etMobileNumber.setBackgroundResource(R.drawable.bg_button_gray)
        binding.etMobileNumber.isClickable = false
        binding.etMobileNumber.isFocusable = false
        binding.etMobileNumber.isFocusableInTouchMode = false
        binding.tvBtnGo.alpha = 0.5f
        binding.tvBtnGo.isClickable = false
        binding.tvBtnChangeNumber.alpha = 1f
        binding.tvBtnChangeNumber.isClickable = true

//        binding.tvBtnRegisterGo.setOnClickListener { registerRemitters() }

    }

    private fun getFetchBeneficiaryDetails(StrMobileNumber: String) {
        this.runIfConnected {
            val fetchBeneficiaryReq = FetchBeneficiaryReq(
                mobile = StrMobileNumber,
                RegistrationID = mStash!!.getStringValue(Constants.MerchantId, "")
            )
            Log.d("getFetchBeneficiary", Gson().toJson(fetchBeneficiaryReq))
            viewModel.getDMTFetchBeneficiary(fetchBeneficiaryReq).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.body()?.let { response ->
                                handleFetchBeneficiaryResponse(response)
                            }
//                            users.body()?.let { response -> handleFetchBeneficiaryResponse(response)}
//                        }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                            toast(it.message.toString())
//                        Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun handleFetchBeneficiaryResponse(response: FetchBeneficiaryRes) {
        try {
            if (response.status == true) {

                binding.llRecipientDetails.visibility = View.VISIBLE
                fetchListData.clear()
                fetchListData.addAll(response.data)  // Add new data

                // Set up RecyclerView and adapter
                val layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.rvRecieptList.layoutManager = layoutManager

                val layoutAnimationController =
                    AnimationUtils.loadLayoutAnimation(context, R.anim.recyclerview_anim_layout)
                binding.rvRecieptList.layoutAnimation = layoutAnimationController
                binding.rvRecieptList.setHasFixedSize(true)

                val adapter = ReceiptAdapter(mContext = context,
                    dataList = fetchListData,
                    clickListener = object : ReceiptAdapter.ClickListener {
                        override fun itemClick(item: Data) {
                            // Handle item click (navigate or show details)
                            val intent = Intent(context, DMTReciepintDetailsActivity::class.java)
                            intent.putExtra("BeneficiaryData", item)
                            startActivity(intent)
                        }

                        override fun transfer(item: Data) {
                            // Handle transfer action

//                        binding.bankAccountNo.requestFocus()
                            binding.transferAmount.requestFocus()
                            binding.bankAccountNo.setText(item.accno.toString())
                            binding.transferMode.post {
                                if (item.banktype != null) {
                                    try {
//                                        binding.transferMode.setSelection(
//                                            (binding.transferMode.adapter as ArrayAdapter<String?>).getPosition(
//                                                item.banktype.toString()
//                                            )
//                                        )
                                        mStash!!.setStringValue(
                                            Constants.beneId, item.beneId.toString()
                                        )
                                        mStash!!.setStringValue(
                                            Constants.ifscCode, item.ifsc.toString()
                                        )
                                        mStash!!.setStringValue(
                                            Constants.bankAccount, item.accno.toString()
                                        )
                                        mStash!!.setStringValue(
                                            Constants.bankOwnerName, item.name.toString()
                                        )
//                                        Constants.beneId = item.beneId.toString()
//                                        Constants.ifscCode = item.ifsc.toString()
//                                        Constants.bankAccount = item.accno.toString()
//                                        Constants.bankOwnerName = item.name.toString()
                                        Log.d("beneId", Constants.beneId)
                                    } catch (e: java.lang.Exception) {
                                        Log.d("Excep_at", "owners_gender_status_spinner")
                                        e.printStackTrace()
                                        toast(R.string.dropdown_excep_atres.toString())
                                    }
                                }
                            }
                            binding.llAddRecipient.visibility = View.GONE
                            binding.llIfMobileNumberExist.visibility = View.GONE
                            binding.llTransferAmount.visibility = View.VISIBLE
                            binding.llRegister.visibility = View.GONE

//                        binding.transactionGoBtn.setOnClickListener{
//                            transferAmountFromDMT(binding.netAmount.text.toString().trim())
//                        }
                        }

                        override fun delete(item: Data) {
                            // Handle delete action (if required)
                            Toast.makeText(context, "Deleted: ${item.bankname}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })

                binding.rvRecieptList.adapter = adapter
                adapter.notifyDataSetChanged()

                binding.tvBtnAddRecipient.visibility = View.VISIBLE
            } else {
                binding.llRecipientDetails.visibility = View.GONE
                Toast.makeText(
                    context, response.message ?: "Failed to load beneficiaries", Toast.LENGTH_SHORT
                ).show()
            }

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Log.d("printStackTrace", e.message.toString() + " " + e.localizedMessage)
        }

    }

    private fun getTransactionSendOtp() {
        this.runIfConnected {
            val transactionSendOtpReq = TransactionSendOtpReq(
                MobileNumber = mobileNo,
//                BeneID = item.bieneId,
                BeneID = mStash!!.getStringValue(Constants.beneId, ""),
                Txntype = "IMPS",
                Amount = binding.transferAmount.text.toString().trim(),
//                Pincode = binding.pinCode.text.toString(),
                Pincode = "110045",
//                Address = binding.address.text.toString(),
                Address = "Delhi",
//                DOB = binding.dobOwner.text.toString(),
                DOB = "20-08-200",
                Latitude = "87.67623",
                Longitute = "77.98754",
                RegistrationID = mStash!!.getStringValue(Constants.MerchantId, "")
            )
            Log.d("transactionSendOtpReq", Gson().toJson(transactionSendOtpReq))
            viewModel.getTransactionOtp(transactionSendOtpReq).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    getTransactionSendOtpRes(response)
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

    private fun getTransactionSendOtpRes(response: TransactionSendOtpRes) {
        if (response.status == true) {
            mStash!!.setStringValue(Constants.stateRespDMT, response.stateresp.toString())
            Toast.makeText(this, response.message.toString(), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, response.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRegister() {
        val registerRemitterReq = RegisterRemitterReq(
            mobile = binding.etMobileNumber.text.toString().trim(),
            firstname = binding.etRegisterCustomerName.text.toString().trim(),
            lastname = "Singh",
            address = binding.etRegisterAddress.text.toString().trim(),
            otp = binding.etRegisterOTP.text.toString().trim(),
            pincode = binding.etRegisterPincode.text.toString().trim(),
            stateresp = mStash!!.getStringValue(Constants.clientStateResponse, ""),
            bank3Flag = "Yes",
            dob = binding.etRegisterDOB.text.toString().trim(),
            gstState = "delhi",
//            RegistrationID = "MP-0002",
            RegistrationID = mStash!!.getStringValue(Constants.MerchantId, ""),
            Status = "",
            message = "",
            Value = ""
        )
        Log.d("registerRemitterReq", Gson().toJson(registerRemitterReq))
        viewModel.registerRemitters(registerRemitterReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response -> getRegisterRemitters(response) }
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


    private fun getRegisterRemitters(response: RegisterRemitterRes) {
        if (response.status == true && response.Status != null) {
            binding.llRegister.visibility = View.GONE
            binding.llAddCustomer.visibility = View.GONE
            binding.llAddRecipient.visibility = View.VISIBLE
            toast(response.message.toString())
        } else {
            toast(response.message.toString())
        }
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()

    }


}