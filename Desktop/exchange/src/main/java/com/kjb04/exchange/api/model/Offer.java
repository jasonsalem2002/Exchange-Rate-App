package com.kjb04.exchange.api.model;

import com.google.gson.annotations.SerializedName;

public class Offer {
    @SerializedName("transaction_type")
    public Boolean transactionType;
    @SerializedName("amount_offered")
    public Float amountOffered;
    @SerializedName("amount_requested")
    public Float amountRequested;
    @SerializedName("added_date")
    public String addedDate;

    public Boolean getTransactionType() {
        return transactionType;
    }
    public Float getAmountOffered() {
        return amountOffered;
    }
    public Float getAmountRequested() {
        return amountRequested;
    }
    public String getAddedDate() {
        return addedDate;
    }

    public Offer(Boolean transactionType, Float amountOffered, Float amountRequested)
    {
        this.transactionType = transactionType;
        this.amountOffered = amountOffered;
        this.amountRequested = amountRequested;
    }
}
