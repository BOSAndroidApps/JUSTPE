package com.bos.payment.appName.ui.view.travel.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.travel.bus.searchBus.Buses
import com.bos.payment.appName.databinding.BusSearchItemBinding
import com.bos.payment.appName.databinding.PassangerdetailsItemlayoutBinding
import com.bos.payment.appName.databinding.SpecialOfferItemlayoutBinding

class PassangerDetailsListShownAdapter (private val context: Context, var passangerList:MutableList<PassangerDataList>):
    RecyclerView.Adapter<PassangerDetailsListShownAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bin = PassangerdetailsItemlayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(bin)
    }


    override fun getItemCount(): Int {
       return passangerList.size
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val passenger = passangerList[position]

        passenger?.let { pax ->
            holder.genderlayout.visibility = if (!pax.gender.isNullOrBlank()) View.VISIBLE else View.GONE
            holder.doblayout.visibility = if (!pax.dob.isNullOrBlank()) View.VISIBLE else View.GONE
            holder.passportnolayout.visibility = if (!pax.passportno.isNullOrBlank()) View.VISIBLE else View.GONE
            holder.passportissuingcountrylayout.visibility = if (!pax.passportissuecountryname.isNullOrBlank()) View.VISIBLE else View.GONE
            holder.passexpirydatelayout.visibility = if (!pax.passportexpirydate.isNullOrBlank()) View.VISIBLE else View.GONE
            holder.lastnamelayout.visibility = if (!pax.lastName.isNullOrBlank()) View.VISIBLE else View.GONE
            holder.firstnamelayout.visibility = if (!pax.firstName.isNullOrBlank()) View.VISIBLE else View.GONE
        }


       holder.passangertype.text= passangerList[position].passangerType
       holder.firstname.text= passangerList[position].title.plus(" ").plus(passangerList[position].firstName)
       holder.lastname.text= passangerList[position].lastName
       holder.gender.text= passangerList[position].gender
       holder.dobtxt.text= passangerList[position].dob


       holder.passportno.text= passangerList[position].passportno
       holder.passissuecountryname.text= passangerList[position].passportissuecountryname
       holder.passexpirydate.text= passangerList[position].passportexpirydate


    }



    class ViewHolder(bind: PassangerdetailsItemlayoutBinding) : RecyclerView.ViewHolder(bind.root) {
        var passangertype = bind.passangertype
        var firstname = bind.firstnametxt
        var lastname = bind.lastnametxt
        var gender = bind.gendertxt
        var dobtxt = bind.dobtxt

        var passportno = bind.passportno
        var passissuecountryname = bind.passportcountryname
        var passexpirydate = bind.passportexpirydate

        var firstnamelayout = bind.firstnamelayout
        var lastnamelayout = bind.lastnamelayout
        var genderlayout = bind.genderlayout
        var doblayout = bind.doblayout
        var passportnolayout = bind.passportNolayout
        var passportissuingcountrylayout = bind.passportissuingcountrylayout
        var passexpirydatelayout = bind.passportexpirydatelayout



    }



}