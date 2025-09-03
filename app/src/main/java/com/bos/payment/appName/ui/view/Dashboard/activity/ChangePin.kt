package com.bos.payment.appName.ui.view.Dashboard.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.pinChange.PinChangeReq
import com.bos.payment.appName.data.model.pinChange.PinChangeRes
import com.bos.payment.appName.data.repository.ServiceChangeRepository
import com.bos.payment.appName.data.viewModelFactory.ServiceChangeViewModelFactory
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.viewmodel.ServiceChangeViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils.toast
import com.bos.payment.appName.utils.encryption.EncryptionUtils
import com.bos.payment.appName.databinding.ActivityChangePinBinding
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

class ChangePin : AppCompatActivity() {
    private lateinit var bin: ActivityChangePinBinding
    private lateinit var viewModel: ServiceChangeViewModel
    private var mStash: MStash? = null
    private lateinit var pd: AlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityChangePinBinding.inflate(layoutInflater)
        setContentView(bin.root)
        initView()
        btnListener()

    }

    private fun btnListener() {
        bin.tvBtnProceed.setOnClickListener {
            pinValidate() }
        bin.changePinToolbar.dashboardImage.setOnClickListener {
            startActivity(Intent(this@ChangePin, DashboardActivity::class.java))
        }
    }

    private fun pinValidate() {
        if (bin.newPinNo.text.isNullOrBlank()) {
            toast("Please enter new pin number")
        } else if (bin.confirmNewPin.text.toString() != bin.newPinNo.text.toString()) {
            toast("Enter confirm pin number")
        } else {
            showChangePinDialog(this)

        }
    }

    private fun changePinApiCalling() {
        this.runIfConnected {
            val secretKey = EncryptionUtils.generateKey()
            val encryptedPin =
                EncryptionUtils.encryptData(bin.newPinNo.text.toString().trim(), secretKey)

            val keyString = EncryptionUtils.keyToString(secretKey)
            mStash!!.setStringValue(Constants.EncryptionKey, keyString)

            val pinChangeReq =
                PinChangeReq(
                    GroupId = mStash!!.getStringValue(Constants.AgentType, ""),
                    RegistrationID = mStash!!.getStringValue(Constants.RegistrationId, ""),
                    TransactionPin = encryptedPin,
                    CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
                )
            viewModel.changePin(
                pinChangeReq
            ).observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.let { users ->
                                users.body()?.let { it1 -> changePinRes(it1) }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }
                    }
                }

            }
        }
    }

    private fun changePinRes(response: PinChangeRes?) {
        if (!response!!.RegistrationId.isNullOrBlank()) {
            toast(response.message.toString())
            startActivity(Intent(this@ChangePin, DashboardActivity::class.java))
        } else {
            toast(response.message.toString())
        }
    }

    private fun initView() {
        pd = PD(this)
        mStash = MStash.getInstance(this@ChangePin)
        viewModel = ViewModelProvider(
            this,
            ServiceChangeViewModelFactory(ServiceChangeRepository(RetrofitClient.apiAllInterface))
        )[ServiceChangeViewModel::class.java]

        try {

            val imageUrl = mStash!!.getStringValue(Constants.CompanyLogo, "")
            Picasso.get()
                .load(imageUrl)
//            .placeholder(R.drawable.placeholder)  // Optional: placeholder while loading
                .error(R.drawable.ic_error)        // Optional: error image if load fails
                .into(bin.changePinToolbar.dashboardImage)
        }catch (e: IllegalArgumentException){
            e.printStackTrace()
        }
    }
    fun showChangePinDialog(activity: Activity) {
        val builder = MaterialAlertDialogBuilder(activity)
        builder.setTitle("Need Permissions")
        builder.setMessage("Are you sure want to change pin")
        builder.setPositiveButton("Yes") { dialog, which ->
//            dialog.cancel()
//            openSettings(activity)
            changePinApiCalling()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }
}