package com.example.max.testjson.dashboard;

import android.util.Log;

import com.example.max.testjson.BorrowedItem;
import com.example.max.testjson.ExpiredItem;

/**
 * Created by max on 2018/4/14.
 */

public class Remind_Expired_Student_News extends News {
    private ExpiredItem item;

    public Remind_Expired_Student_News(ExpiredItem item) {
        Log.i("info","Student not returning");
        newsTitle = "Expiried item";
        newsContent = "The item" + item.getClassification()+" borrowed by" + item.getBorrowPersonName() + "is expired";
    }
}
