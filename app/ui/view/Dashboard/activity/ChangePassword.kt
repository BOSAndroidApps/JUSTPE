package com.bos.payment.appName.ui.view.Dashboard.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.pinChange.ChangePasswordReq
import com.bos.payment.appName.data.model.pinChange.ChangePasswordRes
import com.bos.payment.appName.data.repository.ServiceChangeRepository
import com.bos.payment.appName.data.viewModelFactory.ServiceChangeViewModelFactory
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.viewmodel.ServiceChangeViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils.toast
import com.bos.payment.appName.databinding.ActivityChangePasswordBinding
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso

class ChangePassword : AppCompatActivity() {
    private lateinit var bin: ActivityChangePasswordBinding
    private var mStash: MStash? = null
    private lateinit var pd: AlertDialog
    private lateinit var viewModel: ServiceChangeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(bin.root)
        initView()
        btnListener()

    }

    private fun btnListener() {
        bin.proceedPaaswordBtn.setOnClickListener { validationChangePassword() }

        bin.changePasswordToolbar.dashboardImage.setOnClickListener {
            startActivity(Intent(this@ChangePassword, DashboardActivity::class.java))
        }
        bin.cancelBtn.setOnClickListener {
            startActivity(Intent(this, DashboardActivity::class.java))
        }
    }

    private fun validationChangePassword() {
        if (bin.oldPassword.text.isNullOrBlank()) {
            toast("Enter your old password")
        } else if (bin.newPassword.text.isNullOrBlank()) {
            toast("Enter new password")
        } else if (bin.confirmPassword.text.toString() != bin.newPassword.text.toString()) {
            toast("Enter Confirm password")
        } else {
            showChangePinDialog(this@ChangePassword)
        }
    }

    private fun changePasswordCalling() {
        this.runIfConnected {
            val changePasswordReq = ChangePasswordReq(
                GroupId = mStash!!.getStringValue(Constants.AgentType, ""),
                RegistrationID = mStash!!.getStringValue(Constants.RegistrationId, ""),
                AgentPassword = bin.newPassword.text.toString().trim(),
                CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
            )
            viewModel.changePassword(changePasswordReq).observe(this) { resource ->
                resource.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.let { users ->
                                users.body().let { it1 -> changePasswordRes(it1) }
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

    private fun changePasswordRes(response: ChangePasswordRes?) {
        if (!response!!.RegistrationId.isNullOrBlank()) {
            toast(response.message.toString())
            startActivity(Intent(this@ChangePassword, DashboardActivity::class.java))
        }else {
            toast(response.message.toString())
        }
    }

    private fun initView() {
        pd = PD(this)
        mStash = MStash.getInstance(this)
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
                .into(bin.changePasswordToolbar.dashboardImage)
        }catch (e: IllegalArgumentException){
            e.printStackTrace()
        }

    }
    fun showChangePinDialog(activity: Activity) {
        val builder = MaterialAlertDialogBuilder(activity)
        builder.setTitle("Need Permissions")
        builder.setMessage("Are you sure want to change password")
        builder.setPositiveButton("Yes") { dialog, which ->
//            dialog.cancel()
//            openSettings(activity)
            changePasswordCalling()
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
        builder.show()
    }
}