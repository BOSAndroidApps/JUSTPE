package com.bos.payment.appName.ui.view.Dashboard.Wallet

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityAddAmountBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.ui.view.PayUMoneyActivity
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.squareup.picasso.Picasso

class AddAmountActivity : AppCompatActivity() {
    private var binding: ActivityAddAmountBinding? = null
    private val context: Context = this@AddAmountActivity
    private var mStash: MStash? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityAddAmountBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initView()
        binding!!.toolbar.tvToolbarName.setText("Add Amount")
        binding!!.tvBtnProceed.setOnClickListener {
            if (TextUtils.isEmpty(binding!!.etAmount.text.toString())){
                Toast.makeText(context,"Enter Amount",Toast.LENGTH_SHORT).show()
            }else {
                startActivity(Intent(context, com.bos.payment.appName.ui.view.PayUMoneyActivity::class.java).putExtra("Amount",
                    binding!!.etAmount.text.toString()))
                finish()
            }
        }
        binding!!.tvBtnCancel.setOnClickListener {
            binding!!.etAmount.setText("")
        }
        binding!!.toolbar.dashboardImage.setOnClickListener {
            startActivity(Intent(this@AddAmountActivity, DashboardActivity::class.java))
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