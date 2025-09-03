package com.bos.payment.appName.ui.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.data.model.forgotPassWord.ForgotReq
import com.bos.payment.appName.data.model.forgotPassWord.ForgotRes
import com.bos.payment.appName.data.model.otp.OtpSubmitReq
import com.bos.payment.appName.data.model.otp.OtpSubmitRes
import com.bos.payment.appName.data.repository.ForgotPassWordRepository
import com.bos.payment.appName.data.repository.SubmitOTPRepository
import com.bos.payment.appName.data.viewModelFactory.ForgotPasswordViewModelFactory
import com.bos.payment.appName.data.viewModelFactory.SubmitOTPViewModelFactory
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.viewmodel.ForgotPasswordViewModel
import com.bos.payment.appName.ui.viewmodel.SubmitOTPViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils
import com.bos.payment.appName.utils.Utils.toast
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.ActivityForgotPasswordBinding
import com.bos.payment.appName.databinding.OtpScreenBinding
import com.bos.payment.appName.databinding.TransactionDetailsBinding
import com.bos.payment.appName.utils.Utils.runIfConnected

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var  bin: ActivityForgotPasswordBinding
    private var binding: OtpScreenBinding? = null
    private val context: Context = this@ForgotPasswordActivity
    private lateinit var viewModel: ForgotPasswordViewModel
    private lateinit var viewModel1: SubmitOTPViewModel
    private var mStash: MStash? = null
    private lateinit var pd: AlertDialog
    private var dialog: Dialog? = null
    private var submitBtn: Dialog? = null
    private lateinit var transactionDetailsBinding: TransactionDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        bin = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(bin.root)
        initView()
        btnListener()

    }

    @SuppressLint("SetTextI18n")
    private fun btnListener() {
        bin.updateBtn.setOnClickListener {
//            showDialog()
            validationForgotPassword()
        }
        bin.backBtn.setOnClickListener {
            onBackPressed()
        }
        bin.otpBtn.setOnClickListener { validationOTP() }

//        bin.pinBtn.setOnClickListener{
//            bin.pinview.visibility = View.VISIBLE
//            bin.mobileView.visibility = View.VISIBLE
//            bin.etLoginId.setHint("Enter your pin")
//            bin.etEmailId.setHint("Enter your mobile number")
//        }
//        bin.tvPassword.setOnClickListener{
//            bin.pinview.visibility = View.GONE
//            bin.mobileView.visibility = View.GONE
//            bin.etLoginId.setHint("Enter login Id")
//            bin.etEmailId.setHint("Enter your email Id")
//        }

    }

    private fun validationOTP() {
        if (bin.pinview.text!!.isEmpty()) {
            toast("Please enter your OTP")
        } else {
            submitOTP()
        }
    }

    private fun submitOTP() {
        viewModel1.submitOTP(
            OtpSubmitReq(
                GroupId = mStash!!.getStringValue(Constants.AgentType,""),
                RegistrationID = mStash!!.getStringValue(Constants.RegistrationId, ""),
                OTPNumber = bin.pinview.text.toString().trim(),
                CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
            )
        ).observe(this) { resource ->
            resource.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            pd.dismiss()
                            users.body()?.let { it1 -> OtpRes(it1) }
                        }
                    }

                    ApiStatus.ERROR -> {
                        pd.dismiss()
                        Toast.makeText(context, "Error occurred", Toast.LENGTH_SHORT).show()
                    }

                    ApiStatus.LOADING -> {
                        pd.show()
                    }
                }
            }
        }
    }

    private fun OtpRes(response: OtpSubmitRes) {
        if (!response.RegistrationId.isNullOrBlank()){
            toast(response.message.toString())

            startActivity(Intent(this@ForgotPasswordActivity, LoginActivity::class.java))
        }else{
            toast(response.message.toString())
        }
    }

    private fun initView() {
        binding = OtpScreenBinding.inflate(layoutInflater)
        dialog = Dialog(this@ForgotPasswordActivity)
        mStash = MStash.getInstance(context)
        pd = Utils.PD(this)
        bin.otpLayout.visibility = View.GONE
        bin.otpBtn.visibility = View.GONE

        viewModel = ViewModelProvider(
            this,
            ForgotPasswordViewModelFactory(ForgotPassWordRepository(RetrofitClient.apiAllInterface))
        )[ForgotPasswordViewModel::class.java]

        viewModel1 = ViewModelProvider(
            this,
            SubmitOTPViewModelFactory(SubmitOTPRepository(RetrofitClient.apiAllInterface))
        )[SubmitOTPViewModel::class.java]

//        bin.etLoginId.setText(mStash!!.getStringValue(Constants.RegistrationId, "").toString())
//        bin.etEmailId.setText(mStash!!.getStringValue(Constants.EmailID.toString(), "").toString())

    }

    private fun validationForgotPassword() {
        if (bin.etLoginId.text.isNullOrEmpty()) {
            toast("Please enter login Id")
        } else if (bin.etEmailId.text.isNullOrEmpty()) {
            toast("PLease enter email Id")
        } else {
            forgotPassword()
        }
    }

    private fun forgotPassword() {
        this.runIfConnected {
//        val registrationId = mStash?.getStringValue(Constants.RegistrationId, "")
            val registrationId = bin.etLoginId.text.toString().trim()
            val groupId = if (registrationId.contains("RTE")) "Retailer" else "Customer"

            Log.d("ViewModel", "ViewModel: $viewModel")

            viewModel.forgotPassword(
                ForgotReq(
                    GroupId = groupId,
                    RegistrationID = mStash!!.getStringValue(Constants.RegistrationId, ""),
                    EmailID = bin.etEmailId.text.toString().trim(),
                    CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
                )
            ).observe(this) { resource ->
                resource.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                pd.dismiss()
                                users.body()?.let { it1 -> forgotRes(it1) }
                            }
                        }

                        ApiStatus.ERROR -> {
                            pd.dismiss()
                            Toast.makeText(
                                context,
                                "Error occurred ${it.message.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        ApiStatus.LOADING -> {
                            pd.show()
                        }
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun forgotRes(response: ForgotRes) {
        if (!response.RegistrationId.isNullOrEmpty()) {
            toast(response.message.toString())
//            showDialog()
            bin.otpLayout.visibility = View.VISIBLE
            bin.otpBtn.visibility = View.VISIBLE
            bin.updateBtn.visibility = View.GONE
        } else {
            toast(response.message.toString())
        }
    }

    private fun showDialog() {

        val dialog = Dialog(this@ForgotPasswordActivity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        transactionDetailsBinding = TransactionDetailsBinding.inflate(layoutInflater)
        dialog.setContentView(transactionDetailsBinding.getRoot())
        val yesBtn = dialog.findViewById<Button>(R.id.yesBtn)
        val emplayout = dialog.findViewById<LinearLayoutCompat>(R.id.emp_layout)

        dialog.show()
        val window: Window? = dialog.window
        window!!.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

    }

}