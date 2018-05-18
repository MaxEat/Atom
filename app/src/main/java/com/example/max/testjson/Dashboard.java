package com.example.max.testjson;

import com.example.max.testjson.dashboard.Item_Expired_News;
import com.example.max.testjson.dashboard.Item_Expiring_News;
import com.example.max.testjson.dashboard.News;
import com.example.max.testjson.dashboard.Wish_Item_Available_News;

import java.util.ArrayList;

/**
 * Created by max on 2018/5/17.
 */

public class Dashboard {
    Dashboard(){}
    public ArrayList<News> dashboard;
    public ArrayList<Item_Expired_News> item_expired;
    public ArrayList<Item_Expiring_News> item_expiring;
    public ArrayList<Wish_Item_Available_News> item_available_news;

    public void addNews(News news){
        dashboard.add(news);
    }


}
