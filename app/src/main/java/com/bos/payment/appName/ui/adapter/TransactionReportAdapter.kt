package com.bos.payment.appName.ui.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.justpaymodel.RetailerDataItem
import com.bos.payment.appName.data.model.transactionreportsmodel.DataItem
import com.bos.payment.appName.data.model.transactionreportsmodel.TransactionReportsReq
import com.bos.payment.appName.databinding.ContactlistItemBinding
import com.bos.payment.appName.databinding.TransactionReportItemLayoutBinding
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.Companion.RefID
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.Companion.creditamount
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.Companion.date
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.Companion.debitamount
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.Companion.remarks
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.Companion.servicetype
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.Companion.time
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.Companion.transactionNo
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.Companion.transferfrom
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.Companion.transferto
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.TransactionReportsActivity
import com.bos.payment.appName.utils.ApiStatus
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.Utils.runIfConnected
import com.google.gson.Gson

class TransactionReportAdapter(var context: Context, var transactionList: List<DataItem?>) :
    RecyclerView.Adapter<TransactionReportAdapter.ViewHolder>() {


    class ViewHolder(var binding: TransactionReportItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionReportAdapter.ViewHolder {
        val binding = TransactionReportItemLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionReportAdapter.ViewHolder, position: Int) {

        if (transactionList[position]!!.status!!.toLowerCase().equals("pending")) {
            holder.binding.statustxt.setTextColor(ContextCompat.getColor(context, R.color.orange))
        }

        if (transactionList[position]!!.status!!.toLowerCase().equals("approved")) {
            holder.binding.statustxt.setTextColor(ContextCompat.getColor(context, R.color.green))
        }

        if (transactionList[position]!!.status!!.toLowerCase().equals("rejected")) {
            holder.binding.statustxt.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        holder.binding.statustxt.text = transactionList[position]!!.status
        holder.binding.transactionno.text = transactionList[position]!!.transactionno
        holder.binding.upiRefID.text = transactionList[position]!!.upiRefID
        holder.binding.servicetype.text = transactionList[position]!!.servicETYPE
        holder.binding.datetime.text = "${transactionList[position]!!.date} , ${transactionList[position]!!.time}"

        if (transactionList[position]!!.cr!!.isNotEmpty()) {
            holder.binding.amount.text="+ ₹${transactionList[position]!!.cr}"
            holder.binding.amount.setTextColor(ContextCompat.getColor(context, R.color.green))
        }

        if (transactionList[position]!!.dr!!.isNotEmpty()) {
            holder.binding.amount.text=" - ₹${transactionList[position]!!.dr}"
            holder.binding.amount.setTextColor(ContextCompat.getColor(context, R.color.black))
        }
        holder.binding.remarks.text = transactionList[position]!!.remarks

        holder.binding.raiseticketcard.setOnClickListener {
            servicetype = transactionList[position]!!.servicETYPE!!
            transactionNo = transactionList[position]!!.transactionno!!
            RefID = transactionList[position]!!.upiRefID!!
            date = transactionList[position]!!.date!!
            time = transactionList[position]!!.time!!
            transferfrom = transactionList[position]!!.transferfrom!!
            transferto = transactionList[position]!!.transferto!!
            creditamount = transactionList[position]!!.cr!!
            debitamount = transactionList[position]!!.dr!!
            remarks = transactionList[position]!!.remarks!!
            var activity = context as TransactionReportsActivity
            if(activity!=null){
                activity.hitApiForCheckRaiseTicket(transactionList[position]!!.transactionno!!)
            }

        }
    }


    override fun getItemCount(): Int {
        return transactionList!!.size
    }


}