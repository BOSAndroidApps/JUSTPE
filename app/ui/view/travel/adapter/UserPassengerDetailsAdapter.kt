package com.bos.payment.appName.ui.view.travel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.bus.busRequery.PaXDetails
import com.bos.payment.appName.databinding.BookingItemBinding
import com.bos.payment.appName.ui.view.travel.model.DateModel

class UserPassengerDetailsAdapter(

    private val context: Context,
    private val dateList: List<PaXDetails>
) : RecyclerView.Adapter<UserPassengerDetailsAdapter.DateViewHolder>() {

    inner class DateViewHolder(val bin: BookingItemBinding) :
        RecyclerView.ViewHolder(bin.root) {
        val name: TextView = itemView.findViewById(R.id.userName)
        val age: TextView = itemView.findViewById(R.id.userAge)
        val seatNumber: TextView = itemView.findViewById(R.id.seatNumber)
        val mobileNo: TextView = itemView.findViewById(R.id.mobileNo)
        val emailId: TextView = itemView.findViewById(R.id.emailId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DateViewHolder {
        val view = BookingItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return DateViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DateViewHolder, position: Int) {
        val dateModel = dateList[position]
        holder.name.text = dateModel.title.toString()+". " + dateModel.paXName.toString()
        holder.age.text = dateModel.age.toString() + " Yrs"
        holder.seatNumber.text = "Seat No: " + dateModel.seatNumber.toString()
//        holder.mobileNo.text = dateModel.mo.toString()
//        holder.seatNumber.text = dateModel.seatNumber.toString()
    }

    override fun getItemCount(): Int = dateList.size
}