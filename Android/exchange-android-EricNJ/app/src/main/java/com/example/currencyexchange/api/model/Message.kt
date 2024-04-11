package com.example.currencyexchange.api.model
import com.google.gson.annotations.SerializedName
class Message {
    @SerializedName("msg_id")
    var msgId: Int?=null
    @SerializedName("sender_username")
    var senderUsername:String? = null
    @SerializedName("recipient_username")
    var recepient_Username:String? = null
    @SerializedName("content")
    var content:String? = null
    @SerializedName("added_date")
    var timestamp:String? = null
}