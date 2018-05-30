package com.example.max.testjson;

import android.support.v4.app.Fragment;

/**
 * Created by ASUS on 2018/4/11.
 */

public class Admin_DataGenerator {

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[5];
        fragments[0] = DashBoardFragment.newInstance(1);
        fragments[1] = Admin_AddItemFragment.newInstance();
        fragments[2] = Admin_AvailableItemFragment.newInstance(1);
        fragments[3] = AddItemFragment.newInstance(2);
        fragments[4] = SettingFragment.newInstance();

        return fragments;
    }


}
