package com.example.max.testjson;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;
import com.jcraft.jsch.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView txtJson;
    ProgressDialog pd;
    WebView wv;

    private static Connection connection = null;
    private static Session session = null;

    private Person user;

    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        wv  = (WebView) findViewById(R.id.webview1);
//        wv.getSettings().setJavaScriptEnabled(true);
//        wv.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
//        wv.getSettings().setDomStorageEnabled(true);
//
//        wv.loadUrl("https://labtools.groept.be/inventory/secure"); //加载我的PHP框架代码
//        wv.requestFocus();
//        wv.getSettings().setUseWideViewPort(true);
//        wv.getSettings().setLoadWithOverviewMode(true);
//        wv.getSettings().setSupportZoom(true);
//        WebSettings webSettings = wv.getSettings();
//        webSettings.setBuiltInZoomControls(true);
//
//
//        wv.setWebViewClient(new WebViewClient() {
//
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                if(url.contains("secure")) {
//                    view.loadUrl("javascript:window.local_obj.getHtmlSource(document.body.innerHTML)");
//
//                }
//
//            }
//
//        });
     //   user = new Person("Wang Siyuan","r0609260","r0609653@kuleuven.be");

      //  user.register();
      //  user.getItem();


        Item item = new Item("itemTag1");
        user.borrowItem(item);
    }


    private void showStudentNumber() {
        if (!user.getUserName().equals("None"))
        {

            Toast.makeText(getApplicationContext(), "Welcome! " + user.getUserName(), Toast.LENGTH_LONG).show();

        }
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getHtmlSource(String htmlSource) {

            Log.i("HTML", htmlSource);
            try {

                String studentNumber = "None";
                String userName = "None";
                String email = "None";

                JSONObject obj = new JSONObject(htmlSource);
                userName = obj.getString("email").split("@")[0].replace(".", " ");
                studentNumber = obj.getString("user").split("@")[0]; Log.i("user", studentNumber);
                email = obj.getString("email");

                user = new Person(userName,studentNumber,email);
                showStudentNumber();

            } catch (Throwable t) {
                Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
            }
        }

    }




}