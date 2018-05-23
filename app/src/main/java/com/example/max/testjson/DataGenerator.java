package com.example.max.testjson;

/**
 * Created by ASUS on 2018/4/8.
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DataGenerator {

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[8];
        fragments[0] = HomeFragment.newInstance(from);
        fragments[1] = AddItemFragment.newInstance(1);
        fragments[2] = BorrowedFragment.newInstance(2);
        fragments[3] = AvailableItemFragment.newInstance(1);
        fragments[4] = SettingFragment.newInstance();
        fragments[5] = PersonalFragment_T.newInstance();
        fragments[6] = AdminFragment_T.newInstance();
        fragments[7] = RegisterFragment_T.newInstance();

        return fragments;
    }

//    public static View getTabView(Context context,int position){
//        View view = LayoutInflater.from(context).inflate(R.layout.activity_personal,null);
//        ImageView tabIcon = (ImageView) view.findViewById(R.id.tab_content_image);
//        tabIcon.setImageResource(DataGenerator.mTabRes[position]);
//        TextView tabText = (TextView) view.findViewById(R.id.tab_content_text);
//        tabText.setText(mTabTitle[position]);
//        return view;
//    }
}
