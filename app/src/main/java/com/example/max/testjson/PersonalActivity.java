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

public class PersonalActivity extends AppCompatActivity implements BorrowedFragment.OnListFragmentInteractionListener, AvailableItemFragment.OnListFragmentInteractionListener, ScanResultReceiver{
    private BottomNavigationView mBottomNavigationView;
    private Fragment []mFragments;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        mFragments = DataGenerator.getFragments("BottomNavigationView Tab");
        initView();
    }

    private void initView() {
        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_view);
        //mBottomNavigationView.getMaxItemCount()

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                onTabItemSelected(item.getItemId());
                return true;
            }
        });
        // 由于第一次进来没有回调onNavigationItemSelected，因此需要手动调用一下切换状态的方法
       onTabItemSelected(R.id.tab_menu_home);

    }

    private void onTabItemSelected(int id){
        Fragment fragment = null;
        switch (id){
            case R.id.tab_menu_home:
                fragment = mFragments[0];
                break;

            case R.id.tab_menu_new:
                fragment = mFragments[1];
                break;

            case R.id.tab_menu_discovery:
                TestJson.getUser().getAllItem();
                fragment = mFragments[2];
                break;

            case R.id.tab_menu_available:
                fragment = mFragments[3];
                break;
            case R.id.tab_menu_profile:
                fragment = mFragments[4];
                break;
        }
        if(fragment!=null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(Item item) {

    }

    @Override
    public void onListFragmentInteraction(AvailableItem item) {

    }


    @Override
    public void scanResultData(String codeFormat, String codeContent) {
        Toast.makeText(this, "FORMAT: " + codeFormat, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "CONTENT: " + codeContent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {
        Toast toast = Toast.makeText(this,noScanData.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
    }
}
