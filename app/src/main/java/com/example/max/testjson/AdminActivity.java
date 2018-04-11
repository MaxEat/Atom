package com.example.max.testjson;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class AdminActivity extends AppCompatActivity implements Admin_OverviewFragment.OnListFragmentInteractionListener{
    private BottomNavigationView mBottomNavigationView;
    private Fragment[]mFragments;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mFragments = Admin_DataGenerator.getFragments("adminBottomNavigationView Tab");
        initView();
    }

    private void initView() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.adminBottom_navigation_view);
        //mBottomNavigationView.getMaxItemCount()

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onTabItemSelected(item.getItemId());
                return true;
            }
        });
        // 由于第一次进来没有回调onNavigationItemSelected，因此需要手动调用一下切换状态的方法
        onTabItemSelected(R.id.tab_adminMenu_dashboard);

    }

    private void onTabItemSelected(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.tab_adminMenu_dashboard:
                fragment = mFragments[0];
                break;

            case R.id.tab_adminMenu_addItem:
                fragment = mFragments[1];
                break;

            case R.id.tab_adminMenu_overview:
                fragment = mFragments[2];
                break;

            case R.id.tab_adminMenu_maintain:
                fragment = mFragments[3];
                break;

            case R.id.tab_adminMenu_setting:
                fragment = mFragments[4];
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.adminHome_container,fragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Admin_Overview item) {

    }
}

