package com.example.max.testjson;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;

import java.io.IOException;

import static com.example.max.testjson.TestJson.wv;


public class PersonalFragment_T extends Fragment {

    private BottomNavigationView mBottomNavigationView;
    private android.support.v4.app.Fragment[]mFragments;

    public PersonalFragment_T() {
    }

    public static PersonalFragment_T newInstance() {
        PersonalFragment_T fragment = new PersonalFragment_T();
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
        View view = inflater.inflate(R.layout.activity_personal, container, false);

        mFragments = DataGenerator.getFragments("BottomNavigationView Tab");

        mBottomNavigationView = (BottomNavigationView)view.findViewById(R.id.bottom_navigation_view);
        //mBottomNavigationView.getMaxItemCount()

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                try {
                    onTabItemSelected(item.getItemId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        // 由于第一次进来没有回调onNavigationItemSelected，因此需要手动调用一下切换状态的方法
        try {
            onTabItemSelected(R.id.tab_menu_home);
        } catch (IOException e) {
            e.printStackTrace();
        }



        return view;

    }



    private void onTabItemSelected(int id) throws IOException {

        android.support.v4.app.Fragment fragment = null;
        switch (id){
            case R.id.tab_menu_home:
                //wv.addJavascriptInterface(TestJson.getUser(), "Person");
                TestJson.getUser().setDashboard();
                fragment = mFragments[0];
                break;

            case R.id.tab_menu_new:
                fragment = mFragments[1];
                break;

            case R.id.tab_menu_discovery:
                fragment = mFragments[2];
                break;

            case R.id.tab_menu_available:
                wv.addJavascriptInterface(TestJson.getUser(), "Person");
                TestJson.getUser().getItemsOfSameKind();
                fragment = mFragments[3];
                break;

            case R.id.tab_menu_profile:
                fragment = mFragments[4];
                break;
        }
        if(fragment!=null) {
            getChildFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TestJson.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

}
