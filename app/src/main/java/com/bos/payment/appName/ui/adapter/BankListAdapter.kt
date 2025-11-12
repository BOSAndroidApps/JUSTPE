package com.bos.payment.appName.ui.adapter

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.R
import com.bos.payment.appName.data.model.makepaymentnew.BankDataItem
import com.bos.payment.appName.data.model.supportmanagement.TicketsItem
import com.bos.payment.appName.databinding.BankdetailsitemlayoutBinding
import com.bos.payment.appName.databinding.ImagelayoutforraiseticketBinding
import com.bos.payment.appName.databinding.TicketstatusLayoutBinding
import com.bos.payment.appName.ui.view.makepayment.MakePaymentActivity
import com.bos.payment.appName.ui.view.makepayment.MakePaymentActivity.Companion.BankAccountNumber
import com.bos.payment.appName.ui.view.makepayment.MakePaymentActivity.Companion.BankName
import com.bos.payment.appName.ui.view.supportmanagement.ChatToAdminActivity
import com.bos.payment.appName.ui.view.supportmanagement.ChatToAdminActivity.Companion.checkForSendChat
import com.bos.payment.appName.ui.view.supportmanagement.ChatToAdminActivity.Companion.commentId
import com.bos.payment.appName.utils.Constants
import com.bos.payment.appName.utils.Constants.downloadImageFromUrl
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BankListAdapter(var context: Context, var bankDataList: List<BankDataItem?>?) : RecyclerView.Adapter<BankListAdapter.ViewHolder>() {


    class ViewHolder(var binding: BankdetailsitemlayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankListAdapter.ViewHolder {
        val binding = BankdetailsitemlayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: BankListAdapter.ViewHolder, position: Int) {
        holder.binding.accountHolderName.text = bankDataList!![position]!!.accountHolderName.toString()
        if(bankDataList!![position]!!.status.toString().equals("Deactivate?",ignoreCase = true)){
            holder.binding.status.text = "Active"
            holder.binding.status.setTextColor(context.getColor(R.color.green))
        }else{
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
            context.startActivity(Intent(context,MakePaymentActivity::class.java))
        }


    }

    fun  updateList(filterlist:MutableList<BankDataItem?>){
         bankDataList= filterlist
         notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return bankDataList!!.size
    }




    /*fun popupforshowingraiseticketstatus(url : String){
        dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.fullshowingimagelayout)

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        dialog.setCanceledOnTouchOutside(false)

        val imageview = dialog.findViewById<ImageView>(R.id.imageview)
        val downloadicon = dialog.findViewById<ImageView>(R.id.downloadicon)
        val crossicon = dialog.findViewById<ImageView>(R.id.crossicon)

        downloadicon.setOnClickListener {
            pd = ProgressDialog(context)
            pd.show()
            CoroutineScope(Dispatchers.IO).launch {
                val success = downloadImageFromUrl(context,url,"ticketraiseimage")
                if (success) {
                    pd.dismiss()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Image downloaded successfully!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    pd.dismiss()
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Download failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }

        crossicon.setOnClickListener {
            dialog.dismiss()
        }

        Glide.with(context).load(url).into(imageview)

        dialog.show()

    }*/


}