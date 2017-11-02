package edu.cnm.bootcamp.russell.myapplication.api;

import android.content.Context;

import java.io.IOException;

import edu.cnm.bootcamp.russell.myapplication.R;
import edu.cnm.bootcamp.russell.myapplication.objects.GalleryResponse;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;

/**
 * Created by russell on 5/27/17.
 */

public class API {
    private static ImgurService mService;

    public static void init(final Context context) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
                                      @Override
                                      public Response intercept(Interceptor.Chain chain) throws IOException {
                                          Request original = chain.request();

                                          Request request = original.newBuilder()
                                                  .header(
                                                          "Authorization",
                                                          "Client-ID "
                                                                  + context.getString(R.string.imgur_client_id)
                                                  )
                                                  .method(original.method(), original.body())
                                                  .build();

                                          return chain.proceed(request);
                                      }
                                  });
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
