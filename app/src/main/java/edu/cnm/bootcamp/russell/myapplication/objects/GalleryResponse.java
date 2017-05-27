package edu.cnm.bootcamp.russell.myapplication.objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by russell on 5/27/17.
 */

public class GalleryResponse {
    @SerializedName("success")
    boolean success;

    @SerializedName("status")
    int status;

    @SerializedName("data")
    List<Image> data;
}
