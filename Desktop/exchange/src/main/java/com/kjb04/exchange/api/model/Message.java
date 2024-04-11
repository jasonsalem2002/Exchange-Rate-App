package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("msg_id")
    public Integer msgId;
    @SerializedName("sender_username")
    public String senderUsername;
    @SerializedName("recipient_username")
    public String recipientUsername;
    @SerializedName("content")
    public String content;
    @SerializedName("timestamp")
    public String timestamp;


    public Integer getMsgId() {
        return msgId;
    }
    public String getSenderUsername() {
        return senderUsername;
    }
    public String getRecipientUsername() {
        return recipientUsername;
    }
    public String getContent() { return content; }
    public String getTimestamp() {
        return timestamp;
    }


    public Message(String recipientUsername, String content)
    {
        this.recipientUsername = recipientUsername;
        this.content = content;
    }
}