package com.bos.payment.appName.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.recharge.rechargeHistory.RechargeHistoryRes

class TransactionHistoryAdapter(
    var mContext: Context,
//    rechargeType: String,
//    arrayList: List<TransactionHistoryModelList?>,
    var arrayList: List<com.bos.payment.appName.data.model.recharge.rechargeHistory.Data>,
//    clickListener: ClickListener
) :
    RecyclerView.Adapter<TransactionHistoryAdapter.ViewHolder>() {

    //    private var rechargeType: String = rechargeType
//    private var arrayList = arrayList
//    private val mContext: Context = mContext

    //    private val clickListener = clickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transactionhistory, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = arrayList[position]
        holder.tvDate.text = model.date.toString()
        holder.tvMobileNumber.text = model.caNo
//        holder.tvMobileNumber.text = "9863809638"
        holder.amount.text = "₹" + model.amount
        holder.imageText.text = model.operator
//        if (rechargeType == "DTH") {
//            holder.tvRechargeType.text = "CA Number"
//        } else {
//
//        }

//        holder.itemView.setOnClickListener {
//            clickListener.itemClick(model)
//        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvMobileNumber: TextView = itemView.findViewById(R.id.tvMobileNumber)
        val amount: TextView = itemView.findViewById(R.id.amount)
        val imageText: TextView = itemView.findViewById(R.id.imageText)
//        val tvRechargeLogo: TextView = itemView.findViewById(R.id.operatorLogo)
    }

    interface ClickListener {
        fun itemClick(model: RechargeHistoryRes)
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }
}