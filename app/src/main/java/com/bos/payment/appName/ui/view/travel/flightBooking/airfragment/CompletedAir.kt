package com.bos.payment.appName.ui.view.travel.airfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.FragmentCompletedAirBinding
import com.bos.payment.appName.databinding.FragmentUpcomingAirBinding
import com.bos.payment.appName.ui.view.travel.adapter.AirTicketCancelAdapter
import com.bos.payment.appName.ui.view.travel.adapter.AirTicketCompletedAdapter
import com.bos.payment.appName.ui.view.travel.airfragment.CancelledRefundFlight.Companion.AirCancelTicketList

class CompletedAir : Fragment() {

      lateinit var binding : FragmentCompletedAirBinding
      lateinit var completeAdapter : AirTicketCompletedAdapter

    companion object {
        var AirCompleteTicketList : MutableList<com.bos.payment.appName.data.model.travel.flight.airbookingticketList.DataItem> = mutableListOf()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding= FragmentCompletedAirBinding.inflate(inflater, container, false)
        setDataInList()
        return binding.root
    }

    fun setDataInList(){
        if(AirCompleteTicketList !=null && AirCompleteTicketList.size>0){
            binding.notfounddatalayout.visibility = View.GONE
            binding.showtickets.visibility = View.VISIBLE
            completeAdapter = AirTicketCompletedAdapter(requireContext(), AirCompleteTicketList,this)
            binding.showtickets.adapter = completeAdapter
            completeAdapter.notifyDataSetChanged()
        }else{
            binding.notfounddatalayout.visibility = View.VISIBLE
            binding.showtickets.visibility = View.GONE
        }

    }


}