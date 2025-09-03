package com.bos.payment.appName.ui.view.Dashboard.activity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bos.payment.appName.adapter.TransactionHistoryAdapter
import com.bos.payment.appName.data.model.recharge.rechargeHistory.RechargeHistoryReq
import com.bos.payment.appName.data.model.recharge.rechargeHistory.RechargeHistoryRes
import com.bos.payment.appName.data.model.serviceWiseTrans.TransactionReportReq
import com.bos.payment.appName.data.model.serviceWiseTrans.TransactionReportRes
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.repository.MoneyTransferRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.data.viewModelFactory.MoneyTransferViewModelFactory
import com.bos.payment.appName.databinding.ActivityRechargeHistoryBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.ui.viewmodel.MoneyTransferViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.google.gson.Gson
import java.io.IOException

class RechargeHistory : AppCompatActivity() {
    private lateinit var bin: ActivityRechargeHistoryBinding
    private var mStash: MStash? = null
    private lateinit var viewModel: MoneyTransferViewModel
    private lateinit var pd: AlertDialog
    private lateinit var transactionHistoryAdapter: TransactionHistoryAdapter
    private var rechargeType: String = ""
    private lateinit var rechargeList: ArrayList<com.bos.payment.appName.data.model.recharge.rechargeHistory.Data>
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityRechargeHistoryBinding.inflate(layoutInflater)
        setContentView(bin.root)
        initView()
        getTransactionHistoryList()
//        getAllRechargeHistory()
        btnListener()
    }

    private fun getAllRechargeHistory() {
        val currentDate = Utils.getCurrentDate()
        val transactionReportReq = TransactionReportReq(
            parmFromDate = "2020-12-12",
            parmToDate = currentDate,
            parmFlag = "Payout Report",
            parmFla2 = "All",
            parmUser = mStash!!.getStringValue(Constants.RegistrationId, ""),
            paramMerchantCode = mStash!!.getStringValue(Constants.MerchantId, "")
        )
        Log.d("transactionReportReq", Gson().toJson(transactionReportReq))

        getAllApiServiceViewModel.getAllTransactionReport(transactionReportReq)
            .observe(this) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            pd.dismiss()
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    transactionAPICallingRes(response)
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

    @SuppressLint("NotifyDataSetChanged")
    private fun transactionAPICallingRes(response: TransactionReportRes) {
        try {
            if (response.isSuccess == true) {
                response.data.let { listData ->
                    rechargeList.clear()
//                    rechargeList.addAll(listData)
                    transactionHistoryAdapter.notifyDataSetChanged()
                    Log.d("RechargeHistory", "Data added to the list and adapter notified.")
                }
            } else {
                toast(response.returnMessage.toString())
            }
        } catch (e: IOException) {
            e.printStackTrace()
            toast(e.message.toString())
        }
    }


    private fun btnListener() {
        bin.ivBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun initView() {
        pd = PD(this)
        rechargeList = ArrayList()
        mStash = MStash.getInstance(this)

        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(GetAllAPIServiceRepository(RetrofitClient.apiAllInterface)))[GetAllApiServiceViewModel::class.java]

        viewModel = ViewModelProvider(this, MoneyTransferViewModelFactory(MoneyTransferRepository(RetrofitClient.apiAllInterface)))[MoneyTransferViewModel::class.java]

        bin.rvTransactionHistoryRec.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        transactionHistoryAdapter = TransactionHistoryAdapter(this, rechargeList)

        bin.rvTransactionHistoryRec.adapter = transactionHistoryAdapter

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun getTransactionHistoryList() {
        this.runIfConnected {
            val rechargeHistoryReq = RechargeHistoryReq(registrationID = mStash!!.getStringValue(Constants.RegistrationId, ""), reportCategory = "Mobile")
            Log.d("rechargeHistoryReq", "mobile" + Gson().toJson(rechargeHistoryReq))
            viewModel.transactionHistoryList(rechargeHistoryReq)
                .observe(this) { resource ->
                    resource?.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                pd.dismiss()
                                it.data?.body()?.let { response ->
                                    getAllRechargeHistoryList(response)
                                }
                            }

                            ApiStatus.ERROR -> {
                                pd.dismiss()
                                toast("No data Found")
                            }

                            ApiStatus.LOADING -> {
                                pd.show()
                            }
                        }
                    }
                }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getAllRechargeHistoryList(response: RechargeHistoryRes?) {
        try {
//            Log.d("RechargeHistoryResponse", "Response received: ${Gson().toJson(response)}")
            if (response!!.isSuccess == true) {
                response.data.let { listData ->
                    rechargeList.clear()
                    rechargeList.addAll(listData)
                    transactionHistoryAdapter.notifyDataSetChanged()
                    Log.d("RechargeHistory", "Data added to the list and adapter notified.")
                }
            } else {
                toast(response.returnMessage.toString())
            }
        } catch (e: IOException) {
            e.printStackTrace()
            toast(e.message.toString())
        }
    }
}