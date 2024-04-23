package com.example.currencyexchange

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GroupsRVAdapter(
    private val context: Context,
    private val groups: MutableList<GroupWithLastMessage>,
    private val itemClickListener: (GroupWithLastMessage) -> Unit
) : RecyclerView.Adapter<GroupsRVAdapter.ViewHolder>(), Filterable {

    private var filteredGroups: MutableList<GroupWithLastMessage> = groups

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
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                filteredGroups = if (charSearch.isEmpty()) {
                    groups
                } else {
                    groups.filter {
                        it.groupname.contains(charSearch, ignoreCase = true)
                    }.toMutableList()
                }

                return FilterResults().apply { values = filteredGroups }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredGroups = results?.values as MutableList<GroupWithLastMessage>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val group = filteredGroups[position]
        holder.groupName.text = group.groupname
        holder.lastMessage.text = group.lastMessage
        holder.timestamp.text = group.timestamp
    }

    override fun getItemCount(): Int = filteredGroups.size


}
