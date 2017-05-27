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
}
