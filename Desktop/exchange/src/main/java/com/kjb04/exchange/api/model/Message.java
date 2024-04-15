package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;
import com.kjb04.exchange.DateParser;

import java.util.Date;

public class Message {
    @SerializedName("msg_id")
    public Integer msgId;
    @SerializedName("sender_username")
    public String senderUsername;
    @SerializedName("recipient_username")
    public String recipientUsername;
    @SerializedName("content")
    public String content;
    @SerializedName("added_date")
    public String addedDate;


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
    public Date getAddedDate() {
        return DateParser.convertStringToDate(addedDate);
    }


    public Message(String recipientUsername, String content)
    {
        this.recipientUsername = recipientUsername;
        this.content = content;
    }
}