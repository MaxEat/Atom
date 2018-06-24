package com.example.max.testjson;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.squareup.leakcanary.RefWatcher;

import junit.framework.Test;

import java.io.IOException;

import static com.example.max.testjson.TestJson.wv;


public class SettingFragment extends Fragment {
    private EditText preferEmail;
    private RadioGroup radioGroup;
    private int reminderDaysCache = TestJson.alertDay;

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
        radioGroup = (RadioGroup)view.findViewById(R.id.reminder_days);

        submit = (Button)view.findViewById(R.id.submit_setting);
        logout = (ImageButton)view.findViewById(R.id.logout);

        preferEmail.setText(TestJson.getUser().getAlertEmail());
        radioGroup.check(R.id.sevendays);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.oneday:
                        reminderDaysCache = 1;
                        break;
                    case R.id.threedays:
                        reminderDaysCache = 3;
                        break;
                    case R.id.sevendays:
                        reminderDaysCache = 7;
                        break;
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TestJson.alertDay = reminderDaysCache;
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


                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure you want to logout?");
                builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                            Log.d("clear wv", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                            CookieManager.getInstance().removeAllCookies(null);
                            CookieManager.getInstance().flush();
                        } else
                        {
                            Log.d("clear wv", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
                            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(getContext());
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
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();


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

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TestJson.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }
}
