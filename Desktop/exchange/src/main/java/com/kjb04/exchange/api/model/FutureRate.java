package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

public class FutureRate implements Comparable<FutureRate> {
    @SerializedName("average_exchange_rate")
    public Float averageExchangeRate;
    @SerializedName("date")
    public String date;

    public Float getAverageExchangeRate() {
        return averageExchangeRate;
    }

    public String getDate() {
        return date;
    }


    @Override
    public int compareTo(FutureRate o) {
        return averageExchangeRate.compareTo(o.getAverageExchangeRate());
    }
}
