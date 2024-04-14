package com.example.currencyexchange

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.api.model.Message
import java.text.SimpleDateFormat
import java.util.Locale

class MessageAdapter(private val messages: List<Message>, private val currentUser: String) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

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
        return if (messages[position].senderUsername == currentUser) {
            VIEW_TYPE_SENT
        } else {
            VIEW_TYPE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (viewType == VIEW_TYPE_SENT) R.layout.item_sentmsg else R.layout.item_receivedmsg
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MessageViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(messages[position])
    }

    override fun getItemCount() = messages.size

    class MessageViewHolder(private val view: View, private val viewType: Int) : RecyclerView.ViewHolder(view) {
        fun bind(message: Message) {
            val textView: TextView = view.findViewById(if (viewType == VIEW_TYPE_SENT) R.id.txtSentMessage else R.id.txtReceivedMessage)
            textView.text = message.content
            val textdate: TextView = view.findViewById(if (viewType == VIEW_TYPE_SENT) R.id.txtMessageTime else R.id.txtMessageTime)
            textdate.text = message.timestamp?.let { GroupMessageAdapter.formatCustomDateString(it) }

        }
    }
}
