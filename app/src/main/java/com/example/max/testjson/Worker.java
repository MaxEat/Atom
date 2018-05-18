package com.example.max.testjson;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.example.max.testjson.dashboard.News;
import com.example.max.testjson.dashboard.Remind_Expired_Student_News;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.max.testjson.TestJson.wv;

/**
 * Created by max on 2018/4/13.
 */

public class Worker extends Person{

    private String administorLocation;
    private String administorType;
    private ArrayList<ExpiredItem> expiredItems;

    Worker(String CardID) {super(CardID);}

    Worker(String aUserName, String aKuleuvenID, String Email){
        super(aUserName, aKuleuvenID, Email);
        expiredItems = new ArrayList<ExpiredItem>();
        administorLocation = "all";
        administorType = "all";
    }

    public ArrayList<ExpiredItem> getExpiredItems() {
        return expiredItems;
    }

    public void setDashboard() {
        dashboard = new ArrayList<News>();
        try {
            getExpiredItemPersonDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setDashboardExpiredPerson();
    }

    @Override
    public void formPage() {
        for(Item item:itemList){
            boolean exist = false;
            for(AvailableItem i: availableItems){
                if(item.getItemLocation().equals(i.getItemLocation()) &&
                        item.getClassification().equals(i.getClassification())){
                    i.increaseQuantity();
                    exist = true;
                    break;
                }
            }
            if(!exist){
                AvailableItem newItem = new AvailableItem();
                newItem.setItemLocation(item.getItemLocation());
                newItem.setClassification(item.getClassification());

                availableItems.add(newItem);
                availableItemMap.put(newItem.getClassification()+newItem.getItemLocation(), newItem);
            }
        }
        setMaintainingMark();
    }

    private void setMaintainingMark() {

    }


    public void setDashboardExpiredPerson() {
        for(ExpiredItem expiredItem:expiredItems){
            Remind_Expired_Student_News news = new Remind_Expired_Student_News(expiredItem);
            dashboard.add(news);
        }
    }


    public void getExpiredItemPersonDatabase() throws IOException {
        byte[] array = getExpiredItemPersonDatabase_createJson();
        wv.postUrl(CustomedWebview.getExpiredItemURL,array);
    }

    private byte[] getExpiredItemPersonDatabase_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }

    @JavascriptInterface
    public void getExpiredItemPersonDatabase_interface(String htmlSource) throws JSONException {
        expiredItems = new ArrayList<ExpiredItem>();
        JSONObject jsonObject = new JSONObject(htmlSource);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            ExpiredItem item = new ExpiredItem(json.getString("itemTag"));
            item.setBorrowedTimeStamp(json.getString("borrowTimestamp").substring(0,10));
            item.setBorrowedLocation(json.getString("borrowLocation"));
            item.setClassification(json.getString("itemClassification"));
            item.setBorrowPersonEmail(json.getString("email"));
            item.setBorrowPersonID(json.getString("userID"));
            item.setBorrowPersonName(json.getString("userName"));
            expiredItems.add(item);
        }
    }

    public boolean inMaintainList(Item item) {
        if(item.getStatus().equals("maintaining")){
            return true;
        }
        return false;
    }



}
