package com.example.max.testjson.dashboard;

import android.util.Log;

import com.example.max.testjson.BorrowedItem;
import com.example.max.testjson.Item;

/**
 * Created by max on 2018/4/13.
 */

public class Item_Expiring_News extends News {
    private BorrowedItem item;

    public Item_Expiring_News(BorrowedItem item) {
        Log.i("info","expiring create");
        newsTitle = "Expiring";
        newsContent = "The item" + item.getClassification()+" you are borrowing is expired!";
    }


}
