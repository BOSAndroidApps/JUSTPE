package com.bos.payment.appName.ui.adapter

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.makepaymentnew.BankDataItem
import com.bos.payment.appName.data.model.supportmanagement.TicketsItem
import com.bos.payment.appName.databinding.BankdetailsitemlayoutBinding
import com.bos.payment.appName.databinding.ImagelayoutforraiseticketBinding
import com.bos.payment.appName.databinding.TicketstatusLayoutBinding
import com.bos.payment.appName.ui.view.Dashboard.activity.JustPeDashboard.Companion.QRBimap
import com.bos.payment.appName.ui.view.makepayment.MakePaymentActivity
import com.bos.payment.appName.ui.view.makepayment.MakePaymentActivity.Companion.BankAccountNumber
import com.bos.payment.appName.ui.view.makepayment.MakePaymentActivity.Companion.BankName
import com.bos.payment.appName.ui.view.makepayment.MakePaymentActivity.Companion.checkQR
import com.bos.payment.appName.ui.view.supportmanagement.ChatToAdminActivity
import com.bos.payment.appName.ui.view.supportmanagement.ChatToAdminActivity.Companion.checkForSendChat
import com.bos.payment.appName.ui.view.supportmanagement.ChatToAdminActivity.Companion.commentId
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.Constants.downloadImageFromUrl
import com.bos.payment.appName.utils.Utils.generateQrBitmap
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BankListAdapter(var context: Context, var bankDataList: List<BankDataItem?>?) : RecyclerView.Adapter<BankListAdapter.ViewHolder>() {

    lateinit var dialog: Dialog
    lateinit var pd: ProgressDialog

    class ViewHolder(var binding: BankdetailsitemlayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankListAdapter.ViewHolder {
        val binding = BankdetailsitemlayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: BankListAdapter.ViewHolder, position: Int) {
        holder.binding.accountHolderName.text = bankDataList!![position]!!.accountHolderName.toString()

        if(bankDataList!![position]!!.staticQRCodeIntenturl.isNullOrBlank()){
            holder.binding.qrview.visibility= View.INVISIBLE
        }
        else{
            holder.binding.qrview.visibility= View.VISIBLE
        }

        if(bankDataList!![position]!!.status.toString().equals("Deactivate?",ignoreCase = true)){
            holder.binding.status.text = "Active"
            holder.binding.status.setTextColor(context.getColor(R.color.green))
        }
        else{
            holder.binding.status.text = "Deactivate"
            holder.binding.status.setTextColor(context.getColor(R.color.red))
        }

        holder.binding.bankName.text = bankDataList!![position]!!.bankName.toString()
        holder.binding.accountNo.text = bankDataList!![position]!!.accountNo.toString()
        holder.binding.ifscCode.text = bankDataList!![position]!!.ifsCCode.toString()
        holder.binding.branchName.text = bankDataList!![position]!!.branchName.toString()
        holder.binding.accountType.text = bankDataList!![position]!!.accountType.toString()
        holder.binding.panNo.text = bankDataList!![position]!!.panNo.toString()
        holder.binding.adminCode.text = bankDataList!![position]!!.adminCode.toString()

        holder.binding.makePaymentBtn.setOnClickListener {
            BankName = bankDataList!![position]!!.bankName.toString()
            BankAccountNumber = bankDataList!![position]!!.accountNo.toString()
            checkQR = !bankDataList!![position]!!.staticQRCodeIntenturl.isNullOrBlank()
            context.startActivity(Intent(context,MakePaymentActivity::class.java))
        }

        holder.binding.qrview.setOnClickListener{
            if(bankDataList!![position]!!.staticQRCodeIntenturl.isNullOrBlank() || bankDataList!![position]!!.staticQRCodeIntenturl!!.isBlank() ){

            }else{
                var item = bankDataList!![position]!!
                popupforshowingraiseticketstatus(bankDataList!![position]!!.staticQRCodeIntenturl.toString(),item)
            }

        }
    }

    fun  updateList(filterlist:MutableList<BankDataItem?>){
         bankDataList= filterlist
         notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return bankDataList!!.size
    }


    fun popupforshowingraiseticketstatus(url : String,item:BankDataItem){
        dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.qrlayout)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        dialog.setCanceledOnTouchOutside(false)


        val qrimage = dialog.findViewById<ImageView>(R.id.qrimage)
        val crossicon = dialog.findViewById<ImageView>(R.id.crossicon)
        val bankName = dialog.findViewById<TextView>(R.id.bankName)
        val accountNo = dialog.findViewById<TextView>(R.id.accountNo)
        val ifsc = dialog.findViewById<TextView>(R.id.ifsc)
        val branch = dialog.findViewById<TextView>(R.id.branch)

        bankName.text=item.bankName
        accountNo.text=item.accountNo
        ifsc.text=item.ifsCCode
        branch.text=item.branchName

        crossicon.setOnClickListener {
            dialog.dismiss()
        }

        var QRBimap = generateQrBitmap(url, 500)
        qrimage.setImageBitmap(QRBimap)

        dialog.show()

    }


}