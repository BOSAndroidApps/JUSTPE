package com.bos.payment.appName.ui.view

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.constant.ConstantClass
import com.bos.payment.appName.data.model.creditCard.CreditCardBillPaymentReq
import com.bos.payment.appName.data.model.creditCard.CreditCardBillPaymentRes
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialReq
import com.bos.payment.appName.data.model.merchant.apiServiceCharge.GetPayoutCommercialRes
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsReq
import com.bos.payment.appName.data.model.transferAMountToAgent.TransferAmountToAgentsRes
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceReq
import com.bos.payment.appName.data.model.walletBalance.merchantBal.GetMerchantBalanceRes
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceRes
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.repository.MoneyTransferRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.data.viewModelFactory.MoneyTransferViewModelFactory
import com.bos.payment.appName.databinding.ActivityCreditCardDetailsBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.ui.viewmodel.MoneyTransferViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.getCurrentDateTime
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.google.gson.Gson
import retrofit2.Response

class CreditCardDetailsFragment : Fragment() {
    private lateinit var bin: ActivityCreditCardDetailsBinding
    private lateinit var viewModel: MoneyTransferViewModel
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    private lateinit var referenceId: String
    private lateinit var pd: AlertDialog
    private var mStash: MStash? = null
    private var rechargeType: String = ""


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bin = ActivityCreditCardDetailsBinding.inflate(inflater, container, false)

        intiView()
        btnListener()
        return bin.root

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun btnListener() {
        bin.proceedBtn.setOnClickListener {
            if (bin.creditCardEditText.text.length != 16) {
                Toast.makeText(requireContext(), "Enter Credit card number", Toast.LENGTH_SHORT)
                    .show()
            } else if (bin.amountText.text.toString().isEmpty()) {
                Toast.makeText(requireContext(), "Enter your amount", Toast.LENGTH_SHORT).show()
            } else {
                getAllApiPayoutCommercialCharge(bin.amountText.text.toString())
            }
        }


        bin.payBillBtn.setOnClickListener {
            getAllWalletBalance()
        }

        bin.cancelTransactionBtn.setOnClickListener {
            startActivity(Intent(requireContext(), DashboardActivity::class.java))
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getAllWalletBalance() {
        requireContext().runIfConnected {
            val walletBalanceReq = GetBalanceReq(
                parmUser = mStash!!.getStringValue(Constants.RegistrationId, ""),
                flag = "CreditBalance"
            )
            getAllApiServiceViewModel.getWalletBalance(walletBalanceReq)
                .observe(requireActivity()) { resource ->
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

    @RequiresApi(Build.VERSION_CODES.M)
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

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getMerchantBalance(mainBalance: Double) {
        val getMerchantBalanceReq = GetMerchantBalanceReq(
            parmUser = mStash!!.getStringValue(Constants.MerchantId, ""),
            flag = "DebitBalance"
        )
        getAllApiServiceViewModel.getAllMerchantBalance(getMerchantBalanceReq).observe(viewLifecycleOwner) { resource ->
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

    @RequiresApi(Build.VERSION_CODES.M)
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
                getTransferAmountToAgentWithCal(bin.amountText.text.toString())
            } else {
                pd.dismiss()
                Toast.makeText(
                    requireContext(),
                    "Wallet balance is low. VBal = $mainBalance, MBal = $merchantBalance, totalAmt = $totalAmount",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else {
            pd.dismiss()
            Toast.makeText(requireContext(), response.returnMessage.toString(), Toast.LENGTH_SHORT).show()
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTransferAmountToAgentWithCal(rechargeAmount: String) {

        try {
            val currentDateTime = getCurrentDateTime()
            val creditCardNumber = bin.creditCardEditText.text.toString().trim()
            val transferAmountToAgentsReq = TransferAmountToAgentsReq(
                transferFrom = mStash!!.getStringValue(Constants.RegistrationId, ""),
                transferTo = "Admin",
                transferAmt = mStash!!.getStringValue(Constants.totalTransaction, "")?:"0.00",
                remark = "Credit Card",
                transferFromMsg = "Your Account is debited by $rechargeAmount Rs. Due to Pay Out on number $creditCardNumber.",
                transferToMsg = "Your Account is credited by $rechargeAmount Rs. Due to Pay Out on number $creditCardNumber.",
                amountType = "Payout",
                actualTransactionAmount = rechargeAmount,
                transIpAddress = mStash!!.getStringValue(Constants.deviceIPAddress, ""),
                parmUserName = mStash!!.getStringValue(Constants.RegistrationId, ""),
                merchantCode = mStash!!.getStringValue(Constants.MerchantId, ""),
                servicesChargeAmt = mStash!!.getStringValue(Constants.serviceCharge, "")?:"0.00",
                servicesChargeGSTAmt = mStash!!.getStringValue(Constants.serviceChargeWithGST, "")?:"0.00",
                servicesChargeWithoutGST = mStash!!.getStringValue(Constants.serviceCharge, "")?:"0.00",
                customerVirtualAddress = "",
                retailerCommissionAmt = mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "")?:"0.00",
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
                .observe(viewLifecycleOwner) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
//                                 pd.dismiss()
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        getAllServiceChargeApiCalRes(response, rechargeAmount)
                                    }
                                }
                            }

                            ApiStatus.ERROR -> {
                                pd.dismiss()
                                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
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
                requireContext(),
                e.message.toString() + " " + e.localizedMessage?.toString(),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getAllServiceChargeApiCalRes(
        response: TransferAmountToAgentsRes,
        rechargeAmount: String
    ) {
        if (response.isSuccess == true){
            creditCardValidation()
        }else {
            pd.dismiss()
            toast(response.returnMessage.toString())
        }
    }

    private fun getAllApiPayoutCommercialCharge(rechargeAmount: String) {
        requireContext().runIfConnected {
            val getPayoutCommercialReq = GetPayoutCommercialReq(
                merchant = mStash!!.getStringValue(Constants.RegistrationId, ""),
                productId = "F0125",
                modeofPayment = "IMPS"
            )
            Log.d(TAG, "getAllApiPayoutCommercialCharge: " + Gson().toJson(getPayoutCommercialReq))
            getAllApiServiceViewModel.getAllApiPayoutCommercialCharge(getPayoutCommercialReq)
                .observe(viewLifecycleOwner) { resource ->
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

    @SuppressLint("DefaultLocale", "SetTextI18n", "SuspiciousIndentation")
    private fun getAllApiPayoutCommercialChargeRes(
        response: GetPayoutCommercialRes,
        rechargeAmount: String
    ) {
        response.let {
            if (it.isSuccess == true) {
                bin.paymentBtnLayout.visibility = View.VISIBLE

                // Update visibility based on service type
                bin.serviceChargeCommissionLayout.visibility = if (response.data[0].serviceType != "Not Applicable") View.VISIBLE else View.GONE
//                binding.tvAmount.visibility = View.VISIBLE
//                binding.etAmount.visibility = View.VISIBLE

                // Save commission types in shared preferences
                with(mStash!!) {
                    setStringValue(Constants.admin_CommissionType, response.data[0].adminCommissionType.toString())
                    setStringValue(Constants.retailer_CommissionType, response.data[0].retailerCommissionType.toString())
                    setStringValue(Constants.customer_CommissionType, response.data[0].customerCommissionType.toString())
                    setStringValue(Constants.serviceType, response.data[0].serviceType.toString())
                }

                // Parse values safely
                val rechargeAmountValue = rechargeAmount.toDoubleOrNull() ?: 0.0
                val retailerCommission = response.data[0].retailerCommission?.toDoubleOrNull() ?: 0.0
                val customerCommission = response.data[0].customerCommission?.toDoubleOrNull() ?: 0.0
                val adminCommission = response.data[0].adminCommission?.toDoubleOrNull() ?: 0.0
                val TDSTax = 5.0 // Fixed TDS rate

                // Function to calculate commission with TDS
                fun calculateCommission(amount: Double, type: String?, tdsRate: Double): Double {
                    return when (type) {
                        "Percentage" -> {
                            val commissionAmount = rechargeAmountValue * (amount / 100)
                            commissionAmount - (commissionAmount * (tdsRate / 100))
                        }
                        "Amount" -> {
                            amount - (amount * (tdsRate / 100))
                        }
                        else -> 0.0
                    }
                }
                // calculate admin commission
                val finalAdminCommission = calculateCommission(adminCommission, response.data[0].adminCommissionType, TDSTax )
                mStash!!.setStringValue(Constants.adminCommissionWithoutTDS, String.format("%.2f", finalAdminCommission))

                // Calculate retailer commission
                val finalRetailerCommission = calculateCommission(retailerCommission, response.data[0].retailerCommissionType, TDSTax)
                mStash!!.setStringValue(Constants.retailerCommissionWithoutTDS, String.format("%.2f", finalRetailerCommission))

                // Calculate customer commission
                val finalCustomerCommission = calculateCommission(customerCommission, response.data[0].customerCommissionType, TDSTax)
                mStash!!.setStringValue(Constants.tds, String.format("%.2f", customerCommission * (TDSTax / 100)))
                mStash!!.setStringValue(Constants.customerCommissionWithoutTDS, String.format("%.2f", finalCustomerCommission))

                // Log results
                Log.d("FinalRetailerCommission", String.format("%.2f",finalRetailerCommission))
                Log.d("FinalCustomerCommission", String.format("%.2f",finalCustomerCommission))
                Log.d("FinalAdminCommission", String.format("%.2f",finalAdminCommission))

                // Service Charge with GST Calculation
                val gst = 18.0 // Fixed GST rate of 18%
                val serviceCharge = response.data[0].serviceCharge?.toDoubleOrNull() ?: 0.0
                val totalServiceChargeWithGst = serviceChargeCalculation(serviceCharge, gst, rechargeAmount, response)

                // Total Recharge Amount Calculation
                val totalRechargeAmount = (rechargeAmount.toDoubleOrNull() ?: 0.0) + totalServiceChargeWithGst

                // Store the total transaction in mStash
//                mStash!!.setStringValue(Constants.actualAmountServiceChargeWithGST, String().format("%.2f", totalRechargeAmount))

                mStash!!.setStringValue(Constants.totalTransaction, String.format("%.2f", totalRechargeAmount))
                bin.totalTrnasaction.text = String.format("%.2f", totalRechargeAmount)
                Log.d("TotalRechargeAmount", String.format("%.2f", totalRechargeAmount))
            } else {
                toast(response.returnMessage.toString())
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
                val totalAmount = amount + commissionAmount
                val finalCommission = commissionAmount - commissionTDS

                if (isRetailer) {
                    mStash!!.setStringValue(Constants.retailerCommissionWithoutTDS, String.format("%.2f", finalCommission))
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
//                    bin.retailerCommission.text = String.format("%.2f", commission)
                    bin.totalTrnasaction.text = String.format("%.2f", totalAmount)
//                    bin.retailerCommissionWithoutTDs.text = String.format("%.2f", finalCommission)
//                    bin.tdsText.text = String.format("%.2f", commissionTDS)
//                    bin.retailerCommissionName.text = "Retailer Commission %"
                } else if (isDistributer){
                    mStash!!.setStringValue(Constants.adminCommissionWithoutTDS, String.format("%.2f", finalCommission))
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
                }else if (isMDistributer){
                    mStash!!.setStringValue(Constants.mDistributerCommissionWithoutTDS, String.format("%.2f", finalCommission))
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
                }
                else {
                    bin.totalTrnasaction.text = String.format("%.2f", totalAmount)
                    mStash!!.setStringValue(Constants.customerCommissionWithoutTDS, String.format("%.2f", finalCommission))
//                    bin.customerCommissionText.text = String.format("%.2f", commissionAmount)
//                    bin.customerCommissionWithoutTDSText.text = String.format("%.2f", finalCommission)
                }

                commissionAmount
            }

            "Amount" -> {
                // Fixed commission in amount
                val commissionTDS = commission * (tdsTax / 100)
                val totalAmount = amount + commission
                val finalCommission = commission - commissionTDS

                if (isRetailer) {
                    mStash!!.setStringValue(Constants.retailerCommissionWithoutTDS, String.format("%.2f", finalCommission))
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
                    bin.totalTrnasaction.text = String.format("%.2f", totalAmount)
//                    bin.retailerCommission.text = String.format("%.2f", commission)
//                    bin.retailerCommissionWithoutTDs.text = String.format("%.2f", finalCommission)
//                    bin.tdsText.text = String.format("%.2f", commissionTDS)
//                    bin.retailerCommissionName.text = "Retailer Commission RS"
                } else if (isDistributer){
                    mStash!!.setStringValue(Constants.adminCommissionWithoutTDS, String.format("%.2f", finalCommission))
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
                } else if (isMDistributer){
                    mStash!!.setStringValue(Constants.mDistributerCommissionWithoutTDS, String.format("%.2f", finalCommission))
                    mStash!!.setStringValue(Constants.tds, String.format("%.2f", commissionTDS))
                } else {
                    bin.totalTrnasaction.text = String.format("%.2f", totalAmount)
                    mStash!!.setStringValue(Constants.customerCommissionWithoutTDS, String.format("%.2f", finalCommission))
//                    bin.customerCommissionText.text = String.format("%.2f", commission)
//                    bin.customerCommissionWithoutTDSText.text = String.format("%.2f", finalCommission)
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
//        response: List<GetAPIServiceChargeRes>
        response: GetPayoutCommercialRes
    ): Double {
        val rechargeAmountValue = rechargeAmount.toDoubleOrNull() ?: 0.0

        val totalAmountWithGst = when (response.data[0].serviceType) {
            "Amount" -> {
                // Service charge is a fixed amount
                bin.serviceChargeText.text = "Service Charge Rs"
                val serviceChargeGst = serviceCharge * (gstRate / 100)
//                val serviceWithGST = serviceCharge + serviceChargeGst
//                bin.serviceChargeWithGST.text = String.format("%.2f", serviceWithGST)
                mStash!!.setStringValue(Constants.serviceCharge, String.format("%.2f", serviceCharge))

                serviceCharge + serviceChargeGst
//                mStash.setStringValue(Constants.serviceCharge, )
            }

            "Percentage" -> {
                // Service charge is a percentage of the recharge amount
                bin.serviceChargeText.text = "Service Charge %"
                val serviceInAmount = rechargeAmountValue * (serviceCharge / 100)
                val serviceGst = serviceInAmount * (gstRate / 100)
//                val serviceWithGST = serviceInAmount + serviceGst
//                bin.serviceChargeWithGST.text = String.format("%.2f", serviceWithGST)
                mStash!!.setStringValue(Constants.serviceCharge, String.format("%.2f", serviceInAmount))

//                mStash!!.setStringValue(Constants.serviceChargeWithGST, String.format("%.2f", serviceWithGST))

                Log.d("serviceCharge",String.format("%.2f", serviceInAmount))
                serviceInAmount + serviceGst

            }

            else -> 0.0
        }

//        val actualAmount = totalAmountWithGst + rechargeAmountValue
        // Display service charge and total amount with GST in the UI
        mStash!!.setStringValue(Constants.serviceChargeWithGST, String.format("%.2f", totalAmountWithGst))
        bin.serviceChargeWithGST.text = String.format("%.2f", totalAmountWithGst)
        bin.serviceChargeAmt.text = String.format("%.2f", serviceCharge)
        bin.totalTrnasaction.text = String.format("%.2f", totalAmountWithGst)

        Log.d("serviceChargeWithGST",String.format("%.2f", totalAmountWithGst))
        Log.d("totalTrnasaction",String.format("%.2f", totalAmountWithGst))

//        Log.d("totalAmountWithGst", actualAmount.toString())

        // Return the total service charge (with GST) to include in the final transaction
        return totalAmountWithGst
    }

    private fun creditCardValidation() {
        val referenceID: String = ConstantClass.generateRandomNumber()
        val creditCardBillPaymentReq = CreditCardBillPaymentReq(
            CardHolderName = bin.cardHolderName1.text.toString().trim(),
            cardNumber = bin.creditCardEditText.text.toString().trim(),
            transferAmount = bin.amountText.text.toString().trim(),
            externalRef = referenceID,
            latitude = "89.77",
            longitude = "88.77",
            Remarks = "testing",
            alertEmail = mStash!!.getStringValue(Constants.EmailID, ""),
            RegistrationID = mStash!!.getStringValue(Constants.MerchantId, ""),
        )
        Log.d("creditCardDetails", Gson().toJson(creditCardBillPaymentReq))
        viewModel.creditCardDetails(creditCardBillPaymentReq)
            .observe(viewLifecycleOwner) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.let { users ->
                                creditCardRes(users)
//                                users.body()?.let { it1 -> creditCardRes(it1) }
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

    private fun intiView() {
        pd = PD(requireContext())
        mStash = MStash.getInstance(requireContext())
        referenceId = ConstantClass.generateRandomNumber()

        viewModel = ViewModelProvider(
            this,
            MoneyTransferViewModelFactory(MoneyTransferRepository(RetrofitClient.apiAllAPIService))
        )[MoneyTransferViewModel::class.java]

        getAllApiServiceViewModel = ViewModelProvider(
            this,
            GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface))
        )[GetAllApiServiceViewModel::class.java]

        bin.paymentBtnLayout.visibility = View.GONE
        bin.cardDetailsLayout.visibility = View.GONE
        bin.serviceChargeCommissionLayout.visibility = View.GONE

    }

    private fun creditCardRes(response: Response<CreditCardBillPaymentRes>) {
        if (response.body()!!.StatusCode == "200") {
            pd.dismiss()

            val intent = Intent(requireContext(), CreditCardSuccessfulScreen::class.java).apply {

//                putExtra("transactionId", response.txnStatus.toString())
//                putExtra("operatorName", response.operatorname.toString())
//                putExtra("mobileNumber", mobileNo.toString())
                putExtra("amount", response.body()!!.TxnValue.toString())
//                putExtra("totalAmount", totalAmountNet.toString())
                putExtra("referenceId", response.body()!!.ExternalRef.toString())
                putExtra("transactionId", response.body()!!.TxnReferenceId.toString())
//                putExtra("operationId", body.data?.operatorid.toString())
                putExtra("dateAndTime", response.body()!!.Timestamp.toString())
//                putExtra("tdsTax", response.body().tds.toString())
                putExtra("Status", response.body()!!.StatusCode.toString())
                putExtra("message", response.body()!!.Message.toString())
                putExtra("cardNumber", response.body()!!.CardNumber.toString())
                putExtra("cardHolderName", bin.cardHolderName1.text.toString().trim())
                putExtra("cardNetwork", response.body()!!.CardNetwork.toString())
                putExtra("BankName", response.body()!!.BankName.toString())

                //service and commission charge
//                putExtra("tdsTax", mStash!!.getStringValue(Constants.tds, "0.00"))
//                putExtra("retailerCommissionWithoutTDS", mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "0.00"))
//                putExtra("customerCommissionWithoutTDS", mStash!!.getStringValue(Constants.customerCommissionWithoutTDS, "0.00"))
                putExtra("serviceCharge", mStash!!.getStringValue(Constants.serviceCharge, "0.00"))
                putExtra("serviceChargeWithGST", mStash!!.getStringValue(Constants.serviceChargeWithGST, "0.00"))
                putExtra("totalTransaction", mStash!!.getStringValue(Constants.totalTransaction, "0.00"))

            }
            startActivity(intent)

        } else {
            Toast.makeText(requireContext(), response.body()?.Message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}
