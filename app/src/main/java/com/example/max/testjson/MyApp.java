package com.example.max.testjson;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by max on 2018/4/8.
 */

public class MyApp extends Application {

    private static MyApp instance;
    private static String userCardID;

    @Override
    public void onCreate() {
        super.onCreate();
        this.instance = this;
    }

    public static MyApp getInstance() {
        return instance;
    }

    public static String getUserCardID() {
        return userCardID;
    }

    public static void setUserCardID(String ID) {
        userCardID = ID;
    }

    public static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient okHttpClient = builder.connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
        return okHttpClient;
    }
}
