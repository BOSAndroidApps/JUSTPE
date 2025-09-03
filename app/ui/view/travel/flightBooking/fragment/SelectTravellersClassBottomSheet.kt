package com.bos.payment.appName.ui.view.travel.flightBooking.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bos.payment.appName.R
import com.bos.payment.appName.databinding.TravellersclassItemBottomsheetBinding
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.adultCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.childCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.className
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.infantCount
import com.bos.payment.appName.ui.view.travel.flightBooking.FlightConstant.Companion.totalCount
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.FlightMainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SelectTravellersClassBottomSheet:BottomSheetDialogFragment() {
    private lateinit var binding : TravellersclassItemBottomsheetBinding

    var adultcount: Int = 1
    var childcount: Int = 0
    var infantcount: Int = 0
    var classname : String = ""
    var classnumber: String = ""


    companion object {
        const val TAG = "SelectTravellersClassBottomSheet"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = TravellersclassItemBottomsheetBinding.inflate(inflater, container, false)
        setonclicklistner()
        setdataontext()
        return binding.root
    }

    private fun setdataontext(){
        binding.numberofadult.text=adultCount.toString().trim()
        binding.numberofchild.text=childCount.toString().trim()
        binding.numberofinfanttxt.text=infantCount.toString().trim()
        totalCount = (adultCount+childCount+infantCount).toString()
        if(className.equals("Economy Class")){
            selectionHover()
            binding.economycard.setCardBackgroundColor(resources.getColor(R.color.teal_300))
            binding.ecotxt.setTextColor(resources.getColor(R.color.white))
            className = "Economy Class"
            classnumber = "0"
        }

        if(className.equals("Premium Economy Class")){
            selectionHover()
            binding.preeconomycard.setCardBackgroundColor(resources.getColor(R.color.teal_300))
            binding.preecotxt.setTextColor(resources.getColor(R.color.white))
            classname = "Premium Economy Class"
            classnumber = "3"
        }

        if(className.equals("Business Class")){
            selectionHover()
            binding.businesscard.setCardBackgroundColor(resources.getColor(R.color.teal_300))
            binding.businesstxt.setTextColor(resources.getColor(R.color.white))
           classname = "Business Class"
           classnumber = "1"
        }

        if(className.equals("First Class")){
            selectionHover()
            binding.firstcard.setCardBackgroundColor(resources.getColor(R.color.teal_300))
            binding.firstclasstxt.setTextColor(resources.getColor(R.color.white))
            classname = "First Class"
            classnumber = "2"
        }
    }

    private fun setonclicklistner(){
        binding.cross.setOnClickListener {
            dialog!!.dismiss()
        }
        binding.economycard.setOnClickListener {
            selectionHover()
            binding.economycard.setCardBackgroundColor(resources.getColor(R.color.teal_300))
            binding.ecotxt.setTextColor(resources.getColor(R.color.white))
            classname = "Economy Class"
            classnumber = "0"

        }

        binding.preeconomycard.setOnClickListener {
            selectionHover()
            binding.preeconomycard.setCardBackgroundColor(resources.getColor(R.color.teal_300))
            binding.preecotxt.setTextColor(resources.getColor(R.color.white))
             classname = "Premium Economy Class"
           classnumber = "3"

        }

        binding.businesscard.setOnClickListener {
            selectionHover()
            binding.businesscard.setCardBackgroundColor(resources.getColor(R.color.teal_300))
            binding.businesstxt.setTextColor(resources.getColor(R.color.white))
           classname = "Business Class"
           classnumber = "1"

        }

        binding.firstcard.setOnClickListener {
            selectionHover()
            binding.firstcard.setCardBackgroundColor(resources.getColor(R.color.teal_300))
            binding.firstclasstxt.setTextColor(resources.getColor(R.color.white))
            classname = "First Class"
            classnumber = "2"

        }


        binding.additionAdult.setOnClickListener {
            adultcount++
            binding.numberofadult.text=adultcount.toString().trim()
        }


        binding.minimizeAdult.setOnClickListener {
            if(adultcount>0 && infantcount!=adultcount) {
                adultcount--
                binding.numberofadult.text = adultcount.toString().trim()
            }else{
                Toast.makeText(requireContext(),"Number of Infants can't  exceed number of adults",Toast.LENGTH_SHORT).show()
            }
        }


        binding.additionChild.setOnClickListener {
            childcount++
            binding.numberofchild.text=childcount.toString().trim()
        }


        binding.minimizeChild.setOnClickListener {
            if(childcount>0) {
                childcount--
                binding.numberofchild.text = childcount.toString().trim()
            }
        }


        binding.additionInfanttxt.setOnClickListener {
            if(infantcount<adultcount){
                infantcount++
                binding.numberofinfanttxt.text=infantcount.toString().trim()
            }
            else{
                Toast.makeText(requireContext(),"Number of Infants can't  exceed number of adults",Toast.LENGTH_SHORT).show()
            }

        }


        binding.minimizeInfanttxt.setOnClickListener {
            if(infantcount>0) {
                infantcount--
                binding.numberofinfanttxt.text = infantcount.toString().trim()
            }
        }

        binding.donelayout.setOnClickListener {
            FlightConstant.className = classname
            FlightConstant.classNumber = classnumber
            adultCount=  adultcount
            childCount= childcount
            infantCount= infantcount
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
        binding.economycard.setCardBackgroundColor(resources.getColor(R.color.white))
        binding.preeconomycard.setCardBackgroundColor(resources.getColor(R.color.white))
        binding.businesscard.setCardBackgroundColor(resources.getColor(R.color.white))
        binding.firstcard.setCardBackgroundColor(resources.getColor(R.color.white))

        binding.businesstxt.setTextColor(resources.getColor(R.color.inputtext_boxstroke))
        binding.preecotxt.setTextColor(resources.getColor(R.color.inputtext_boxstroke))
        binding.ecotxt.setTextColor(resources.getColor(R.color.inputtext_boxstroke))
        binding.firstclasstxt.setTextColor(resources.getColor(R.color.inputtext_boxstroke))
    }



    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? FlightMainActivity)?.setData()
    }



}