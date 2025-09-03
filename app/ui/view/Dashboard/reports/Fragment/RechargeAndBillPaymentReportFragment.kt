package com.bos.payment.appName.ui.view.Dashboard.reports.Fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bos.payment.appName.R
import com.bos.payment.appName.adapter.reports.RechargeBillReportsAdapter
import com.bos.payment.appName.data.model.reports.rechargeAndBill.RechargeWiseReportReq
import com.bos.payment.appName.data.model.reports.rechargeAndBill.RechargeWiseReportRes
import com.bos.payment.appName.data.repository.ReportsRepository
import com.bos.payment.appName.data.viewModelFactory.ReportsViewModelFactory
import com.bos.payment.appName.databinding.FragmentRechargeAndBillPaymentReportBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.Dashboard.activity.DashboardActivity
import com.bos.payment.appName.ui.viewmodel.ReportsViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.bos.payment.appName.utils.Utils.toast
import com.google.gson.Gson
import java.io.IOException
import java.util.Calendar

class RechargeAndBillPaymentReportFragment : Fragment() {
    private lateinit var bin: FragmentRechargeAndBillPaymentReportBinding
    private val myCalender = Calendar.getInstance()
    private val myCalender1 = Calendar.getInstance()
    private var rechargeBillText: String? = ""
    private lateinit var viewModel: ReportsViewModel
    private var mStash: MStash? = null
    private lateinit var pd: AlertDialog
    var rechargeWiseReportList = ArrayList<RechargeWiseReportRes>()
    private lateinit var rechargeBillReportsAdapter: RechargeBillReportsAdapter

    private val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalender.set(Calendar.YEAR, year)
        myCalender.set(Calendar.MONTH, monthOfYear)
        myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        bin.fromDate1.let { Utils.updateLabel(it, myCalender, "Update From Date") }
    }

    private val date1 = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalender1.set(Calendar.YEAR, year)
        myCalender1.set(Calendar.MONTH, monthOfYear)
        myCalender1.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        bin.toDate1.let { Utils.updateLabel(it, myCalender1, "Update To Date") }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        bin = FragmentRechargeAndBillPaymentReportBinding.inflate(inflater, container, false)
        initView()
        setDropDown()
        btnListener()
        return bin.root
    }

    private fun btnListener() {
        bin.checkBoxDateRechage.setOnCheckedChangeListener { _, isChecked ->
            bin.fromToDateLayout1.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        bin.fromDate1.setOnClickListener {
            Utils.hideKeyboard(requireActivity())
            DatePickerDialog(
                requireActivity(),
                date,
                myCalender[Calendar.YEAR],
                myCalender[Calendar.MONTH],
                myCalender[Calendar.DAY_OF_MONTH]
            ).show()
        }
        bin.toDate1.setOnClickListener {
            Utils.hideKeyboard(requireActivity())
            DatePickerDialog(
                requireActivity(),
                date1,
                myCalender1[Calendar.YEAR],
                myCalender1[Calendar.MONTH],
                myCalender1[Calendar.DAY_OF_MONTH]
            ).show()
        }
        bin.proceedBtn.setOnClickListener {
            validationBill()
        }
        bin.cancelBtn.setOnClickListener {
            startActivity(Intent(requireActivity(), DashboardActivity::class.java))
        }

    }

    private fun validationBill() {
        if (rechargeBillText.isNullOrBlank()) {
            Toast.makeText(requireActivity(), "Select your Type", Toast.LENGTH_SHORT).show()
        } else {
            rechargeBillCalling()
        }
    }

    private fun rechargeBillCalling() {
        requireContext().runIfConnected {
            val rechargeWiseReportReq = RechargeWiseReportReq(
                RegistrationID = mStash!!.getStringValue(Constants.RegistrationId, ""),
                ReportCategory = rechargeBillText,
                CompanyCode = mStash!!.getStringValue(Constants.CompanyCode, "")
            )
            Log.d("rechargeWiseReportReq", Gson().toJson(rechargeWiseReportReq))
            viewModel.getAllRechargeAndBill(rechargeWiseReportReq)
                .observe(requireActivity()) { resource ->
                    resource.let {
                        when (it.apiStatus) {
                            ApiStatus.SUCCESS -> {
                                it.data?.let { users ->
                                    pd.dismiss()
                                    users.body()?.let { it1 -> rechargeBillRes(it1) }
                                }
                            }

                            ApiStatus.ERROR -> {
                                pd.dismiss()
                                toast(it.message.toString())

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
    private fun rechargeBillRes(response: List<RechargeWiseReportRes>) {
        try {
            if (response[0].Status == true) {
                bin.listLayout.visibility = View.VISIBLE
                rechargeWiseReportList.clear()
                rechargeWiseReportList.addAll(response)
                rechargeBillReportsAdapter.notifyDataSetChanged()
                Toast.makeText(requireActivity(), response[0].message, Toast.LENGTH_SHORT).show()
            }else {
                bin.listLayout.visibility = View.GONE
                toast(response[0].message.toString())
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(requireActivity(), e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        mStash = MStash.getInstance(requireActivity())!!
        pd = Utils.PD(requireActivity())
        bin.fromToDateLayout1.visibility = View.GONE
        viewModel = ViewModelProvider(
            requireActivity(),
            ReportsViewModelFactory(ReportsRepository(RetrofitClient.apiAllInterface))
        )[ReportsViewModel::class.java]

        bin.recycle1.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        rechargeBillReportsAdapter =
            RechargeBillReportsAdapter(requireActivity(), rechargeWiseReportList)
        bin.recycle1.adapter = rechargeBillReportsAdapter
    }

    private fun setDropDown() {
        val arrayListSpinner = resources.getStringArray(R.array.recharge_array)
        val adapters = ArrayAdapter(
            requireActivity(), R.layout.spinner_right_aligned, arrayListSpinner
        )
        adapters.setDropDownViewResource(R.layout.spinner_right_aligned)
        bin.allType.adapter = adapters
        bin.allType.setSelection(0)
        bin.allType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                rechargeBillText = if (position > 0) {
                    parent!!.getItemAtPosition(position).toString()
                } else {
                    null
                }
                Log.e("TAG", "onItemSelected: " + rechargeBillText)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

}