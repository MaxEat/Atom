package com.example.max.testjson;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.ArrayList;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;



public class HomeFragment extends Fragment {
    private String mFrom;

    static HomeFragment newInstance(String from){
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mFrom = getArguments().getString("from");
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,null);
        TextView textView = (TextView) view.findViewById(R.id.title_from);
        TextView content = (TextView) view.findViewById(R.id.fragment_content);
//        textView.setText(mFrom);
//        content.setText("Homefragment");

        // 绑定Layout里面的ListView
        ListView list = (ListView)view.findViewById(R.id.list_view_home);

        // 生成动态数组，加入数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < 16; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", R.drawable.ic_dashboard);// 图像资源的ID
            map.put("ItemTitle", "Level " + i);
            map.put("ItemText", "Custom content! Custom content! ");
            listItem.add(map);
        }
        // 生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter = new SimpleAdapter(getActivity().getApplicationContext(), listItem,// 数据源
                R.layout.fragment_home_listview,// ListItem的XML实现
                // 动态数组与ImageItem对应的子项
                new String[] { "ItemImage", "ItemTitle", "ItemText" },
                // ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[] { R.id.ItemImage, R.id.ItemTitle, R.id.ItemText });

        // 添加并且显示
        list.setAdapter(listItemAdapter);

        // 添加点击
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
               // setTitle("点击第" + arg2 + "项");
            }
        });

        // 添加长按点击
        list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("长按菜单-ContextMenu");
                menu.add(0, 0, 0, "弹出长按菜单0");
                menu.add(0, 1, 0, "弹出长按菜单1");
            }
        });


        return view;
    }

//    // 长按菜单响应函数
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        setTitle("点击了长按菜单的第" + item.getItemId() + "项");
//        return super.onContextItemSelected(item);
//    }
}