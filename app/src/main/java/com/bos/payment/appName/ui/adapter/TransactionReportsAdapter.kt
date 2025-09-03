package com.bos.payment.appName.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.data.model.serviceWiseTrans.Data
import com.bos.payment.appName.databinding.ServiceWiseTransactionItemBinding

class TransactionReportsAdapter(
    var context: Context,
    var transactionList: List<Data>
) : RecyclerView.Adapter<TransactionReportsAdapter.ViewHolder>() {


    inner class ViewHolder(var bin: ServiceWiseTransactionItemBinding) :
        RecyclerView.ViewHolder(bin.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val bin = ServiceWiseTransactionItemBinding.inflate(LayoutInflater.from(context), parent,false)
        return ViewHolder(bin)
    }

    override fun getItemCount(): Int {
        Log.d("transactionList", transactionList.size.toString())
        return transactionList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bin.serialNo.text = transactionList[position].sNo .toString()
        holder.bin.transactionNoText.text = transactionList[position].transactionno .toString()
        holder.bin.upiRefIdText.text = transactionList[position].upiRefID .toString()
        holder.bin.dateText.text = transactionList[position].date .toString()
//        holder.bin.timeText.text = transactionList[position].TIME .toString()
        holder.bin.transferFromText.text = transactionList[position].transferfrom .toString()
        holder.bin.transferToText.text = transactionList[position].transferto .toString()
        holder.bin.serviceTypeText.text = transactionList[position].servicETYPE .toString()
        holder.bin.crText.text = transactionList[position].cr .toString()
        holder.bin.drText.text = transactionList[position].dr .toString()
        holder.bin.balanceText.text = transactionList[position].tranAmt .toString()
        holder.bin.remarksText.text = transactionList[position].remarks .toString()

//        Log.d("transactionList",transactionList[1].TRANSFERFROM .toString())
    }
}

