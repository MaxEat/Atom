package com.example.max.testjson;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by max on 2018/4/8.
 */

public class TestJson extends Application {

   // private static TestJson instance;
    private static Person user;
    public static int alertDay = 14;
    public static CustomedWebview wv;
 //   public static HashMap<String, Integer> permission_days_student = new HashMap<String, Integer>();
  //  public static HashMap<String, Integer> permission_days_worker = new HashMap<String, Integer>();
    public static HashMap<String, Integer> permission_days = new HashMap<>();
    public static String classificationPictureArray[];
    public static HashMap<String, String> pictureMap = new HashMap<>();
    public static String permissionArray[];
    public static String locationArray[];
    public static String classificationArray[];

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);

//        permission_days_student.put("laptop", 14);
//        permission_days_student.put("FPGA", 14);
//        permission_days_student.put("ipad", 14);
//        permission_days_worker.put("laptop",28);
//        permission_days_worker.put("FPGA", 28);
//        permission_days_worker.put("ipad", 28);
    }


    public static RefWatcher getRefWatcher(Context context) {
        TestJson application = (TestJson) context.getApplicationContext();
        return application.refWatcher;
    }

//    public void addPermission_days(String userType, String itemType, int days) {
//        if(userType.equals("Student"))
//            permission_days_student.put(itemType,days);
//        if(userType.equals("worker"))
//            permission_days_worker.put(itemType,days);
//    }

    public static void resetWebView() {

        wv.clearHistory();
        wv.clearCache(true);

    //    wv.loadUrl("about:blank");
    //    wv.onPause();
    //    wv.removeAllViews();
    //    wv.destroyDrawingCache();
    }



    public static Person getUser() {
        return user;
    }

    public static void setUser(Person User) {
        user = User;
    }

    public static OkHttpClient getOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
