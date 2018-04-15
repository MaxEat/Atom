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


    News() {}

    public String getNewsContent() {
        return newsContent;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

//    public static ArrayList<News> createDashBoard(Person person) {
//        ArrayList<News> dashboard = new ArrayList<>();
//
//        if(TestJson.getUser().getUserType() == "Student")
//        {
//            ArrayList<Item> wishes = person.getWishItems();
//            for (Item wish:wishes) {
//                if(wish.checkItemAvailable())
//                {
//                    News news = new Wish_Item_Available_News(wish);
//                    dashboard.add(news);
//                }
//            }
//            ArrayList<BorrowedItem> borrowedItems = person.getBorrowedItems();
//            for(BorrowedItem borrowedItem:borrowedItems) {
//                if(Integer.parseInt(borrowedItem.getLeftDays()) <= TestJson.alertDay){
//                    News news = new Item_Expiring_News(borrowedItem);
//                    dashboard.add(news);
//                }
//            }
//        }
//        if(TestJson.getUser().getUserType() == "Worker"){
//
//        }
//
//        return dashboard;
//    }




}

