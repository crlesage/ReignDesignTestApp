package com.lesage.chris.reigndesigntestapp;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Chris on 1/15/2016.
 */
public class JsonData {

    @SerializedName("hits")
    public ArrayList<Hits> hits;

    @SerializedName("nbHits")
    public int nbHits;

    @SerializedName("page")
    public int page;

    @SerializedName("nbPages")
    public int nbPages;

    @SerializedName("hitsPerPage")
    public int hitsPerPage;

    @SerializedName("processingTimeMS")
    public int processingTimeMS;

    @SerializedName("query")
    public String query;

    @SerializedName("params")
    public String params;

    public ArrayList<Hits> getHits() {
        return hits;
    }

    public int getNbHits() {
        return nbHits;
    }

    public int getPage() {
        return page;
    }

    public int getNbPages() {
        return nbPages;
    }

    public int getHitsPerPage() {
        return hitsPerPage;
    }

    public int getProcessingTimeMS() {
        return processingTimeMS;
    }

    public String getQuery() {
        return query;
    }

    public String getParams() {
        return params;
    }
}
