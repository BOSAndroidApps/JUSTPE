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
import com.bos.payment.appName.data.model.supportmanagement.TicketsItem
import com.bos.payment.appName.databinding.ImagelayoutforraiseticketBinding
import com.bos.payment.appName.databinding.TicketstatusLayoutBinding
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

class TicketStatusAdapter(var context: Context, var transactionList: List<TicketsItem?>?) :
    RecyclerView.Adapter<TicketStatusAdapter.ViewHolder>() {
    lateinit var adapter :ImageAdapter
    lateinit var  selectedUris :List<String>


    class ViewHolder(var binding: TicketstatusLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketStatusAdapter.ViewHolder {
        val binding = TicketstatusLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketStatusAdapter.ViewHolder, position: Int) {

        if (transactionList?.get(position)!!.status!!.toLowerCase().equals("inprogress")) {
            holder.binding.statustxt.setTextColor(ContextCompat.getColor(context, R.color.orange))
        }

        if (transactionList?.get(position)!!.status!!.toLowerCase().equals("closed")) {
            holder.binding.statustxt.setTextColor(ContextCompat.getColor(context, R.color.green))
            holder.binding.chattxt.text="View Chat"
        }
        else{
            holder.binding.chattxt.text="Chat"
        }

        if (transactionList?.get(position)!!.status!!.toLowerCase().equals("open")) {
            holder.binding.statustxt.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        if (transactionList?.get(position)!!.status!!.toLowerCase().equals("dispute")) {
            holder.binding.statustxt.setTextColor(ContextCompat.getColor(context, R.color.teal_200))
        }


        holder.binding.statustxt.text = transactionList?.get(position)!!.status
        holder.binding.ticketnumber.text = transactionList?.get(position)!!.ticketNumber
        holder.binding.datetime.text = "${transactionList?.get(position)!!.createdDate}"

        selectedUris =  transactionList?.get(position)!!.getImageUrls("https://bosapi.bos.center")

        adapter = ImageAdapter(context, selectedUris)
        holder.binding.imageviewliast.adapter = adapter


        holder.binding.raiseticketcard.setOnClickListener {
            commentId =  transactionList?.get(position)!!.complaintID!!.toInt()
            checkForSendChat = transactionList?.get(position)!!.status!!
            context.startActivity(Intent(context, ChatToAdminActivity::class.java))
        }


    }

    override fun getItemCount(): Int {
        return transactionList!!.size
    }


    class ImageAdapter(var context: Context, private val items: List<String>) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {
        lateinit var dialog: Dialog
        lateinit var pd: ProgressDialog
        inner class ViewHolder(val binding: ImagelayoutforraiseticketBinding) :
            RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ImagelayoutforraiseticketBinding.inflate(LayoutInflater.from(context), parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            Glide.with(context).load(items[position]).into( holder.binding.image)
            holder.binding.image.setOnClickListener {
                popupforshowingraiseticketstatus(items[position])
            }

        }

        override fun getItemCount(): Int = items.size


        fun popupforshowingraiseticketstatus(url : String){
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

        }

    }


}