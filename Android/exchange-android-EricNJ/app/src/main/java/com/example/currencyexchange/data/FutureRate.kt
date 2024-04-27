package com.example.currencyexchange.data

import com.google.gson.annotations.SerializedName

data class FutureRate(
    @SerializedName("average_exchange_rate")
    var averageExchangeRate:Float,
    @SerializedName("date")
     var date:String)
