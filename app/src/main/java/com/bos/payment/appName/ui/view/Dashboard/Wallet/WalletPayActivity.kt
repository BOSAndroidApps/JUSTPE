package com.bos.payment.appName.ui.view.Dashboard.Wallet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager.widget.ViewPager
import com.bos.payment.appName.R
import com.bos.payment.appName.adapter.ViewpagerAdapter
import com.bos.payment.appName.databinding.ActivityWalletPayBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.squareup.picasso.Picasso

class WalletPayActivity : AppCompatActivity() {
    private var binding: ActivityWalletPayBinding? = null
    private val context: Context = this@WalletPayActivity
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private var mStash: MStash? = null

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityWalletPayBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initView()

        binding!!.toolbar.tvToolbarName.text = "Wallet Pay"
        binding!!.tablayout1.addTab(binding!!.tablayout1.newTab().setText("Transfer Amount From"))
        binding!!.tablayout1.addTab(binding!!.tablayout1.newTab().setText("Account Transfer To"))
        binding!!.viewpager1.adapter = ViewpagerAdapter(supportFragmentManager,context)
        binding!!.viewpager1.addOnPageChangeListener(TabLayoutOnPageChangeListener(binding!!.tablayout1))
        binding!!.tablayout1.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding!!.viewpager1.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        binding!!.toolbar.dashboardImage.setOnClickListener {
            startActivity(Intent(this@WalletPayActivity, DashboardActivity::class.java))
            finish()
        }
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
}