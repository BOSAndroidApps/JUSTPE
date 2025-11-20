package com.bos.payment.appName.ui.view.travel.busactivity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.bus.busRequery.BusRequeryReq
import com.bos.payment.appName.data.model.travel.bus.busRequery.BusRequeryRes
import com.bos.payment.appName.data.model.travel.bus.city.BusCityListReq
import com.bos.payment.appName.data.model.travel.bus.city.BusCityListRes
import com.bos.payment.appName.data.model.travel.bus.city.CityDetails
import com.bos.payment.appName.data.repository.TravelRepository
import com.bos.payment.appName.data.viewModelFactory.TravelViewModelFactory
import com.bos.payment.appName.databinding.ActivityBookingTravelBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.travel.model.DateModel
import com.bos.payment.appName.ui.viewmodel.TravelViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils
import com.bos.payment.appName.utils.Utils.PD
import com.bos.payment.appName.utils.Utils.generateRandomNumber
import com.bos.payment.appName.utils.Utils.toast
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingTravel : AppCompatActivity() {
    private lateinit var bin: ActivityBookingTravelBinding
    private var mStash: MStash? = null
    private lateinit var pd: AlertDialog
    private lateinit var viewModel: TravelViewModel
    private lateinit var busList: ArrayList<CityDetails>
    private val myCalender = Calendar.getInstance()
    private var fromDesignation: String? = null
    private var fromDesignationName: String? = null
    private var toDesignation: String? = null
    private var toDesignationName: String? = null
    private var isSeaterBus: Boolean? = false
    private var isSleeperBus: Boolean? = false
    private var isAcBus: Boolean? = false
    private  var passengerList: MutableList<com.bos.payment.appName.data.model.travel.bus.busRequery.PaXDetails> = mutableListOf()

    private val bookingDate = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalender.set(Calendar.YEAR, year)
        myCalender.set(Calendar.MONTH, monthOfYear)
        myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()) // you can format it as needed
        val selectedDate = sdf.format(myCalender.time)

        bin.date.setText(selectedDate) // set selected date in EditText
        mStash?.setStringValue(Constants.dateAndTime, selectedDate) // save date in shared prefs
        Log.d("SelectedDate", selectedDate) // for debug
    }

    val dateList = mutableListOf<DateModel>()
    val calendar = Calendar.getInstance()
    val dateFormatDayNumber = SimpleDateFormat("dd", Locale.getDefault())
    val dateFormatDayName = SimpleDateFormat("EEE", Locale.getDefault())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bin = ActivityBookingTravelBinding.inflate(layoutInflater)
        setContentView(bin.root)
        initView()
        getAllTravelBusList()
        setDropDown()
        btnListener()
       // mStash?.setStringValue(Constants.MerchantId, "AOP-554") // for testing purpose
    }

    private fun setDropDown() {
        /**************************************** Get All from location ***************************/
        Constants.getAllBusListAdapter = ArrayAdapter<String>(this, R.layout.spinner_item_selected, Constants.busListName!!)
        Constants.getAllBusListAdapter!!.setDropDownViewResource(R.layout.spinner_right_aligned)
        bin.fromDestinationSp.adapter = Constants.getAllBusListAdapter
        bin.fromDestinationSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (pos > 0) {
                    try {
                        fromDesignation = Constants.busListNameMap?.get(parent?.getItemAtPosition(pos)).toString()
                        fromDesignationName = parent?.getItemAtPosition(pos).toString()
                        mStash?.setStringValue(Constants.fromDesignationName, fromDesignationName)
                        mStash?.setStringValue(Constants.fromDesignationId, fromDesignation)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    fromDesignation = ""
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        Constants.getAllBusListAdapter!!.notifyDataSetChanged()

        /**************************************** Get All to location ***************************/
        Constants.getAllBusListAdapter = ArrayAdapter<String>(this, R.layout.spinner_item_selected, Constants.toLocationName!!)

        Constants.getAllBusListAdapter!!.setDropDownViewResource(R.layout.spinner_right_aligned)

        bin.toDestinationSp.adapter = Constants.getAllBusListAdapter

        bin.toDestinationSp.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                if (pos > 0) {
                    try {
                        toDesignation = Constants.toLocationNameMap?.get(parent?.getItemAtPosition(pos)).toString()
                        toDesignationName = parent?.getItemAtPosition(pos).toString()

                        mStash?.setStringValue(Constants.toDesignationId, toDesignation)
                        mStash?.setStringValue(Constants.toDesignationName, toDesignationName)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    toDesignation = ""
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
        Constants.getAllBusListAdapter!!.notifyDataSetChanged()

    }


    private fun getAllTravelBusList() {
        val requestId = generateRandomNumber()
        mStash?.setStringValue(Constants.requestId, requestId)
        val busCityListReq = BusCityListReq(
            iPAddress = mStash!!.getStringValue(Constants.deviceIPAddress, ""),
            requestId =  mStash?.getStringValue(Constants.requestId, ""),
            imeINumber = "2232323232323",
            registrationID = mStash!!.getStringValue(Constants.MerchantId, "")
        )
        Log.d("busCityListReq",Gson().toJson(busCityListReq))
        viewModel.getAllBusCityList(busCityListReq).observe(this) { resource ->
            resource?.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        pd.dismiss()
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                getAllTravelBusListRes(response)
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


    private fun getAllTravelBusListRes(response: BusCityListRes?) {
        if (response?.responseHeader?.errorCode == "0000"){
            Constants.busListName?.clear()
            Constants.toLocationName?.clear()
            Constants.busListName?.add("Select from location")
            Constants.toLocationName?.add("Select to location")
            response.cityDetails.forEach { busListData ->
                Constants.busListName?.add(busListData.cityName!!)
                Constants.toLocationName?.add(busListData.cityName!!)
                Constants.busListNameMap?.put(busListData.cityName!!, busListData.cityID!!)
                Constants.toLocationNameMap?.put(busListData.cityName!!, busListData.cityID!!)
            }
            Constants.getAllBusListAdapter?.notifyDataSetChanged()
        }
        else {
            toast("Error Message")
            Toast.makeText(this, response?.responseHeader?.errorDesc.toString(), Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("ResourceAsColor")
    private fun btnListener() {
        bin.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
//                R.id.nav_home -> {
////                    navController.popBackStack()
//                }

                R.id.bookingBtn -> {
                    startActivity(Intent(this, MyBookingBusActivity::class.java))
                    //getAllBusRequaryTicket()


                }

            }
            return@setOnItemSelectedListener true
        }

        bin.busLayout.setOnClickListener {
            bin.cardView.visibility = View.VISIBLE
            setHoverSelection()
            bin.buscard.setCardBackgroundColor(resources.getColor(R.color.white))
            bin.busicon.imageTintList= resources.getColorStateList(R.color.blue)
            bin.bus.setTextColor(resources.getColor(R.color.blue))
        }

        bin.flightsLayout.setOnClickListener {
           /* //toast("Available Soon")
           // bin.cardView.visibility = View.GONE
            setHoverSelection()
            bin.flightcard.setCardBackgroundColor(resources.getColor(R.color.white))
            bin.flighticon.imageTintList= resources.getColorStateList(R.color.blue)
            bin.flights.setTextColor(resources.getColor(R.color.blue))*/
           //startActivity(Intent(this, FlightMainActivity::class.java))
        }

        bin.trainsLayout.setOnClickListener {
            toast("Available Soon")
            bin.cardView.visibility = View.GONE
            setHoverSelection()
            bin.traincard.setCardBackgroundColor(resources.getColor(R.color.white))
            bin.trainicon.imageTintList= resources.getColorStateList(R.color.blue)
            bin.trains.setTextColor(resources.getColor(R.color.blue))
        }

        bin.hotelsLayout.setOnClickListener {
            toast("Available Soon")
            bin.cardView.visibility = View.GONE
            setHoverSelection()
            bin.hotelcard.setCardBackgroundColor(resources.getColor(R.color.white))
            bin.hotelicon.imageTintList= resources.getColorStateList(R.color.blue)
            bin.hotels.setTextColor(resources.getColor(R.color.blue))
        }


        bin.backBtn.setOnClickListener {
            onBackPressed()
        }

        bin.searchBuses.setOnClickListener {
            validationCity()
        }
        bin.seaterBusCard.setOnClickListener {
            if (isSeaterBus == true){
                isSeaterBus = false
                bin.seaterBusCard.background?.setTint(ContextCompat.getColor(this, R.color.white))
                bin.seater.setTextColor(resources.getColor(R.color.blue))
            }else {
                isSeaterBus = true
                isSleeperBus = false
                isAcBus = false
                bin.seaterBusCard.background?.setTint(ContextCompat.getColor(this, R.color.blue))
                bin.seater.setTextColor(resources.getColor(R.color.white))
            }
        }
        bin.sleeperBusCard.setOnClickListener {
            if (isSleeperBus == true){
                isSleeperBus = false
                bin.sleeperBusCard.background?.setTint(ContextCompat.getColor(this, R.color.white))
                bin.sleeper.setTextColor(resources.getColor(R.color.blue))
            }else {
                isSeaterBus = false
                isSleeperBus = true
                isAcBus = false
                bin.sleeperBusCard.background?.setTint(ContextCompat.getColor(this, R.color.blue))
                bin.sleeper.setTextColor(resources.getColor(R.color.white))
            }
        }
        bin.acBusCard.setOnClickListener {
            if (isAcBus == true) {
                isAcBus = false
                bin.acBusCard.background?.setTint(ContextCompat.getColor(this, R.color.white))
                bin.acBus.setTextColor(resources.getColor(R.color.blue))
            } else {
                isSeaterBus = false
                isSleeperBus = false
                isAcBus = true
                bin.acBusCard.background?.setTint(ContextCompat.getColor(this, R.color.blue))
                bin.acBus.setTextColor(resources.getColor(R.color.white))
            }
        }

        bin.date.setOnClickListener {
            Utils.hideKeyboard(this)
            DatePickerDialog(
                this,
                bookingDate,
                myCalender[Calendar.YEAR],
                myCalender[Calendar.MONTH],
                myCalender[Calendar.DAY_OF_MONTH]
            ).show()
        }

    }

    private fun validationCity() {
        val errorMessage: String? = when{
            fromDesignation.isNullOrBlank() -> "Please select from location"
            toDesignation.isNullOrBlank() -> "Please select to location"
            bin.date.text?.trim().isNullOrEmpty() -> "Please select date"

            else -> null
        }
        if (errorMessage != null){
            toast(errorMessage)
        } else {
            startActivity(Intent(this, BusSearchDetails::class.java))
        }
    }


    private fun setHoverSelection(){
        bin.flightcard.setCardBackgroundColor(resources.getColor(R.color.blue))
        bin.buscard.setCardBackgroundColor(resources.getColor(R.color.blue))
        bin.traincard.setCardBackgroundColor(resources.getColor(R.color.blue))
        bin.hotelcard.setCardBackgroundColor(resources.getColor(R.color.blue))

        bin.flighticon.imageTintList= resources.getColorStateList(R.color.white)
        bin.busicon.imageTintList= resources.getColorStateList(R.color.white)
        bin.trainicon.imageTintList= resources.getColorStateList(R.color.white)
        bin.hotelicon.imageTintList= resources.getColorStateList(R.color.white)

        bin.flights.setTextColor(resources.getColor(R.color.white))
        bin.bus.setTextColor(resources.getColor(R.color.white))
        bin.trains.setTextColor(resources.getColor(R.color.white))
        bin.hotels.setTextColor(resources.getColor(R.color.white))

    }

    @SuppressLint("ResourceAsColor")
    private fun initView() {
        pd = PD(this)
        busList = ArrayList()
        mStash = MStash.getInstance(this)
        bin.bus.requestFocus()

        Constants.busListName = ArrayList()
        Constants.toLocationName = ArrayList()
        Constants.busListName?.add("Select from location")
        Constants.toLocationName?.add("Select to location")

        Constants.busListNameMap = HashMap()
        Constants.toLocationNameMap = HashMap()

        Constants.busListNameMapForGettingBusListName = HashMap()
        Constants.toLocationNameMapForGettingToLocationName = HashMap()

        viewModel = ViewModelProvider(
            this,
            TravelViewModelFactory(TravelRepository(RetrofitClient.apiAllTravelAPI,RetrofitClient.apiBusAddRequestlAPI))
        )[TravelViewModel::class.java]


        for (i in 0..3) { // Today + next 3 days
            if (i == 0) {
                dateList.add(DateModel(dateFormatDayNumber.format(calendar.time), "Today"))
            } else {
                dateList.add(DateModel(dateFormatDayNumber.format(calendar.time), dateFormatDayName.format(calendar.time)))
            }
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

// Add "Calendar" icon manually
        dateList.add(DateModel("", "Calendar"))

//        bin.dateRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        val adapter = DateAdapter(dateList)
//        bin.dateRecyclerView.adapter = adapter

    }




    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    private fun getAllBusRequaryTicketRes(response: BusRequeryRes?) {
        if (response?.responseHeader?.errorCode == "0000") {
            passengerList.clear()

            response.paXDetails.forEach { passengerData ->
                passengerList.add(passengerData)
            }

            BusTicketConsListClass.RequeryResponse = response





        } else {
            Toast.makeText(this, response?.responseHeader?.errorInnerException.toString(), Toast.LENGTH_SHORT).show()
        }
    }

}