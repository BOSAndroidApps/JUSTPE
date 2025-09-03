package com.bos.payment.appName.ui.view.travel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.bus.history.BusAvailableBookings
import com.bos.payment.appName.databinding.BookingHistoryItemBinding

class BusTicketBookingHistoryAdapter(private val context: Context, private val bookingList: ArrayList<BusAvailableBookings>):

    RecyclerView.Adapter<BusTicketBookingHistoryAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(val bin: BookingHistoryItemBinding):
        RecyclerView.ViewHolder(bin.root){
        val bookingReference: TextView = itemView.findViewById(R.id.referenceNumber)
        val pnrNumber: TextView = itemView.findViewById(R.id.transport_PNR)
        val operatorName: TextView = itemView.findViewById(R.id.busOperatorName)
        val fromCity: TextView = itemView.findViewById(R.id.fromCity)
        val toCity: TextView = itemView.findViewById(R.id.toCity)
        val passengerName: TextView = itemView.findViewById(R.id.passengerName)
        val mobileNo: TextView = itemView.findViewById(R.id.mobileNo)
        val ticketDate: TextView = itemView.findViewById(R.id.ticketingDate)
        val totalAmount: TextView = itemView.findViewById(R.id.totalAmount)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val bin = BookingHistoryItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(bin)
    }



    override fun getItemCount(): Int {
        return bookingList.size
    }


    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val model = bookingList[position]

        holder.bookingReference.text = model.bookingRefNo.toString()
        holder.pnrNumber.text = model.transportPNR.toString()
        holder.operatorName.text = model.operatorName.toString()
        holder.fromCity.text = model.fromCity.toString()
        holder.toCity.text = model.toCity.toString()
        holder.passengerName.text = model.paXName.toString()
        holder.mobileNo.text = model.customerMobile.toString()
        holder.ticketDate.text = model.ticketingDate.toString()
        holder.totalAmount.text = "₹ " +model.totalAmount.toString()
    }


}