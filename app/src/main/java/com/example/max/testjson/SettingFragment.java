package com.example.max.testjson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import junit.framework.Test;

import java.io.IOException;

import static com.example.max.testjson.TestJson.wv;


public class SettingFragment extends Fragment {
    private CheckBox twoWeeks;
    private CheckBox oneWeek;
    private CheckBox threedays;
    private EditText preferEmail;
    private Button submit;
    private ImageButton logout;


    private OnFragmentInteractionListener mListener;

    public SettingFragment() {

    }


    public static SettingFragment newInstance() {
        SettingFragment fragment = new SettingFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        preferEmail = (EditText)view.findViewById(R.id.email_input);
        twoWeeks = (CheckBox)view.findViewById(R.id.twoweeks);
        oneWeek = (CheckBox)view.findViewById(R.id.oneweek);
        threedays = (CheckBox)view.findViewById(R.id.threedays);
        submit = (Button)view.findViewById(R.id.submit_setting);
        logout = (ImageButton)view.findViewById(R.id.logout);

        preferEmail.setText(TestJson.getUser().getAlertEmail());
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(twoWeeks.isChecked())
                {
                    TestJson.alertDay = 14;
                }
                if(threedays.isChecked())
                {
                    TestJson.alertDay = 3;
                }
                if(oneWeek.isChecked())
                {
                    TestJson.alertDay = 7;
                }

                String email = preferEmail.getText().toString();
                if(isEmail(email))
                {
                    TestJson.getUser().setAlertEmail(email);
                    try {
                        TestJson.getUser().updateAlertEmail(email);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
                    alertBuilder.setTitle("Okay");
                    alertBuilder.setMessage("Personal setting updated");
                    alertBuilder.create().show();
                }
                else{
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(view.getContext());
                    alertBuilder.setTitle("Error");
                    alertBuilder.setMessage("Please enter a valid email address");
                    alertBuilder.create().show();
                }

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                    Log.d("clear wv", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                    CookieManager.getInstance().removeAllCookies(null);
                    CookieManager.getInstance().flush();
                } else
                {
                    Log.d("clear wv", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                    CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(view.getContext());
                    cookieSyncMngr.startSync();
                    CookieManager cookieManager=CookieManager.getInstance();
                    cookieManager.removeAllCookie();
                    cookieManager.removeSessionCookie();
                    cookieSyncMngr.stopSync();
                    cookieSyncMngr.sync();
                }
                TestJson.resetWebView();
                TestJson.setUser(null);
                Intent intent = new Intent(getActivity(), SplashActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
