package com.bos.payment.appName.utils.searchSpinnerWithRecyclerview

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.flight.DataItem
import com.bos.payment.appName.ui.view.travel.adapter.AirPortListAdapter
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.FlightMainActivity


@SuppressLint("ValidFragment")
class SearchableListFlightSpinnerDialog  (var dialogContext : Context, private val items: MutableList<DataItem>, val clicklistner :AirPortListAdapter.setClickListner )  : DialogFragment(){

    private lateinit var adapter: AirPortListAdapter


     override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(dialogContext).inflate(R.layout.searchflightspinneritemlayout, null)

        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        val recyclerView = view.findViewById<RecyclerView>(R.id.airportList)
        val canceltext = view.findViewById<TextView>(R.id.cancel)

        adapter = AirPortListAdapter(dialogContext,items.toMutableList(), object : AirPortListAdapter.setClickListner {
            override fun itemclickListner(position: Int,item :DataItem) {
                clicklistner.itemclickListner(position,item) // callback to outside
                dismiss()
            }
        })

        recyclerView.layoutManager = LinearLayoutManager(dialogContext)
        recyclerView.adapter = adapter


        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val filtered = items.filter {   it.city.contains(s.toString(), ignoreCase = true) ||
                        it.country.contains(s.toString(), ignoreCase = true) ||
                        it.airportCode.contains(s.toString(), ignoreCase = true) }
                adapter.updateList(filtered)
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

         canceltext.setOnClickListener {
             ( dialogContext as FlightMainActivity).setData()
             dismiss() // Dismiss the dialog
         }


         val dialog = AlertDialog.Builder(dialogContext).setView(view).create()

         // Make background transparent
         dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

         return dialog
    }



}