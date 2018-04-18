package com.example.max.testjson;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.json.JSONObject;

public class RegisterFragment_T extends Fragment {

    private android.support.v4.app.Fragment[]mFragments;

    private Person user;
    private String cardID;
    private ToggleButton userType;
    private Button register;
    private WebView wv;

    private String studentNumber = "None";
    private String userName = "None";
    private String email = "None";
    private String userPermission = "Student";

    @SuppressLint("JavascriptInterface")

    public RegisterFragment_T() {
        // Required empty public constructor
    }

    public static RegisterFragment_T newInstance() {
        RegisterFragment_T fragment = new RegisterFragment_T();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_register, container, false);

        mFragments = DataGenerator.getFragments("BottomNavigationView Tab");

//        Intent intent = getActivity().getIntent();
//        cardID = intent.getStringExtra("cardID");

        cardID = getArguments().getString("cardID");

        userType = (ToggleButton) view.findViewById(R.id.userType);
        register = (Button) view.findViewById(R.id.register);
        wv  = (WebView) view.findViewById(R.id.webview);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
        wv.getSettings().setDomStorageEnabled(true);

        wv.loadUrl("https://labtools.groept.be/inventory/secure"); //加载我的PHP框架代码
        wv.requestFocus();
        wv.getSettings().setUseWideViewPort(true);
        wv.getSettings().setLoadWithOverviewMode(true);
        wv.getSettings().setSupportZoom(true);
        WebSettings webSettings = wv.getSettings();
        webSettings.setBuiltInZoomControls(true);


        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
            @Override
            public void onPageFinished(WebView view, String url) {
                if(url.contains("secure")) {
                    view.loadUrl("javascript:window.local_obj.getHtmlSource(document.body.innerHTML)");
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(v);
            }
        });

        return view;
    }

    public void register(View view) {
        if(userType.isChecked()){
            userPermission = "Student";
            user = new Student(userName,studentNumber,email);
        }

        else{
            userPermission = "Administrator";
            user = new Worker(userName,studentNumber,email);
        }
        student正常，Administrator有问题




//        if(userType.isChecked())
//            userPermission = "Student";
//        else
//            userPermission = "Administrator";
//        user = new Person(userName,studentNumber,email);


        user.setCardID(cardID);
        user.setUserType(userPermission);

        if (!user.getUserName().equals("None"))
        {
            int error = user.register();
            if(error!=3)
            {
                TestJson.setUser(user);
                user.getAllItem();
                AvailableItem.getAllAvailableItems();

                Fragment fragment = null;

                fragment = mFragments[5];

                if(fragment!=null) {
                    getFragmentManager().beginTransaction().replace(R.id.home_container_main,fragment).commit();
                }
//                Intent personalIntent = new Intent(getContext(), PersonalActivity.class);
//                startActivity(personalIntent);
            }
            else
                Toast.makeText(getActivity().getApplicationContext(), "The user already registered! " + user.getUserName(), Toast.LENGTH_LONG).show();
        }
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getHtmlSource(String htmlSource) {

            Log.i("HTML", htmlSource);
            try {

                JSONObject obj = new JSONObject(htmlSource);
                userName = obj.getString("email").split("@")[0].replace(".", " ");
                studentNumber = obj.getString("user").split("@")[0]; Log.i("user", studentNumber);
                email = obj.getString("email");
                Toast.makeText(getActivity().getApplicationContext(), "Welcome! " + userName, Toast.LENGTH_LONG).show();

            } catch (Throwable t) {
                Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
            }
        }
    }



}
