package edu.cnm.bootcamp.russell.myapplication.api;

import edu.cnm.bootcamp.russell.myapplication.objects.GalleryResponse;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;

/**
 * Created by russell on 5/27/17.
 */

public class API {
    private static ImgurService mService;

    public static void init() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.imgur.com/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build())
                .build();
        mService = retrofit.create(ImgurService.class);
    }

    public static Single<GalleryResponse> subredditGallery(String subreddit) {
        return mService.subredditGallery(subreddit, "time", "week", 0);
    }
}
