package com.bos.payment.appName.ui.view.Dashboard.Wallet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityMyWalletBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.squareup.picasso.Picasso

class MyWalletActivity : AppCompatActivity() {
    private var binding: ActivityMyWalletBinding? = null
    private val context: Context = this@MyWalletActivity
    private var mStash: MStash? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMyWalletBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initView()
        btnListener()
        binding!!.toolbar.tvToolbarName.setText("Add Amount")
    }

    private fun btnListener() {
        binding!!.toolbar.dashboardImage.setOnClickListener {
            startActivity(Intent(this@MyWalletActivity, DashboardActivity::class.java))
            finish()
        }
    }

    private fun initView() {
        mStash = MStash.getInstance(this)

        try {
            val imageUrl = mStash!!.getStringValue(Constants.CompanyLogo, "")
            Picasso.get().load(imageUrl)
//            .placeholder(R.drawable.placeholder)  // Optional: placeholder while loading
                .error(R.drawable.ic_error)        // Optional: error image if load fails
                .into(binding!!.toolbar.dashboardImage)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        binding!!.availableCredit.setText(mStash!!.getStringValue(Constants.availCreditAmount, ""))
        binding!!.mainBalance.setText(mStash!!.getStringValue(Constants.retailerMainVirtualAmount, ""))
        binding!!.actualBalance.setText(mStash!!.getStringValue(Constants.retailerActualAvailAmount, ""))
        binding!!.holdAmount.setText(mStash!!.getStringValue(Constants.retailerHoldAmt, ""))
        binding!!.creditLimit.setText(mStash!!.getStringValue(Constants.retailerCreditLimit, ""))
    }
}