package com.bos.payment.appName.ui.view.travel.flightBooking.activity

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.bus.busRequery.TicketDetailsForGenerateTicket
import com.bos.payment.appName.data.model.travel.flight.AirTicketingResponse
import com.bos.payment.appName.data.model.travel.flight.FlightTicketDataModel
import com.bos.payment.appName.data.model.travel.flight.FlightsItem
import com.bos.payment.appName.data.model.travel.flight.PaxDetails
import com.bos.payment.appName.data.model.travel.flight.SegmentsItem
import com.bos.payment.appName.data.model.travel.flight.airReprintresponse.AirPNRDetailsItem
import com.bos.payment.appName.data.model.travel.flight.airReprintresponse.AirReprintRespo
import com.bos.payment.appName.databinding.ActivityFlightBookedTicketBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.JustPeDashboard
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.FlightDetails
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.FlightList
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.GetAirlineLogo
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.calculateTotalFlightDuration
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.calculateTotalFlightDurationTicketPrint
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.convertDurationToReadableFormat
import com.bos.payment.appName.utils.QRCodeGenerator.generateAirTicket
import com.bos.payment.appName.utils.QRCodeGenerator.generateQRPdf
import com.bumptech.glide.Glide



class FlightBookedTicketActivity : AppCompatActivity() {
    lateinit var binding : ActivityFlightBookedTicketBinding
    var airPNRDetails :MutableList<AirPNRDetailsItem?> = mutableListOf()
    var segmentList :MutableList<com.bos.payment.appName.data.model.travel.flight.airReprintresponse.SegmentsItem?> = mutableListOf()
    var paxDetails :MutableList<com.bos.payment.appName.data.model.travel.flight.airReprintresponse.PaxTicketDetailsItem?> = mutableListOf()
    var paxDetailsForPrint:MutableList<PaxDetails> = mutableListOf()
    var FlightTicketDetailsList :MutableList<FlightTicketDataModel?> = mutableListOf()


   companion object{
      lateinit var BookedTicketList: AirReprintRespo
   }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityFlightBookedTicketBinding.inflate(layoutInflater)
        setContentView(binding.root)

        airPNRDetails= BookedTicketList.airPNRDetails!!
        // tomorrow check it .....

        if(airPNRDetails.size>0){
            segmentList.clear()
            paxDetails.clear()

            airPNRDetails[airPNRDetails.size-1]!!.flights!![0].segments?.let { segmentList.addAll(it) }

            airPNRDetails[airPNRDetails.size-1]!!.paxTicketDetails?.let { paxDetails.addAll(it) }
            FlightTicketDetailsList.clear()

            var flightnumber= segmentList!![0]!!.airlineCode.plus(" ").plus(segmentList!![0]!!.flightNumber)
            var origindatetime = ""
            var destinationdatetime = ""
            var duration = ""
            var gender = ""

            if(segmentList.size==1){

                var fromtime= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.departureDateTime).second
                var totime= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.arrivalDateTime).second

                var fromdate= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.departureDateTime).first
                var todate= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.arrivalDateTime).first

                origindatetime = fromtime.plus("|").plus(fromdate)
                destinationdatetime = totime.plus("|").plus(todate)

                duration= convertDurationToReadableFormat(segmentList[segmentList.size-1]!!.duration, addHours = 1)

            }
            else{
                var fromtime= FlightConstant.splitDateTimeTicket(segmentList[0]!!.departureDateTime).second
                var totime= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.arrivalDateTime).second

                var fromdate= FlightConstant.splitDateTimeTicket(segmentList[0]!!.departureDateTime).first
                var todate= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.arrivalDateTime).first

                origindatetime = fromtime.plus("|").plus(fromdate)
                destinationdatetime = totime.plus("|").plus(todate)

                val segments = listOf(mapOf("departure_DateTime" to segmentList[0]!!.departureDateTime, "arrival_DateTime" to segmentList[0]!!.arrivalDateTime), mapOf("departure_DateTime" to segmentList[segmentList.size-1]!!.departureDateTime, "arrival_DateTime" to segmentList[segmentList.size-1]!!.arrivalDateTime))

                duration =calculateTotalFlightDurationTicketPrint(segments)

            }

            paxDetailsForPrint.clear()

           var handbag = airPNRDetails[airPNRDetails.size-1]!!.flights!![0].fares?.get(0)!!.fareDetails!![0].freeBaggage.handBaggage
           var checkinbag =  airPNRDetails[airPNRDetails.size-1]!!.flights!![0].fares?.get(0)!!.fareDetails!![0].freeBaggage.checkInBaggage

            paxDetails.forEach{
                it->
                var paxtype=""
                if(it!!.paxType == "0")
                {
                    paxtype= "Adult"
                }
                if(it.paxType == "1"){
                paxtype= "Child"
                }

                if(it.paxType == "2"){
                    paxtype= "Infant"
                }

                if(it.gender.equals("0")){
                    gender= "Male"
                }
                if(it.gender.equals("1")){
                    gender= "Female"
                }

                paxDetailsForPrint.add(PaxDetails("",it.firstName,paxtype,handbag,checkinbag,gender,it.ticketStatus))
            }


                FlightTicketDetailsList.add(FlightTicketDataModel(BookedTicketList.retailerDetail.retailerCompanyName,BookedTicketList.retailerDetail.retailerAddress,BookedTicketList.retailerDetail.retailerGSTno,BookedTicketList.retailerDetail.retailerEmailId,
                segmentList!![0]!!.airlineName, segmentList!![0]!!.airlineCode, BookedTicketList.airPNRDetails!![0]!!.airlinePNR,BookedTicketList.bookingRefNo, BookedTicketList.airPNRDetails!![0]!!.ticketingDate,flightnumber,FlightConstant.classnameforprint, segmentList!![0]!!.origin,
                segmentList!![0]!!.destination,origindatetime,destinationdatetime,duration, BookedTicketList.airPNRDetails!![0]!!.paxTicketDetails!![0].ticketStatus,BookedTicketList.paXMobile,BookedTicketList.paXEmailId,paxDetailsForPrint,FlightConstant.BaseFare,FlightConstant.TaxAndFees,"",FlightConstant.GrossFare))


        }

        setDataOnUI()

        setonclicklistner()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDataOnUI(){
        binding.flightname.text= segmentList!![0]!!.airlineName
        var airportIcon = GetAirlineLogo(airPNRDetails[0]!!.airlineCode)
        Glide.with(this).load(airportIcon).into(binding.flighticon)
        binding.pnrNo.text= airPNRDetails[0]!!.airlinePNR
        binding.passangerName.text= paxDetails[0]!!.firstName.plus(" ").plus(paxDetails[0]!!.lastName)
        binding.flightnumber.text= segmentList!![0]!!.flightNumber
        binding.classname.text= FlightConstant.classnameforprint

        if(segmentList.size==1){

            val cityName = segmentList[segmentList.size-1]!!.origin.substringBefore(" (").trim()
            val airportCode = segmentList[segmentList.size-1]!!.origin.substringAfter("(").substringBefore(")").trim()

            binding.fromcityname.text= cityName
            binding.fromairportcode.text= airportCode

            val destinationcityName = segmentList[segmentList.size-1]!!.destination.substringBefore(" (").trim()
            val destinationairportCode = segmentList[segmentList.size-1]!!.destination.substringAfter("(").substringBefore(")").trim()

            binding.tocityname.text= destinationcityName
            binding.toairportcode.text= destinationairportCode


            binding.fromtime.text= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.departureDateTime).second
            binding.totime.text= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.arrivalDateTime).second

            binding.fromdate.text= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.departureDateTime).first
            binding.todate.text= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.arrivalDateTime).first

            binding.originterminal.text= segmentList[segmentList.size-1]!!.originTerminal
            binding.arrivalterminal.text= segmentList[segmentList.size-1]!!.destinationTerminal

        }
        else{
            val cityName = segmentList[0]!!.origin.substringBefore(" (").trim()
            val airportCode = segmentList[0]!!.origin.substringAfter("(").substringBefore(")").trim()

            binding.fromcityname.text= cityName
            binding.fromairportcode.text= airportCode

            val destinationcityName = segmentList[segmentList.size-1]!!.destination.substringBefore(" (").trim()
            val destinationairportCode = segmentList[segmentList.size-1]!!.destination.substringAfter("(").substringBefore(")").trim()

            binding.tocityname.text= destinationcityName
            binding.toairportcode.text= destinationairportCode

            binding.fromtime.text= FlightConstant.splitDateTimeTicket(segmentList[0]!!.departureDateTime).second
            binding.totime.text= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.arrivalDateTime).second


            binding.originterminal.text= segmentList[0]!!.originTerminal
            binding.arrivalterminal.text= segmentList[segmentList.size-1]!!.destinationTerminal

            binding.fromdate.text= FlightConstant.splitDateTimeTicket(segmentList[0]!!.departureDateTime).first
            binding.todate.text= FlightConstant.splitDateTimeTicket(segmentList[segmentList.size-1]!!.arrivalDateTime).first

        }

    }

    fun setonclicklistner(){

        binding.downloadticket.setOnClickListener {
            saveTicketInPdf()
        }

        binding.back1.setOnClickListener {
            startActivity(Intent(this@FlightBookedTicketActivity,JustPeDashboard::class.java))
            finish()
        }

    }



    fun saveTicketInPdf(){

        generateAirTicket(this,FlightTicketDetailsList){it.second.let {
                Toast.makeText(this, "PDF saved to Downloads", Toast.LENGTH_SHORT).show()
        }

        }

    }

}