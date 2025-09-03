package com.bos.payment.appName.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bos.payment.appName.R

class RechargeOperator (context: Context, algorithmList: ArrayList<com.bos.payment.appName.data.model.recharge.operator.Data>) :
    ArrayAdapter<com.bos.payment.appName.data.model.recharge.operator.Data>(context, 0, algorithmList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        var updatedConvertView = convertView

        if (updatedConvertView == null) {
            updatedConvertView = LayoutInflater.from(context).inflate(R.layout.custom_spinner, parent, false)
        }

        val textViewName: TextView = updatedConvertView!!.findViewById(R.id.text_view)
        val currentItem: com.bos.payment.appName.data.model.recharge.operator.Data? = getItem(position)

        if (currentItem != null) {
            textViewName.text = currentItem.name
        }

        return updatedConvertView
    }
}
