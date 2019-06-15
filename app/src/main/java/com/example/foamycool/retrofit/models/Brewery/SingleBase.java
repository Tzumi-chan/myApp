package com.example.foamycool.retrofit.models.Brewery;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SingleBase {
    @SerializedName("currentPage")
    @Expose
    private Integer currentPage;
    @SerializedName("numberOfPages")
    @Expose
    private Integer numberOfPages;
    @SerializedName("totalResults")
    @Expose
    private Integer totalResults;
    @SerializedName("data")
    @Expose
    private Datum data = null;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(Integer numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public Datum getData() {
        return data;
    }

    public void setData(Datum data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
