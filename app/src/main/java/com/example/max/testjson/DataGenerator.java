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

    public static final int []mTabRes = new int[]{R.drawable.ic_dashboard,R.drawable.ic_dashboard,R.drawable.ic_dashboard,R.drawable.ic_dashboard};
    public static final int []mTabResPressed = new int[]{R.drawable.ic_dashboard,R.drawable.ic_dashboard,R.drawable.ic_dashboard,R.drawable.ic_dashboard};
    public static final String []mTabTitle = new String[]{"首页","发现","关注","我的"};

    public static Fragment[] getFragments(String from){
        Fragment fragments[] = new Fragment[5];
        fragments[0] = HomeFragment.newInstance(from);
        fragments[1] = AddItemFragment.newInstance(1);
        fragments[2] = BorrowedFragment.newInstance(2);
        fragments[3] = AvailableItemFragment.newInstance(1);
        fragments[4] = HomeFragment.newInstance(from);
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
