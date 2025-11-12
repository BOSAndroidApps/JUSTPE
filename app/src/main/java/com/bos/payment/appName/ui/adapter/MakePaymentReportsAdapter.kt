package com.bos.payment.appName.ui.adapter

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.makepaymentnew.DataItem
import com.bos.payment.appName.data.model.supportmanagement.TicketsItem
import com.bos.payment.appName.databinding.ImagelayoutforraiseticketBinding
import com.bos.payment.appName.databinding.MakepaymentreportBinding
import com.bos.payment.appName.databinding.TicketstatusLayoutBinding
import com.bos.payment.appName.ui.view.supportmanagement.ChatToAdminActivity
import com.bos.payment.appName.ui.view.supportmanagement.ChatToAdminActivity.Companion.checkForSendChat
import com.bos.payment.appName.ui.view.supportmanagement.ChatToAdminActivity.Companion.commentId
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.Constants.downloadImageFromUrl
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class MakePaymentReportsAdapter(var context: Context, var makepaymentreportlist: MutableList<DataItem?>?) :
    RecyclerView.Adapter<MakePaymentReportsAdapter.ViewHolder>() {


    class ViewHolder(var binding: MakepaymentreportBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MakePaymentReportsAdapter.ViewHolder {
        val binding = MakepaymentreportBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MakePaymentReportsAdapter.ViewHolder, position: Int) {

        if (makepaymentreportlist?.get(position)!!.apporvedStatus!!.toLowerCase().equals("approved")) {
            holder.binding.statustxt.setTextColor(ContextCompat.getColor(context, R.color.green))
        }

        if (makepaymentreportlist?.get(position)!!.apporvedStatus!!.toLowerCase().equals("pending")) {
            holder.binding.statustxt.setTextColor(ContextCompat.getColor(context, R.color.orange))

        }

        if (makepaymentreportlist?.get(position)!!.apporvedStatus!!.toLowerCase().equals("rejected")) {
            holder.binding.statustxt.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        if(makepaymentreportlist?.get(position)!!.paymentMode!!.toLowerCase().equals("cash deposit")){
            holder.binding.txnidlayout.visibility=View.GONE
        }else{
            holder.binding.txnidlayout.visibility=View.VISIBLE
        }
        holder.binding.statustxt.text = makepaymentreportlist?.get(position)!!.apporvedStatus
        holder.binding.referenceID.text = makepaymentreportlist?.get(position)!!.refrenceID
        holder.binding.transactionid.text = makepaymentreportlist?.get(position)!!.transactionID
        holder.binding.paymentmode.text = makepaymentreportlist?.get(position)!!.paymentMode
        val amount = makepaymentreportlist?.get(position)?.amount ?: 0.0
        holder.binding.amount.text= "₹ ${String.format("%.2f", amount)}"


        val dateTime = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.parse(makepaymentreportlist?.get(position)!!.paymentDate)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        val dateOnly = dateTime.toLocalDate().toString()

        holder.binding.datetime.text = "${dateOnly}"

    }

    override fun getItemCount(): Int {
        return makepaymentreportlist!!.size
    }





}