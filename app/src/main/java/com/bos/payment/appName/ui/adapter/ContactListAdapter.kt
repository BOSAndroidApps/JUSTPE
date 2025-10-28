package com.bos.payment.appName.ui.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.data.model.justpaymodel.RetailerDataItem
import com.bos.payment.appName.databinding.ContactlistItemBinding
import com.bos.payment.appName.databinding.DashboardServicesServicesLayoutBinding
import com.bos.payment.appName.ui.view.Dashboard.tomobile.SendWalletAmountPage
import com.bos.payment.appName.ui.view.Dashboard.tomobile.SendWalletAmountPage.Companion.agencyName
import com.bos.payment.appName.ui.view.Dashboard.tomobile.SendWalletAmountPage.Companion.mobileNumber
import com.bos.payment.appName.ui.view.Dashboard.tomobile.SendWalletAmountPage.Companion.name
import com.bos.payment.appName.ui.view.Dashboard.tomobile.SendWalletAmountPage.Companion.userID

class ContactListAdapter(var context: android.content.Context,var listContact:List<RetailerDataItem?>?):RecyclerView.Adapter<ContactListAdapter.ViewHolder>(){

    val colors = listOf(
        Color.parseColor("#FFCDD2"), // Light Red
        Color.parseColor("#C8E6C9"), // Light Green
        Color.parseColor("#BBDEFB"), // Light Blue
        Color.parseColor("#FFF9C4"), // Light Yellow
        Color.parseColor("#D1C4E9")  // Light Purple
    )


    inner class ViewHolder(val binding: ContactlistItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactListAdapter.ViewHolder {
        val binding = ContactlistItemBinding.inflate(LayoutInflater.from(context), parent, false)
         return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ContactListAdapter.ViewHolder, position: Int) {
        val randomColor = colors.random()
        holder.binding.cards.setCardBackgroundColor(randomColor)
        holder.binding.charactername.text = listContact!![position]!!.name!!.take(2).uppercase()
       holder.binding.agencyname.text= "${listContact!![position]!!.agencyName}"
       holder.binding.name.text= "${listContact!![position]!!.name}"
       holder.binding.contactNo.text= "${listContact!![position]!!.mobileNo}"

       holder.itemView.setOnClickListener {
           agencyName = listContact!![position]!!.agencyName
           name = listContact!![position]!!.name
           mobileNumber = listContact!![position]!!.mobileNo
           userID = listContact!![position]!!.userID
           context.startActivity(Intent(context,SendWalletAmountPage::class.java))
       }
    }


    override fun getItemCount(): Int {
        return  listContact!!.size
    }

    fun updateList(newList: List<RetailerDataItem?>?) {
        listContact = newList
        notifyDataSetChanged()
    }



}