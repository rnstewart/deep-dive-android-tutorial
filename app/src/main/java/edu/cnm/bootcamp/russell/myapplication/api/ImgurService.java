package edu.cnm.bootcamp.russell.myapplication.api;

import edu.cnm.bootcamp.russell.myapplication.objects.GalleryResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Single;

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
