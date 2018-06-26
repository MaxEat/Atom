package com.example.max.testjson.dashboard;

import com.example.max.testjson.BorrowedItem;
import com.example.max.testjson.Item;
import com.example.max.testjson.Person;
import com.example.max.testjson.TestJson;

import java.util.ArrayList;

/**
 * Created by max on 2018/4/13.
 */

public class News {
    protected String newsContent;
    protected String newsTitle;
    protected Item item;

    News() {}

    public Item getItem() {
        return item;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public String getNewsTitle() {
        return newsTitle;
    }
}

