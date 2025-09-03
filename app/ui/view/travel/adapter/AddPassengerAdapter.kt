package com.bos.payment.appName.ui.view.travel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R

class AddPassengerAdapter (private val passengerList: List<PassangerDataList>) :
    RecyclerView.Adapter<AddPassengerAdapter.PassengerViewHolder>() {

    inner class PassengerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitleName: TextView = itemView.findViewById(R.id.tvTitleName)
        val tvGender: TextView = itemView.findViewById(R.id.tvGender)
        val tvDob: TextView = itemView.findViewById(R.id.tvDob)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.addtravelleritemlayout, parent, false)
        return PassengerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {
        val passenger = passengerList[position]
        holder.tvTitleName.text = "${passenger.title} ${passenger.firstName} ${passenger.lastName}"
        holder.tvGender.text = passenger.gender
        holder.tvDob.text = passenger.dob
    }

    override fun getItemCount(): Int = passengerList.size

}