package com.example.max.testjson;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;

import com.squareup.leakcanary.RefWatcher;

import java.io.IOException;

import static com.example.max.testjson.TestJson.wv;


public class AdminFragment_T extends Fragment {
    private BottomNavigationView mBottomNavigationView;
    private android.support.v4.app.Fragment[]mFragments;

    public AdminFragment_T() {}

    public static AdminFragment_T newInstance() {
        AdminFragment_T fragment = new AdminFragment_T();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wv.addJavascriptInterface(TestJson.getUser(), "Person");
        try {
            TestJson.getUser().getAllAvailableItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_admin, container, false);

        mFragments = Admin_DataGenerator.getFragments("BottomNavigationView Tab");

        mBottomNavigationView = (BottomNavigationView)view.findViewById(R.id.adminBottom_navigation_view);

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
            onTabItemSelected(R.id.tab_adminMenu_dashboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return view;
    }

    private void onTabItemSelected(int id) throws IOException {
        android.support.v4.app.Fragment fragment = null;
        switch (id){
            case R.id.tab_adminMenu_dashboard:
                wv.addJavascriptInterface(TestJson.getUser(), "Person");
                TestJson.getUser().setDashboard();
                Log.i("here dash size", Integer.toString(TestJson.getUser().getDashboard().size()));
                fragment = mFragments[0];
                break;

            case R.id.tab_adminMenu_addItem:
                fragment = mFragments[1];
                break;

            case R.id.tab_adminMenu_overview:
                wv.addJavascriptInterface(TestJson.getUser(), "Person");
                ((Worker)TestJson.getUser()).getItemsOfSameKind_Worker();
                fragment = mFragments[2];
                break;

            case R.id.tab_adminMenu_maintain:
                fragment = mFragments[3];
                break;

//            case R.id.tab_adminMenu_setting:
//                wv.addJavascriptInterface(TestJson.getUser(), "Person");
//                TestJson.getUser().getPreferedEmailAndHeadShot();
//                Log.i("here dash size", Integer.toString(TestJson.getUser().getDashboard().size()));
//                fragment = mFragments[4];
//                break;
        }
        if(fragment!=null) {
            getChildFragmentManager().beginTransaction().replace(R.id.adminHome_container,fragment).commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TestJson.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

}
