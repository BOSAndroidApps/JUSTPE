package com.bos.payment.appName.ui.view.Dashboard.tomobile

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.justpaymodel.RetailerContactListRequestModel
import com.bos.payment.appName.data.model.justpaymodel.SendMoneyToMobileReqModel
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceReq
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceRes
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.databinding.ActivitySendWalletAmountPageBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.adapter.ContactListAdapter
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.google.gson.Gson
import org.apache.poi.sl.draw.geom.Context

class SendWalletAmountPage : AppCompatActivity() {

    lateinit var binding: ActivitySendWalletAmountPageBinding
    private var mStash: MStash? = null
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    lateinit var pd : ProgressDialog
    var totalamoutForWithdraw: Double = 0.0
    var mainBalance: Double = 0.0

    companion object{
        var name : String ? = ""
        var agencyName : String ?= ""
        var mobileNumber : String ? = ""
        var userID : String ? = ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySendWalletAmountPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mStash = MStash.getInstance(this)
        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface)))[GetAllApiServiceViewModel::class.java]
        pd = ProgressDialog(this)


        getAllWalletBalance()
        setUIData()
        setClickListner()

    }


    fun setUIData(){
        binding.name.text= "$name ( $userID )"
        binding.contactno.text= mobileNumber
        binding.agencynumber.text= agencyName
        binding.firstCharacter.text = name!!.take(2).uppercase()
        binding.etAmount.requestFocus()
        binding.etAmount.postDelayed({
            binding.etAmount.requestFocus()
            val imm = getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.etAmount, InputMethodManager.SHOW_FORCED)
        }, 300)

    }


    fun setClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.tvBtnProceed.setOnClickListener{
            var amount = binding.etAmount.text.toString()
            var remarks = binding.remarks.text.toString()

            if(amount.isNullOrBlank()){
                Toast.makeText(this,"Please enter amount for send to retailer wallet",Toast.LENGTH_SHORT).show()
            }else {
                getAllWalletBalance()
            }

        }

        binding.main.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            binding.main.getWindowVisibleDisplayFrame(r)
            val screenHeight = binding.main.rootView.height
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.20) {
                // Keyboard is open
                if (binding.remarks.hasFocus()) {
                    binding.scrollview.post {
                        binding.scrollview.smoothScrollTo(0, binding.scrollview.bottom)
                    }
                }
            }
        }

    }


    private fun getAllWalletBalance() {
        runIfConnected {
            val walletBalanceReq = GetBalanceReq(
                parmUser = mStash!!.getStringValue(Constants.RegistrationId, ""),
                flag = "CreditBalance"
            )

            getAllApiServiceViewModel.getWalletBalance(walletBalanceReq)
                .observe(this) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                it.data?.let { users ->
                                    users.body()?.let { response ->
                                        getAllWalletBalanceRes(response)
                                    }
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


    @SuppressLint("SetTextI18n", "DefaultLocale")
    private fun getAllWalletBalanceRes(response: GetBalanceRes) {
        if (response.isSuccess == true) {
            mainBalance = (response.data[0].result!!.toDoubleOrNull() ?: 0.0)
            totalamoutForWithdraw = binding.etAmount.text.toString()  !!.toDoubleOrNull() ?: 0.0

            if(totalamoutForWithdraw<= mainBalance){
                hitApiForSendMoneyToAdmin(totalamoutForWithdraw)
            }
            else{
                pd.dismiss()
                Toast.makeText(this, "Wallet balance is low. VBal = $mainBalance,  totalAmt = $totalamoutForWithdraw", Toast.LENGTH_LONG).show()
            }

            binding.walletamt.text = "You can send money up to ₹$mainBalance only"

            Log.d("actualBalance", "main = $mainBalance")

        } else {
            toast(response.returnMessage.toString())
        }

    }



    fun hitApiForSendMoneyToAdmin(amount : Double){

        val txnId = "TXN" + (1000..9999).random()

        val getRetailerContactList = SendMoneyToMobileReqModel(
            actualTransactionAmount = amount,
            flag = "MINUS",
            amountType = "Payout",
            transferFromMsg = "₹ ${amount} transferred to Admin (ID: ${userID}). Txn ID: $txnId.",
            transIpAddress = "",
            remark = binding.remarks.text.toString(),
            transferTo = mStash!!.getStringValue(Constants.RegistrationId, ""),
            transferToMsg = "You have received ₹${amount}from Retailer. User ID: ${userID}.",
            transferAmt = amount,
            parmUserName =  mStash!!.getStringValue(Constants.RegistrationId, "")
        )

        Log.d("sendMoneyToAdminreq", Gson().toJson(getRetailerContactList))

        getAllApiServiceViewModel.sendMoneyToMobileReqModel(getRetailerContactList).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {

                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("sendMoneyToAdminresp", Gson().toJson(response))
                                if(response.isSuccess!!){
                                    hitApiForSendMoneyToRetailer(amount)
                                }
                                else{
                                    pd.dismiss()
                                }
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        pd.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        pd.dismiss()
                    }

                }

            }
        }


    }


    fun hitApiForSendMoneyToRetailer(amount : Double){

        val txnId = "TXN" + (1000..9999).random()

        val getRetailerContactList = SendMoneyToMobileReqModel(
            actualTransactionAmount = amount,
            flag = "PLUS",
            amountType = "Deposit",
            transferFromMsg = "₹ ${amount} transferred to Retailer (ID: ${userID}). Txn ID: $txnId.",
            transIpAddress = "",
            remark = binding.remarks.text.toString(),
            transferTo = userID,
            transferToMsg = "You have received ₹${amount}from Retailer. User ID: ${userID}.",
            transferAmt = amount,
            parmUserName = userID
        )

        Log.d("sendMoneyToRetailerreq", Gson().toJson(getRetailerContactList))

        getAllApiServiceViewModel.sendMoneyToMobileReqModel(getRetailerContactList).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {

                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("sendMoneyToRetailerresp", Gson().toJson(response))
                                pd.dismiss()
                                if(response.isSuccess!!){
                                    Toast.makeText(this,response.returnMessage,Toast.LENGTH_SHORT).show()
                                }
                                else{
                                    Toast.makeText(this,response.returnMessage,Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        pd.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        pd.dismiss()
                    }

                }

            }
        }


    }






}