//package com.bos.bos.app.adapter
//
//import android.content.Context
//import android.os.Build
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.annotation.RequiresApi
//import androidx.recyclerview.widget.RecyclerView
//import com.bos.bos.app.data.model.dmt.fetchBenificary.Data
//import com.bos.bos.app.R
//import com.bos.bos.app.data.model.dmt.fetchBenificary.FetchBeneficiaryRes
//
//class ReceiptAdapter(
//    private val mContext: Context,
//    private val dataList: ArrayList<Data>,
//    private val clickListener: ClickListener
//) : RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.item_receiptlist, parent, false)
//        return ViewHolder(view)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.M)
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val model = dataList[position]
//        holder.tvRecipientBankName.text = model.bankname
//        holder.tvRecipientId.text = model.bankid
//        holder.tvRecipientVerified.text = model.verified
//        when {
//            position < dataList.size -> {
//                val model = dataList[position]
//                holder.tvRecipientBankName.text = model.bankname
//                holder.tvRecipientId.text = model.bankid
//                holder.tvRecipientVerified.text = model.verified
//                holder.itemView.setOnClickListener {
//                    model.let { it1 -> clickListener.itemClick(it1) }
//                }
//            } else -> {
//
//            }
//        }
//        Log.d("tvRecipientBankName", model.bankid.toString())
//        Log.d("tvRecipientBankName", model.verified.toString())
//        Log.d("tvRecipientBankName", model.bankname.toString())
//
//        holder.itemView.setOnClickListener {
//            clickListener.itemClick(model)
//        }
//        holder.ivTransfer.setOnClickListener {
//            clickListener.transfer(model)
//        }
//        holder.ivDelete.setOnClickListener {
//            clickListener.delete(model)
//        }
//    }
//
//    interface ClickListener{
//        fun itemClick(item: Data)
//
//    }
//
//    override fun getItemCount(): Int {
//        return dataList.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val tvRecipientBankName: TextView = itemView.findViewById(R.id.tvRecipientBankName)
//        val tvRecipientId: TextView = itemView.findViewById(R.id.tvRecipientId)
//        val tvRecipientVerified: TextView = itemView.findViewById(R.id.tvRecipientVerified)
//        val ivTransfer: ImageView = itemView.findViewById(R.id.ivTransfer)
//        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
//    }
//
//    interface ClickListener {
//        fun itemClick(reciptdata: Data)
//        fun transfer(reciptdata: Data)
//        fun delete(reciptdata: Data)
//    }
//}

package com.bos.payment.appName.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.dmt.fetchBenificary.Data

class ReceiptAdapter(
    private val mContext: Context,
    private var dataList: ArrayList<Data>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<ReceiptAdapter.ViewHolder>() {

    // ViewHolder class for managing item views
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRecipientBankName: TextView = itemView.findViewById(R.id.tvRecipientBankName)
        val tvRecipientId: TextView = itemView.findViewById(R.id.tvRecipientId)
        val tvRecipientVerified: TextView = itemView.findViewById(R.id.tvRecipientVerified)
        val ivTransfer: ImageView = itemView.findViewById(R.id.ivTransfer)
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)

        fun bind(model: Data) {
            // Bind data to the views
            tvRecipientBankName.text = model.bankname
            tvRecipientId.text = model.bankid
            tvRecipientVerified.text = model.verified

            // Set up click listeners
            itemView.setOnClickListener {
                clickListener.itemClick(model)
            }
            ivTransfer.setOnClickListener {
                clickListener.transfer(model)
            }
            ivDelete.setOnClickListener {
                clickListener.delete(model)
            }
        }
    }

    // Create ViewHolder by inflating the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_receiptlist, parent, false)
        return ViewHolder(view)
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = dataList[position]
        holder.bind(model)  // Bind data for the item
    }

    // Get total item count
    override fun getItemCount(): Int {
        return dataList.size
    }

    // Update the dataList and notify changes
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newDataList: ArrayList<Data>) {
        dataList.clear()  // Clear existing data
        dataList.addAll(newDataList)  // Add new data
        notifyDataSetChanged()  // Notify adapter to refresh
    }

    // Interface for handling click events
    interface ClickListener {
        fun itemClick(item: Data)
        fun transfer(item: Data)
        fun delete(item: Data)
    }
}

