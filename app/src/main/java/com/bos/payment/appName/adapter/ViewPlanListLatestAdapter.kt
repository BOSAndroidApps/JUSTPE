package com.bos.payment.appName.adapter

import ThreeGFourg
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.recharge.mobile.Plan
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.AnnualPlansItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.Data
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.DataPacksItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.EntertainmentPlansItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.GamingItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.IRWiFiCallingItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.ISDItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.InFlightPacksItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.InternationalRoamingItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.JioBharatPhoneItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.JioLinkItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.JioPhoneDataAddOnItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.JioPhoneItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.JioPhonePrimaItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.JioSaavnProItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.PopularPlansItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.TopUpItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.True5GUnlimitedPlansItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.TrueUnlimitedUpgradeItem
import com.bos.payment.appName.data.model.recharge.newapiflowforrecharge.ValueItem
import com.bos.payment.appName.data.model.recharge.plan.COMBO
import com.bos.payment.appName.data.model.recharge.plan.Romaing
import com.bos.payment.appName.data.model.recharge.plan.TOPUP

class ViewPlanListLatestAdapter(
    private val mContext: Context,
    private val popularPlan: List<Plan?>,
    private val clickListener: ClickListener) : RecyclerView.Adapter<ViewPlanListLatestAdapter.ViewHolder>() {

    // These lists will hold the filtered data
    private var FilterpopularPlanList: List<Plan?> = popularPlan

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_viewplan, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when {
                position < FilterpopularPlanList.size -> {
                val model = FilterpopularPlanList[position]
                holder.bind(model?.rs.toString(), model?.desc, model?.validity)
                holder.itemView.setOnClickListener {  clickListener.itemClick(FilterpopularPlanList[position]!!.rs) }
            }


        }
    }

    override fun getItemCount(): Int {
        return FilterpopularPlanList.size
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
        fun itemClick(rechargeAmount: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filter(searchText: String) {

        FilterpopularPlanList = if (searchText.isEmpty()) {
            popularPlan
        } else {
            popularPlan.filter { it?.rs.toString()?.contains(searchText, ignoreCase = true) == true }
        }

        notifyDataSetChanged()
    }

}
