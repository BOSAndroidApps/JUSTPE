package com.bos.payment.appName.ui.view.Dashboard.Wallet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatDelegate
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityLoadWalletBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.squareup.picasso.Picasso

class LoadWalletActivity : AppCompatActivity() {
    private var binding: ActivityLoadWalletBinding? = null
    private val context: Context = this@LoadWalletActivity
    private var mStash: MStash? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityLoadWalletBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initView()
        btnListener()
        binding!!.toolbar.tvToolbarName.setText("Load Wallet")
        var str:String = "<font color=FF000000>Drag & drop or </font><font color=#0371B0><u>browser</u></font>"
        binding!!.tvDragnDrop.setText(Html.fromHtml(str))

    }

    private fun initView() {
        mStash = MStash.getInstance(this)
        val imageUrl = mStash!!.getStringValue(Constants.CompanyLogo, "")
        Picasso.get()
            .load(imageUrl)
//            .placeholder(R.drawable.placeholder)  // Optional: placeholder while loading
            .error(R.drawable.ic_error)        // Optional: error image if load fails
            .into(binding!!.toolbar.dashboardImage)

    }

    private fun btnListener() {
        binding!!.toolbar.dashboardImage.setOnClickListener {
            startActivity(Intent(this@LoadWalletActivity, DashboardActivity::class.java))
            finish()
        }
    }
}