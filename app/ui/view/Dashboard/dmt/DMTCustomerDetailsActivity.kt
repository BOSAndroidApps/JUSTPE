package com.bos.payment.appName.ui.view.Dashboard.dmt

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.dmt.queryRemitters.Data
import com.bos.payment.appName.databinding.ActivityDmtcustomerDetailsBinding
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.squareup.picasso.Picasso

class DMTCustomerDetailsActivity : AppCompatActivity() {
    private var binding: ActivityDmtcustomerDetailsBinding? = null
    private val context: Context = this@DMTCustomerDetailsActivity
    private lateinit var dmtModel: Data
    private var mStash: MStash? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityDmtcustomerDetailsBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        initView()

//        binding!!.toolbar.tvToolbarName.text = "Customer Details"
        binding!!.toolbar.ivBack.setOnClickListener { onBackPressed() }
        dmtModel = intent.getSerializableExtra("AttendanceViewModel") as Data
//        if (dmtModel.fname == ""){
//            binding!!.tvParveen.setText("N/A")
//        }else{
//            binding!!.tvParveen.setText(dmtModel.fname)
//        }
        if (dmtModel.mobile == ""){
            binding!!.tvMobileNumber.setText("N/A")
        }else{
            binding!!.tvMobileNumber.setText(dmtModel.mobile)
        }
//        if (dmtModel.message == ""){
//            binding!!.tvMessage.setText("N/A")
//        }else{
//            binding!!.tvMessage.setText(dmtModel.message)
//        }
//        if (dmtModel.bank1Limit.toString() == ""){
//            binding!!.tvLimit.setText("N/A")
//        }else{
//            binding!!.tvLimit.setText(dmtModel.bank1Limit.toString())
//        }
    }

    private fun initView() {
        mStash = MStash.getInstance(this)
        val imageUrl = mStash!!.getStringValue(Constants.CompanyLogo, "")
        Picasso.get()
            .load(imageUrl)
//            .placeholder(R.drawable.placeholder)  // Optional: placeholder while loading
            .error(R.drawable.ic_error)        // Optional: error image if load fails
            .into(binding!!.toolbar.tvToolbarName)
    }
}