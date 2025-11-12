package com.bos.payment.appName.ui.view.makepayment.fragment

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.constant.ConstantClass.saveImageToCache
import com.bos.payment.appName.data.model.makepaymentnew.DataItem
import com.bos.payment.appName.data.model.makepaymentnew.RaiseMakePaymentReq
import com.bos.payment.appName.data.repository.GetAllAPIServiceRepository
import com.bos.payment.appName.data.viewModelFactory.GetAllApiServiceViewModelFactory
import com.bos.payment.appName.databinding.FragmentMakePaymentReportsBinding
import com.bos.payment.appName.databinding.FragmentRaiseMakePaymentBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.adapter.MakePaymentReportsAdapter
import com.bos.payment.appName.ui.viewmodel.GetAllApiServiceViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


class MakePaymentReports : Fragment() {
    lateinit var binding: FragmentMakePaymentReportsBinding
    private lateinit var getAllApiServiceViewModel: GetAllApiServiceViewModel
    lateinit var pd: ProgressDialog
    private var mStash: MStash? = null
    private var getMakePaymentReportsList: MutableList<DataItem?>? = arrayListOf()
    var reportModeList : MutableList<String?> = arrayListOf()
    private val myCalender1 = Calendar.getInstance()
    var ToDate: String =""

    companion object {

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding =  FragmentMakePaymentReportsBinding.inflate(layoutInflater, container, false)

        getAllApiServiceViewModel = ViewModelProvider(this, GetAllApiServiceViewModelFactory(
           GetAllAPIServiceRepository(RetrofitClient.apiAllInterface)
        )
        )[GetAllApiServiceViewModel::class.java]

        mStash = MStash.getInstance(requireContext())
        pd = ProgressDialog(requireContext())
        HitApiForTransferAmountToAdminAccount()
        setReportModeInSpinner()
        setOnClickListner()
        return binding.root
    }

    private fun setReportModeInSpinner(){
        reportModeList.clear()
        reportModeList.add("All")
        reportModeList.add("Approved")
        reportModeList.add("Pending")
        reportModeList.add("Rejected")
        var adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, reportModeList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.reportmode.adapter = adapter
    }

    private fun setOnClickListner(){

        binding.toDate.setOnClickListener {
            DatePickerDialog(
                requireContext(), { _, year, monthOfYear, dayOfMonth ->
                    val actualMonth = monthOfYear + 1 // ✅ Fix month (0-based → 1-based)
                    myCalender1.set(year, actualMonth - 1, dayOfMonth)

                    // Show date correctly (e.g., 10/11/2025)
                    binding.toDate.text = "$dayOfMonth/$actualMonth/$year"

                    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                    calendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0) // keep original monthOfYear here
                    calendar.set(Calendar.MILLISECOND, 860)

                    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    sdf.timeZone = TimeZone.getTimeZone("UTC")

                    val isoString = sdf.format(calendar.time)
                    ToDate = isoString

                    val selectedStatus = binding.reportmode.selectedItem.toString().trim()

                    if (selectedStatus.isNotEmpty() && !selectedStatus.equals("All", ignoreCase = true) && ToDate.isNotEmpty()) {
                        val filterList = getMakePaymentReportsList!!.filter { item ->
                            item!!.apporvedStatus.equals(selectedStatus, ignoreCase = true) &&
                                    item.paymentDate!!.startsWith(ToDate.substring(0, 10)) // compare only date part
                        }.toMutableList()

                        setAdapter(filterList)
                    } else {
                        val filterList = getMakePaymentReportsList!!.filter { item ->
                            item!!.paymentDate!!.startsWith(ToDate.substring(0, 10))
                        }.toMutableList()

                        setAdapter(filterList)
                    }

                    Log.d("ToDate", ToDate)
                },
                myCalender1.get(Calendar.YEAR),
                myCalender1.get(Calendar.MONTH),
                myCalender1.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.reportmode.onItemSelectedListener= object : OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var selectedItem = binding.reportmode.selectedItem.toString().trim()

                if (selectedItem.isNotEmpty() && !selectedItem.equals("All",ignoreCase = true) || ToDate.isNotEmpty()) {

                    val selectedStatus = binding.reportmode.selectedItem.toString().trim()

                    // Filter list based on selected status and date
                    val filterList = getMakePaymentReportsList!!.filter { item ->
                        item!!.apporvedStatus.equals(selectedStatus, ignoreCase = true) &&
                                item.paymentDate!!.startsWith(ToDate) // works if paymentDate is like "2025-11-10T00:00:00"
                    }.toMutableList()

                    setAdapter(filterList)
                }
                else {
                    // Filter list based on selected status and date
                    val filterList = getMakePaymentReportsList!!.filter { item ->
                        item!!.paymentDate!!.startsWith(ToDate) // works if paymentDate is like "2025-11-10T00:00:00"
                    }.toMutableList()

                    setAdapter(filterList)


                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }



    }

    private fun HitApiForTransferAmountToAdminAccount(){
        var userCode = mStash!!.getStringValue(Constants.RegistrationId, "").toString()
        var adminCode = mStash!!.getStringValue(Constants.AdminCode, "").toString()

            val request = RaiseMakePaymentReq(
                Mode = "GET",
                RID = "",
                RefrenceID = "",
                PaymentMode = "",
                PaymentDate = "",
                DepositBankName = "",
                BranchCode_ChecqueNo = "",
                Remarks = "",
                TransactionID ="",
                DocumentPath = "",
                RecordDateTime = "",
                UpdatedBy = userCode,
                UpdatedOn = "",
                ApprovedBy = "",
                ApprovedDateTime = "",
                ApporvedStatus = "Pending",
                RegistrationId = userCode,
                ApporveRemakrs = "",
                Amount = "",
                CompanyCode = "",
                BeneId = "",
                AccountHolder = "",
                PaymentType = "",
                Flag = "",
                AdminCode = adminCode,
                imagefile1 = null
            )

            Log.d("GetMakePaymentReq", Gson().toJson(request))

            getAllApiServiceViewModel.RaisMakePaymentReq(request).observe(requireActivity()) { resource ->
                resource?.let {
                    when (it.apiStatus) {
                        ApiStatus.SUCCESS -> {
                            it.data?.let { users ->
                                users.body()?.let { response ->
                                    Log.d("GetMakePaymentResp", Gson().toJson(response))
                                    pd.dismiss()
                                    if (response.isSuccess!!) {
                                        getMakePaymentReportsList = response.data
                                        if(getMakePaymentReportsList!!.size>0){
                                            setAdapter(getMakePaymentReportsList)
                                        }else{
                                            binding.notfoundlayout.visibility=View.VISIBLE
                                            binding.makepaymentreports.visibility=View.GONE
                                        }

                                    } else {
                                        binding.notfoundlayout.visibility=View.VISIBLE
                                        binding.makepaymentreports.visibility=View.GONE
                                        Toast.makeText(requireContext(), response.returnMessage, Toast.LENGTH_SHORT).show()
                                    }
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

    private fun setAdapter(getMakePaymentReportsList:MutableList<DataItem?>?){
        if(getMakePaymentReportsList!!.size>0){
            binding.notfoundlayout.visibility=View.GONE
            binding.makepaymentreports.visibility=View.VISIBLE
            var adapter =   MakePaymentReportsAdapter(requireContext(),getMakePaymentReportsList)
            binding.makepaymentreports.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        else{
            binding.notfoundlayout.visibility=View.VISIBLE
            binding.makepaymentreports.visibility=View.GONE
        }
    }


}