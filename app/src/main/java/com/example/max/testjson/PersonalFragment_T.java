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


public class PersonalFragment_T extends Fragment {

    private BottomNavigationView mBottomNavigationView;
    private android.support.v4.app.Fragment[]mFragments;

    public PersonalFragment_T() {
        // Required empty public constructor
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
                onTabItemSelected(item.getItemId());
                return true;
            }
        });
        // 由于第一次进来没有回调onNavigationItemSelected，因此需要手动调用一下切换状态的方法
        onTabItemSelected(R.id.tab_menu_home);


        return view;

    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//
//
//
//
//        if (savedInstanceState == null) {
////            getChildFragmentManager().beginTransaction().replace(R.id.fl_chilid, new ChildFragmentOne("Child Fragment One")).commit();
////            mCurrentlyShowingFragment = 0;
//
//
//        } else {
// //           mCurrentlyShowingFragment = savedInstanceState.getInt("currently_showing_fragment");
//
//
//        }
// //       adjustButtonText();
//
//        super.onViewCreated(view, savedInstanceState);
//    }

    private void onTabItemSelected(int id){
        android.support.v4.app.Fragment fragment = null;
        switch (id){
            case R.id.tab_menu_home:
                fragment = mFragments[0];
                break;

            case R.id.tab_menu_new:
                fragment = mFragments[1];
                break;

            case R.id.tab_menu_discovery:
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
            getChildFragmentManager().beginTransaction().replace(R.id.home_container,fragment).commit();
        }
    }


}
