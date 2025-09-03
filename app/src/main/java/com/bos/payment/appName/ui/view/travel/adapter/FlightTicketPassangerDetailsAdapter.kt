package com.bos.payment.appName.ui.view.travel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.data.model.travel.flight.FlightTicketDataModel
import com.bos.payment.appName.data.model.travel.flight.PaxDetails
import com.bos.payment.appName.databinding.AirPassangerticketDetailsBinding
import com.bos.payment.appName.databinding.AirlineslistitemlayotBinding
import com.bos.payment.appName.ui.view.travel.adapter.AirPortListAdapter.Companion
import com.bos.payment.appName.ui.view.travel.adapter.AirPortListAdapter.setClickListner


class FlightTicketPassangerDetailsAdapter(private val context: Context ?, private val paxDetailsList: MutableList<PaxDetails> = mutableListOf()) : RecyclerView.Adapter<FlightTicketPassangerDetailsAdapter.CustomViewHolder>() {



    inner class CustomViewHolder(val bin: AirPassangerticketDetailsBinding) : RecyclerView.ViewHolder(bin.root) {
        var barcode = bin.barcode
        var pnrNo = bin.pnrno
        var passangerName = bin.passangerName
        var passanfertype = bin.passangertype
        var cabinweight = bin.cabinbagweight
        var checkinweight = bin.checkinbagweight
        var gender = bin.gender
        var statuspassangerticket = bin.statusp

    }



    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.pnrNo.text = paxDetailsList[position].ticketNo
        holder.passangerName.text = paxDetailsList[position].passangerName
        holder.passanfertype.text = paxDetailsList[position].passangerType
        holder.cabinweight.text = paxDetailsList[position].cabinweight
        holder.checkinweight.text = paxDetailsList[position].checkinweight
        holder.gender.text= paxDetailsList[position].gender
        holder.statuspassangerticket.text = paxDetailsList[position].status

    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val bin = AirPassangerticketDetailsBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(bin)
    }



    override fun getItemCount(): Int {
        return paxDetailsList.size
    }


}