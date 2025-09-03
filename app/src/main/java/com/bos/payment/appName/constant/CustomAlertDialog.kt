package com.bos.payment.appName.constant

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.bos.payment.appName.R

object CustomAlertDialog {
    @RequiresApi(Build.VERSION_CODES.M)
    fun addRecipientDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val view: View = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.custom_alertdialog_addrecipient, null,false)

        val tvBtnNo = view.findViewById<View>(R.id.tvBtnNo) as TextView
        val tvBtnYes = view.findViewById<View>(R.id.tvBtnYes) as TextView
        builder.setView(view)
        val alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimationUptoDown
        alertDialog.show()
        alertDialog.setCancelable(false)

        tvBtnNo.setOnClickListener { alertDialog.dismiss() }
        tvBtnYes.setOnClickListener { alertDialog.dismiss() }
        return alertDialog
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun viewRecipientDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val view: View = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.custom_alertdialog_viewrecipient, null,false)

        val tvBtnNo = view.findViewById<View>(R.id.tvBtnNo) as TextView
        val tvBtnYes = view.findViewById<View>(R.id.tvBtnYes) as TextView
        builder.setView(view)
        val alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimationUptoDown
        alertDialog.show()
        alertDialog.setCancelable(false)

        tvBtnNo.setOnClickListener { alertDialog.dismiss() }
        tvBtnYes.setOnClickListener { alertDialog.dismiss() }
        return alertDialog
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun viewBillDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)
        val view: View = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
            R.layout.custom_alertdialog_viewbill, null,false)

        val tvBtnNo = view.findViewById<View>(R.id.tvBtnNo) as TextView
        val tvBtnYes = view.findViewById<View>(R.id.tvBtnYes) as TextView
        builder.setView(view)
        val alertDialog = builder.create()
        alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimationUptoDown
        alertDialog.show()
        alertDialog.setCancelable(false)

        tvBtnNo.setOnClickListener { alertDialog.dismiss() }
        tvBtnYes.setOnClickListener { alertDialog.dismiss() }
        return alertDialog
    }
}