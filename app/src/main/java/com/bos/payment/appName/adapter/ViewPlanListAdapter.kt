package com.bos.payment.appName.adapter

import ThreeGFourg
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
import com.bos.payment.appName.data.model.recharge.plan.COMBO
import com.bos.payment.appName.data.model.recharge.plan.Romaing
import com.bos.payment.appName.data.model.recharge.plan.TOPUP

class ViewPlanListAdapter(
    private val mContext: Context,
    private val threeGFourgList: List<ThreeGFourg?>,
    private val topupList: List<TOPUP>,
    private val roamingList: List<Romaing>,
    private val comboList: List<COMBO>,
    private val clickListener: ClickListener
) : RecyclerView.Adapter<ViewPlanListAdapter.ViewHolder>() {

    // These lists will hold the filtered data
    private var filteredThreeGFourgList: List<ThreeGFourg?> = threeGFourgList
    private var filteredTopupList: List<TOPUP> = topupList
    private var filteredRoamingList: List<Romaing> = roamingList
    private var filteredComboList: List<COMBO> = comboList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viewplan, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when {
            position < filteredThreeGFourgList.size -> {
                val model = filteredThreeGFourgList[position]
                holder.bind(model?.rs, model?.desc, model?.validity)
                holder.itemView.setOnClickListener { model?.let { it1 -> clickListener.itemClick(it1) } }
            }
            position < filteredThreeGFourgList.size + filteredTopupList.size -> {
                val model1 = filteredTopupList[position - filteredThreeGFourgList.size]
                holder.bind(model1.rs, model1.desc, model1.validity)
                holder.itemView.setOnClickListener { clickListener.itemClick(model1) }
            }
            position < filteredThreeGFourgList.size + filteredTopupList.size + filteredRoamingList.size -> {
                val model2 = filteredRoamingList[position - filteredThreeGFourgList.size - filteredTopupList.size]
                holder.bind(model2.rs, model2.desc, model2.validity)
                holder.itemView.setOnClickListener { clickListener.itemClick(model2) }
            }
            else -> {
                val model3 = filteredComboList[position - filteredThreeGFourgList.size - filteredTopupList.size - filteredRoamingList.size]
                holder.bind(model3.rs, model3.desc, model3.validity)
                holder.itemView.setOnClickListener { clickListener.itemClick(model3) }
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredThreeGFourgList.size + filteredTopupList.size + filteredRoamingList.size + filteredComboList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        val tvData: TextView = itemView.findViewById(R.id.tvData)
        val tvValidity: TextView = itemView.findViewById(R.id.tvValidity)

        fun bind(amount: String?, data: String?, validity: String?) {
            tvAmount.text = amount
            tvData.text = data
            tvValidity.text = validity
        }
    }

    interface ClickListener {
        fun itemClick(item: Any)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(searchText: String) {
        filteredThreeGFourgList = if (searchText.isEmpty()) {
            threeGFourgList
        } else {
            threeGFourgList.filter { it?.rs?.contains(searchText, ignoreCase = true) == true }
        }

        filteredTopupList = if (searchText.isEmpty()) {
            topupList
        } else {
            topupList.filter { it.rs!!.contains(searchText, ignoreCase = true) }
        }

        filteredRoamingList = if (searchText.isEmpty()) {
            roamingList
        } else {
            roamingList.filter { it.rs!!.contains(searchText, ignoreCase = true) }
        }

        filteredComboList = if (searchText.isEmpty()) {
            comboList
        } else {
            comboList.filter { it.rs!!.contains(searchText, ignoreCase = true) }
        }

        notifyDataSetChanged()
    }
}
