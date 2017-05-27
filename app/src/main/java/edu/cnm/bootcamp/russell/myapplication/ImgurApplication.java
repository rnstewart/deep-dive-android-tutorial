package edu.cnm.bootcamp.russell.myapplication;

import android.app.Application;

import edu.cnm.bootcamp.russell.myapplication.api.API;

/**
 * Created by russell on 5/27/17.
 */

public class ImgurApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        API.init();
    }
}
