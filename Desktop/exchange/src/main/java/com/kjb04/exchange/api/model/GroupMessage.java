package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class GroupMessage {
    @SerializedName("msg_id")
    public Integer msgId;
    @SerializedName("group_name")
    public String groupName;
    @SerializedName("sender_username")
    public String senderUsername;
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
    public String getGroupName() {
        return groupName;
    }
    public String getContent() { return content; }
    public String getAddedDate() {
        return addedDate;
    }


    public GroupMessage(String groupName, String content)
    {
        this.groupName = groupName;
        this.content = content;
    }
}
