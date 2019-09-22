package com.elblasy.navigation.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlacesResults implements Serializable {


    @SerializedName("results")
    private List<Result> results = new ArrayList<Result>();


    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
