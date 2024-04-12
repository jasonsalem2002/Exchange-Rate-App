package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Group {
    @SerializedName("group_id")
    public Integer groupId;
    @SerializedName("group_name")
    public String groupName;

    public Integer getGroupId() {
        return groupId;
    }
    public String getGroupName() {
        return groupName;
    }


    public Group(String groupName)
    {
        this.groupName = groupName;
    }
}
