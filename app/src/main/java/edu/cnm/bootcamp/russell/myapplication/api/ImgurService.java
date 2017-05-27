package edu.cnm.bootcamp.russell.myapplication.api;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by russell on 5/27/17.
 */

public interface ImgurService {

    @GET("gallery/r/{subreddit}/{sort}/{window}/{page}")
    Single<GalleryResponse> subredditGallery(
            @Path("subreddit") String subreddit,
            @Path("sort") String sort,
            @Path("window") String window,
            @Path("page") int page
    );

}
