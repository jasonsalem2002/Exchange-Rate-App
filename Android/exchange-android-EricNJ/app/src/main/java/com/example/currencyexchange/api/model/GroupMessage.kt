package com.example.currencyexchange.api.model

import com.google.gson.annotations.SerializedName
class GroupMessage {
    @SerializedName("msg_id")
    var msgId: Int? = null

    @SerializedName("sender_username")
    var senderUsername: String? = null

    @SerializedName("group_name")
    var groupName: String? = null

    @SerializedName("content")
    var content: String? = null

    @SerializedName("added_date")
    var addedDate: String? = null
}