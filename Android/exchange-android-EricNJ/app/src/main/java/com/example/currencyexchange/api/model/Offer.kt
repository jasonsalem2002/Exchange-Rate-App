package com.example.currencyexchange.api.model
import com.google.gson.annotations.SerializedName
class Offer {
    @SerializedName("id")
    var id:Int?=null
    @SerializedName("amount_requested")
    var amountrequested: Float? = null
    @SerializedName("amount_to_trade")
    var amounttotrade: Float? = null
    @SerializedName("usd_to_lbp")
    var usdToLbp: Boolean? = null
    @SerializedName("username")
    var user:String?=null
    @SerializedName("added_date")
    var addedDate:String?=null
}