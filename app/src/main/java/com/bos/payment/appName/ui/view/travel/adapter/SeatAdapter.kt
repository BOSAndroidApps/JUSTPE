package com.bos.payment.appName.ui.view.travel.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.bus.busSeatMap.SeatMap

class SeatAdapter(
    private val seatList: List<SeatMap>,
    private val context: Context
) : RecyclerView.Adapter<SeatAdapter.SeatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.seat_item, parent, false)
        return SeatViewHolder(view)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val seat = seatList[position]
        holder.seatButton.text = seat.seatNumber

        // Check if the seat is a "seater" or "sleeper"
        if (seat.length == "1") {
            holder.seatButton.layoutParams = GridLayout.LayoutParams().apply {
                width = 200 // Adjust width for seater seats
                height = 150 // Adjust height for seater seats
            }
            holder.seatButton.setBackgroundColor(ContextCompat.getColor(context, R.color.green))
        } else if (seat.length == "2") {
            holder.seatButton.layoutParams = GridLayout.LayoutParams().apply {
                width = 400 // Adjust width for sleeper seats
                height = 300 // Adjust height for sleeper seats
            }
            holder.seatButton.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
        }

        // Check if the seat is bookable
        if (!seat.bookable!!) {
            holder.seatButton.isEnabled = false
            holder.seatButton.setBackgroundColor(ContextCompat.getColor(context, R.color.grey))
        } else {
            holder.seatButton.setOnClickListener {
                // Handle seat selection here
                Toast.makeText(context, "Seat ${seat.seatNumber} clicked!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount(): Int {
        return seatList.size
    }

    class SeatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val seatButton: Button = view.findViewById(R.id.seatButton)
    }
}
