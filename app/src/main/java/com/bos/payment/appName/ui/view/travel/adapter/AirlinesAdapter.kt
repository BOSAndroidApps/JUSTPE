package com.bos.payment.appName.ui.view.travel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.databinding.AirlineslistitemlayotBinding
import com.bos.payment.appName.ui.view.travel.adapter.AirPortListAdapter.Companion
import com.bos.payment.appName.ui.view.travel.adapter.AirPortListAdapter.setClickListner


class AirlinesAdapter(private val context: Context, private val FlightList: MutableList<Pair<String,Boolean>> = mutableListOf(), val btnlistener: OnClickListner) : RecyclerView.Adapter<AirlinesAdapter.CustomViewHolder>() {


    companion object {
        var mClickListener: OnClickListner? = null
        var check: Boolean = false
        var selectedPosition = -1
    }



    inner class CustomViewHolder(val bin: AirlineslistitemlayotBinding) :
        RecyclerView.ViewHolder(bin.root) {
        var check = bin.airlinecheck
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val bin = AirlineslistitemlayotBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(bin)
    }



    override fun getItemCount(): Int {
        return FlightList.size
    }



    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        mClickListener = btnlistener
        holder.check.text = FlightList[position].first
        holder.check.setOnCheckedChangeListener(null)

        holder.check.isChecked = FlightList[position].second
        val selectedAirlines = FlightList.filter { it.second }.map { it.first }
        mClickListener?.setonclicklistner(selectedAirlines)

        holder.check.setOnCheckedChangeListener { _,checked->

            FlightList[position] = FlightList[position].copy(second = checked)

            // Get selected airline names
            val selectedAirlines = FlightList.filter { it.second }.map { it.first }

            // Callback with updated list
            mClickListener?.setonclicklistner(selectedAirlines)

            holder.itemView.post {
                notifyItemChanged(position)
            }

        }

    }




    open interface OnClickListner {
        fun setonclicklistner(AirlineName: List<String>)
    }


}