package com.coding.imagesliderwithdotindicatorviewpager2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bumptech.glide.Glide

// Adapter now works with a list of image URLs (Strings)
class ImageAdapter : ListAdapter<String, ImageAdapter.ViewHolder>(DiffCallback()) {

    class DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem // Compare URLs directly
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bindData(imageUrl: String) {
            // Load the image from the URL using Glide
            Glide.with(itemView.context)
                .load(imageUrl) // The URL to load the image from
//                .placeholder(R.drawable.placeholder_image) // Placeholder image
                .error(R.drawable.no_image) // Error image if loading fails
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view, parent, false) // Inflate the item layout
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = getItem(position) // Get the image URL at the current position
        holder.bindData(imageUrl) // Bind the image URL to the ViewHolder
    }
}
