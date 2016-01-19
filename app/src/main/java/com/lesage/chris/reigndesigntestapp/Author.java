package com.lesage.chris.reigndesigntestapp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by Chris on 1/15/2016.
 */
public class Author {

    @SerializedName("value")
    public String value;

    @SerializedName("matchLevel")
    public String matchLevel;

    @SerializedName("matchWords")
    public ArrayList<String> matchWords;

    public String getValue() {
        return value;
    }

    public String getMatchLevel() {
        return matchLevel;
    }

    public ArrayList<String> getMatchWords() {
        return matchWords;
    }
}
