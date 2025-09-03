package com.bos.payment.appName.ui.view.travel.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R

class AddPassengerAdapter (private val passengerList: List<PassangerDataList>) :
    RecyclerView.Adapter<AddPassengerAdapter.PassengerViewHolder>() {

    inner class PassengerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitleName: TextView = itemView.findViewById(R.id.tvTitleName)
        val tvGender: TextView = itemView.findViewById(R.id.tvGender)
        val tvDob: TextView = itemView.findViewById(R.id.tvDob)

        val passportno: TextView = itemView.findViewById(R.id.psno)
        val passissuecountry: TextView = itemView.findViewById(R.id.pass_country_name)
        val passexpirydate: TextView = itemView.findViewById(R.id.pass_expiry_date)
        
        // all layout 

        val genderLayout: LinearLayout = itemView.findViewById(R.id.genderlayout)
        val dobLayoutLayout: LinearLayout = itemView.findViewById(R.id.doblayout)
        val passportNoLayoutLayout: LinearLayout = itemView.findViewById(R.id.passportlayout)
        val passportCountryNameLayout: LinearLayout = itemView.findViewById(R.id.passportissuingcountrylayout)
        val passportExpiryDateLayout: LinearLayout = itemView.findViewById(R.id.passportexpirydatelayout)
       

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.addtravelleritemlayout, parent, false)
        return PassengerViewHolder(view)
    }


    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {
        val passenger = passengerList[position]

        passenger?.let { pax ->
            holder.genderLayout.visibility = if (!pax.gender.isNullOrBlank()) View.VISIBLE else View.GONE
            holder.dobLayoutLayout.visibility = if (!pax.dob.isNullOrBlank()) View.VISIBLE else View.GONE
            holder.passportNoLayoutLayout.visibility = if (!pax.passportno.isNullOrBlank()) View.VISIBLE else View.GONE
            holder.passportCountryNameLayout.visibility = if (!pax.passportissuecountryname.isNullOrBlank()) View.VISIBLE else View.GONE
            holder.passportExpiryDateLayout.visibility = if (!pax.passportexpirydate.isNullOrBlank()) View.VISIBLE else View.GONE
        }

        holder.tvTitleName.text = "${passenger.title} ${passenger.firstName} ${passenger.lastName}"
        holder.tvGender.text = passenger.gender
        holder.tvDob.text = passenger.dob
        holder.passportno.text = passenger.passportno
        holder.passissuecountry.text = passenger.passportissuecountryname
        holder.passexpirydate.text = passenger.passportexpirydate
    }


    override fun getItemCount(): Int = passengerList.size


}