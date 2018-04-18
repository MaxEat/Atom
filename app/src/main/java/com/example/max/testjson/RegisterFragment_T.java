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
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.json.JSONObject;

import java.io.IOException;

import static com.example.max.testjson.TestJson.wv;

public class RegisterFragment_T extends Fragment {

    private android.support.v4.app.Fragment[]mFragments;

    private String cardID;
    private ToggleButton userType;
    private Button register;

    private String kuleuvenID = "None";
    private String userName = "None";
    private String email = "None";
    private String userPermission = "Student";

    @SuppressLint("JavascriptInterface")

    public RegisterFragment_T() {
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



    public void clearWebViewCache() {
        CookieSyncManager.createInstance(getContext());
        CookieManager.getInstance().removeAllCookie();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_register, container, false);

        mFragments = DataGenerator.getFragments("BottomNavigationView Tab");
        cardID = getArguments().getString("cardID");
        userType = (ToggleButton) view.findViewById(R.id.userType);
        register = (Button) view.findViewById(R.id.register);

        wv.addJavascriptInterface(new getFormData(),"local");
        wv.show();
        wv.loadUrl(CustomedWebview.baseIndexURL);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    register(v);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    public void register(View view) throws IOException {

        Person user;
        if(userType.isChecked()){
            userPermission = "Student";
            user = new Student(userName, kuleuvenID, email);
            user.setCardID(cardID);
            user.setUserType(userPermission);
        }

        else{
            userPermission = "Administrator";
            user = new Worker(userName, kuleuvenID, email);
            user.setCardID(cardID);
            user.setUserType(userPermission);
        }
        wv.addJavascriptInterface(user,"Person");
        user.register();
        TestJson.setUser(user);
        user.getAllItem();
        AvailableItem.getAllAvailableItems();

        Fragment fragment = mFragments[5];

        if(fragment!=null) {
            wv.hide();
            getFragmentManager().beginTransaction().replace(R.id.home_container_main,fragment).commit();
        }
        else
        {
            Log.i("here", "null");
        }

    }

    private final class getFormData
    {
        @JavascriptInterface
        public void getRegisterInfo(String htmlSource) {

            Log.i("HTML", htmlSource);
            try {
                JSONObject obj = new JSONObject(htmlSource);
                userName = obj.getString("email").split("@")[0].replace(".", " ");
                kuleuvenID = obj.getString("user").split("@")[0];
                email = obj.getString("email");
            } catch (Throwable t) {
                Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
            }
        }
    }



}
