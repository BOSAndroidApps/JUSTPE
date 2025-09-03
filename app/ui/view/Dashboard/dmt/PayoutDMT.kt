package com.bos.payment.appName.ui.view.Dashboard.dmt

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetAPIServiceChargeReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetAPIServiceChargeRes
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialRes
import com.bos.payment.appName.data.model.recharge.payout.PayoutAmountRes
import com.bos.payment.appName.data.model.recharge.payout.payoutBos.PayoutAppReq
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
import com.bos.payment.appName.databinding.ActivityPayoutDmtBinding
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
import com.bos.payment.appName.utils.Utils.toast
import com.google.gson.Gson
import com.squareup.picasso.Picasso

class PayoutDMT : AppCompatActivity() {
    private lateinit var binding : ActivityPayoutDmtBinding
    private var mStash: MStash? = null
    private lateinit var pd: AlertDialog
    private lateinit var viewModel: MoneyTransferViewModel
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    private lateinit var viewModel1: PayoutViewModel
    private var serviceCharge: Double = 0.0
    var isSlabMatched = false // Flag to check if any slab matches

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayoutDmtBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        btnListener()
    }

    private fun btnListener() {

        binding.toolbar.tvToolbarName.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
        binding.CancelBtn.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
        binding.transactionCancelBtn.setOnClickListener {
            binding.serviceChargeLayout.visibility = View.GONE
        }
        binding.payBtn.setOnClickListener {
            getAllWalletBalance()
        }
        binding.proceedBtn.setOnClickListener {
            transactionValidation()
        }
        binding.amount.setOnClickListener {
            binding.proceedBtnLayout.visibility = View.VISIBLE
            binding.serviceChargeLayout.visibility = View.GONE
        }
    }

    private fun transactionValidation() {
        if (binding.ownerName.text.length < 3){
            binding.ownerName.requestFocus()
            toast("Please enter atleast 3 char your name")
        }else if (binding.amount.text.toString().isEmpty()) {
            binding.amount.requestFocus()
            toast("Please enter your amount")
        } else if (binding.bankAccountNo.text.toString().isEmpty()) {
            binding.bankAccountNo.requestFocus()
            toast("Please enter bank account no")
        } else if (binding.ifscCode.text.toString().isEmpty()) {
            binding.ifscCode.requestFocus()
            toast("Please enter ifsc code")
        }else {
            val transferAmountText = binding.amount.text.toString().trim()
//            getAllServiceCharge(transferAmountText)
            getAllApiPayoutCommercialCharge(transferAmountText)

        }

    }

    private fun getAllApiPayoutCommercialCharge(rechargeAmount: String) {
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

    private fun getAllApiPayoutCommercialChargeRes(
        response: GetPayoutCommercialRes,
        rechargeAmount: String
    ) {
        if (response.isSuccess == true) {
            binding.serviceChargeLayout.visibility = View.VISIBLE
            binding.proceedBtnLayout.visibility = View.GONE

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
                        serviceChargeCalculation(serviceCharge, gst, rechargeAmount, response, min, max)
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

    private fun getAllServiceCharge(rechargeAmount: String) {
        val getAPIServiceChargeReq = GetAPIServiceChargeReq(
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
        response?.let {
            if (it[0].Status == true) {
                binding.serviceChargeLayout.visibility = View.VISIBLE
                binding.amountPayLayout.visibility = View.VISIBLE

                val rechargeAmountValue = rechargeAmount.toDoubleOrNull() ?: 0.0
                val TDSTax = 5.0 // Fixed TDS rate of 5%

                // Retailer Commission Calculation
//                val retailerCommissionWithTDS = calculateCommissionWithTDS(
//                    commission = response.RetailerCommission?.toDoubleOrNull() ?: 0.0,
//                    commissionType = response.RetailerCommissionType,
//                    amount = rechargeAmountValue,
//                    tdsTax = TDSTax,
//                    isRetailer = true,
//                    isDistributer = false,
//                    isMDistributer = false
//                )
//                val subDisCommissionWithTDS = calculateCommissionWithTDS(
//                    commission = response.SubDisCommission?.toDoubleOrNull() ?: 0.0,
//                    commissionType = response.SubDisCommissionType,
//                    amount = rechargeAmountValue,
//                    tdsTax = TDSTax,
//                    isRetailer = false,
//                    isDistributer = true,
//                    isMDistributer = false
//                )
//                val mDisCommissionWithTDS = calculateCommissionWithTDS(
//                    commission = response.DisCommission?.toDoubleOrNull() ?: 0.0,
//                    commissionType = response.DisCommissionType,
//                    amount = rechargeAmountValue,
//                    tdsTax = TDSTax,
//                    isRetailer = false,
//                    isDistributer = false,
//                    isMDistributer = true
//                )
//                // Customer Commission Calculation
//                val customerCommissionWithTDS = calculateCommissionWithTDS(
//                    commission = response.CustomerCommission?.toDoubleOrNull() ?: 0.0,
//                    commissionType = response.CustomerCommissionType,
//                    amount = rechargeAmountValue,
//                    tdsTax = TDSTax,
//                    isRetailer = false,
//                    isMDistributer = true,
//                    isDistributer = false
//                )

                try {
                for (i in response.indices){
                    val gst = 18.0 // Fixed GST rate of 18%
                    // Get characters between ":" and "-" and convert to Double
                    val min = response[i].ServiceChargeSlab?.substringAfter(":")?.substringBefore("-")?.toDoubleOrNull()?:0.0

                    // Get characters after "-" and convert to Double
                    val max = response[i].ServiceChargeSlab?.substringAfter("-")?.toDoubleOrNull()?: 0.0

//                    if (rechargeAmount <= to.toString()) {
                        if (rechargeAmountValue in min..max) {
                            isSlabMatched = true   // Mark that a matching slab was found
//                            binding.serviceChargeLayout.visibility = View.VISIBLE
                            serviceCharge = response[i].ServiceCharge?.toDoubleOrNull() ?: 0.0
                            Log.d("serviceCharge", "$min $max")
//                            serviceChargeCalculation(serviceCharge, gst, rechargeAmount, response, min, max)
                            break
                        }
                }
                }catch (e: Exception){
                    e.printStackTrace()
                    Toast.makeText(this, "Out of bound exception", Toast.LENGTH_SHORT).show()
                }

                //if no matching slab was found, show an error message
                if (!isSlabMatched) {
                    isSlabMatched = false
                    binding.serviceChargeLayout.visibility = View.GONE
                    toast("No matching slab found for the amount: $rechargeAmountValue")
                }


                // Service Charge with GST Calculation
//                val gst = 18.0 // Fixed GST rate of 18%
//                val serviceCharge = response.ServiceCharge?.toDoubleOrNull() ?: 0.0
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
//                    Constants.totalTransaction,
//                    String.format("%.2f", totalRechargeAmount)
//                )
//                binding.totalTransferText.setText(String.format("%.2f", totalRechargeAmount))
//                Log.d("Total Recharge Amount", String.format("%.2f", totalRechargeAmount))
            } else {
                binding.serviceChargeLayout.visibility = View.GONE
                Toast.makeText(this, response[0].message.toString(), Toast.LENGTH_SHORT).show()
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
//                    )
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
        binding.serviceChargeLayout.visibility = View.VISIBLE

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
//            if (response.data[i].slabName?.contains(slabLimit.toString()) == true) {
            if (slabLimit != null) {
                Log.d("ServiceType", "${response.data[i].serviceType} - Index $i")
                 serviceChargeWithGst = when (response.data[i].serviceType) {
                    "Amount" -> {
                        binding.serviceChargeName.text = "Service Charge Rs"
                        val serviceChargeGst = serviceCharge * (gstRate / 100)
                        mStash?.setStringValue(Constants.serviceCharge, String.format("%.2f", serviceCharge))
                        serviceCharge + serviceChargeGst
                    }
                    "Percentage" -> {
                        binding.serviceChargeName.text = "Service Charge %"
                        val serviceInAmount = rechargeAmountValue * (serviceCharge / 100)
                        val serviceWithGst = serviceInAmount * (gstRate / 100)
                        mStash?.setStringValue(Constants.serviceCharge, String.format("%.2f", serviceInAmount))
                        serviceInAmount + serviceWithGst
                    }
                    else -> 0.0
                }
                break
//                totalServiceChargeWithGst += serviceChargeWithGst
            }
        }

        if (serviceChargeWithGst == 0.0) {
            Toast.makeText(this, "No matching slab found for the entered amount", Toast.LENGTH_SHORT).show()
            return 0.0
        }

        val actualAmount = serviceChargeWithGst + rechargeAmountValue
        mStash?.apply {
            setStringValue(Constants.actualAmountServiceChargeWithGST, String.format("%.2f", actualAmount))
            setStringValue(Constants.serviceChargeWithGST, String.format("%.2f", serviceChargeWithGst))
            setStringValue(Constants.totalTransaction, String.format("%.2f", actualAmount))
        }

        binding.apply {
            gstWithServiceCharge.setText(String.format("%.2f", serviceChargeWithGst))
            serviceChargeAmount.setText(String.format("%.2f", serviceCharge))
            totalTransferText.setText(String.format("%.2f", actualAmount))
        }

        Log.d("TotalRechargeAmount", String.format("%.2f", actualAmount))
        Log.d("totalAmountWithGst", String.format("%.2f", serviceChargeWithGst))

        return serviceChargeWithGst
    }

    private fun initView() {
        mStash = MStash.getInstance(this)
        pd = PD(this)

        try {
            val imageUrl = mStash!!.getStringValue(Constants.CompanyLogo, "")

            Picasso.get().load(imageUrl)
//            .placeholder(R.drawable.placeholder)  // Optional: placeholder while loading
                .error(R.drawable.no_image)        // Optional: error image if load fails
                .into(binding.toolbar.tvToolbarName)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        getAllApiServiceViewModel = ViewModelProvider(
            this,
            GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface))
        )[GetAllApiServiceViewModel::class.java]

        viewModel1 = ViewModelProvider(
            this,
            PayoutViewModelFactory(PayoutRepository(RetrofitClient.apiInterface))
        )[PayoutViewModel::class.java]
    }

    private fun getAllWalletBalance() {
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
        getAllApiServiceViewModel.getAllMerchantBalance(getMerchantBalanceReq).observe(this) { resource ->
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

    private fun getTransferAmountToAgentWithCal(rechargeAmount: String) {
        try {
            val currentDateTime = Utils.getCurrentDateTime()
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
                servicesChargeAmt = mStash!!.getStringValue(Constants.serviceCharge, ""),
                servicesChargeGSTAmt = mStash!!.getStringValue(Constants.serviceChargeWithGST, ""),
                servicesChargeWithoutGST = mStash!!.getStringValue(Constants.serviceCharge, ""),
                customerVirtualAddress = "",
                retailerCommissionAmt = mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, ""),
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
        if (response.isSuccess == true){
//            pd.dismiss()
            getPayoutAmountTransaction()
//            toast(response.message.toString())
        }else {
            pd.dismiss()
            toast(response.returnMessage.toString())
        }
    }

    private fun getPayoutAmountTransaction() {
        val payoutAppReq = PayoutAppReq(
            registrationID = mStash!!.getStringValue(Constants.MerchantId, ""),
            accountName = binding.ownerName.text.toString().trim(),
            accountNumber = binding.bankAccountNo.text.toString().trim(),
            ifscCode = binding.ifscCode.text.toString().trim(),
            amount = binding.amount.text.toString().trim(),
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
                putExtra("amount", binding.amount.text.toString())
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
                putExtra("customerBankName", binding.ownerName.text.toString())
                putExtra("bankIFSC", binding.ifscCode.text.toString())
                putExtra("bankAccountNO", binding.bankAccountNo.text.toString())


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
}