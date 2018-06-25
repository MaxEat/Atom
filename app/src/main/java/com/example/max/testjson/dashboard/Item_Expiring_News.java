package com.example.max.testjson.dashboard;

import android.util.Log;

import com.example.max.testjson.BorrowedItem;
import com.example.max.testjson.Item;

/**
 * Created by max on 2018/4/13.
 */

public class Item_Expiring_News extends News {

    public Item_Expiring_News(BorrowedItem borrowedItem) {
        item = borrowedItem;
        Log.i("info","expiring create");
        newsTitle = "Item Expiring";
        newsContent = "The " + ((BorrowedItem)item).getClassification()+" you are borrowing is expiring in " + ((BorrowedItem)item).getLeftDays() +
                " days. ";
    }


}
