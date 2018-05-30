package com.example.max.testjson;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.max.testjson.dashboard.News;
import com.squareup.picasso.Picasso;


public class HomeFragment extends Fragment {
    private String mFrom;
    private final  String returnMessage = "The item you borrowed is expired. Please return it as soon as possible.";
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
        final LayoutInflater factory = getLayoutInflater();
        View listView = factory.inflate(R.layout.fragment_home_listview, null);

        ListView list = (ListView)view.findViewById(R.id.list_view_home);
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();

        ArrayList<News> personalNews = TestJson.getUser().getDashboard();
        for(News news: personalNews)
        {
            HashMap<String, Object> map = new HashMap<String, Object>();



            //加显示图片
            Log.i("item classification", news.getItem().getClassification());
            String pictureUrl = TestJson.pictureMap.get(news.getItem().getClassification());

            Log.i("url", pictureUrl);
            if(pictureUrl==null)
            {
                map.put("ItemImage", R.drawable.ic_dashboard);// 图像资源的ID

            }
            else
            {
                ImageView mImage = (ImageView)listView.findViewById(R.id.ItemImage);
                Log.i("mimage", mImage.toString());
                Picasso.with(getContext()).load(pictureUrl).resize(120, 60).into((ImageView) mImage);
                map.put("ItemImage", mImage);// 图像资源的ID
            }
            //
            map.put("ItemTitle", news.getNewsTitle());
            map.put("ItemText", news.getNewsContent());
            listItem.add(map);
        }

        TextView emptyDash = (TextView) view.findViewById(R.id.emptyDash);
        if(personalNews.isEmpty()){
            emptyDash.setVisibility(View.VISIBLE);
        }
        else {
            emptyDash.setVisibility(View.GONE);
        }

        ImageButton imageButton = (ImageButton) view.findViewById(R.id.sendEmail);
        Log.i("type", TestJson.getUser().getUserType());
        if(TestJson.getUser().getUserType().equals("Student"))
        {
            imageButton.setVisibility(View.GONE);
        }
        else {
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("here", "clicked");
                    sendAlertEmail();

                }
            });
        }

        SimpleAdapter listItemAdapter = new SimpleAdapter(getActivity().getApplicationContext(),
                                                    listItem,
                                                    R.layout.fragment_home_listview,
                                                    new String[] { "ItemImage", "ItemTitle", "ItemText" },
                                                    new int[] { R.id.ItemImage, R.id.ItemTitle, R.id.ItemText });



        list.setAdapter(listItemAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
               // setTitle("点击第" + arg2 + "项");
            }
        });

        list.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("长按菜单-ContextMenu");
                menu.add(0, 0, 0, "弹出长按菜单0");
                menu.add(0, 1, 0, "弹出长按菜单1");
            }
        });


        TestJson.getUser().setDashboard();

        return view;
    }

    private void sendAlertEmail() {
        Log.i("Send email", "");
        String TO[] = new String[((Worker)TestJson.getUser()).getExpiredItems().size()];
        int i = 0;
        for(ExpiredItem item:((Worker)TestJson.getUser()).getExpiredItems()){
            TO[i] = item.getBorrowPersonEmail();
            i++;
        }
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Returning item reminder");
        emailIntent.putExtra(Intent.EXTRA_TEXT, returnMessage);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
           // getActivity().finish();
            Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

}