package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;
import com.kjb04.exchange.DateParser;

import java.util.Date;

public class Message {
    @SerializedName("msg_id")
    private Integer msgId;
    @SerializedName("sender_username")
    private String senderUsername;
    @SerializedName("recipient_username")
    private String recipientUsername;
    @SerializedName("content")
    private String content;
    @SerializedName("added_date")
    private String addedDate;


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