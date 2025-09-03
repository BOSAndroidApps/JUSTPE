package com.bos.payment.appName.ui.view.Dashboard.Wallet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.bos.payment.appName.databinding.ActivityRechargeDetailsBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity

class RechargeDetailsActivity : AppCompatActivity() {
    private var binding: ActivityRechargeDetailsBinding? = null
    private val context: Context = this@RechargeDetailsActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityRechargeDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initView()
        btnListener()
        binding!!.toolbar.tvToolbarName.setText("Recharge")
    }

    private fun btnListener() {
        binding!!.toolbar.dashboardImage.setOnClickListener {
            startActivity(Intent(this@RechargeDetailsActivity, DashboardActivity::class.java))
        }
    }

    private fun initView() {

    }
}