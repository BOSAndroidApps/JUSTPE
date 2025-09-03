package com.bos.payment.appName.ui.view.travel.flightBooking.activity

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.NestedScrollView
import com.bos.payment.appName.data.model.travel.flight.FlightsItem
import com.bos.payment.appName.data.model.travel.flight.SegmentsItem
import com.bos.payment.appName.databinding.ActivityAddDetailsPassangerBinding
import com.bos.payment.appName.ui.view.travel.adapter.AddPassengerAdapter
import com.bos.payment.appName.ui.view.travel.adapter.PassangerDataList
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.GetAirlineLogo
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.adultCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.calculateTotalFlightDuration
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.childCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.className
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.formatDate1
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.formatDate2
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.infantCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.slideInFromTop
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.slideOutToTop
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.splitDateTime
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.totalDurationTime
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddContactAndMobileBottomSheet
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddContactAndMobileBottomSheet.Companion.contactNumber
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddContactAndMobileBottomSheet.Companion.emailid
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddGSTInformationBottomSheet
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddGSTInformationBottomSheet.Companion.CheckBox
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddGSTInformationBottomSheet.Companion.companyName
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddGSTInformationBottomSheet.Companion.registrationNo
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddTravellerBottomSheet
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddTravellerBottomSheet.Companion.ClickAdult
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddTravellerBottomSheet.Companion.ClickChild
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.AddTravellerBottomSheet.Companion.ClickInfant
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.FareBreakUpBottomSheet
import com.bos.payment.appName.ui.view.travel.flightBooking.fragment.FlightDetailsBottomSheet
import com.bumptech.glide.Glide

class AddDetailsPassangerActivity : AppCompatActivity() {
    lateinit var binding : ActivityAddDetailsPassangerBinding
    lateinit var adapter : AddPassengerAdapter
    var stops:String=""

    var adultcount:Int= 0
    var childcount:Int= 0
    var infantcount:Int= 0


    var fromTocityname:String=""
    var passangerdetails:String=""

    var lastScrollY = 0
    val threshold = 10
    var currentHeader = 1



    companion object{
        var segmentListPassangerDetail : MutableList<SegmentsItem?> = mutableListOf()
        var flightDetailsPassangerDetail :MutableList<FlightsItem?> = mutableListOf()
        var adultList :MutableList<PassangerDataList> = mutableListOf()
        var childList :MutableList<PassangerDataList> = mutableListOf()
        var infantList :MutableList<PassangerDataList> = mutableListOf()
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddDetailsPassangerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val segments = listOf(mapOf("departure_DateTime" to segmentListPassangerDetail[0]!!.departureDateTime, "arrival_DateTime" to segmentListPassangerDetail[0]!!.arrivalDateTime), mapOf("departure_DateTime" to segmentListPassangerDetail[segmentListPassangerDetail.size-1]!!.departureDateTime, "arrival_DateTime" to segmentListPassangerDetail[segmentListPassangerDetail.size-1]!!.arrivalDateTime))
        totalDurationTime = calculateTotalFlightDuration(segments)

        var originDateTime = splitDateTime(segmentListPassangerDetail[0]!!.departureDateTime)

        var originDate = formatDate1(originDateTime.first).plus(",") .plus(formatDate2(originDateTime.first))

        var originTime = originDateTime.second

        var destinationDateTime = splitDateTime(segmentListPassangerDetail[segmentListPassangerDetail.size-1]!!.arrivalDateTime)
        var DestinationTime = destinationDateTime.second

        adultcount= adultCount
        childcount= childCount
        infantcount= infantCount

        if(adultcount>0){
            binding.AdultLayout.visibility=View.VISIBLE
        }

        if(childcount>0){
            binding.ChildLayout.visibility=View.VISIBLE
        }else{
            binding.ChildLayout.visibility=View.GONE
        }

        if(infantcount>0){
            binding.InfantLayout.visibility=View.VISIBLE
        }else{
            binding.InfantLayout.visibility=View.GONE
        }

        fromTocityname= segmentListPassangerDetail[0]!!.originCity.plus(" to ").plus(segmentListPassangerDetail[segmentListPassangerDetail.size-1]!!.destinationCity)
        passangerdetails= originDate.plus(",").plus(adultCount).plus("Adult").plus(",").plus(childCount).plus("Child").plus(",").plus(infantCount).plus("Infant").plus(",").plus(className)

        if(segments.size==1){
            stops="Non stops"
        }
        else{
            stops= (segments.size-1).toString().plus(" stop")
        }
        // "Fri,4 Jul| 19:10 - 01:05|5h 55m |1 Stop|Economy Class"
        var traveldetails= originDate.plus(" | ").plus(originTime).plus(" - ").plus(DestinationTime).plus(" | ").plus(
            totalDurationTime).plus(" | ").plus(stops).plus(" | ").plus(className)

        setDataOnView(traveldetails)
        setOnClickListner()
        setDataOfGst()

    }


    fun setDataOnView( traveldetails:String){
        binding.destinationCityName.text= segmentListPassangerDetail[segmentListPassangerDetail.size-1]!!.destinationCity
        binding.fromAirportCode.text= flightDetailsPassangerDetail[0]!!.origin
        binding.toAirportCode.text= flightDetailsPassangerDetail[0]!!.destination
        binding.travelprimedetails.text= traveldetails

        binding.fromtocitytxt.text= fromTocityname
        binding.passangerdetailstxt.text= passangerdetails
        binding.cabinbagweight.text= flightDetailsPassangerDetail!![0]?.fares!![0].fareDetails!![0].freeBaggage.handBaggage .plus(" (1 piece only)/Adult")
        binding.checkinbagweight.text= flightDetailsPassangerDetail!![0]?.fares!![0].fareDetails!![0].freeBaggage.checkInBaggage .plus(" (1 piece only)/Adult" )

        var airportIcon = GetAirlineLogo(flightDetailsPassangerDetail[0]!!.airlineCode)
        Glide.with(this).load(airportIcon).into(binding.airlineicon)

        binding.adultcount.text= adultList.size.toString().plus("/").plus(adultcount)
        binding.childcount.text= childList.size.toString().plus("/").plus(childcount)
        binding.infantcount.text= infantList.size.toString().plus("/").plus(infantcount)

    }


    fun setAdultData(){
        if(adultList.size>0) {
            binding.showadultlist.visibility=View.VISIBLE
            adapter = AddPassengerAdapter(adultList)
            binding.showadultlist.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.adultcount.text= adultList.size.toString().plus("/").plus(adultcount)

        }

    }


    fun setChildData(){
        if(childList.size>0) {
            binding.showchildlist.visibility=View.VISIBLE
            adapter = AddPassengerAdapter(childList)
            binding.showchildlist.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.childcount.text= childList.size.toString().plus("/").plus(childcount)
        }

    }


    fun setInfantData(){
        if(infantList.size>0) {
            binding.showinfantlist.visibility=View.VISIBLE
            adapter = AddPassengerAdapter(infantList)
            binding.showinfantlist.adapter = adapter
            adapter.notifyDataSetChanged()
            binding.infantcount.text= infantList.size.toString().plus("/").plus(infantcount)
        }

    }


    fun setDataForContactDetails(){
        binding.mailidTxt.text = emailid
        binding.mobileNumber.text = contactNumber
    }


    fun setDataOfGst(){
        if(CheckBox){
         binding.gstCheckBox.isChecked=true
         binding.gstlayout.visibility= View.VISIBLE
         binding.companyName.text= companyName
         binding.gstnumber.text= registrationNo
        }
        else{
            if(!companyName.isNullOrBlank() || !registrationNo.isNullOrBlank()){
                binding.gstCheckBox.isChecked=true
            }else{
                binding.gstCheckBox.isChecked=false
            }

        }
    }




    fun setOnClickListner() {


        binding.back.setOnClickListener { finish() }

        binding.back1.setOnClickListener { finish() }

        binding.scrollview.setOnScrollChangeListener { v: NestedScrollView, _, scrollY, _, oldScrollY ->
            val deltaY = scrollY - oldScrollY
            if (kotlin.math.abs(deltaY) > threshold) {
                if (deltaY > 0 && currentHeader != 2) {
                    // Scroll Down → Show Header2
                    binding.header1.visibility = View.VISIBLE
                    binding.header2.visibility = View.GONE
                    currentHeader = 2
                }
                else if (deltaY < 0 && currentHeader != 1) {
                    // Scroll Up → Show Header1
                    binding.header2.visibility = View.VISIBLE
                    binding.header1.visibility = View.GONE
                    currentHeader = 1
                }
                lastScrollY = scrollY
            }

        }

        binding.addAdult.setOnClickListener {
            if(adultList.size<adultcount){
                ClickAdult=true
                ClickChild = false
                ClickInfant  = false
                val bottomfrag = AddTravellerBottomSheet()
                supportFragmentManager.let { bottomfrag.show(it, AddTravellerBottomSheet.TAG)

                }
            }
            else{
                Toast.makeText(this,"You have already selected $adultcount ADULT",Toast.LENGTH_SHORT).show()
            }

        }

        binding.addChild.setOnClickListener {
            if(childList.size<childcount){
                ClickAdult=false
                ClickChild = true
                ClickInfant  = false
                val bottomfrag = AddTravellerBottomSheet()
                supportFragmentManager.let { bottomfrag.show(it, AddTravellerBottomSheet.TAG)

                }
            }
            else{
                Toast.makeText(this,"You have already selected $childcount CHILD",Toast.LENGTH_SHORT).show()
            }

        }

        binding.addInfant.setOnClickListener {
            if(infantList.size<infantcount){
                ClickAdult=false
                ClickChild = false
                ClickInfant  = true
                val bottomfrag = AddTravellerBottomSheet()
                supportFragmentManager.let { bottomfrag.show(it, AddTravellerBottomSheet.TAG)
                }
            }
            else{
                Toast.makeText(this,"You have already selected $infantcount INFANT",Toast.LENGTH_SHORT).show()
            }

        }

        binding.sentmailphonelayout.setOnClickListener {
            val bottomfrag = AddContactAndMobileBottomSheet()
            supportFragmentManager.let {
                bottomfrag.show(it, AddContactAndMobileBottomSheet.TAG)
            }
        }

        binding.gstCheckBox.setOnClickListener {
            val bottomfrag = AddGSTInformationBottomSheet()
            supportFragmentManager.let {
                bottomfrag.show(it, AddGSTInformationBottomSheet.TAG)
            }
        }

        binding.editIcon.setOnClickListener {
            if(!companyName.isNullOrBlank() || !registrationNo.isNullOrBlank()){
                CheckBox= true
            }else{
                CheckBox= false
            }
            val bottomfrag = AddGSTInformationBottomSheet()
            supportFragmentManager.let {
                bottomfrag.show(it, AddGSTInformationBottomSheet.TAG)
            }
        }

        binding.priceBreakup.setOnClickListener {
            val bottomfrag = FareBreakUpBottomSheet()
            supportFragmentManager.let {
                bottomfrag.show(it, FareBreakUpBottomSheet.TAG)
            }
        }


    }



}