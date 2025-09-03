package com.bos.payment.appName.ui.view.Dashboard.dmt

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityDmtrachargeSuccessfulPageBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.utils.MStash
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DMTRechargeSuccessfulPage : AppCompatActivity() {
    private lateinit var binding: ActivityDmtrachargeSuccessfulPageBinding
    private var mStash: MStash? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDmtrachargeSuccessfulPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        btnListener()

    }

    private fun btnListener() {
        binding.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val currentDateTime = dateFormat.format(calendar.time)

        println("Current Date and Time: $currentDateTime")

        mStash = MStash.getInstance(this)
        binding.repeatRecharge.visibility = View.GONE
//        binding.let {
//            binding.operatorName.text =
//                "Pay " + intent.getStringExtra("operatorName") + " Prepaid"
            binding.totalAmount.text = "₹" + intent.getStringExtra("totalTransaction")
        binding.amount.text = "₹" + intent.getStringExtra("amount")
//            binding.dateAndTime.text = intent.getStringExtra("dateAndTime")
//        binding.dateAndTime.text = currentDateTime.toString()
        binding.dateAndTime.text = intent.getStringExtra("transactionDate")
//            binding.transactionId.text = intent.getStringExtra("transactionId")
        binding.referenceId.text = intent.getStringExtra("referenceId")
//        binding.toUpiId.text = intent.getStringExtra("upiId")
        binding.utrNumber.text = intent.getStringExtra("transactionId")
        binding.customerName.text = intent.getStringExtra("customerBankName")
        binding.accountNo.text = intent.getStringExtra("bankAccountNO")
        binding.ifscCode.text = intent.getStringExtra("bankIFSC")

//            binding.mobileNo.text = "+91" + intent.getStringExtra("mobileNumber")
//            binding.customerCharge.text = "₹" + intent.getStringExtra("CustomerCharge")
//            binding.bcCharge.text = "₹" + intent.getStringExtra("BCShare")
//            binding.netCommission.text = "₹" + intent.getStringExtra("NetCommission")
//            binding.gstCharge.text = "₹" + intent.getStringExtra("GST")
//            binding.tdsCharge.text = "₹" + intent.getStringExtra("TDS")
//            binding.status.text = "₹" + intent.getStringExtra("message")


        // Logic for service and commission charges
//        when {
//            // Handle retailer commission visibility and data
////            !mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "")
////                .isNullOrEmpty() -> {
////                binding.retailerCommissionLayout.visibility = View.VISIBLE
////                binding.customerCommWithoutTDSNameLayout.visibility = View.GONE
////                binding.serviceChargeLayout.visibility = View.GONE
////                binding.totalAmount.text = "₹${intent.getStringExtra("amount")}"
////            }
////
////            // Handle customer commission visibility and data
////            !mStash!!.getStringValue(Constants.customerCommissionWithoutTDS, "")
////                .isNullOrEmpty() -> {
////                binding.retailerCommissionLayout.visibility = View.VISIBLE
////                binding.customerCommWithoutTDSNameLayout.visibility = View.VISIBLE
////                binding.retailerCommWithoutTDSNamelayout.visibility = View.GONE
////                binding.serviceChargeLayout.visibility = View.GONE
////                binding.totalAmount.text = "₹${intent.getStringExtra("amount")}"
////            }
//
//            // Handle service charge with GST visibility and data
//            !mStash!!.getStringValue(Constants.serviceChargeWithGST, "").isNullOrEmpty() -> {
////                binding.retailerCommissionLayout.visibility = View.GONE
//                binding.serviceChargeLayout.visibility = View.VISIBLE
////                binding.totalAmount.text = "₹${intent.getStringExtra("totalTransaction")}"
//                binding.totalAmount.text =
//                    "₹${mStash!!.getStringValue(Constants.actualAmountServiceChargeWithGST, "")}"
//            }
//
//            else -> {
//                // Reset visibility or any default behavior if needed
//            }
//        }

        //  Set remaining commission and tax values
//        binding.retailerCommWithoutTDSText.text =
//            intent.getStringExtra("retailerCommissionWithoutTDS")
//        binding.customerCommWithoutTDSText.text =
//            intent.getStringExtra("customerCommissionWithoutTDS")
//        binding.tdsText.text = intent.getStringExtra("tdsTax")
//        binding.serviceChargeText.text = intent.getStringExtra("serviceCharge")
        binding.serviceChargeWithGSTText.text = "₹" + intent.getStringExtra("serviceChargeWithGST")
        binding.totalTransactionText.text = "₹" + intent.getStringExtra("totalTransaction")


//        binding.status.text = intent.getStringExtra("message")

        // Set transaction status and related UI components
//        val isSuccess = intent.getStringExtra("Status") == "true"
        val isSuccess = (intent.getStringExtra("Status") == "true" || intent.getStringExtra("Status") == "SUCCESS")
        val colorResId = if (isSuccess) R.drawable.green_circle else R.drawable.green_circle
        binding.circle.setBackgroundResource(colorResId)
        binding.status.text = if (isSuccess) "Transaction Successful" else "Transaction Successful"

//            val imageResourceName = intent.getStringExtra("image")
//            if (imageResourceName != null) {
//                val imageResId = resources.getIdentifier(imageResourceName, "drawable", packageName)
//                it.operatorLogo.setImageResource(imageResId)
//            } else {
//                it.operatorLogo.setImageResource(R.drawable.no_image)
//            }

    }
}