package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;
public class ExchangeRates {
    @SerializedName("usd_to_lbp")
    private Float usdToLbp;
    @SerializedName("lbp_to_usd")
    private Float lbpToUsd;

    public Float getUsdToLbp() {
        return usdToLbp;
    }

    public Float getLbpToUsd() {
        return lbpToUsd;
    }
}
