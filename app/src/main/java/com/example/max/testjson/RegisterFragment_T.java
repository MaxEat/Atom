package com.example.max.testjson;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.leakcanary.RefWatcher;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.max.testjson.TestJson.wv;

public class RegisterFragment_T extends Fragment {

    private android.support.v4.app.Fragment[]mFragments;

    private TextView textView;
    private String cardID;
    private ToggleButton userType;
    private Button register;

    private String kuleuvenID = "None";
    private String userName;
    private String email = "None";
    private String userPermission = "Student";
    private String type;
    private Fragment fragment;

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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_register, container, false);

        mFragments = DataGenerator.getFragments("BottomNavigationView Tab");
        kuleuvenID = getArguments().getString("kuleuvenID");
        cardID = getArguments().getString("cardID");
        userName = getArguments().getString("userName");
        email = getArguments().getString("email");
        type = getArguments().getString("role");

        userType = (ToggleButton) view.findViewById(R.id.userType);
        register = (Button) view.findViewById(R.id.register);
        textView = (TextView)view.findViewById(R.id.textView);

        if(type.contains("student")){
            userPermission = "Student";
            register.setVisibility(View.GONE);
            userType.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            try {
                fragment = mFragments[5];
                userPermission = "Student";

                Person user = new Student(userName, kuleuvenID, email);
                Log.i("Register as student", user.getUserName());
                user.setCardID(cardID);
                user.setUserType(userPermission);

                TestJson.setUser(user);
                wv.addJavascriptInterface(new registerPerson(),"registerPerson");
                register();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            register.setVisibility(View.VISIBLE);
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
        }

        return view;
    }

    public void register(View view) throws IOException {

        if(userType.isChecked()){
            fragment = mFragments[5];
            userPermission = "Student";
            Person user = new Student(userName, kuleuvenID, email);
            Log.i("user", user.getUserName());
            user.setCardID(cardID);
            user.setUserType(userPermission);

            TestJson.setUser(user);
            wv.addJavascriptInterface(new registerPerson(),"registerPerson");
            register();
        }
        else{
            fragment = mFragments[6];
            userPermission = "Administrator";
            Person user = new Worker(userName, kuleuvenID, email);
            Log.i("user", user.getUserName());
            user.setCardID(cardID);
            user.setUserType(userPermission);

            TestJson.setUser(user);
            wv.addJavascriptInterface(new registerPerson(),"registerPerson");
            register();
        }

    }

    public void register() throws IOException {
        byte[] array = register_createJson();
        wv.postUrl(CustomedWebview.registerPersonURL, array);
    }


    protected byte[] register_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID", kuleuvenID);
            postdata.put("cardID", cardID);
            postdata.put("email", email);
            postdata.put("userName", userName);
            postdata.put("userType", userPermission);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }

    private final class registerPerson{
        @JavascriptInterface
        public void registerPerson_interface(String htmlSource) {
            Log.i("register", htmlSource);
            try {
                JSONObject json = new JSONObject(htmlSource);
                int error = json.getInt("error_message");
                Log.i("error", Integer.toString(error));

                if(fragment!=null) {
                    wv.hide();
                    getFragmentManager().beginTransaction().replace(R.id.home_container_main,fragment).commit();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TestJson.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

}
