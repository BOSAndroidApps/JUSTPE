package com.bos.payment.appName.adapter.reports

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.data.model.reports.rechargeAndBill.RechargeWiseReportRes
import com.bos.payment.appName.databinding.RechargeBillReportslistBinding

class RechargeBillReportsAdapter(
    var context: Context,
    var rechargeWiseReportList: List<RechargeWiseReportRes>
) : RecyclerView.Adapter<RechargeBillReportsAdapter.CustomViewHolder>() {

    inner class CustomViewHolder(var bin: RechargeBillReportslistBinding) :
        RecyclerView.ViewHolder(bin.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val bin = RechargeBillReportslistBinding.inflate(LayoutInflater.from(context),parent, false)
        return CustomViewHolder(bin)
    }

    override fun getItemCount(): Int {
        return rechargeWiseReportList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
//        holder.setIsRecyclable(false)
        holder.bin.mobileNoRecharge.text = rechargeWiseReportList[position].CANo.toString()
        holder.bin.dateRecharge.text = rechargeWiseReportList[position].Date.toString()
        holder.bin.amountRecharge.text = rechargeWiseReportList[position].Amount.toString()
        holder.bin.operatorRecharge.text = rechargeWiseReportList[position].Operator.toString()
        holder.bin.transactionRecharge.text = rechargeWiseReportList[position].TRANSNO.toString()
        holder.bin.statusRecharge.text = rechargeWiseReportList[position].Status.toString()
        holder.bin.remarksRecharge.text =
            rechargeWiseReportList[position].RechargeServiceType.toString()

//        holder.bin.printPdf.setOnClickListener {
//
//        }
    }

}