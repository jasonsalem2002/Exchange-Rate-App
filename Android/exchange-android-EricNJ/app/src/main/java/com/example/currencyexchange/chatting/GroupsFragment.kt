package com.example.currencyexchange.chatting
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.R
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.Group
import com.example.currencyexchange.api.model.GroupMessage
import com.example.currencyexchange.data.GroupWithLastMessage
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
    private var mygroups:MutableList<String> = mutableListOf()

    private var groupsWithLastMessage: MutableList<GroupWithLastMessage> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View= inflater.inflate(R.layout.fragment_groups, container, false)
        val joinButton: Button = view.findViewById(R.id.joinbutton)
        joinButton.setOnClickListener {
            showUsernamesDropdown()
        }
        val backButton: ImageView = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            requireActivity().finish()
        }
        val addButton:ImageView=view.findViewById(R.id.addButton)
        addButton.setOnClickListener {
                showGroupNameDialog()
        }
        val editText: EditText = view.findViewById(R.id.grpsearch)
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                groupsAdapter.filter.filter(s)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

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
        val intent3 = Intent(activity, GroupConvo::class.java)
        intent3.putExtra("groupname", group.groupname)
        startActivity(intent3)
    }

    private fun fetchGroups() {
        mygroups.clear()
        ExchangeService.exchangeApi().getMyGroupNames("Bearer ${Authentication.getToken()}")
            .enqueue(object : Callback<List<String>> {
                override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                    userGroupList = response.body() ?: emptyList()
                    mygroups= response.body() as MutableList<String>
                    displayGroupsPreview()
                    groupsRecyclerView.adapter?.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<List<String>>, throwable: Throwable) {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Groups Failed")
                            .setMessage("Failed to fetch groups.Check Your internet connection.")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
            })


    }


    fun displayGroupsPreview() {
        groupsWithLastMessage.clear()
        var fetchCount = 0
        val totalRequests = userGroupList.size

        if (userGroupList.isEmpty()) {
            groupsRecyclerView.adapter?.notifyDataSetChanged()
            return
        }


        for (groupName in userGroupList) {
            fetchGroupMessages(groupName) { groupMessages ->
                fetchCount++

                val newMessage = groupMessages?.lastOrNull()?.let {
                    // This block executes only if there is at least one message in the list
                    GroupWithLastMessage(
                        groupname = groupName,
                        lastMessage = it.content ?: "No message",
                        timestamp = it.addedDate ?: "Unknown time"
                    )
                } ?: GroupWithLastMessage(
                    // This block executes if the list is null or empty
                    groupname = groupName,
                    lastMessage = "No Messages Yet",
                    timestamp = ""  // Empty string for timestamp if no messages
                )


                synchronized(groupsWithLastMessage) {
                        if (!groupsWithLastMessage.any { message -> message.groupname == newMessage.groupname }) {
                            groupsWithLastMessage.add(newMessage)
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
                        response.body()?.let { groupnames ->
                        val filteredGroupnames = groupnames.filterNot { mygroups.contains(it) }
                        displayDropdown(filteredGroupnames)}
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
                ExchangeService.exchangeApi().joinGroup(if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null, selectedGroup ).enqueue(object : Callback<Any> {

                    override fun onResponse(call: Call<Any>, response: Response<Any>) {
                        if (response.isSuccessful) {
                            fetchGroups()

                        } else {
                            Toast.makeText(requireContext(), "Failed to Join group", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<Any>, t: Throwable) {
                        Toast.makeText(requireContext(), "Could not join group", Toast.LENGTH_LONG).show()
                    }
                })
                fetchGroups()
            }
            .show()
    }




        fun parseDate(dateString: String): Date {
            val dateFormatter = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
            return try {
                dateFormatter.parse(dateString) ?: Date(0)
            } catch (e: ParseException) {
                Date(0)
            }
        }
    fun showGroupNameDialog() {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_creategroup,null)
        val editText = dialogView.findViewById<EditText>(R.id.editGroupName)

        // Build the dialog
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Set Group Name")
            .setView(dialogView) // Set the custom layout
            .setPositiveButton("Submit") { dialog, which ->
                // Handle the submission
                val groupName = editText.text.toString()
                if (groupName.isNotEmpty()) {
                    createGroupName(groupName)
                    fetchGroups()

                } else {
                    Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            .create()

        dialog.show()
    }

    fun createGroupName(name: String) {
        var grp=Group().apply {
            this.grpname = name
        }
        ExchangeService.exchangeApi().createGroup(grp, if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null).enqueue(object : Callback<Any> {

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Toast.makeText(requireContext(), "Created Group", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to Create Group.Check your Internet Connection", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(requireContext(), "Could not create group", Toast.LENGTH_LONG).show()
            }
        })
    }


}

