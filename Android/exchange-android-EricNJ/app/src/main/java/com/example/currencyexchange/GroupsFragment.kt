package com.example.currencyexchange

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.GroupMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class GroupsFragment : Fragment() {
    private lateinit var groupsRecyclerView: RecyclerView
    private lateinit var groupsAdapter: GroupsRVAdapter
    var groupMessageMap: MutableMap<String, List<GroupMessage>> = mutableMapOf()
    private var userGroupList: List<String> = listOf()
    private var userGroupListdropdown: List<String> = listOf()
    val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)

    private var groupsWithLastMessage: MutableList<GroupWithLastMessage> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View= inflater.inflate(R.layout.fragment_groups, container, false)
        val addButton: ImageView = view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
            showUsernamesDropdown()
        }
        val backButton: ImageView = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().finish()
        }
        groupsRecyclerView = view.findViewById(R.id.idRVcurrency)
        groupsAdapter = GroupsRVAdapter(requireContext(), groupsWithLastMessage, this::onGroupClicked)
        groupsRecyclerView.adapter = groupsAdapter
        groupsRecyclerView.layoutManager = LinearLayoutManager(context)
        fetchGroups()

        return view
    }

    override fun onResume() {
        super.onResume()
        fetchGroups()
    }
    private fun onGroupClicked(group: GroupWithLastMessage) {
        Toast.makeText(context, "Clicked on: ${group.groupname}", Toast.LENGTH_SHORT).show()
        // Here, you can handle navigation or other actions like showing group details
    }

    private fun fetchGroups() {
        ExchangeService.exchangeApi().getMyGroupNames("Bearer ${Authentication.getToken()}")
            .enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    userGroupList = response.body() ?: emptyList()
                    Log.e("number of my group",userGroupList.size.toString())
                    displayGroupsPreview()
                    groupsRecyclerView.adapter?.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<List<String>>, throwable: Throwable) {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Groups Failed")
                            .setMessage("Failed to fetch groups.")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            })


    }


    fun displayGroupsPreview() {
        // Clear the list at the start to avoid old data persisting.
        groupsWithLastMessage.clear()

        // Initialize a counter to track completed fetch operations.
        var fetchCount = 0
        val totalRequests = userGroupList.size

        if (userGroupList.isEmpty()) {
            groupsRecyclerView.adapter?.notifyDataSetChanged()
            return
        }

        for (groupName in userGroupList) {
            fetchGroupMessages(groupName) { groupMessages ->
                fetchCount++
                // Process each group only if the fetch was successful.
                groupMessages?.lastOrNull()?.let {
                    val newMessage = GroupWithLastMessage(
                        groupname = groupName,
                        lastMessage = it.content ?: "No message",
                        timestamp = it.addedDate ?: "Unknown time"
                    )

                    // Check if the new message is already in the list to avoid duplicates.
                    synchronized(groupsWithLastMessage) {
                        if (!groupsWithLastMessage.any { message -> message.groupname == newMessage.groupname }) {
                            groupsWithLastMessage.add(newMessage)
                        }
                    }
                }

                // Update RecyclerView only after all requests have completed.
                if (fetchCount == totalRequests) {
                    // Apply sorting based on the timestamp, or any other criteria you previously used.
                    groupsWithLastMessage.sortByDescending { parseDate(it.timestamp) }
                    groupsRecyclerView.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    fun fetchGroupMessages(groupName: String, callback: (List<GroupMessage>?) -> Unit) {
        ExchangeService.exchangeApi().getGroupMessages("Bearer ${Authentication.getToken()}", groupName)
            .enqueue(object : Callback<List<GroupMessage>> {
                override fun onResponse(call: Call<List<GroupMessage>>, response: Response<List<GroupMessage>>) {
                    if (response.isSuccessful) {
                        groupMessageMap[groupName] = response.body()!!
                        callback(response.body())
                    } else {
                        callback(null)
                    }
                }

                override fun onFailure(call: Call<List<GroupMessage>>, throwable: Throwable) {
                    callback(null)
                }
            })
    }

    private fun showUsernamesDropdown() {
        ExchangeService.exchangeApi().getGroupNames("Bearer ${Authentication.getToken()}")
            .enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    userGroupListdropdown = response.body() ?: emptyList()
                    if (userGroupListdropdown.isNotEmpty()) {
                        displayDropdown(userGroupListdropdown)
                    } else {
                        Toast.makeText(requireContext(), "No groups available to join.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<String>>, throwable: Throwable) {
                  //  showAlert("Groups Failed", "Failed to fetch groups: ${throwable.message}")
                }
            })
    }

    private fun displayDropdown(groups: List<String>) {
        val arrayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, groups)
        AlertDialog.Builder(requireContext())
            .setTitle("Join Group")
            .setAdapter(arrayAdapter) { dialog, which ->
                val selectedGroup = groups[which]
                Toast.makeText(requireContext(), "Selected: $selectedGroup", Toast.LENGTH_LONG).show()
               // Log.e("hellloooo",selectedGroup)
                ExchangeService.exchangeApi().joinGroup(if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null, selectedGroup ).enqueue(object : Callback<Any> {

                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        if (response.isSuccessful) {
                            Toast.makeText(requireContext(), "Joined Group", Toast.LENGTH_LONG).show()

                        } else {
                            Toast.makeText(requireContext(), "Failed to Join group", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Toast.makeText(requireContext(), "Could not join group", Toast.LENGTH_LONG).show()
                    }
                })
            // Optionally, perform additional actions here, such as joining the selected group
            }
            .show()
    }




        fun parseDate(dateString: String): Date {
            val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
            return try {
                dateFormatter.parse(dateString) ?: Date(0)  // Return an early default date if parsing fails
            } catch (e: ParseException) {
                Date(0)  // Return an early default date if any exception occurs
            }
        }


    }

