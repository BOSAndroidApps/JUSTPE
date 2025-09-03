package com.bos.payment.appName.ui.view.travel.airfragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bos.payment.appName.data.model.travel.bus.busTicket.BusPassengerDetailsReq
import com.bos.payment.appName.data.model.travel.bus.busTicket.DataItem
import com.bos.payment.appName.data.model.travel.bus.busTicket.PaXDetailsItem
import com.bos.payment.appName.data.model.travel.flight.airbookingticketList.AirTicketListResp
import com.bos.payment.appName.data.repository.TravelRepository
import com.bos.payment.appName.data.viewModelFactory.TravelViewModelFactory
import com.bos.payment.appName.databinding.FragmentCancelledFlightBinding
import com.bos.payment.appName.databinding.FragmentCancelledRefundBusBinding
import com.bos.payment.appName.network.RetrofitClient
import com.bos.payment.appName.ui.view.travel.adapter.AirTicketCancelAdapter
import com.bos.payment.appName.ui.view.travel.adapter.AirTicketUpcomingAdapter
import com.bos.payment.appName.ui.view.travel.adapter.BusTicketCancelledAdapter
import com.bos.payment.appName.ui.view.travel.airfragment.UpcomingAir.Companion.AirBookingTicketList
import com.bos.payment.appName.ui.view.travel.busactivity.BusTicketConsListClass
import com.bos.payment.appName.ui.viewmodel.TravelViewModel
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.MStash
import com.bos.payment.appName.utils.Utils.PD
import com.google.gson.Gson


class CancelledRefundFlight : Fragment() {
    lateinit var binding: FragmentCancelledFlightBinding
    private var mStash: MStash? = null
    private lateinit var viewModel: TravelViewModel
    lateinit var upcominadapter : AirTicketCancelAdapter


    companion object{
        var AirCancelTicketList : MutableList<com.bos.payment.appName.data.model.travel.flight.airbookingticketList.DataItem> = mutableListOf()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentCancelledFlightBinding.inflate(inflater,container,false)

        viewModel = ViewModelProvider(this, TravelViewModelFactory(TravelRepository(RetrofitClient.apiAllTravelAPI, RetrofitClient.apiBusAddRequestlAPI)))[TravelViewModel::class.java]
        mStash = MStash.getInstance(requireContext())
        setDataInList()

        return binding.root
    }


    fun setDataInList(){
        if(AirCancelTicketList !=null && AirCancelTicketList.size>0){
            binding.notfounddatalayout.visibility = View.GONE
            binding.showtickets.visibility = View.VISIBLE
            upcominadapter = AirTicketCancelAdapter(requireContext(), AirCancelTicketList,this)
            binding.showtickets.adapter = upcominadapter
            upcominadapter.notifyDataSetChanged()
        }else{
            binding.notfounddatalayout.visibility = View.VISIBLE
            binding.showtickets.visibility = View.GONE
        }

    }



}