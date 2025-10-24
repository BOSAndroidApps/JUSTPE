package com.bos.payment.appName.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.justpaymodel.MoneyTransferServicesModel
import com.bos.payment.appName.databinding.MoneytransferServicesLayoutBinding
import com.bos.payment.appName.databinding.SeemoreLayoutBinding
import com.bos.payment.appName.databinding.ShowingDetailsDthinfoBinding
import com.bos.payment.appName.ui.adapter.DTHViewInfoAdapter.ViewHolder
import com.bos.payment.appName.ui.view.CreditCardDetailsFragment
import com.bos.payment.appName.ui.view.Dashboard.Wallet.Fragment.RechargeFragment
import com.bos.payment.appName.ui.view.Dashboard.activity.AllServicesSelectionActivity
import com.bos.payment.appName.ui.view.Dashboard.dmt.DMTMobileActivity
import com.bos.payment.appName.ui.view.travel.busactivity.BookingTravel
import com.bos.payment.appName.ui.view.travel.flightBooking.activity.FlightMainActivity

class MoneyTransferServicesAdapter( var servicesList:List<MoneyTransferServicesModel>, var context: Context,private val onServiceClick: (MoneyTransferServicesModel) -> Unit,
                                    private val onSeeMoreClick: () -> Unit /*private val activity: AppCompatActivity*/) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val TYPE_ITEM = 0
    private val TYPE_SEE_MORE = 1

    inner  class ServiceViewHolder (var binding : MoneytransferServicesLayoutBinding) : RecyclerView.ViewHolder(binding.root)


    inner  class SeeMoreViewHolder (var binding : SeemoreLayoutBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val view = MoneytransferServicesLayoutBinding.inflate(LayoutInflater.from(context), parent,false)
            ServiceViewHolder(view)
        }
        else {
            val view = SeemoreLayoutBinding.inflate(LayoutInflater.from(context), parent,false)
            SeeMoreViewHolder(view)
        }

       /* val bin = MoneytransferServicesLayoutBinding.inflate(LayoutInflater.from(context), parent,false)
        return ViewHolder(bin)*/
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

       if(holder is ServiceViewHolder){
           val model = servicesList[position]
           holder.binding.servicesName.text= servicesList[position].name
           holder.binding.servicesImage.setImageResource(servicesList[position].image)

           holder.itemView.setOnClickListener {

               onServiceClick(model)

/*               if(servicesList[position].name.equals(context.getString(R.string.flight)))
               {
                   context.startActivity(Intent(context, FlightMainActivity::class.java))
               }

               if(servicesList[position].name.equals(context.getString(R.string.bus))){
                   context.startActivity(Intent(context, BookingTravel::class.java))
               }

               if(servicesList[position].name.equals(context.getString(R.string.recharge))){
                   if (activity is AllServicesSelectionActivity) {
                       activity.callFragment(RechargeFragment(), "mobile")
                   }
               }

               if(servicesList[position].name.equals(context.getString(R.string.postpaid))){
                   if (activity is AllServicesSelectionActivity) {
                       activity.callFragment(RechargeFragment(), "postpaid")
                   }
               }

               if(servicesList[position].name.equals(context.getString(R.string.dth))){
                   if (activity is AllServicesSelectionActivity) {
                       activity.callFragment(RechargeFragment(), "dth")
                   }
               }

               if(servicesList[position].name.equals(context.getString(R.string.electricity))){
                   if (activity is AllServicesSelectionActivity) {
                       activity.callFragment(RechargeFragment(), "Electricity")
                   }
               }

               if(servicesList[position].name.equals(context.getString(R.string.gas))){
                   if (activity is AllServicesSelectionActivity) {
                       activity.callFragment(RechargeFragment(), "Gas")
                   }
               }

               if(servicesList[position].name.equals(context.getString(R.string.waterbill))){
                   if (activity is AllServicesSelectionActivity) {
                       activity. callFragment(RechargeFragment(), "Water")
                   }
               }

               if(servicesList[position].name.equals(context.getString(R.string.broadband))){
                   if (activity is AllServicesSelectionActivity) {
                       activity.callFragment(RechargeFragment(), "Broadband")
                   }
               }

               if(servicesList[position].name.equals(context.getString(R.string.emi))){
                   if (activity is AllServicesSelectionActivity) {
                       activity.callFragment(RechargeFragment(), "EMI")
                   }
               }

               if(servicesList[position].name.equals(context.getString(R.string.creditcard))){
                   if (activity is AllServicesSelectionActivity) {
                       activity. callFragment(CreditCardDetailsFragment(), "CreditCard")
                   }
               }

               if(servicesList[position].name.equals(context.getString(R.string.muncipal))){
                   if (activity is AllServicesSelectionActivity) {
                       activity.callFragment(RechargeFragment(), "Municipality")
                   }
               }


               if(servicesList[position].name.equals(context.getString(R.string.dmt)))
               {
                   context.startActivity(Intent(context, DMTMobileActivity::class.java))
               }

               val position = holder.adapterPosition
               if (position == RecyclerView.NO_POSITION) return@setOnClickListener
               val recyclerView = (holder.itemView.parent as RecyclerView)
               val layoutManager = recyclerView.layoutManager as LinearLayoutManager

               val lastVisiblePos = layoutManager.findLastVisibleItemPosition()-1
               if (position == lastVisiblePos) {
                   val nextPos = position + 1
                   if (nextPos < itemCount) {
                       recyclerView.smoothScrollToPosition(nextPos)
                   }
               }*/

           }
       }
        else if (holder is SeeMoreViewHolder){
           holder.binding.root.setOnClickListener { onSeeMoreClick() }
       }


    }


    override fun getItemCount(): Int {
      return  servicesList.size
    }


    override fun getItemViewType(position: Int): Int {
        return if (servicesList[position].featurecode == "SEE_MORE") TYPE_SEE_MORE else TYPE_ITEM
    }


}