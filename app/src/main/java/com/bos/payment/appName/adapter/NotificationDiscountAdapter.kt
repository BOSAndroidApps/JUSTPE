package com.bos.payment.appName.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.notification.GetNotificationRes
import com.bos.payment.appName.databinding.DiscountItemlistBinding
import com.bumptech.glide.Glide

class NotificationDiscountAdapter(
    private val context: Context,
    private val notificationListData: ArrayList<GetNotificationRes>
) : RecyclerView.Adapter<NotificationDiscountAdapter.DiscountViewHolder>() {

    inner class DiscountViewHolder(val bin: DiscountItemlistBinding) :
        RecyclerView.ViewHolder(bin.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscountViewHolder {
        val bin = DiscountItemlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscountViewHolder(bin)
    }

    override fun getItemCount(): Int {
        return notificationListData.size
    }

    override fun onBindViewHolder(holder: DiscountViewHolder, position: Int) {
        val notification = notificationListData[position]

        val imageUrl = notification.NotificationPicUrl?.trim()
        if (imageUrl != null) {
            Log.d("NotificationPicUrl", "notification$imageUrl")
        }
//        holder.bin.notificationImage = imageUrl
//        val imageUrl = notification.NotificationPicUrl!!.trim()
        Glide.with(context)
            .load(imageUrl)
//            .placeholder(R.drawable.placeholder_image) // Optional placeholder
            .error(R.drawable.ic_error)
            .into(holder.bin.notificationImage)
    }

}