package com.bos.payment.appName.ui.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityCreditCardSuccessfulScreenBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash

class CreditCardSuccessfulScreen : AppCompatActivity() {
    private lateinit var binding : ActivityCreditCardSuccessfulScreenBinding
    private var mStash: MStash? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditCardSuccessfulScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        btnListener()
    }

    private fun btnListener() {
        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
//            onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        mStash = MStash.getInstance(this)
        binding.repeatRecharge.visibility = View.GONE
        binding.mobileNo.visibility = View.GONE
        binding.mobileText.visibility = View.GONE
//        binding.let {
//            binding.operatorName.text =
//                "Pay " + intent.getStringExtra("operatorName") + " Prepaid"
//        binding.totalAmount.text = "₹" + intent.getStringExtra("amount")
        binding.amount.text = "₹" + intent.getStringExtra("amount")
//            binding.dateAndTime.text = intent.getStringExtra("dateAndTime")
        binding.dateAndTime.text = intent.getStringExtra("dateAndTime")
//            binding.transactionId.text = intent.getStringExtra("transactionId")
        binding.referenceId.text = intent.getStringExtra("referenceId")
        binding.transactionId.text = intent.getStringExtra("transactionId")
        binding.cardHolderName.text = intent.getStringExtra("cardHolderName")
        binding.cardNetwork.text = intent.getStringExtra("cardNetwork")
        binding.cardNumber.text = intent.getStringExtra("cardNumber")
        binding.bankName.text = intent.getStringExtra("BankName")

        binding.mobileNo.text = "+91" + intent.getStringExtra("mobileNumber")


        // Logic for service and commission charges
        when {
            // Handle retailer commission visibility and data
//            !mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "").isNullOrEmpty() -> {
//                binding.retailerCommissionLayout.visibility = View.VISIBLE
//                binding.customerCommWithoutTDSNameLayout.visibility = View.GONE
//                binding.serviceChargeLayout.visibility = View.GONE
//                binding.totalAmount.text = "₹${intent.getStringExtra("amount")}"
//            }
//
//            // Handle customer commission visibility and data
//            !mStash!!.getStringValue(Constants.customerCommissionWithoutTDS, "").isNullOrEmpty() -> {
//                binding.retailerCommissionLayout.visibility = View.VISIBLE
//                binding.customerCommWithoutTDSNameLayout.visibility = View.VISIBLE
//                binding.retailerCommWithoutTDSNamelayout.visibility = View.GONE
//                binding.serviceChargeLayout.visibility = View.GONE
//                binding.totalAmount.text = "₹${intent.getStringExtra("amount")}"
//            }

            // Handle service charge with GST visibility and data
            !mStash!!.getStringValue(Constants.serviceChargeWithGST, "").isNullOrEmpty() -> {
//                binding.retailerCommissionLayout.visibility = View.GONE
                binding.serviceChargeLayout.visibility = View.VISIBLE
                binding
//                binding.totalAmount.text = "₹${intent.getStringExtra("totalTransaction")}"
            }

            else -> {
                // Reset visibility or any default behavior if needed
            }
        }

        // Set remaining commission and tax values
//        binding.retailerCommWithoutTDSText.text = intent.getStringExtra("retailerCommissionWithoutTDS")
//        binding.customerCommWithoutTDSText.text = intent.getStringExtra("customerCommissionWithoutTDS")
//        binding.tdsText.text = intent.getStringExtra("tdsTax")
        binding.totalAmount.text = "₹" + intent.getStringExtra("totalTransaction")

//        binding.serviceChargeText.text = intent.getStringExtra("serviceCharge")
        binding.serviceChargeWithGSTText.text = "₹" + intent.getStringExtra("serviceChargeWithGST")
        binding.totalTransactionText.text = "₹" + intent.getStringExtra("totalTransaction")

//        binding.status.text = "₹" + intent.getStringExtra("message")
        binding.status.text = intent.getStringExtra("message")



        // Set transaction status and related UI components
//        val isSuccess = intent.getStringExtra("Status") == "true"
        val isSuccess = (intent.getStringExtra("Status") == "200")
        val colorResId = if (isSuccess) R.drawable.green_circle else R.drawable.red_circle
        binding.circle.setBackgroundResource(colorResId)
        binding.status.text = if (isSuccess) "Transaction Successful" else "Transaction Failed"
    }
}