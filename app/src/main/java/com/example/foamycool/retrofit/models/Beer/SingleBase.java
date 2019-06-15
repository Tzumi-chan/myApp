package com.example.foamycool.retrofit.models.Beer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleBase {
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private BeerData data;
    @SerializedName("status")
    @Expose
    private String status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BeerData getData() {
        return data;
    }

    public void setData(BeerData data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
