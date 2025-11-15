package com.bos.payment.appName.ui.view.Dashboard.Wallet.Recharge

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityRechargeBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.ui.view.Dashboard.Wallet.Fragment.RechargeFragment
import com.bos.payment.appName.ui.view.Dashboard.activity.JustPeDashboard
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash


class RechargeSuccessfulPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRechargeBinding
    private var mStash: MStash? = null

    companion object{
      lateinit  var operatorLogo: Drawable
      lateinit  var rechargeStatus: String
      lateinit  var Datetime: String
      lateinit  var planPrice: String
      lateinit  var serviceChargeWithGST: String
      lateinit  var totalTransaction: String
      lateinit  var transactionID: String
      lateinit  var referenceId: String
      lateinit  var mobileNumber: String
      lateinit  var orderID: String
      lateinit  var operatorName: String
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityRechargeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        btnListener()

    }

    private fun btnListener() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.repeatRecharge.setOnClickListener {
            callFragment(RechargeFragment(), "mobile")

        }
    }

    @SuppressLint("SetTextI18n", "ResourceType")
    private fun initView() {
        mStash = MStash.getInstance(this)
        binding.repeatRecharge.visibility = View.GONE

        binding.let {
            // Set various UI components with data from the intent
            binding.operatorName.text = "Pay $operatorName"
            binding.rechargeAmount.text = "₹ ${planPrice}"
            binding.dateAndTime.text = Datetime
            binding.transactionId.text = transactionID
            binding.referenceId.text =  referenceId
            binding.mobileNo.text = "+91 ${ mobileNumber}"

            // Logic for service and commission charges
            when {
                // Handle service charge with GST visibility and data
                !mStash!!.getStringValue(Constants.serviceChargeWithGST, "").isNullOrEmpty() -> {
                    binding.serviceChargeLayout.visibility = View.VISIBLE
                }

                else -> {
                    // Reset visibility or any default behavior if needed
                }
            }


            // Set remaining commission and tax values
            binding.totalAmount.text = "₹ ${totalTransaction}"
            binding.serviceChargeWithGSTText.text = "₹ ${serviceChargeWithGST}"
            binding.totalTransactionText.text = "₹ ${totalTransaction}"

            if(serviceChargeWithGST.toDouble()>0.0){
                binding.servicewithgstlayout.visibility=View.VISIBLE
            }
            else{
                binding.servicewithgstlayout.visibility=View.GONE
            }

            // Set transaction status and related UI components
            val isSuccess = rechargeStatus == "SUCCESS"
            val colorResId = if (isSuccess) R.drawable.green_circle else R.drawable.red_circle

            binding.circle.setBackgroundResource(colorResId)
            binding.status.text = if (isSuccess) "Recharge Successful" else "Recharge Failed"

            if(operatorLogo!=null){
                it.operatorLogo.setImageDrawable(operatorLogo)
            }
            else{
                it.operatorLogo.setImageResource(R.drawable.no_image)
            }
        }

    }

    private fun callFragment(fragment: Fragment, rechargeType: String) {
        val bundle = Bundle()
        bundle.putString("RechargeType", rechargeType)
        fragment.arguments = bundle
        val fragmentManager: FragmentManager = this.supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        startActivity(Intent(this, JustPeDashboard::class.java))
        finish()
    }

}