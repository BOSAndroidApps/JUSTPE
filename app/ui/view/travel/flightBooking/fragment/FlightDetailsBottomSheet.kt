package com.bos.payment.appName.ui.view.travel.flightBooking.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.flight.FlightsItem
import com.bos.payment.appName.data.model.travel.flight.SegmentsItem
import com.bos.payment.appName.databinding.FlightdetailsItemBottomsheetBinding
import com.bos.payment.appName.databinding.TravellersclassItemBottomsheetBinding
import com.bos.payment.appName.ui.view.travel.adapter.FlightTicketDetailsAdapter
import com.bos.payment.appName.ui.view.travel.adapter.PassangerDataList
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.FlightDetails
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.adultCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.calculateTotalFlightDuration
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.childCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.className
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.infantCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.totalCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.totalDurationTime
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.flightDetailsPassangerDetail
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.segmentListPassangerDetail
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.FlightMainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class FlightDetailsBottomSheet:BottomSheetDialogFragment() {
    private lateinit var binding : FlightdetailsItemBottomsheetBinding
    lateinit var flightadapter: FlightTicketDetailsAdapter
    var flightDetails :MutableList<FlightsItem?> = mutableListOf()
    var segmentList :MutableList<SegmentsItem?> = mutableListOf()

    companion object {
        const val TAG = "FlightDetailsBottomSheet"

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FlightdetailsItemBottomsheetBinding.inflate(inflater, container, false)

        if(FlightDetails.size>0){
            flightDetails.clear()
            flightDetails.addAll(FlightDetails)
        }

        if(FlightDetails.size>0){
            segmentList.clear()
            flightDetails[flightDetails.size-1]!!.segments?.let { segmentList.addAll(it) }
        }

        setonclicklistner()
        setDataOnView()
        return binding.root
    }


    private fun setDataOnView(){
        flightadapter= FlightTicketDetailsAdapter(requireContext(),flightDetails,segmentList)
        binding.showterminallist.adapter=flightadapter
        binding.price.text= "₹ ".plus(flightDetails[0]!!.fares?.get(0)?.fareDetails?.get(0)?.totalAmount)
        val segments = listOf(mapOf("departure_DateTime" to segmentList[0]!!.departureDateTime, "arrival_DateTime" to segmentList[0]!!.arrivalDateTime), mapOf("departure_DateTime" to segmentList[segmentList.size-1]!!.departureDateTime, "arrival_DateTime" to segmentList[segmentList.size-1]!!.arrivalDateTime))
        totalDurationTime= calculateTotalFlightDuration(segments)

    }


    private fun setonclicklistner(){

        binding.cross.setOnClickListener {
            dialog!!.dismiss()
        }

        binding.booknow.setOnClickListener {
            segmentListPassangerDetail.clear()
            flightDetailsPassangerDetail.clear()
            segmentListPassangerDetail.addAll(segmentList)
            flightDetailsPassangerDetail.addAll(flightDetails)
            startActivity(Intent(requireContext(),AddDetailsPassangerActivity::class.java))
            dismiss()
        }


    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // used to show the bottom sheet dialog
        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                    sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED

                val layoutParams = sheet.layoutParams
                val windowHeight = Resources.getSystem().displayMetrics.heightPixels
                layoutParams.height = windowHeight
                sheet.layoutParams = layoutParams
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }


    private  fun selectionHover(){

    }



    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? FlightMainActivity)?.setData()
    }



}