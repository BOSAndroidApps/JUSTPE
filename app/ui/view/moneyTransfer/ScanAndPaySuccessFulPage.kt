package com.bos.payment.appName.ui.view.moneyTransfer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityScanAndPaySuccessFulPageBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.utils.MStash

class ScanAndPaySuccessFulPage : AppCompatActivity() {
    private lateinit var bin: ActivityScanAndPaySuccessFulPageBinding
    private var mStash: MStash? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityScanAndPaySuccessFulPageBinding.inflate(layoutInflater)
        setContentView(bin.root)
        
        initView()
        btnListener()
        
    }

    private fun btnListener() {
        bin.ivBack.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        mStash = MStash.getInstance(this)
        bin.repeatRecharge.visibility = View.GONE
//        bin.mobileNo.visibility = View.GONE
//        bin.mobileText.visibility = View.GONE
//        bin.let {
//            bin.operatorName.text =
//                "Pay " + intent.getStringExtra("operatorName") + " Prepaid"
        bin.totalAmount.text = "₹" + intent.getStringExtra("totalTransaction")
        bin.amount.text = "₹" + intent.getStringExtra("amount")
//            bin.dateAndTime.text = intent.getStringExtra("dateAndTime")
        bin.dateAndTime.text = intent.getStringExtra("dateAndTime")
//            bin.transactionId.text = intent.getStringExtra("transactionId")
        bin.referenceId.text = intent.getStringExtra("referenceId")
        bin.toUpiId.text = intent.getStringExtra("upiId")
        bin.toUpiIdName.text = "To: " + intent.getStringExtra("payeeName")
//        bin.transactionId.text = intent.getStringExtra("transactionId")
        bin.utrNumber.text = intent.getStringExtra("utrNo")
//        bin.mobileNo.text = "+91" + intent.getStringExtra("mobileNumber")

        // Logic for service and commission charges
//        when {
//            // Handle retailer commission visibility and data
////            !mStash!!.getStringValue(Constants.retailerCommissionWithoutTDS, "").isNullOrEmpty() -> {
////                bin.retailerCommissionLayout.visibility = View.VISIBLE
////                bin.customerCommWithoutTDSNameLayout.visibility = View.GONE
////                bin.serviceChargeLayout.visibility = View.GONE
////                bin.totalAmount.text = "₹${intent.getStringExtra("amount")}"
////            }
////
////            // Handle customer commission visibility and data
////            !mStash!!.getStringValue(Constants.customerCommissionWithoutTDS, "").isNullOrEmpty() -> {
////                bin.retailerCommissionLayout.visibility = View.VISIBLE
////                bin.customerCommWithoutTDSNameLayout.visibility = View.VISIBLE
////                bin.retailerCommWithoutTDSNamelayout.visibility = View.GONE
////                bin.serviceChargeLayout.visibility = View.GONE
////                bin.totalAmount.text = "₹${intent.getStringExtra("amount")}"
////            }
//
//            // Handle service charge with GST visibility and data
//            !mStash!!.getStringValue(Constants.serviceChargeWithGST, "").isNullOrEmpty() -> {
////                bin.retailerCommissionLayout.visibility = View.GONE
//                bin.serviceChargeLayout.visibility = View.VISIBLE
////                bin.totalAmount.text = "₹${intent.getStringExtra("totalTransaction")}"
//                bin.totalAmount.text = "₹${mStash!!.getStringValue(Constants.actualAmountServiceChargeWithGST, "")}"
//            }
//
//            else -> {
//                // Reset visibility or any default behavior if needed
//            }
//        }

//         Set remaining commission and tax values
//        bin.retailerCommWithoutTDSText.text = intent.getStringExtra("retailerCommissionWithoutTDS")
//        bin.customerCommWithoutTDSText.text = intent.getStringExtra("customerCommissionWithoutTDS")
//        bin.tdsText.text = intent.getStringExtra("tdsTax")
//        bin.serviceChargeText.text = intent.getStringExtra("serviceCharge")
        bin.serviceChargeWithGSTText.text = "₹" + intent.getStringExtra("serviceChargeWithGST")
        bin.totalTransactionText.text = "₹" + intent.getStringExtra("totalTransaction")

//        bin.status.text = "₹" + intent.getStringExtra("message")
        bin.status.text = intent.getStringExtra("message")



        // Set transaction status and related UI components
//        val isSuccess = intent.getStringExtra("Status") == "true"
        val isSuccess = (intent.getStringExtra("Status") == "true")
        val colorResId = if (isSuccess) R.drawable.green_circle else R.drawable.green_circle
        bin.circle.setBackgroundResource(colorResId)
        bin.status.text = if (isSuccess) "Transaction Successful" else "Transaction Successful"
    }
}