package com.example.max.testjson.dashboard;

import android.util.Log;

import com.example.max.testjson.Item;

/**
 * Created by max on 2018/4/13.
 */

public class Wish_Item_Available_News extends News {
    private Item wishItem;

    public Wish_Item_Available_News(Item item) {
        Log.i("info","wish create");
        newsTitle = "Wish Item";
        newsContent = item.getClassification()+" at " + item.getItemLocation()+" is currently available!";
    }
}
