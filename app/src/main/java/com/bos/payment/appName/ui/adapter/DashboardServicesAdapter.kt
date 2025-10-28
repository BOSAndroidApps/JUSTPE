package com.bos.payment.appName.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.justpaymodel.MoneyTransferServicesModel
import com.bos.payment.appName.databinding.DashboardServicesServicesLayoutBinding
import com.bos.payment.appName.databinding.MoneytransferServicesLayoutBinding
import com.bos.payment.appName.databinding.SeemoreLayoutBinding

class DashboardServicesAdapter(
    servicesList: List<MoneyTransferServicesModel>, // pass list here
    private val context: Context,
    private val onServiceClick: (MoneyTransferServicesModel) -> Unit,
    private val onSeeMoreClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_ITEM = 0
    private val TYPE_SEE_MORE = 1

    // Adapter maintains its **own internal copy** of the list
    private val servicesList = servicesList.toMutableList()

    // --- ViewHolders ---
    inner class ServiceViewHolder(val binding: DashboardServicesServicesLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class SeeMoreViewHolder(val binding: SeemoreLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    // --- Inflate Views ---
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val binding = DashboardServicesServicesLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            ServiceViewHolder(binding)
        }
        else {
            val binding = SeemoreLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            SeeMoreViewHolder(binding)
        }
    }



    // --- Bind Data ---
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = servicesList[position]

        if (holder is ServiceViewHolder) {
            holder.binding.servicesName.text = model.name
            holder.binding.servicesImage.setImageResource(model.image)
            holder.itemView.setOnClickListener {
                onServiceClick(model)
            }
        }
        else if (holder is SeeMoreViewHolder) {
            holder.binding.tvSeeMore.text= model.name
            holder.binding.root.setOnClickListener {
                onSeeMoreClick()

            }
        }

    }


    override fun getItemCount(): Int = servicesList.size


    override fun getItemViewType(position: Int): Int {
        return if (servicesList[position].featurecode == "SEE_MORE") TYPE_SEE_MORE else TYPE_ITEM
    }


    // --- Update list safely ---
    fun updateList(newList: List<MoneyTransferServicesModel>) {
        servicesList.clear()
        servicesList.addAll(newList) // always replaces old list
        notifyDataSetChanged()
    }

}
