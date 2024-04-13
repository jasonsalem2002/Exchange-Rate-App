package com.example.currencyexchange

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupsRVAdapter(
    private val context: Context,
    private val groups: MutableList<GroupWithLastMessage>,
    private val itemClickListener: (GroupWithLastMessage) -> Unit
) : RecyclerView.Adapter<GroupsRVAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val groupName: TextView = view.findViewById(R.id.displayNameText)
        val lastMessage: TextView = view.findViewById(R.id.statusText)
        val timestamp: TextView = view.findViewById(R.id.dateText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_users, parent, false)
        return ViewHolder(view).apply {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener(groups[position])
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = groups[position]
        holder.groupName.text = group.groupname
        holder.lastMessage.text = group.lastMessage
        holder.timestamp.text = group.timestamp
    }

    override fun getItemCount(): Int = groups.size


}
