package com.example.max.testjson;

/**
 * Created by ASUS on 2018/4/8.
 */
import android.support.v4.app.Fragment;

public class DataGenerator {

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[8];
        fragments[0] = DashBoardFragment.newInstance(1);
        fragments[1] = AddItemFragment.newInstance(1);
        fragments[2] = BorrowedFragment.newInstance(2);
        fragments[3] = AvailableItemFragment.newInstance(1);
        fragments[4] = SettingFragment.newInstance();
        fragments[5] = PersonalFragment_T.newInstance();
        fragments[6] = AdminFragment_T.newInstance();
        fragments[7] = RegisterFragment_T.newInstance();
        return fragments;
    }

}
