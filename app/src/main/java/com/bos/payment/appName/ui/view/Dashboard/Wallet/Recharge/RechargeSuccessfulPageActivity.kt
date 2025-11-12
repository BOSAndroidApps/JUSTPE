package com.bos.payment.appName.ui.view.Dashboard.Wallet.Recharge

import android.annotation.SuppressLint
import android.content.Intent
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
            binding.operatorName.text = "Pay ${intent.getStringExtra("operatorName")} Prepaid"
            binding.rechargeAmount.text = "₹${intent.getStringExtra("amount")}"
            binding.dateAndTime.text = intent.getStringExtra("dateAndTime")
            binding.transactionId.text = intent.getStringExtra("transactionId")
            binding.referenceId.text = intent.getStringExtra("referenceId")
            binding.mobileNo.text = "+91${intent.getStringExtra("mobileNumber")}"

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
            binding.totalAmount.text = "₹" + intent.getStringExtra("totalTransaction")
            binding.serviceChargeWithGSTText.text = "₹" + intent.getStringExtra("serviceChargeWithGST")
            binding.totalTransactionText.text = "₹" + intent.getStringExtra("totalTransaction")

            // Set transaction status and related UI components
            val isSuccess = intent.getStringExtra("Status") == "true"
            val colorResId = if (isSuccess) R.drawable.green_circle else R.drawable.red_circle
            binding.circle.setBackgroundResource(colorResId)
            binding.status.text = if (isSuccess) "Recharge Successful" else "Recharge Failed"

            // Set the operator logo or a fallback image
            val imageResourceName = intent.getStringExtra("image")
            if (!imageResourceName.isNullOrEmpty()) {
                val imageResId = resources.getIdentifier(imageResourceName, "drawable", packageName)
                it.operatorLogo.setImageResource(imageResId)
            } else {
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