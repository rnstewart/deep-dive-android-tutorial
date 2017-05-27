package edu.cnm.bootcamp.russell.myapplication.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by russell on 5/27/17.
 */

public class Image {
    @SerializedName("id")
    String id;

    @SerializedName("title")
    String title;

    @SerializedName("description")
    String description;

    @SerializedName("link")
    String link;

    @SerializedName("datetime")
    long datetime;

    @SerializedName("views")
    int views;

    @SerializedName("score")
    int score;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public long getDatetime() {
        return datetime;
    }

    public int getViews() {
        return views;
    }

    public int getScore() {
        return score;
    }
}
