package com.bos.payment.appName.ui.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.recharge.mobile.MobileRechargePlanModel
import com.bos.payment.appName.databinding.LayoutForPlanNameBinding
import com.bos.payment.appName.databinding.ShowingDetailsDthinfoBinding


class DTHViewInfoAdapter(var context: android.content.Context, var planName:MutableList<com.bos.payment.appName.data.model.recharge.recharge.DataItem>, private val clickListener: ClickListener) : RecyclerView.Adapter<DTHViewInfoAdapter.ViewHolder>() {

    class ViewHolder (var bin: ShowingDetailsDthinfoBinding) : RecyclerView.ViewHolder(bin.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bin = ShowingDetailsDthinfoBinding.inflate(LayoutInflater.from(context), parent,false)
        return ViewHolder(bin)
    }

    override fun getItemCount(): Int {
        return planName!!.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bin.customerName.text = planName[position].customerName
        holder.bin.status.text = planName[position].status
        holder.bin.rechargeDate.text = planName[position].nextRechargeDate
        holder.bin.packamount.text = "₹" + planName[position].monthlyRecharge
        holder.bin.planname.text = planName[position].planname


        holder.itemView.setOnClickListener {
            clickListener.itemClick(planName[position].monthlyRecharge!!)

        }


    }

    fun interface ClickListener {
        fun itemClick(recharge: Int)
    }

}