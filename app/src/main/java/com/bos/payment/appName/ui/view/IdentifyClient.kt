package com.bos.payment.appName.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bos.bos.app.ui.view.DeviceInfoHelper
import com.bos.payment.appName.data.model.merchant.clientDetails.GetClientRegistrationReq
import com.bos.payment.appName.data.model.merchant.clientDetails.GetClientRegistrationRes
import com.bos.payment.appName.data.repository.MoneyTransferRepository
import com.bos.payment.appName.data.viewModelFactory.MoneyTransferViewModelFactory
import com.bos.payment.appName.databinding.ActivityIdentifyClientBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.viewmodel.MoneyTransferViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.google.gson.Gson

class IdentifyClient : AppCompatActivity() {
    private lateinit var bin: ActivityIdentifyClientBinding
    private lateinit var viewModel: MoneyTransferViewModel
    private lateinit var pd: AlertDialog
    private var mStash: MStash? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityIdentifyClientBinding.inflate(layoutInflater)
        setContentView(bin.root)

        intiView()
        showBiometricDialog()
        showConsentDialog()
        btnListener()

    }

    private fun showBiometricDialog() {
        AlertDialog.Builder(this)
            .setTitle("Need your permission")
            .setMessage("LogIn using your screen lock biometric credential")
            .setPositiveButton("Allow") { dialog, which ->
                mStash!!.setBooleanValue(Constants.isUpdate.toString(), true)
            }
            .setNegativeButton("Deny") { dialog, which ->
                toast("You denied the biometric lock")
                mStash!!.setBooleanValue(Constants.isUpdate.toString(), false)

            }
            .setCancelable(false)
            .show()
    }

    private fun showConsentDialog() {
        AlertDialog.Builder(this)
            .setTitle("Need your permission")
            .setMessage("We collect your device information (such as your IP address) to enhance the app's functionality. Your data will be securely stored and not shared with third parties.")
            .setPositiveButton("Allow") { dialog, which ->
                // User has given consent, proceed with data collection
                val deviceInfoHelper = DeviceInfoHelper(this)

                // Get the IP Address
                val ipAddress = deviceInfoHelper.getIpAddress()
                mStash!!.setStringValue(Constants.deviceIPAddress, ipAddress)
                Log.d("MainActivity", "IP Address: "+ mStash!!.getStringValue(Constants.deviceIPAddress, ""))
            }
            .setNegativeButton("Deny") { dialog, which ->
                startActivity(Intent(this, SplashActivity::class.java))
//                finish()
                // User denied consent, handle this appropriately
                toast("You denied the consent")
            }
            .setCancelable(false)
            .show()
    }

    private fun btnListener() {
        bin.verifyButton.setOnClickListener {
            if (bin.companyCode.text.isNullOrEmpty()) {
                toast("Please enter your company code")
            } else {
                getAllMerchantList(bin.companyCode.text.toString().trim().uppercase())
            }
        }
    }

    private fun getAllMerchantList(code: String?) {
        this.runIfConnected {
            val getClientRegistrationReq =
                GetClientRegistrationReq(bin.companyCode.text.toString().trim().uppercase())
            Log.d("getALLMerchantList", Gson().toJson(getClientRegistrationReq))
            viewModel.getClientDetails(getClientRegistrationReq)
                .observe(this@IdentifyClient) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                pd.dismiss()
                                it.data?.let { users ->
                                    getAllMerchantListRes(users.body()!!)
//                                users.body()?.let { it1 -> getAllMerchantListRes(it1) }
                                }
                            }

                            ApiStatus.ERROR -> {
                                toast("User not found")
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

    private fun getAllMerchantListRes(response: List<GetClientRegistrationRes>) {
        if (response[0].Status == true) {
            mStash!!.setStringValue(Constants.CompanyName, response[0].CompanyName.toString())
            mStash!!.setStringValue(Constants.MobileNumber, response[0].MobileNo.toString())
            mStash!!.setStringValue(Constants.EmailID, response[0].EmailID.toString())
            mStash!!.setStringValue(Constants.CompanyCode, response[0].CompanyCode.toString().uppercase())
            mStash!!.setStringValue(Constants.CompanyLogo, response[0].Companylogo.toString())
            Log.d("getAllMerchantListRes", "${mStash!!.getStringValue(Constants.CompanyLogo, "")}")
//            if (mStash!!.getBoolanValue(Constants.IS_FIRST_LAUNCH.toString(), true)){
//                startActivity(Intent(this, FingerprintActivity::class.java))
//            }
//            toast(mStash!!.getStringValue(Constants.CompanyLogo, ""))

            mStash!!.setBooleanValue(Constants.IS_FIRST_LAUNCH.toString(), false)
            startActivity(Intent(this, LoginActivity::class.java).putExtra("UpdateProfile", true))
            finish()
//            toast(response[0].message.toString())
        } else {
            Toast.makeText(this, response[0].message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun intiView() {
        pd = PD(this)
        mStash = MStash.getInstance(this)
        viewModel = ViewModelProvider(
            this, MoneyTransferViewModelFactory(
                MoneyTransferRepository(RetrofitClient.apiAllInterface)
            )
        )[MoneyTransferViewModel::class.java]
        mStash!!.setBooleanValue(Constants.isUpdate.toString(), true)
    }

}