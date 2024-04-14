package com.example.currencyexchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.model.GroupMessage
import com.example.currencyexchange.api.model.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GroupMessageAdapter(private val groupmessages: List<GroupMessage>) :
    RecyclerView.Adapter<GroupMessageAdapter.GroupMessageViewHolder>() {


    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2

        fun formatCustomDateString(dateString: String): String {
            val originalFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH)
            val targetFormat = SimpleDateFormat("MMM dd, HH:mm", Locale.ENGLISH)
            return try {
                val date = originalFormat.parse(dateString) ?: return ""
                targetFormat.format(date)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (groupmessages[position].senderUsername ==Authentication.getUsername()) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupMessageViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_SENT) R.layout.item_sentmsg else R.layout.item_recieved_group
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return GroupMessageViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: GroupMessageViewHolder, position: Int) {
        holder.bind(groupmessages[position])
    }

    override fun getItemCount() = groupmessages.size

    class GroupMessageViewHolder(private val view: View, private val viewType: Int) : RecyclerView.ViewHolder(view) {
        fun bind(message: GroupMessage) {
            val messageTextView: TextView = view.findViewById(
                if (viewType == VIEW_TYPE_SENT) R.id.txtSentMessage else R.id.txtReceivedMessage
            )
            messageTextView.text = message.content
            val timeTextView: TextView = view.findViewById(
                if (viewType == VIEW_TYPE_SENT) R.id.txtMessageTime else R.id.txtMessageTime  // Assuming same ID in both layouts
            )
            timeTextView.text = message.addedDate?.let { formatCustomDateString(it) }
            var sender: TextView? =null
            if (viewType == VIEW_TYPE_RECEIVED){ sender=view.findViewById(R.id.txtSenderName)}
            if (sender != null) {
                sender.text = message.senderUsername
            }

        }

    }


}
