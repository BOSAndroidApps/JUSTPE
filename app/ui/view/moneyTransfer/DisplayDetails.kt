package com.bos.payment.appName.ui.view.moneyTransfer;

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetAPIServiceChargeReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetAPIServiceChargeRes
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialRes
import com.bos.payment.appName.data.model.recharge.payout.PayoutAmountReq
import com.bos.payment.appName.data.model.recharge.payout.PayoutAmountRes
import com.bos.payment.appName.data.model.recharge.payout.payoutStatus.PayoutStatusReq
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsReq
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsRes
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceReq
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceRes
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceRes
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.repository.PayoutRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.data.viewModelFactory.PayoutViewModelFactory
import com.bos.payment.appName.databinding.ActivityDisplayDetailsBinding
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
import com.bos.payment.appName.utils.Utils.hideKeyboard
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.google.gson.Gson

class DisplayDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisplayDetailsBinding

    //    private lateinit var viewModel: MoneyTransferViewModel
    private lateinit var viewModel: PayoutViewModel
    private var mStash: MStash? = null
    private lateinit var pd: AlertDialog
    private lateinit var timer11: CountDownTimer
    private lateinit var viewModel1: MoneyTransferViewModel
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    private var serviceCharge: Double = 0.0
    private var isSlabMatched = false // Flag to check if any slab matches


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisplayDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
//        sendPayoutAmountResStatus("20241023220141PBOS2197")
//        callingBeneficiaryApi()
        btnListener()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun initView() {
        pd = PD(this)
        mStash = MStash.getInstance(this)
        binding.amount.requestFocus()
        viewModel = ViewModelProvider(
            this,
            PayoutViewModelFactory(PayoutRepository(RetrofitClient.apiInterface))
        )[PayoutViewModel::class.java]

        getAllApiServiceViewModel = ViewModelProvider(
            this,
            GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface))
        )[GetAllApiServiceViewModel::class.java]



        binding.amount.focusable
        binding.paymentBtn.visibility = View.GONE

        val upiId = intent.getStringExtra("upiId") ?: "N/A"
        val payeeName = intent.getStringExtra("payeeName") ?: "N/A"
        val bankingName = intent.getStringExtra("bankingName") ?: ""
        val amount = intent.getStringExtra("amount") ?: ""

        Log.d("upiId", intent.getStringExtra("upiId").toString())
        Log.d("upiId", intent.getStringExtra("payeeName").toString())
        Log.d("upiId", intent.getStringExtra("bankingName").toString())
        Log.d("upiId", intent.getStringExtra("amount").toString())

        val merchantCode = intent.getStringExtra("merchantCode") ?: "N/A"
        val transactionId = intent.getStringExtra("transactionId") ?: "N/A"
        val referenceId = intent.getStringExtra("referenceId") ?: "N/A"
        val transactionNote = intent.getStringExtra("transactionNote") ?: "N/A"
        val currencyCode = intent.getStringExtra("currencyCode") ?: "N/A"

        binding.tvUpiId.text = "UPI ID: $upiId"
        binding.tvAccountNumber.text = "Paying: $payeeName"
        binding.addMoney.text = "₹:$amount"
        if ("Banking name:$bankingName" != null) {
            binding.tvBankingName.text = "Banking name: $bankingName"
        } else {
            binding.tvBankingName.visibility = View.GONE
        }
        binding.paymentBtn.text = "₹" + binding.amount.text.toString()

//        binding.merchantCodeTextView.text = "Merchant Code: $merchantCode"
//        binding.transactionIdTextView.text = "Transaction ID: $transactionId"
//        binding.referenceIdTextView.text = "Reference ID: $referenceId"
//        binding.transactionNoteTextView.text = "Transaction Note: $transactionNote"
//        binding.currencyCodeTextView.text = "Amount: $currencyCode"

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun btnListener() {
        binding.crossBtn.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        binding.amount.setOnClickListener {
            binding.enterBtn.visibility = View.VISIBLE
            binding.paymentBtn.visibility = View.GONE

        }
//        binding.enterBtn.setOnClickListener{
//            if (binding.amount.text.toString().isNotEmpty()){
//                toast()
//            }
//            binding.paymentBtn.visibility = View.VISIBLE
//            val transferAmountText = binding.amount.text.toString().trim()
//            getAllServiceCharge(transferAmountText)
//            Log.d("getAllServiceCharge", transferAmountText)
//        }

        binding.enterBtn.setOnClickListener {
            if (binding.amount.text!!.toString() < "1") {
                toast("Payment must be at least ₹1")
                binding.enterBtn.visibility = View.VISIBLE
                binding.paymentBtn.visibility = View.GONE
                binding.amount.requestFocus()

            } else {
                val transferAmountText = binding.amount.text.toString().trim()
//                getAllServiceCharge(transferAmountText)
                getAllApiPayoutCommercialCharge(transferAmountText)
                Log.d("getAllServiceCharge", transferAmountText)
                goPaymentCalling()
            }
        }
        binding.paymentBtn.setOnClickListener {
            getAllWalletBalance()
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
                getTransferAmountToAgentWithCal(binding.amount.text.toString())
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

    private fun getTransferAmountToAgentWithCal(rechargeAmount: String) {
        try {
            val currentDateTime = Utils.getCurrentDateTime()
            val transferAmountToAgentsReq = TransferAmountToAgentsReq(
                transferFrom = mStash!!.getStringValue(Constants.RegistrationId, ""),
                transferTo = "Admin",
                transferAmt = mStash!!.getStringValue(Constants.totalTransaction, ""),
                remark = "Payout",
                transferFromMsg = "Your Account is debited by $rechargeAmount Rs. Due to Pay Out on number ${
                    intent.getStringExtra(
                        "upiId"
                    )
                }.",
                transferToMsg = "Your Account is credited by $rechargeAmount Rs. Due to Pay Out on number ${
                    intent.getStringExtra(
                        "upiId"
                    )
                }.",
                amountType = "Payout",
//                actualTransactionAmount = Integer.valueOf(rechargeAmount),
                actualTransactionAmount = rechargeAmount,
                transIpAddress = mStash!!.getStringValue(Constants.deviceIPAddress, ""),
                parmUserName = mStash!!.getStringValue(Constants.RegistrationId, ""),
                merchantCode = mStash!!.getStringValue(Constants.MerchantId, ""),
                servicesChargeAmt = mStash!!.getStringValue(Constants.serviceCharge, ""),
                servicesChargeGSTAmt = mStash!!.getStringValue(Constants.serviceChargeWithGST, ""),
                servicesChargeWithoutGST = mStash!!.getStringValue(Constants.serviceCharge, ""),
                customerVirtualAddress = "",
                retailerCommissionAmt = mStash!!.getStringValue(
                    Constants.retailerCommissionWithoutTDS,
                    ""
                ),
                retailerId = "",
                paymentMode = "UPI",
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
                                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT)
                                    .show()
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
        try {
            if (response.isSuccess == true) {
                sendingMoney()
            } else {
                pd.dismiss()
                Toast.makeText(this, response.returnMessage.toString(), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            pd.dismiss()
            e.printStackTrace()
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    private fun getAllApiPayoutCommercialCharge(rechargeAmount: String) {
        this.runIfConnected {
            val getPayoutCommercialReq = GetPayoutCommercialReq(
                merchant = mStash!!.getStringValue(Constants.RegistrationId, ""),
                productId = "F0112",
                modeofPayment = "UPI"
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

    private fun getAllApiPayoutCommercialChargeRes(
        response: GetPayoutCommercialRes,
        rechargeAmount: String
    ) {
        if (response.isSuccess == true) {
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
                    }
                }
                //if no matching slab was found, show an error message
                if (!isSlabMatched) {
                    isSlabMatched = false
//                        binding.serviceChargeLayout.visibility = View.GONE
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

    private fun getAllServiceCharge(rechargeAmount: String) {
        val getAPIServiceChargeReq = GetAPIServiceChargeReq(
//            APIName = mStash!!.getStringValue(Constants.APIName, ""),
            APIName = "Payout Api",
//            Category = mStash!!.getStringValue(Constants.APIName, ""),
            Category = "Payout",
            Code = "Payout",
            CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
        )

        Log.d("getAllServiceCharge", Gson().toJson(getAPIServiceChargeReq))

        viewModel1.getAllAPIServiceCharge(getAPIServiceChargeReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                getAllServiceChargeApiRes(response, rechargeAmount)
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
        response: List<GetAPIServiceChargeRes>,
        rechargeAmount: String
    ) {
        try {
            response.let {
                if (it[0].Status == true) {

//                handleVisibilityShowingWithCommissionType(response)
//                binding.commissionLayout.visibility = View.VISIBLE
//                binding.amountPayLayout.visibility = View.VISIBLE

                    val rechargeAmountValue = rechargeAmount.toDoubleOrNull() ?: 0.0
                    val TDSTax = 5.0 // Fixed TDS rate of 5%

//                    // Retailer Commission Calculation
//                    val retailerCommissionWithTDS = calculateCommissionWithTDS(
//                        commission = response.RetailerCommission?.toDoubleOrNull() ?: 0.0,
//                        commissionType = response.RetailerCommissionType,
//                        amount = rechargeAmountValue,
//                        tdsTax = TDSTax,
//                        isRetailer = true,
//                        isDistributer = false,
//                        isMDistributer = false
//                    )
//                    val subDisCommissionWithTDS = calculateCommissionWithTDS(
//                        commission = response.SubDisCommission?.toDoubleOrNull() ?: 0.0,
//                        commissionType = response.SubDisCommissionType,
//                        amount = rechargeAmountValue,
//                        tdsTax = TDSTax,
//                        isRetailer = false,
//                        isDistributer = true,
//                        isMDistributer = false
//                    )
//                    val mDisCommissionWithTDS = calculateCommissionWithTDS(
//                        commission = response.DisCommission?.toDoubleOrNull() ?: 0.0,
//                        commissionType = response.DisCommissionType,
//                        amount = rechargeAmountValue,
//                        tdsTax = TDSTax,
//                        isRetailer = false,
//                        isDistributer = false,
//                        isMDistributer = true
//                    )
                    // Customer Commission Calculation
//                    val customerCommissionWithTDS = calculateCommissionWithTDS(
//                        commission = response.CustomerCommission?.toDoubleOrNull() ?: 0.0,
//                        commissionType = response.CustomerCommissionType,
//                        amount = rechargeAmountValue,
//                        tdsTax = TDSTax,
//                        isRetailer = false,
//                        isMDistributer = true,
//                        isDistributer = false
//                    )

                    for (i in response.indices) {
                        val gst = 18.0 // Fixed GST rate of 18%
                        // Get characters between ":" and "-" and convert to Double
                        val min =
                            response[i].ServiceChargeSlab?.substringAfter(":")?.substringBefore("-")
                                ?.toDoubleOrNull() ?: 0.0

                        // Get characters after "-" and convert to Double
                        val max =
                            response[i].ServiceChargeSlab?.substringAfter("-")?.toDoubleOrNull()
                                ?: 0.0

                        if (rechargeAmountValue in min..max) {
                            isSlabMatched = true   // Mark that a matching slab was found
                            serviceCharge = response[i].ServiceCharge?.toDoubleOrNull() ?: 0.0
                            Log.d("serviceCharge", serviceCharge.toString())
//                            serviceChargeCalculation(serviceCharge, gst, rechargeAmount, response, min, max)
                        }
                    }
                    //if no matching slab was found, show an error message
                    if (!isSlabMatched) {
                        isSlabMatched = false
//                        binding.serviceChargeLayout.visibility = View.GONE
                        toast("No matching slab found for the amount: $rechargeAmountValue")
                    }

//                    // Service Charge with GST Calculation
//                    val gst = 18.0 // Fixed GST rate of 18%
////                    val serviceCharge = response.ServiceCharge?.toDoubleOrNull() ?: 0.0
//                    val totalServiceChargeWithGst =
//                        serviceChargeCalculation(serviceCharge, gst, rechargeAmount, response)
//
//                    // Total Recharge Amount Calculation
//                    val totalRechargeAmount = rechargeAmountValue +
////                        retailerCommissionWithTDS +
////                        customerCommissionWithTDS +
////                        mDisCommissionWithTDS +
////                        subDisCommissionWithTDS +
//                            totalServiceChargeWithGst
//
//                    // Store the total transaction in mStash
//                    mStash!!.setStringValue(
//                        Constants.totalTransaction,
//                        String.format("%.2f", totalRechargeAmount)
//                    )
//                binding.retailerCommissionText
//                binding.totalTransferText.setText(String.format("%.2f", totalRechargeAmount))
//                    Log.d("Total Recharge Amount", String.format("%.2f", totalRechargeAmount))
                } else {
                    Toast.makeText(this, response[0].message.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast(e.message.toString())
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
//                    binding.retailerCommissionWithoutTDs.setText(
//                        String.format(
//                            "%.2f",
//                            finalCommission
//                        )
//                    )
//                    binding.tdsChargetext.setText(String.format("%.2f", commissionTDS))
//                    binding.retailerCommissionName.text = "Retailer Commission %"
                } else if (isDistributer) {
                    mStash!!.setStringValue(
                        Constants.adminCommissionWithoutTDS,
                        String.format("%.2f", finalCommission)
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
//                      )
//                    binding.tdsChargetext.setText(String.format("%.2f", commissionTDS))
//                    binding.retailerCommissionName.text = "Retailer Commission RS"
                } else if (isDistributer) {
                    mStash!!.setStringValue(
                        Constants.adminCommissionWithoutTDS,
                        String.format("%.2f", finalCommission)
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


        if (mStash!!.getStringValue(Constants.RegistrationId, "")!!.isNotEmpty()) {
            slabLimit = when {
                rechargeAmountValue <= max -> max.toString()
//                rechargeAmountValue <= 25000 -> "25000"
//                rechargeAmountValue <= 100000 -> "100000"
                else -> {
                    null
                }
            }
        } else {
            Toast.makeText(
                this,
                "No valid slab found for the amount: $rechargeAmountValue",
                Toast.LENGTH_SHORT
            ).show()
        }
//        else if (mStash!!.getStringValue(Constants.CompanyCode, "") == "CMP1349"){
//            slabLimit = when {
//                rechargeAmountValue <= max -> max.toString()
////                rechargeAmountValue <= 3000 -> "3000"
////                rechargeAmountValue <= 4000 -> "4000"
////                rechargeAmountValue <= 5000 -> "5000"
//                else -> {
//                    null
//                }
//            }
//        } else if (mStash!!.getStringValue(Constants.CompanyCode, "") == "CMP1085"){
//            slabLimit = when {
//                rechargeAmountValue <= max -> max.toString()
//                else -> {
//                    null
//                }
//            }
//        }

//        if (slabLimit == null) {
//            toast("No valid slab found for the amount: $rechargeAmountValue")
//            return 0.0
//        }

        for (i in response.data.indices) {
//            if (slabLimit != null && response.data[i].slabName?.contains(slabLimit) == true) {
            if (slabLimit != null) {
                Log.d("ServiceType", "${response.data[i].serviceType} - Index $i")
                serviceChargeWithGst = when (response.data[i].serviceType) {
                    "Amount" -> {
//                        binding.serviceChargeName.text = "Service Charge Rs"
                        val serviceChargeGst = serviceCharge * (gstRate / 100)
                        mStash?.setStringValue(
                            Constants.serviceCharge,
                            String.format("%.2f", serviceCharge)
                        )

                        serviceCharge + serviceChargeGst
                    }

                    "Percentage" -> {
//                        binding.serviceChargeName.text = "Service Charge %"
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

            Log.d(
                "serviceChargeWithGST",
                String.format("%.2f", actualAmount) + " = " + String.format(
                    "%.2f",
                    serviceChargeWithGst
                ) + " = " + String.format("%.2f", actualAmount)
            )
        }

//        binding.apply {
//            gstWithServiceCharge.setText(String.format("%.2f", serviceChargeWithGst))
//            serviceChargeAmount.setText(String.format("%.2f", serviceCharge))
//            totalTransferText.setText(String.format("%.2f", actualAmount))
//        }

        Log.d("TotalRechargeAmount", String.format("%.2f", actualAmount))
        Log.d("totalAmountWithGst", String.format("%.2f", serviceChargeWithGst))

        return serviceChargeWithGst
    }

    private fun sendingMoney() {
        this.runIfConnected {
            val payoutAmountReq = PayoutAmountReq(
                registrationID = mStash!!.getStringValue(Constants.MerchantId, ""),
                upiID = intent.getStringExtra("upiId"),
                amount = binding.amount.text.toString().trim()
            )
            pd.show()
            Log.d("payoutAmountReq", Gson().toJson(payoutAmountReq))
            viewModel.sendAmount(payoutAmountReq).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
//                        pd.dismiss()
                            it.data?.let { users ->
                                users.body()?.let { it1 ->
                                    sendAllAmount(it1)
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

    //TODO changes as per discuss naim sir payout all condition true and false status show always successful page
    private fun sendAllAmount(response: PayoutAmountRes) {
        try {
            if (response.statuss == true || response.statuss == false) {
                pd.dismiss()
                val intent = Intent(this, ScanAndPaySuccessFulPage::class.java).apply {
                    putExtra("amount", binding.amount.text.toString())
                    putExtra(
                        "upiId",
                        intent.getStringExtra("upiId")
                    ) // Fix: Use correct Intent value
                    putExtra("referenceId", response.data!![0].payoutRef.toString())
                    putExtra(
                        "payeeName",
                        intent.getStringExtra("payeeName")
                    ) // Fix: Use correct Intent value
                    putExtra("dateAndTime", response.data!![0].drLedgerPassedOnTimestamp.toString())
                    putExtra("Status", response.statuss.toString())
                    putExtra("message", response.message.toString())
//                putExtra("utrNo",response.data!![0].utr.toString())
                    putExtra("utrNo", response.data!![0].payoutRef.toString())

                    // Service and commission charge details
//                putExtra("tdsTax", mStash!!.getStringValue(Constants.tds, "0.00"))
//                putExtra("retailerCommissionWithoutTDS", mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "0.00"))
//                putExtra("customerCommissionWithoutTDS", mStash!!.getStringValue(Constants.customerCommissionWithoutTDS, "0.00"))
                    putExtra(
                        "serviceCharge",
                        mStash!!.getStringValue(Constants.serviceCharge, "0.00")
                    )
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
//            sendPayoutAmountResStatus(response.data!![0].payoutRef.toString())
            } else {
                Toast.makeText(this, response.message.toString(), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast(e.message.toString())
        }
    }

    private fun sendPayoutAmountResStatus(referenceId: String) {
        val payoutStatusReq = PayoutStatusReq(
            txnRefranceID = referenceId,
            registrationID = mStash!!.getStringValue(Constants.MerchantId, "")
        )
        Log.d("payoutStatusReq", "Gson " + Gson().toJson(payoutStatusReq))

        // Show progress dialog initially
        pd.show()

        // Start a countdown timer for a maximum of 30 seconds with 3-second intervals
        timer11 =
            object : CountDownTimer(5 * 60000, 3000) { // 30 seconds duration, 3 seconds interval
                override fun onTick(millisUntilFinished: Long) {
                    // Call the status API every 3 seconds
                    viewModel.getPayoutStatus(payoutStatusReq)
                        .observe(this@DisplayDetailsActivity) { resource ->
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
                    toast("UTR not received within 30 seconds. Please try again.")
                }
            }

        // Start the countdown timer
        timer11.start()
    }

    private fun sendAllAmountStatusRes(response: PayoutAmountRes) {
        if (response.data != null) {
            pd.dismiss()
            // UTR received, process the response and move to the success page
            val intent = Intent(this, ScanAndPaySuccessFulPage::class.java).apply {
                putExtra("amount", binding.amount.text.toString())
                putExtra("upiId", intent.getStringExtra("upiId")) // Fix: Use correct Intent value
                putExtra("referenceId", response.data!![0].payoutRef.toString())
                putExtra(
                    "payeeName",
                    intent.getStringExtra("payeeName")
                ) // Fix: Use correct Intent value
                putExtra("dateAndTime", response.data!![0].drLedgerPassedOnTimestamp.toString())
                putExtra("Status", response.status.toString())
                putExtra("message", response.message.toString())
                putExtra("utrNo", response.data!![0].utr.toString())

                // Service and commission charge details
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
                    "totalTransaction",
                    mStash!!.getStringValue(Constants.totalTransaction, "0.00")
                )
            }
            startActivity(intent)
        } else {
            toast(response.message.toString())
        }
    }

//    private fun getAdminToDistributeAmount(
//        rechargeAmount: String,
//        mobileNo: String,
//        transferAmountToAgentsReq: TransferAmountToAgentsReq,
//        finalCommission: String?
//    ) {
//
//        Log.d("final_method", mobileNo)
//        Log.d("final_method", Gson().toJson(transferAmountToAgentsReq))
//
//        viewModel1.getTransferAmountToAgents(transferAmountToAgentsReq).observe(this) { resource ->
//            resource?.let {
//                when (it.apiStatus) {
//                    ApiStatus.SUCCESS -> {
//                        Log.d("is_success", mobileNo)
//                        Log.d("is_success", it.data!!.body().toString())
//                        pd.dismiss()
//                        it.data.let { users ->
//                            users.body()?.let { response ->
//                                getTransferAmountToAgentsRes(
//                                    response,
//                                    mobileNo
//                                )
//                            }
//                        }
//                    }
//
//                    ApiStatus.ERROR -> {
//                        Log.d("is_error", mobileNo)
//
//                    }
//
//                    ApiStatus.LOADING -> {
//                        Log.d("is_loading", mobileNo)
//
//                    }
//                }
//            }
//        }
//    }

//    private fun getAllServiceChargeAPI() {
//
//        getTransferServiceChargeAmountToAgents(binding.amount.text.toString(), intent.getStringExtra("upiId").toString())
//
//
//        // Recharge Commission-  from Distributer To Retailer
//
//
//        // Recharge Commission-  from Master Distributer To Distributer
//
//        // Recharge Commission-  from Admin To Master Distributer
//    }
//
//    private fun getAllCommissionChargeAPI() {
//        Log.d(
//            "retailer_CommissionType",
//            mStash!!.getStringValue(Constants.retailer_CommissionType, "").toString()
//        );
//        Log.d(
//            "dis_CommissionType",
//            mStash!!.getStringValue(Constants.dis_CommissionType, "").toString()
//        );
//        Log.d(
//            "mDis_CommissionType",
//            mStash!!.getStringValue(Constants.mDis_CommissionType, "").toString()
//        );
////        getTransferAmountToAgents(
////            binding.amount.text.toString(),
////            intent.getStringExtra("upiId").toString()
////        )
//
//
//        // Recharge Commission-  from Distributer To Retailer
//        if (mStash!!.getStringValue(Constants.retailer_CommissionType, "") != "Not Applicable") {
//            Log.d(
//                "inside_retailer",
//                mStash!!.getStringValue(Constants.retailer_CommissionType, "").toString()
//            );
//
//            val calendar = Calendar.getInstance()
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//            val currentDateTime = dateFormat.format(calendar.time)
//            val mobileNo = intent.getStringExtra("upiId").toString()
//            println("Current Date and Time: $currentDateTime")
//
//            val transferAmountToAgentsReq = TransferAmountToAgentsReq(
//                TransIpAddress = mStash!!.getStringValue(Constants.deviceIPAddress, ""),
//                TransferToMsg = "Your Account is credited by commission ${
//                    mStash!!.getStringValue(
//                        Constants.retailerCommissionWithoutTDS,
//                        ""
//                    )
//                } Rs. Due to UPI on number ${mobileNo}.",
//                TransferFromMsg = "Your Account is debited by commission ${
//                    mStash!!.getStringValue(
//                        Constants.retailerCommissionWithoutTDS,
//                        ""
//                    )
//                } Rs. Due to UPI on number ${mobileNo}.",
//                AmountType = "Commission",
//                Remark = "UPI",
//                TransactionDate = currentDateTime.toString(),
////            TransferFrom = mStash!!.getStringValue(Constants.RegistrationId, ""),
//                TransferFrom = "ADMIN",
////            TransferTo = mStash!!.getStringValue(Constants.RegistrationId, ""),
//                TransferTo = mStash!!.getStringValue(Constants.RegistrationId, ""),
//                TransferAmt = mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, ""),
//                UpdatedBy = mStash!!.getStringValue(Constants.RegistrationId, ""),
//                ActualTransactionAmount = mStash!!.getStringValue(Constants.totalTransaction, ""),
//                ActualCommissionAmt = mStash!!.getStringValue(
//                    Constants.retailerCommissionWithoutTDS,
//                    ""
//                ),
//                GSTAmt = "0.00",
//                CommissionWithoutGST = "0.00",
//                TDSAmt = mStash!!.getStringValue(Constants.tds, ""),
//                NetCommissionAmt = mStash!!.getStringValue(
//                    Constants.retailerCommissionWithoutTDS,
//                    ""
//                )
//            )
//
//            getAdminToDistributeAmount(
//                binding.amount.text.toString(), "retailer_CommissionType",
//                transferAmountToAgentsReq,
//                mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "")
//            )
////                    getDistributerToRetailer(binding.etAmount.text.toString(), binding.etMobileNumber.text.toString().trim(),
////                        mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, ""))
//
//        }
//
//        // Recharge Commission-  from Master Distributer To Distributer
//        if (mStash!!.getStringValue(Constants.dis_CommissionType, "") != "Not Applicable") {
//            Log.d(
//                "inside_dis",
//                mStash!!.getStringValue(Constants.dis_CommissionType, "").toString()
//            );
//
//            val calendar = Calendar.getInstance()
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//            val currentDateTime = dateFormat.format(calendar.time)
//            val mobileNo = intent.getStringExtra("upiId").toString()
//            println("Current Date and Time: $currentDateTime")
//
//            val transferAmountToAgentsReq = TransferAmountToAgentsReq(
//                TransIpAddress = mStash!!.getStringValue(Constants.deviceIPAddress, ""),
//                TransferToMsg = "Your Account is credited by commission ${
//                    mStash!!.getStringValue(
//                        Constants.subDisCommissionWithoutTDS,
//                        ""
//                    )
//                } Rs. Due to UPI on number ${mobileNo}.",
//                TransferFromMsg = "Your Account is debited by commission ${
//                    mStash!!.getStringValue(
//                        Constants.subDisCommissionWithoutTDS,
//                        ""
//                    )
//                } Rs. Due to UPI on number ${mobileNo}.",
//                AmountType = "Commission",
//                Remark = "UPI",
//                TransactionDate = currentDateTime.toString(),
////            TransferFrom = mStash!!.getStringValue(Constants.RegistrationId, ""),
//                TransferFrom = mStash!!.getStringValue(Constants.mD_RegistrationId, ""),
////            TransferTo = mStash!!.getStringValue(Constants.RegistrationId, ""),
//                TransferTo = mStash!!.getStringValue(Constants.dIS_RegistrationId, ""),
//                TransferAmt = mStash!!.getStringValue(Constants.subDisCommissionWithoutTDS, ""),
//                UpdatedBy = mStash!!.getStringValue(Constants.RegistrationId, ""),
//                ActualTransactionAmount = mStash!!.getStringValue(Constants.totalTransaction, ""),
//                ActualCommissionAmt = mStash!!.getStringValue(
//                    Constants.subDisCommissionWithoutTDS,
//                    ""
//                ),
//                GSTAmt = "0.00",
//                CommissionWithoutGST = "0.00",
//                TDSAmt = mStash!!.getStringValue(Constants.tds, ""),
//                NetCommissionAmt = mStash!!.getStringValue(Constants.subDisCommissionWithoutTDS, "")
//            )
//
//            getAdminToDistributeAmount(
//                binding.amount.text.toString(), "dis_CommissionType",
//                transferAmountToAgentsReq,
//                mStash!!.getStringValue(Constants.subDisCommissionWithoutTDS, "")
//            )
////                    getDistributerAmountToSubDistributer(binding.etAmount.text.toString(), binding.etMobileNumber.text.toString().trim(),
////                        mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, ""))
//
//        }
//        // Recharge Commission-  from Admin To Master Distributer
//        if (mStash!!.getStringValue(Constants.mDis_CommissionType, "") != "Not Applicable") {
//            Log.d(
//                "inside_mDis",
//                mStash!!.getStringValue(Constants.mDis_CommissionType, "").toString()
//            );
//
//            val calendar = Calendar.getInstance()
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//            val currentDateTime = dateFormat.format(calendar.time)
//            val mobileNo = intent.getStringExtra("upiId").toString()
//            println("Current Date and Time: $currentDateTime")
//
//            val transferAmountToAgentsReq = TransferAmountToAgentsReq(
//                TransIpAddress = mStash!!.getStringValue(Constants.deviceIPAddress, ""),
//                TransferToMsg = "Your Account is credited by commission ${
//                    mStash!!.getStringValue(
//                        Constants.mDistributerCommissionWithoutTDS,
//                        ""
//                    )
//                } Rs. Due to UPI on number ${mobileNo}.",
//                TransferFromMsg = "Your Account is debited by commission ${
//                    mStash!!.getStringValue(
//                        Constants.mDistributerCommissionWithoutTDS,
//                        ""
//                    )
//                } Rs. Due to UPI on number ${mobileNo}.",
//                AmountType = "Commission",
//                Remark = "UPI",
//                TransactionDate = currentDateTime.toString(),
////            TransferFrom = mStash!!.getStringValue(Constants.RegistrationId, ""),
//                TransferFrom = "ADMIN",
//                TransferTo = mStash!!.getStringValue(Constants.mD_RegistrationId, ""),
//                TransferAmt = mStash!!.getStringValue(
//                    Constants.mDistributerCommissionWithoutTDS,
//                    ""
//                ),
//                UpdatedBy = mStash!!.getStringValue(Constants.RegistrationId, ""),
//                ActualTransactionAmount = mStash!!.getStringValue(Constants.totalTransaction, ""),
//                ActualCommissionAmt = mStash!!.getStringValue(
//                    Constants.mDistributerCommissionWithoutTDS,
//                    ""
//                ),
//                GSTAmt = "0.00",
//                CommissionWithoutGST = "0.00",
//                TDSAmt = mStash!!.getStringValue(Constants.tds, ""),
//                NetCommissionAmt = mStash!!.getStringValue(
//                    Constants.mDistributerCommissionWithoutTDS,
//                    ""
//                )
//            )
//
//            getAdminToDistributeAmount(
//                binding.amount.text.toString(), "mDis_CommissionType",
//                transferAmountToAgentsReq,
//                mStash!!.getStringValue(Constants.mDistributerCommissionWithoutTDS, "")
//            )
//        }
////                getTransferAmountToAgents(binding.etAmount.text.toString(), binding.etMobileNumber.text.toString().trim())
////                showUpdateMobileDetails(requireContext())
//
//    }
//
//    private fun getTransferServiceChargeAmountToAgents(rechargeAmount: String, mobileNo: String) {
//        val currentDateTime = getCurrentDateTime()
//        val transferAmountToAgentsReq = createTransferAmountToAgentsReq(
//            rechargeAmount = rechargeAmount,
//            mobileNo = mobileNo,
//            currentDateTime = currentDateTime,
//            amountType = "UPI",
//            remark = "UPI"
//        )
//
//        viewModel1.getTransferAmountToAgents(transferAmountToAgentsReq).observe(this) { resource ->
//            resource?.let {
//                when (it.apiStatus) {
//                    ApiStatus.SUCCESS -> {
//                        pd.dismiss()
//                        it.data?.let { response ->
//                            getTransferAmountToAgentsRes(response.body(), "test")
//                        }
//                    }
//                    ApiStatus.ERROR -> {
//                        pd.dismiss()
//                        // Handle error (log or show message)
//                    }
//                    ApiStatus.LOADING -> {
//                        // Show loading if needed
//                    }
//                }
//            }
//        }
//    }
//
//    // Function to handle commissions logic
//    private fun getTransferAmountToAgentsRes(response: TransferAmountToAgentsRes?, from: String) {
//        Log.d("final_response", "TransferAmountToAgentsRes from $from")
//        if (response?.Status == true) {
//            handleCommissionTransfer()
//        } else {
//            Log.d("false_from", from)
//            toast(response?.message.toString())
//        }
//    }
//
//    // Handles commission logic
//    private fun handleCommissionTransfer() {
//        when {
//            mStash!!.getStringValue(Constants.serviceChargeWithGST, "").toString() > "0.00" -> {
//                transferServiceCharge()
//            }
//            mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "").toString() > "0.00" -> {
//                transferRetailerCommission()
//            }
//            mStash!!.getStringValue(Constants.subDisCommissionWithoutTDS, "").toString() > "0.00" -> {
//                transferDistributorCommission()
//            }
//            mStash!!.getStringValue(Constants.mDistributerCommissionWithoutTDS, "").toString() > "0.00" -> {
//                transferMasterDistributorCommission()
//            }
//            else -> {
//                // Proceed with final transaction if no commissions apply
//                sendingMoney()
//            }
//        }
//    }
//
//    private fun transferMasterDistributorCommission() {
//        val currentDateTime = getCurrentDateTime()
//        val serviceChargeWithGST = mStash!!.getStringValue(Constants.mDistributerCommissionWithoutTDS, "")
//        val mobileNo = intent.getStringExtra("upiId").toString()
//
//        val transferAmountToAgentsReq = createTransferAmountToAgentsReq(
//            rechargeAmount = serviceChargeWithGST!!,
//            mobileNo = mobileNo,
//            currentDateTime = currentDateTime,
//            amountType = "Commission",
//            remark = "UPI"
//        )
//
//        hitTransferAmountApi(transferAmountToAgentsReq, "ServiceCharge")
//    }
//
//    private fun transferDistributorCommission() {
//        val currentDateTime = getCurrentDateTime()
//        val serviceChargeWithGST = mStash!!.getStringValue(Constants.subDisCommissionWithoutTDS, "")
//        val mobileNo = intent.getStringExtra("upiId").toString()
//
//        val transferAmountToAgentsReq = createTransferAmountToAgentsReq(
//            rechargeAmount = mStash!!.getStringValue(Constants.serviceChargeWithGST, "").toString(),
//            mobileNo = mobileNo,
//            currentDateTime = currentDateTime,
//            amountType = "Commission",
//            remark = "UPI"
//        )
//
//        hitTransferAmountApi(transferAmountToAgentsReq, "ServiceCharge")
//    }
//
//    private fun transferRetailerCommission() {
//        val currentDateTime = getCurrentDateTime()
//        val serviceChargeWithGST = mStash!!.getStringValue(Constants.serviceChargeWithGST, "")
//        val mobileNo = intent.getStringExtra("upiId").toString()
//
//        val transferAmountToAgentsReq = createTransferAmountToAgentsReq(
//            rechargeAmount = mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "").toString(),
//            mobileNo = mobileNo,
//            currentDateTime = currentDateTime,
//            amountType = "Commission",
//            remark = "UPI"
//        )
//
//        hitTransferAmountApi(transferAmountToAgentsReq, "ServiceCharge")
//    }
//
//    // Function to transfer service charge
//    private fun transferServiceCharge() {
//        val currentDateTime = getCurrentDateTime()
//        val serviceChargeWithGST = mStash!!.getStringValue(Constants.serviceChargeWithGST, "")
//        val mobileNo = intent.getStringExtra("upiId").toString()
//
//        val transferAmountToAgentsReq = createTransferAmountToAgentsReq(
//            rechargeAmount = serviceChargeWithGST!!,
//            mobileNo = mobileNo,
//            currentDateTime = currentDateTime,
//            amountType = "Service Charge",
//            remark = "Service Charge"
//        )
//
//        hitTransferAmountApi(transferAmountToAgentsReq, "ServiceCharge")
//    }

// Other commission transfer functions can be written similarly...

//    // Utility function to create request
//    private fun createTransferAmountToAgentsReq(
//        rechargeAmount: String,
//        mobileNo: String,
//        currentDateTime: String,
//        amountType: String,
//        remark: String
//    ): TransferAmountToAgentsReq {
//        return TransferAmountToAgentsReq(
//            TransIpAddress = mStash!!.getStringValue(Constants.deviceIPAddress, ""),
//            TransferToMsg = "Your Account is credited by $rechargeAmount Rs. due to $amountType on number $mobileNo.",
//            TransferFromMsg = "Your Account is debited by $rechargeAmount Rs. due to $amountType on number $mobileNo.",
//            AmountType = amountType,
//            Remark = remark,
//            TransactionDate = currentDateTime,
//            TransferFrom = mStash!!.getStringValue(Constants.RegistrationId, ""),
//            TransferTo = "Admin", // Change as necessary
//            TransferAmt = rechargeAmount,
//            UpdatedBy = mStash!!.getStringValue(Constants.RegistrationId, ""),
//            ActualTransactionAmount = "0.00",
//            ActualCommissionAmt = "0.00",
//            GSTAmt = "0.00",
//            CommissionWithoutGST = "0.00",
//            TDSAmt = "0.00",
//            NetCommissionAmt = "0.00"
//        )
//    }
//
//    // Function to get current date-time as a formatted string
//    private fun getCurrentDateTime(): String {
//        val calendar = Calendar.getInstance()
//        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//        return dateFormat.format(calendar.time)
//    }
//
//    // Hit API and observe result
//    private fun hitTransferAmountApi(request: TransferAmountToAgentsReq, type: String) {
//        viewModel1.getTransferAmountToAgents(request).observe(this) { resource ->
//            resource?.let {
//                when (it.apiStatus) {
//                    ApiStatus.SUCCESS -> {
//                        pd.dismiss()
//                        it.data?.let { response -> getTransferAmountToServiceChargeRes(response.body()!!, type) }
//                    }
//                    ApiStatus.ERROR -> {
//                        pd.dismiss()
//                    }
//                    ApiStatus.LOADING -> {
//                        // Show loading if needed
//                    }
//                }
//            }
//        }
//    }
//
//
//    private fun getTransferAmountToMDisRes(response: TransferAmountToAgentsRes, s: String) {
//        if (response.Status == true){
//            toast(response.message.toString())
//        }else {
//            toast(response.message.toString())
//        }
//    }
//
//    private fun getTransferAmountToDistributerRes(response: TransferAmountToAgentsRes, s: String) {
//        if (response.Status == true){
//            toast(response.message.toString())
//        }else {
//            toast(response.message.toString())
//        }
//    }
//
//    private fun getTransferAmountToRetalerRes(response: TransferAmountToAgentsRes, s: String) {
//        if (response.Status == true){
//            toast(response.message.toString())
//        }else {
//            toast(response.message.toString())
//        }
//    }

//    private fun getTransferAmountToServiceChargeRes(response: TransferAmountToAgentsRes, s: String) {
//        if (response.Status == true){
//            toast(response.message.toString())
//        }else {
//            toast(response.message.toString())
//        }
//    }

    @SuppressLint("SetTextI18n")
    private fun goPaymentCalling() {
        hideKeyboard(this)
        binding.paymentBtn.visibility = View.VISIBLE
        binding.enterBtn.visibility = View.GONE
        binding.paymentBtn.text = "Pay ₹" + binding.amount.text.toString()
    }
}
