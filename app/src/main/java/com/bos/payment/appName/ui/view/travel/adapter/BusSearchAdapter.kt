package com.bos.payment.appName.ui.view.travel.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.bus.searchBus.Buses
import com.bos.payment.appName.databinding.BusSearchItemBinding
import com.bos.payment.appName.ui.view.travel.busactivity.BusSeating
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.Utils

class BusSearchAdapter(private val context: Context, private val busList: ArrayList<Buses>) : RecyclerView.Adapter<BusSearchAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(val bin: BusSearchItemBinding): RecyclerView.ViewHolder(bin.root) {
        val companyName: TextView = itemView.findViewById(R.id.companyName)
        val busName: TextView = itemView.findViewById(R.id.busName)
        val arrivalTime: TextView = itemView.findViewById(R.id.arrivalTime)
        val availableSeat: TextView = itemView.findViewById(R.id.availableSeat)
        val totalTime: TextView = itemView.findViewById(R.id.totalTime)
        val amount: TextView = itemView.findViewById(R.id.travelAmount)
        var boardingId: String?= null
        var droppingId: String?= null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val bin = BusSearchItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return CustomViewHolder(bin)
    }

    override fun getItemCount(): Int {
        return busList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val model = busList[position]

        holder.companyName.text = model.operatorName.toString()
        holder.busName.text = model.busType.toString()
        holder.availableSeat.text = model.availableSeats.toString() + " Seat Left"
        holder.arrivalTime.text = model.departureTime.toString() + " - " + model.arrivalTime.toString()

        val calculateTotalTime = Utils.calculateTimeDifference(model.departureTime.toString(), model.arrivalTime.toString())
        holder.totalTime.text = calculateTotalTime

        val minAmount = model?.fareMasters
            ?.mapNotNull { it.basicAmount } // Convert to a list of non-null amounts
            ?.minOrNull() // Get the minimum value

        if (minAmount != null) {
            holder.amount.text = "₹$minAmount"
        } else {
            holder.amount.text = "₹0"
        }

        model.boardingDetails.forEach { boardingDetails ->
            holder.boardingId = boardingDetails.boardingId.toString()
        }

        model.droppingDetails.forEach { droppingId ->
            holder.droppingId = droppingId.droppingId.toString()
        }

        holder.itemView.setOnClickListener {
            try {
            val intent = Intent(context, BusSeating::class.java).apply {
                putExtra(Constants.busKey, model.busKey.toString())
                putExtra(Constants.boarding_Id, holder.boardingId.toString())
                putExtra(Constants.dropping_Id, holder.droppingId.toString())
                putExtra(Constants.travelCompanyName, model.operatorName.toString())
                putExtra(Constants.travelTime, calculateTotalTime)
                putExtra(Constants.busName, model.busType.toString())
                putExtra(Constants.travelAmount, minAmount.toString())
                putExtra(Constants.arrivalTime, model.departureTime.toString() + " - " + model.arrivalTime.toString())
            }
            context.startActivity(intent)
            }
            catch (e: IndexOutOfBoundsException){
                e.printStackTrace()
            }
        }

    }
}