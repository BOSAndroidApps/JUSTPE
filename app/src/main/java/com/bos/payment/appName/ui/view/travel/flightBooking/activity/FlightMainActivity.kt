/*
package com.bos.payment.appName.ui.view.travel.flightBooking.activity

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.constant.ConstantClass
import com.bos.payment.appName.constant.CustomFuseLocationActivity
import com.bos.payment.appName.data.model.travel.flight.AirportListReq
import com.bos.payment.appName.data.model.travel.flight.DataItem
import com.bos.payment.appName.data.model.travel.flight.FilterAirLine
import com.bos.payment.appName.data.model.travel.flight.FlightSearchReq
import com.bos.payment.appName.data.model.travel.flight.TripInfo
import com.bos.payment.appName.data.model.walletBalance.walletBalanceCal.GetBalanceRes
import com.bos.payment.appName.data.repository.TravelRepository
import com.bos.payment.appName.data.viewModelFactory.TravelViewModelFactory
import com.bos.payment.appName.databinding.ActivityFlightSearchBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.travel.adapter.AirPortListAdapter
import com.bos.payment.appName.ui.view.travel.adapter.FlightSpecialOfferAdapter
import com.bos.payment.appName.ui.view.travel.busactivity.MyBookingBusActivity
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.AllAirNameList
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.AllFlightList
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.DepartureDateAndTime
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.FlightHint
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.FlightList
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.FlightListForFilter
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.TripDetailsList
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.adultCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.after12pmlayout
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.after6amlayout
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.after6pmlayout
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.airportNameList
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.before6layout
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.bookingType
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.checkFrom
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.checkTo
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.childCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.className
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.classNumber
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.defenceFare
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.fromAirportCode
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.fromAirportName
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.fromCityName
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.fromCountryName
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.infantCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.srCitizen
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.studentFare
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.toAirportCode
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.toAirportName
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.toCityName
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.toCountryName
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.totalCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.travelDate
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.travelType
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.adultList
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.childList
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.infantList
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddGSTInformationBottomSheet.Companion.CheckBox
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddGSTInformationBottomSheet.Companion.companyName
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddGSTInformationBottomSheet.Companion.registrationNo
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.ReviewDetailsPassangersBottomSheet.Companion.passangerDetailsList
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.SelectTravellersClassBottomSheet
import com.bos.payment.appName.ui.viewmodel.TravelViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.Constants.uploadDataOnFirebaseConsole
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils.generateRandomNumber
import com.bos.payment.appName.utils.Utils.toast
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.log

class FlightMainActivity : AppCompatActivity(), FlightSpecialOfferAdapter.setClickListner {

    private var firstDateCalendar: Calendar? = null

    lateinit var binding: ActivityFlightSearchBinding

    var specialFareList: MutableList<Pair<String, Boolean>> = mutableListOf()

    var checkReverse: Boolean = false
    lateinit var viewModel: TravelViewModel
    lateinit var pd: ProgressDialog
    var AirportDataList: MutableList<DataItem> = mutableListOf()
    private lateinit var mStash: MStash
    private var customFuseLocation: CustomFuseLocationActivity? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFlightSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mStash = MStash(this)

        pd = ProgressDialog(this)
        pd.setCanceledOnTouchOutside(false)

        viewModel = ViewModelProvider(this, TravelViewModelFactory(TravelRepository(RetrofitClient.apiAllTravelAPI, RetrofitClient.apiBusAddRequestlAPI)))[TravelViewModel::class.java]


        selectionHoverColor()
        bookingType = 0
        binding.onewaytxt.setTextColor(resources.getColor(R.color.blue))
        binding.onewaytxt.background = resources.getDrawable(R.drawable.selectedbutton)

        setclickListner()
        setCurrentDate()
        addDataInList()
        getFuseLocation()
        hitApiForGetAirportList("")


    }


    private fun getFuseLocation() {
        customFuseLocation = CustomFuseLocationActivity(this
                , this) {
                    mCurrentLocation ->
                ConstantClass.latdouble = mCurrentLocation.getLatitude()
                ConstantClass.longdouble = mCurrentLocation.getLongitude()
            }
    }


    fun setSpinnerData() {
        FlightHint!!.clear()
        FlightHint!!.add(DataItem("", "Select City", "", ""))
        FlightConstant.getAllFlightListAdapter = ArrayAdapter<DataItem>(this, R.layout.flightsearchspinnerlayout, FlightHint!!)
        FlightConstant.getAllFlightListAdapter!!.setDropDownViewResource(R.layout.spinner_right_aligned)

        binding.searchableSp.adapter = FlightConstant.getAllFlightListAdapter
        binding.searchableSpto.adapter = FlightConstant.getAllFlightListAdapter

    }


    override fun onResume() {
        super.onResume()
        setData()
    }


    fun addDataInList() {
        specialFareList.clear()
        specialFareList.add(Pair("Defence Forces", false))
        specialFareList.add(Pair("Students", false))
        specialFareList.add(Pair("Senior Citizens", false))
        specialFareList.add(Pair("Others", false))
        var adapter = FlightSpecialOfferAdapter(this, specialFareList, this)
        binding.extraofferList.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    public fun setData() {

        binding.fromlayout.visibility = View.VISIBLE
        binding.searchableSp.visibility = View.GONE

        binding.searchableSpto.visibility = View.GONE
        binding.tolayout.visibility = View.VISIBLE

        totalCount = (adultCount + childCount + infantCount).toString()
        binding.totalcountpassanger.text = totalCount
        binding.classname.text = className

        if (childCount == 0 && infantCount == 0) {
            FlightConstant.datepassangerandclassstring = binding.datemonthdeparture.text.toString().plus("|").plus(adultCount).plus("Adult").plus("|").plus(className)
        } else {
            FlightConstant.datepassangerandclassstring = binding.datemonthdeparture.text.toString().plus("|").plus(totalCount).plus("Travellers").plus("|").plus(className)
        }

        if (checkFrom) {
            binding.fromcityname.text = fromCityName
            binding.fromairportcode.text = fromAirportCode
            binding.fromairportname.text = fromAirportName
            binding.fromcityLayout.background = resources.getDrawable(R.drawable.fromtoback)
        } else {
            binding.tocityname.text = toCityName
            binding.toairportcode.text = toAirportCode
            binding.toairportname.text = toAirportName
            binding.tocityLayout.background = resources.getDrawable(R.drawable.fromtoback)
        }

    }


    private fun setCurrentDate() {
        // Get current date for initializing DatePicker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val currentHour = c.get(Calendar.HOUR_OF_DAY)
        val currentMinute = c.get(Calendar.MINUTE)


        val formattedDate = String.format("%02d/%02d/%04d", day, month + 1, year)
        binding.datemonthdeparture.text = formatDate1(formattedDate)
        binding.dayyear.text = formatDate2(formattedDate)
        travelDate = String.format("%02d/%02d/%04d", month + 1, day, year)

        val time = String.format("%02d:%02d", currentHour, currentMinute)

        DepartureDateAndTime = "$travelDate"

    }


    private fun setclickListner() {

        binding.back.setOnClickListener {
            finish()
        }

        binding.fromcityLayout.setOnClickListener {
            checkFrom = true
            checkTo = false
            binding.fromlayout.visibility = View.GONE
            binding.searchableSp.visibility = View.VISIBLE
            binding.fromcityLayout.background = resources.getDrawable(R.drawable.selectedbutton)
            binding.searchableSp.openDialog()
        }


        binding.tocityLayout.setOnClickListener {
            checkFrom = false
            checkTo = true
            binding.tolayout.visibility = View.GONE
            binding.searchableSpto.visibility = View.VISIBLE
            binding.tocityLayout.background = resources.getDrawable(R.drawable.selectedbutton)
            binding.searchableSpto.openDialog()
        }


        binding.onewaytxt.setOnClickListener {
            selectionHoverColor()
            bookingType = 0
            binding.onewaytxt.setTextColor(resources.getColor(R.color.blue))
            binding.onewaytxt.background = resources.getDrawable(R.drawable.selectedbutton)
        }


        binding.roundtriptxt.setOnClickListener {
            selectionHoverColor()
            bookingType = 1
            binding.roundtriptxt.setTextColor(resources.getColor(R.color.blue))
            binding.roundtriptxt.background = resources.getDrawable(R.drawable.selectedbutton)
        }


        binding.multicitytxt.setOnClickListener {
            selectionHoverColor()
            bookingType = 2
            binding.multicitytxt.setTextColor(resources.getColor(R.color.blue))
            binding.multicitytxt.background = resources.getDrawable(R.drawable.selectedbutton)
        }


        binding.departureDatelayout.setOnClickListener {

            // Get current date for initializing DatePicker0
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(
                this,
                R.style.MyDatePickerDialogTheme,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    firstDateCalendar = c
                    val formattedDate = String.format(
                        "%02d/%02d/%04d",
                        selectedDay,
                        selectedMonth + 1,
                        selectedYear
                    )
                    binding.datemonthdeparture.text = formatDate1(formattedDate)
                    binding.dayyear.text = formatDate2(formattedDate)
                    travelDate = String.format(
                        "%02d/%02d/%04d",
                        selectedMonth + 1,
                        selectedDay,
                        selectedYear
                    )

                    // Get current time
                    val calendar = Calendar.getInstance()
                    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                    val currentMinute = calendar.get(Calendar.MINUTE)

                    val time = String.format("%02d:%02d", currentHour, currentMinute)
                    DepartureDateAndTime = "$travelDate"

                },
                year,
                month,
                day
            )

            dpd!!.setOnShowListener {
                dpd!!.getButton(DatePickerDialog.BUTTON_POSITIVE)
                    ?.setTextColor(ContextCompat.getColor(this, R.color.green))
                dpd!!.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                    ?.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
            dpd!!.datePicker.minDate = c.timeInMillis

            dpd!!.show()

        }


        binding.addreturndatelayout.setOnClickListener {

            binding.crossicon.visibility = View.GONE
            binding.addreturnlayout.visibility = View.VISIBLE
            binding.returndatelayout.visibility = View.INVISIBLE

            selectionHoverColor()
            binding.onewaytxt.setTextColor(resources.getColor(R.color.blue))
            binding.onewaytxt.background = resources.getDrawable(R.drawable.selectedbutton)
            bookingType = 0


            val calendar = firstDateCalendar?.clone() as? Calendar ?: Calendar.getInstance()

            val year2 = calendar.get(Calendar.YEAR)
            val month2 = calendar.get(Calendar.MONTH)
            val day2 = calendar.get(Calendar.DAY_OF_MONTH)

            val secondDatePicker = DatePickerDialog(
                this, R.style.MyDatePickerDialogTheme, { _, y, m, d ->
                    val formattedDate = String.format("%02d/%02d/%04d", d, m + 1, y)
                    binding.addreturnlayout.visibility = View.GONE
                    binding.returndatelayout.visibility = View.VISIBLE
                    binding.crossicon.visibility = View.VISIBLE
                    selectionHoverColor()
                    bookingType = 1
                    binding.roundtriptxt.setTextColor(resources.getColor(R.color.blue))
                    binding.roundtriptxt.background =
                        resources.getDrawable(R.drawable.selectedbutton)
                    binding.datemonthreturn.text = formatDate1(formattedDate)
                    binding.dayyearreturn.text = formatDate2(formattedDate)

                },
                year2, month2, day2
            )

            secondDatePicker!!.setOnShowListener {
                secondDatePicker!!.getButton(DatePickerDialog.BUTTON_POSITIVE)
                    ?.setTextColor(ContextCompat.getColor(this, R.color.green))
                secondDatePicker!!.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                    ?.setTextColor(ContextCompat.getColor(this, R.color.red))
            }
            secondDatePicker!!.datePicker.minDate = calendar.timeInMillis

            secondDatePicker.show()


        }


        binding.crossicon.setOnClickListener {

            binding.crossicon.visibility = View.GONE
            binding.addreturnlayout.visibility = View.VISIBLE
            binding.returndatelayout.visibility = View.INVISIBLE

            selectionHoverColor()
            binding.onewaytxt.setTextColor(resources.getColor(R.color.blue))
            binding.onewaytxt.background = resources.getDrawable(R.drawable.selectedbutton)
            bookingType = 0

        }


        binding.travellersandclasslayout.setOnClickListener {
            val bottomfrag = SelectTravellersClassBottomSheet()
            supportFragmentManager.let { bottomfrag.show(it, SelectTravellersClassBottomSheet.TAG) }
        }


        binding.reversetrip.setOnClickListener {

            fromCityName = binding.fromcityname.text.toString()
            fromAirportCode = binding.fromairportcode.text.toString()
            fromAirportName = binding.fromairportname.text.toString()

            toCityName = binding.tocityname.text.toString()
            toAirportCode = binding.toairportcode.text.toString()
            toAirportName = binding.toairportname.text.toString()

            var tempCountryName: String

            tempCountryName = fromCountryName
            fromCountryName = toCountryName
            toCountryName = tempCountryName


            if (checkReverse) {
                binding.fromcityname.text = toCityName
                binding.fromairportcode.text = toAirportCode
                binding.fromairportname.text = toAirportName

                binding.tocityname.text = fromCityName
                binding.toairportcode.text = fromAirportCode
                binding.toairportname.text = fromAirportName

                checkReverse = false
            }
            else {
                binding.tocityname.text = fromCityName
                binding.toairportcode.text = fromAirportCode
                binding.toairportname.text = fromAirportName

                binding.fromcityname.text = toCityName
                binding.fromairportcode.text = toAirportCode
                binding.fromairportname.text = toAirportName


                checkReverse = true
            }


        }


        binding.searchflightlayout.setOnClickListener {

            if (!fromCountryName.equals("India", ignoreCase = true) || !toCountryName.equals("India", ignoreCase = true)) {
                travelType = 1
            }
            else {
                travelType = 0
            }

            if (bookingType !in 0..2) {
                toast("Invalid Booking Type")
                return@setOnClickListener
            }

            if (adultCount < 1) {
                toast("At least one adult must be selected")
                return@setOnClickListener
            }

            if (childCount < 0 || infantCount < 0) {
                toast("Child/Infant count cannot be negative")
                return@setOnClickListener
            }

            if (classNumber !in listOf("0", "1", "2", "3")) {
                toast("Invalid travel class selected")
                return@setOnClickListener
            }

            if (mStash?.getStringValue(Constants.MerchantId, "").isNullOrEmpty()) {
                toast("Merchant ID is missing")
                return@setOnClickListener
            }

            if (mStash?.getStringValue(Constants.deviceIPAddress, "").isNullOrEmpty()) {
                toast("Device IP address is missing")
                return@setOnClickListener
            }

            if (getTripInfoList().isEmpty()) {
                toast("Trip information is missing")
                return@setOnClickListener //
            }

            if (travelDate.equals("")) {
                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)
                travelDate = String.format("%02d/%02d/%04d", month + 1, day, year)
            }


            // All validations passed
            hitApiForSearchFlight()
        }


        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
//                R.id.nav_home -> {
////                    navController.popBackStack()
//                }

                R.id.bookingBtn -> {
                    startActivity(Intent(this, FlightBookingPage::class.java))
                }

            }
            return@setOnItemSelectedListener true
        }


    }


    private fun selectionHoverColor() {
        binding.onewaytxt.setTextColor(resources.getColor(R.color.text_color))
        binding.roundtriptxt.setTextColor(resources.getColor(R.color.text_color))
        binding.multicitytxt.setTextColor(resources.getColor(R.color.text_color))
        binding.onewaytxt.background = null
        binding.roundtriptxt.background = null
        binding.multicitytxt.background = null
    }


    fun formatDate1(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM ", Locale.getDefault())

        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }


    fun formatDate2(inputDate: String): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat(" EEE, yyyy", Locale.getDefault())

        return try {
            val date = inputFormat.parse(inputDate)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            "Invalid Date"
        }
    }


    override fun itemclickListner(position: Int) {

        if (specialFareList.get(position).first.equals("Senior Citizens")) {
            srCitizen = true
            studentFare = false
            defenceFare = false
        }


        if (specialFareList.get(position).first.equals("Students")) {
            srCitizen = false
            studentFare = true
            defenceFare = false

        }

        if (specialFareList.get(position).first.equals("Defence Forces")) {
            srCitizen = false
            studentFare = false
            defenceFare = true
        }


        if (specialFareList.get(position).first.equals("Others")) {
            srCitizen = false
            studentFare = false
            defenceFare = false
        }


    }


    fun hitApiForGetAirportList(airportfilterString: String) {
        var airportListReq = AirportListReq(searchTerm = airportfilterString)

        Log.d("AirPortListRequest", Gson().toJson(airportListReq))

        viewModel.getAirportListRequet(airportListReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("AirPortListResponse", Gson().toJson(response))
                                uploadDataOnFirebaseConsole(Gson().toJson(response),"FlightMainActivityAirportList",this@FlightMainActivity)
                                var getdata = response.data
                                if (!getdata.isNullOrEmpty()) {
                                    AirportDataList.clear()
                                    FlightConstant.FlightList!!.clear()

                                    AirportDataList.addAll(getdata)

                                    if (AirportDataList.size > 0) {

                                        FlightList!!.addAll(AirportDataList)

                                    }

                                    setSpinnerData()

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


    fun hitApiForSearchFlight() {

        val requestId = generateRandomNumber()
        mStash?.setStringValue(Constants.requestId, requestId)
        var airportListReq = FlightSearchReq(
            requestId =  requestId,
            imeInumber = "0054748569",
            travelType = travelType, // depend of from and to trip from city
            bookingType = bookingType, //0 – One Way, 1 – Round Trip, 2 – Special Round Trip
            adultCount = adultCount.toString(),
            childCount = childCount.toString(),
            infantCount = infantCount.toString(),
            classOfTravel = classNumber,// Possible values: 0- ECONOMY/ 1- BUSINESS/ 2- FIRST/ 3- PREMIUM_ECONOMY
            inventoryType = 0,//0–RetailFare (public) fixed
            sourceType = 0,//0 – All Fares, 1 – Series Fare (Own + 3rd Party), 2 – Non-Series Fare Only fixed
            srCitizenSearch = srCitizen,
            studentFareSearch = studentFare,
            defenceFareSearch = defenceFare,
            registartionId = mStash?.getStringValue(Constants.MerchantId, ""),
            ipAddress = mStash?.getStringValue(Constants.deviceIPAddress, ""),
            filterAirlineList = getFilterAirLineList(),
            tripeInfoList = getTripInfoList()
        )

        Log.d("AirPortListRequest", Gson().toJson(airportListReq))

        viewModel.getFlightSearchRequest(airportListReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                uploadDataOnFirebaseConsole(Gson().toJson(response),"FlightMainActivitySearchRequest",this@FlightMainActivity)
                                Log.d("AirPortListResponse", Gson().toJson(response))
                                if (response.responseHeader.errorCode.equals("0000")) {
                                    adultList.clear()
                                    childList.clear()
                                    infantList.clear()
                                    passangerDetailsList.clear()
                                    companyName = ""
                                    registrationNo = ""
                                    CheckBox = false
                                    before6layout = false
                                    after6amlayout = false
                                    after12pmlayout = false
                                    after6pmlayout = false
                                    if (childCount == 0 && infantCount == 0) {
                                        FlightConstant.datepassangerandclassstring =
                                            binding.datemonthdeparture.text.toString().plus("|").plus(adultCount).plus("Adult")
                                                .plus("|").plus(className)
                                    } else {
                                        FlightConstant.datepassangerandclassstring =
                                            binding.datemonthdeparture.text.toString().plus("|").plus(totalCount)
                                                .plus("Travellers").plus("|").plus(className)
                                    }

                                    mStash.setStringValue(Constants.FlightSearchKey, response.searchKey)
                                    Log.d("SearchKey", response.searchKey)
                                    var getData = response.tripDetails
                                    mStash.setFlightSearchData("flightsearchdata", getData, this)

                                    TripDetailsList.clear()
                                    getData?.let { it1 -> TripDetailsList.addAll(it1) }

                                    airportNameList.clear()

                                    airportNameList = TripDetailsList[0]?.flights
                                        ?.mapNotNull { it.segments?.getOrNull(0)?.airlineName }
                                        ?.distinct()
                                        ?.map { it to false }
                                        ?.toMutableList() ?: mutableListOf()

                                    Log.d("AirlineName",Gson().toJson(airportNameList))

                                    AllFlightList.clear()

                                    TripDetailsList[0]!!.flights?.let { AllFlightList.addAll(it) }


                                    fromCityName = binding.fromcityname.text.toString()
                                    toCityName = binding.tocityname.text.toString()
                                    startActivity(Intent(this, FlightDetailListActivity::class.java))
                                    pd.dismiss()
                                } else {
                                    toast(response.responseHeader.errorInnerException)
                                    pd.dismiss()
                                }

                            }
                        }
                    }

                    ApiStatus.ERROR -> {
                        pd.dismiss()
                        Toast.makeText(this@FlightMainActivity,"Server busy",Toast.LENGTH_SHORT)
                    }

                    ApiStatus.LOADING -> {
                        pd.show()
                    }

                }
            }
        }
    }


    fun getFilterAirLineList(): List<FilterAirLine> {
        var airlinecodeList: MutableList<FilterAirLine> = mutableListOf()
        airlinecodeList.add(FilterAirLine(airlineCode = ""))
        return airlinecodeList
    }


    fun getTripInfoList(): List<TripInfo> {
        var airlinecodeList: MutableList<TripInfo> = mutableListOf()
        airlinecodeList.add(
            TripInfo(
                origin = binding.fromairportcode.text.toString(),
                destination = binding.toairportcode.text.toString(),
                traveldate = travelDate,
                tripId = 0
            )
        )
        return airlinecodeList
    }


    */
/*fun uploadAirportListOnFirebaseConsole(data:String, collectionPath:String){

        val db = Firebase.firestore

        val sdf = SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.getDefault())
        val currentDateTime = sdf.format(Date())


        // Create a new user with a first and last name
        val logData = hashMapOf(
            "filename" to "aopaytravel.txt",
            "content" to data,
            "timestamp" to System.currentTimeMillis()
        )

        db.collection(collectionPath)
            .document(currentDateTime)
            .set(logData)
            .addOnSuccessListener {
                Toast.makeText(this, "Log saved in Firestore", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.d("Error", " $e.message")
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }*//*



}*/
