package com.example.currencyexchange.chatting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyexchange.R
import com.example.currencyexchange.api.Authentication
import com.example.currencyexchange.api.ExchangeService
import com.example.currencyexchange.api.model.GroupMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupConvo : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageAdapter: GroupMessageAdapter
    private lateinit var groupName:String
    private var messages = mutableListOf<GroupMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_convo)
        groupName = intent.getStringExtra("groupname") ?: return
        var tt:TextView=findViewById(R.id.usernameTextView)
        tt.text=groupName
        val sendButton: Button = findViewById(R.id.sendButton)
        val messageEditText: EditText = findViewById(R.id.messageEditText)

        sendButton.setOnClickListener {
            val messageText = messageEditText.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
                messageEditText.text.clear()  // Clear the EditText after sending
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        recyclerView = findViewById(R.id.messagesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        messageAdapter = GroupMessageAdapter(messages) !!
        recyclerView.adapter = messageAdapter
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }
        val leavebutton:Button=findViewById(R.id.leaveButton)
        leavebutton.setOnClickListener {
            ExchangeService.exchangeApi().leaveGroup(if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null, groupName ).enqueue(object : Callback<Any> {

                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@GroupConvo, "Left Group", Toast.LENGTH_LONG).show()

                    } else {
                        Toast.makeText(this@GroupConvo, "Failed to Left group", Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Toast.makeText(this@GroupConvo, "Could not leave group", Toast.LENGTH_LONG).show()
                }
            })
            finish()

        }
        fetchchat()
    }
    private fun fetchchat() {
        Authentication.getToken()?.let { token ->
            val senderUsername = Authentication.getUsername()
            ExchangeService.exchangeApi().getGroupMessages("Bearer $token", groupName)
                .enqueue(object : Callback<List<GroupMessage>> {
                    override fun onResponse(
                        call: Call<List<GroupMessage>>,
                        response: Response<List<GroupMessage>>
                    ) {
                        if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                            messages.clear()
                            messages.addAll(response.body()!!)
                            messageAdapter.notifyDataSetChanged()
                            scrollToBottom()
                        } else {
                            Toast.makeText(this@GroupConvo, "No messages to display.", Toast.LENGTH_LONG).show()
                            messages.clear()}
                    }

                    override fun onFailure(call: Call<List<GroupMessage>>, t: Throwable) {
                        Toast.makeText(
                            this@GroupConvo,
                            "Failed to fetch chat: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }


    }
    private fun scrollToBottom() {
        if (messages.isNotEmpty()) {
            recyclerView.post {
                recyclerView.smoothScrollToPosition(messages.size - 1)
            }}
    }

    private fun sendMessage(s:String){
        val msg = GroupMessage().apply {
            this.groupName = groupName
            this.content= s
        }
        ExchangeService.exchangeApi().sendGroupMessage(msg, if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null,groupName).enqueue(object : Callback<Any> {

            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@GroupConvo, "Sent chat", Toast.LENGTH_LONG).show()
                    fetchchat()

                } else {
                    Toast.makeText(this@GroupConvo, "Failed to Send chat", Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@GroupConvo, "Could not add chat", Toast.LENGTH_LONG).show()
            }
        })

    }

}