package com.bos.payment.appName.ui.view.travel.flightBooking.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.flight.FlightsItem
import com.bos.payment.appName.data.model.travel.flight.SegmentsItem
import com.bos.payment.appName.databinding.AddtravellersitemlayoutBinding
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
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.adultList
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.childList
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.flightDetailsPassangerDetail
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.infantList
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.AddDetailsPassangerActivity.Companion.segmentListPassangerDetail
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.FlightMainActivity
import com.bos.payment.appName.utils.Utils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar
import java.util.Locale

class AddTravellerBottomSheet:BottomSheetDialogFragment() {
    private lateinit var binding : AddtravellersitemlayoutBinding
    val titles = listOf("MR", "MRS", "MS")
    var gender :String=""
    var title : String=""
    var dob: String=""
    var firstname:String=""
    var lastname:String=""

    private val myCalender = Calendar.getInstance()
    val calendar = Calendar.getInstance()

    private val date = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        myCalender.set(Calendar.YEAR, year)
        myCalender.set(Calendar.MONTH, monthOfYear)
        myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        val format = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        val selectedDate = format.format(myCalender.time)

        binding.dob.setText(selectedDate)
    }


    companion object {
        const val TAG = "AddTravellerBottomSheet"
        var ClickAdult: Boolean = false
        var ClickChild: Boolean = false
        var ClickInfant: Boolean = false
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = AddtravellersitemlayoutBinding.inflate(inflater, container, false)


        setonclicklistner()
        setDataOnView()
        clickforhoverbutton()

        return binding.root
    }


    private fun setDataOnView(){

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, titles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.titleSpinner.adapter = adapter


    }


    private fun setonclicklistner(){

        binding.dobcalander.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }


        binding.cross.setOnClickListener {
            dialog!!.dismiss()
        }


        binding.titleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedTitle = titles[position]
                title = selectedTitle

            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }



        binding.malecard.setOnClickListener {
            clickforhoverbutton()
            binding.malecard.setCardBackgroundColor(context?.resources!!.getColor(R.color.teal_700))
            binding.maletxt.setTextColor(context?.resources!!.getColor(R.color.white))
            gender="Male"
        }



        binding.femalecard.setOnClickListener {
            clickforhoverbutton()
            binding.femalecard.setCardBackgroundColor(context?.resources!!.getColor(R.color.teal_700))
            binding.femaletxt.setTextColor(context?.resources!!.getColor(R.color.white))
            gender="Female"

        }



        binding.confirmbutton.setOnClickListener {
            firstname= binding.firstname.text.toString()
            lastname= binding.lastname.text.toString()
            dob= binding.dob.text.toString()

          if(gender.isNullOrBlank() || title.isNullOrBlank()|| firstname.isNullOrBlank() || lastname.isNullOrBlank() || dob.isNullOrBlank()){
             Toast.makeText(context,"Please enter all field",Toast.LENGTH_SHORT).show()
          }
          else{
              val newPassenger = PassangerDataList(title, firstname, lastname, dob, gender)

              if(ClickAdult){
                  if (adultList.isEmpty() || !adultList.contains(newPassenger)) {
                      adultList.add(newPassenger)
                      (context as AddDetailsPassangerActivity ).setAdultData()
                  }
              }
              if(ClickChild){
                  if (childList.isEmpty() || !childList.contains(newPassenger)) {
                      childList.add(newPassenger)
                      (context as AddDetailsPassangerActivity ).setChildData()
                  }
              }

              if(ClickInfant){
                  if (infantList.isEmpty() || !infantList.contains(newPassenger)) {
                      infantList.add(newPassenger)
                      (context as AddDetailsPassangerActivity ).setInfantData()
                  }
              }


              dismiss()
          }


        }




    }


    fun clickforhoverbutton()
    {
        binding.malecard.setCardBackgroundColor(context?.resources!!.getColor(R.color.white))
        binding.femalecard.setCardBackgroundColor(context?.resources!!.getColor(R.color.white))
        binding.maletxt.setTextColor(context?.resources!!.getColor(R.color.teal_700))
        binding.femaletxt.setTextColor(context?.resources!!.getColor(R.color.teal_700))
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