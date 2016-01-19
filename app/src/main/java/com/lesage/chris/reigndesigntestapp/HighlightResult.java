package com.lesage.chris.reigndesigntestapp;

import com.google.gson.annotations.SerializedName;


/**
 * Created by Chris on 1/15/2016.
 */
public class HighlightResult {

    @SerializedName("author")
    public Author author;

    @SerializedName("comment_text")
    public CommentText comment_text;

    @SerializedName("story_title")
    public StoryTitle story_title;

    @SerializedName("story_url")
    public StoryUrl story_url;

    public Author getAuthor() {
        return author;
    }

    public CommentText getComment_text() {
        return comment_text;
    }

    public StoryTitle getStory_title() {
        return story_title;
    }

    public StoryUrl getStory_url() {
        return story_url;
    }
}
