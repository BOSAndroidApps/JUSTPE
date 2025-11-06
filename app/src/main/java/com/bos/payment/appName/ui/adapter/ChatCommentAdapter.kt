package com.bos.payment.appName.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bos.payment.appName.data.model.supportmanagement.CommentsItem
import com.bos.payment.appName.databinding.ChatReceiverBinding
import com.bos.payment.appName.databinding.ChatSenderBinding
import com.bos.payment.appName.ui.view.Dashboard.transactionreports.RaiseTicketActivity.ImageAdapter

class ChatCommentAdapter(var context: Context, var commentList: List<CommentsItem?>,  private val currentUserCode: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    lateinit var adapter :ImageAdapter

    private val VIEW_TYPE_SENT = 1
    private val VIEW_TYPE_RECEIVED = 2

    override fun getItemViewType(position: Int): Int {
        return if (commentList[position]!!.commentBy == currentUserCode) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    class SentViewHolder(var binding: ChatSenderBinding) : RecyclerView.ViewHolder(binding.root)

    class ReceivedViewHolder(var binding: ChatReceiverBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SENT) {
            val binding = ChatSenderBinding.inflate(LayoutInflater.from(context), parent, false)
            return SentViewHolder(binding)
        } else {
            val binding = ChatReceiverBinding.inflate(LayoutInflater.from(context), parent, false)
            return ReceivedViewHolder(binding)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chat = commentList[position]
        val formattedTime = chat!!.commentDate!!.substring(11, 16)

        if (holder is SentViewHolder) {
            holder.binding.txtMessage.text = chat.comment
            holder.binding.txtTime.text = formattedTime
        }
        else if (holder is ReceivedViewHolder) {
            holder.binding.admin.text="Admin(${chat.commentBy})"
            holder.binding.txtMessage.text = chat.comment
            holder.binding.txtTime.text = formattedTime
        }

    }


    override fun getItemCount(): Int {
        return commentList!!.size
    }




}