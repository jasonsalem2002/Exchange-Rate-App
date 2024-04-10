package com.example.currencyexchange.api.model
import com.google.gson.annotations.SerializedName
class Offer {
    @SerializedName("amount_requested")
    var amountrequested: Float? = null
    @SerializedName("amount_to_trade")
    var amounttotrade: Float? = null
    @SerializedName("usd_to_lbp")
    var usdToLbp: Boolean? = null
    @SerializedName("id")
    var id:Int?=null
    @SerializedName("added_date")
    var addedDate:String?=null
}