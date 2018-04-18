//package com.example.max.testjson.obviousVersion;
//
//import android.annotation.SuppressLint;
//import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.webkit.JavascriptInterface;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.Toast;
//import android.widget.ToggleButton;
//
//import com.example.max.testjson.Person;
//import com.example.max.testjson.AvailableItem;
//import com.example.max.testjson.R;
//import com.example.max.testjson.TestJson;
//
//import org.json.JSONObject;
//
//public class RegisterActivity extends AppCompatActivity {
//
//    private Person user;
//    private String cardID;
//    private ToggleButton userType;
//    private Button register;
//    private WebView wv;
//
//    private String studentNumber = "None";
//    private String userName = "None";
//    private String email = "None";
//    private String userPermission = "Student";
//
//    @SuppressLint("JavascriptInterface")
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//        Intent intent = getIntent();
//        cardID = intent.getStringExtra("cardID");
//
//        userType = (ToggleButton) findViewById(R.id.userType);
//        register = (Button) findViewById(R.id.register);
//        wv  = (WebView) findViewById(R.id.webview);
//
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
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                if(url.contains("secure")) {
//                    view.loadUrl("javascript:window.local_obj.getHtmlSource(document.body.innerHTML)");
//                }
//            }
//        });
//
//    }
//
//    public void register(View view) {
//        if(userType.isChecked())
//            userPermission = "student";
//        else
//            userPermission = "administrator";
//        user = new Person(userName,studentNumber,email);
//        user.setCardID(cardID);
//        user.setUserType(userPermission);
//
//        if (!user.getUserName().equals("None"))
//        {
//            int error = user.register();
//            if(error!=3)
//            {
//                TestJson.setUser(user);
//                user.getAllItem();
//                AvailableItem.getAllAvailableItems();
//                Intent personalIntent = new Intent(RegisterActivity.this, PersonalActivity.class);
//                startActivity(personalIntent);
//            }
//            else
//                Toast.makeText(getApplicationContext(), "The user already registered! " + user.getUserName(), Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public final class InJavaScriptLocalObj {
//        @JavascriptInterface
//        public void getHtmlSource(String htmlSource) {
//
//            Log.i("HTML", htmlSource);
//            try {
//
//                JSONObject obj = new JSONObject(htmlSource);
//                userName = obj.getString("email").split("@")[0].replace(".", " ");
//                studentNumber = obj.getString("user").split("@")[0]; Log.i("user", studentNumber);
//                email = obj.getString("email");
//                Toast.makeText(getApplicationContext(), "Welcome! " + userName, Toast.LENGTH_LONG).show();
//
//            } catch (Throwable t) {
//                Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
//            }
//        }
//    }
//
//}

