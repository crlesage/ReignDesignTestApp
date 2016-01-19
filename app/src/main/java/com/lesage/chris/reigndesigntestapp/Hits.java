package com.lesage.chris.reigndesigntestapp;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by Chris on 1/15/2016.
 */
public class Hits {

    @SerializedName("created_at")
    public String created_at;

    @SerializedName("title")
    public String title;

    @SerializedName("url")
    public String url;

    @SerializedName("author")
    public String author;

    @SerializedName("points")
    public int points;

    @SerializedName("story_text")
    public String story_text;

    @SerializedName("comment_text")
    public String comment_text;

    @SerializedName("num_comments")
    public int num_comments;

    @SerializedName("story_id")
    public int story_id;

    @SerializedName("story_title")
    public String story_title;

    @SerializedName("story_url")
    public String story_url;

    @SerializedName("parent_id")
    public int parent_id;

    @SerializedName("created_at_i")
    public long created_at_i;

    @SerializedName("_tags")
    public ArrayList<String> _tags;

    @SerializedName("objectID")
    public int objectID;

    @SerializedName("_highlightResult")
    public HighlightResult _highlightResult;

    public transient int position;

    public Hits(String title, String url, String author, String story_title, long created_at_i, int position, String story_url) {
        this.title = title;
        this.url = url;
        this.author = author;
        this.story_title = story_title;
        this.created_at_i = created_at_i;
        this.position = position;
        this.story_url = story_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getAuthor() {
        return author;
    }

    public int getPoints() {
        return points;
    }

    public String getStory_text() {
        return story_text;
    }

    public String getComment_text() {
        return comment_text;
    }

    public int getNum_comments() {
        return num_comments;
    }

    public int getStory_id() {
        return story_id;
    }

    public String getStory_title() {
        return story_title;
    }

    public String getStory_url() {
        return story_url;
    }

    public int getParent_id() {
        return parent_id;
    }

    public long getCreated_at_i() {
        return created_at_i;
    }

    public ArrayList<String> get_tags() {
        return _tags;
    }

    public int getObjectID() {
        return objectID;
    }

    public HighlightResult get_highlightResult() {
        return _highlightResult;
    }

    public int getPosition() {
        return position;
    }
}
