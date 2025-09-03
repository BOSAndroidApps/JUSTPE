package com.bos.payment.appName.ui.view.travel.busactivity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bos.payment.appName.data.model.travel.bus.history.BusAvailableBookings
import com.bos.payment.appName.data.model.travel.bus.history.BusHistoryReq
import com.bos.payment.appName.data.model.travel.bus.history.BusHistoryRes
import com.bos.payment.appName.data.repository.TravelRepository
import com.bos.payment.appName.data.viewModelFactory.TravelViewModelFactory
import com.bos.payment.appName.databinding.ActivityBusHistoryBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.travel.adapter.BusTicketBookingHistoryAdapter
import com.bos.payment.appName.ui.viewmodel.TravelViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.toast
import java.util.Calendar

class BusHistory : AppCompatActivity() {
    private lateinit var bin: ActivityBusHistoryBinding
    private var mStash: MStash? = null
    private val myCalender = Calendar.getInstance()
    private val myCalender1 = Calendar.getInstance()
    private lateinit var pd: AlertDialog
    private var selectedYear: String? = null
    private var selectedMonth: String? = null
    private lateinit var viewModel: TravelViewModel
    private val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    private val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    private lateinit var busTicketBookingHistoryAdapter: BusTicketBookingHistoryAdapter
    private lateinit var bookingHistoryList: ArrayList<BusAvailableBookings>

    private var yearsList = ArrayList<String>()
    private var monthsList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityBusHistoryBinding.inflate(layoutInflater)
        setContentView(bin.root)

        initView()
        setAdapter()
        btnListener()
    }

    private fun setAdapter() {
        for (year in 1980..currentYear + 1) {
            yearsList.add(year.toString())
        }

        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, yearsList)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bin.yearSp.adapter = yearAdapter

        bin.yearSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedYear = yearsList.getOrNull(position)
                if (selectedYear != null && selectedYear != "Select Year") {
                    toast("Selected Year: $selectedYear")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        for (month in 1..12) {
            monthsList.add(month.toString().padStart(2, '0'))  // 01, 02...12
        }

        val monthAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, monthsList)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bin.monthSp.adapter = monthAdapter

        bin.monthSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMonth = monthsList.getOrNull(position)
                if (selectedMonth != null && selectedMonth != "Select Month") {
                    toast("Selected Month: $selectedMonth")
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun btnListener() {
        bin.fromDate.setOnClickListener {
            Utils.hideKeyboard(this)
            DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    myCalender.set(year, monthOfYear, dayOfMonth)
                    Utils.updateLabel(bin.fromDate, myCalender, "Update From Date")
                },
                myCalender.get(Calendar.YEAR),
                myCalender.get(Calendar.MONTH),
                myCalender.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        bin.toDate.setOnClickListener {
            Utils.hideKeyboard(this)
            DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    myCalender1.set(year, monthOfYear, dayOfMonth)
                    Utils.updateLabel(bin.toDate, myCalender1, "Update To Date")
                },
                myCalender1.get(Calendar.YEAR),
                myCalender1.get(Calendar.MONTH),
                myCalender1.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        bin.proceedBtn.setOnClickListener {
            getAllBusTicketHistory()
        }
    }

    private fun getAllBusTicketHistory() {
        val busHistoryReq = BusHistoryReq(
            fromdate = bin.fromDate.text.toString().trim(),
            month = selectedMonth,
            todate = "bin.toDate.text.toString().trim(),",
            type = 0,
            year = selectedYear,
            iPAddress = mStash?.getStringValue(Constants.deviceIPAddress, ""),
            requestId = "34601352",
            imeINumber = "0054748569",
            registrationID = mStash?.getStringValue(Constants.MerchantId, "")
        )
        viewModel.getAllBusTicketHistory(busHistoryReq).observe(this) {resource ->
            resource?.let {
                when(it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                getAllBusTicketHistoryRes(response)
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
    private fun getAllBusTicketHistoryRes(response: BusHistoryRes) {
        if (response.responseHeader?.errorCode == "0000") {
            bookingHistoryList.clear()
            bin.recLayout.visibility = View.VISIBLE
            response.busAvailableBookings.forEach { bookingList ->
                bookingHistoryList.add(bookingList)
            }
            busTicketBookingHistoryAdapter.notifyDataSetChanged()
        }else {
            Toast.makeText(this, response.responseHeader?.errorDesc.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun initView() {
        mStash = MStash.getInstance(this)
        pd = PD(this)

        viewModel = ViewModelProvider(this, TravelViewModelFactory(TravelRepository(RetrofitClient.apiAllTravelAPI,RetrofitClient.apiBusAddRequestlAPI)))[TravelViewModel::class.java]

        bookingHistoryList = ArrayList()

        bin.recycle1.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        busTicketBookingHistoryAdapter = BusTicketBookingHistoryAdapter(this, bookingHistoryList)
        bin.recycle1.adapter = busTicketBookingHistoryAdapter

        yearsList.add("Select Year")
        monthsList.add("Select Month")
    }
}