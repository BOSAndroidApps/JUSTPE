package com.bos.payment.appName.ui.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.ui.view.Dashboard.activity.JustPeDashboard
import com.bumptech.glide.Glide

class ImageSliderAdapter(
    private val bannerList: List<JustPeDashboard.BannerItem>
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageSliderImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_image_slider, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val item = bannerList[position]

        // Load image from URL using Glide
        Glide.with(holder.itemView.context)
            .load(item.imagePath)
            .placeholder(R.drawable.image1) // optional placeholder
            .error(R.drawable.image2) // optional error image
            .into(holder.imageView)

        // On click, open the redirect URL
        holder.imageView.setOnClickListener {
            if (item.urlRedirect.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.urlRedirect))
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = bannerList.size
}
