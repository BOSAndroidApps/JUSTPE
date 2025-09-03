package com.bos.payment.appName.ui.view.travel.flightBooking.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.flight.FlightsItem
import com.bos.payment.appName.data.model.travel.flight.TripDetailsItem
import com.bos.payment.appName.databinding.ActivityFlightDetailListBinding
import com.bos.payment.appName.databinding.ActivityFlightSearchBinding
import com.bos.payment.appName.ui.view.travel.adapter.FlightDetailsAdapter
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.TripDetailsList

class FlightDetailListActivity : AppCompatActivity() {

   lateinit  var binding: ActivityFlightDetailListBinding
   var FlightList: MutableList<FlightsItem> = mutableListOf()
   lateinit var adapter: FlightDetailsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityFlightDetailListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(TripDetailsList.size>0){
            FlightList.clear()
            TripDetailsList[0]!!.flights?.let { FlightList.addAll(it) }
            Log.d("FlightList","List:"+FlightList)
        }

        setdataonView()
        setClickListner()

    }

    fun setdataonView(){
        binding.fromtocityname.text=FlightConstant.fromCityName.plus(" to ").plus(FlightConstant.toCityName)
        binding.datepassangerclassname.text= FlightConstant.datepassangerandclassstring

        adapter = FlightDetailsAdapter(this,FlightList)
        binding.flightlistshown.adapter= adapter
        adapter.notifyDataSetChanged()
    }


    fun setClickListner(){
        binding.back.setOnClickListener { finish() }
    }


}