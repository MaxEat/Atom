package com.example.max.testjson.dashboard;

import android.icu.util.Calendar;
import android.util.Log;

import com.example.max.testjson.BorrowedItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by max on 2018/5/16.
 */

public class Item_Expired_News extends News {

    public Item_Expired_News(BorrowedItem borrowedItem) {
        item = borrowedItem;
        Log.i("info","expired create");
        newsTitle = "Item Expired";
        newsContent = "The " + item.getClassification()+" you are borrowing has expired " + ((BorrowedItem)item).getLeftDays()  + "days";
    }
}
