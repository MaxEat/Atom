package com.example.max.testjson;

import android.os.Message;
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

    public void initializeWorker() throws IOException {
        byte[] array = initializeWorker_createJson();
        wv.postUrl(wv.initializeWorkerURL, array);

    }

    public byte[] initializeWorker_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }

    public void setDashboard() {
        try {
            getExpiredItemPersonDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void formPage() {
        Log.i("itemlsit size", Integer.toString(itemList.size()));

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
        Log.i("avail itemlist size", Integer.toString(availableItems.size()));
        setMaintainingMark();

    }

    private void setMaintainingMark() {

    }


    public void setDashboardExpiredPerson() {

        for(ExpiredItem expiredItem:expiredItems){
            Remind_Expired_Student_News news = new Remind_Expired_Student_News(expiredItem);
            if(dashboardMap.containsKey(expiredItem.getItemTag()))
                continue;
            dashboard.add(news);
            dashboardMap.put(expiredItem.getItemTag(), news);
        }
        Log.i("dash info size", Integer.toString(dashboard.size()));

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
    public void initializeWorker_interface(String htmlSource) {
        Log.i("Admin_get initialize", htmlSource);
        try {
            expiredItems = new ArrayList<ExpiredItem>();
            availableItems = new ArrayList<AvailableItem>();
            availableItemMap = new HashMap<String, AvailableItem>();

            JSONObject jsonObject = new JSONObject(htmlSource);
            JSONArray jsonArrayExpire = jsonObject.getJSONArray("expire");
            JSONArray jsonArrayOverview = jsonObject.getJSONArray("overview");

            for (int i = 0; i < jsonArrayExpire.length(); i++) {
                JSONObject json = jsonArrayExpire.getJSONObject(i);
                ExpiredItem item = new ExpiredItem(json.getString("itemTag"));
                item.setBorrowedTimeStamp(json.getString("borrowTimestamp").substring(0, 10));
                item.setBorrowedLocation(json.getString("borrowLocation"));
                item.setClassification(json.getString("itemClassification"));
                item.setBorrowPersonEmail(json.getString("email"));
                item.setBorrowPersonID(json.getString("userID"));
                item.setBorrowPersonName(json.getString("userName"));
                expiredItems.add(item);
            }


            for (int i = 0; i < jsonArrayOverview.length(); i++) {
                JSONObject json = jsonArrayOverview.getJSONObject(i);
                String itemClassification = json.getString("itemClassification");
                String itemLocation = json.getString("itemLocation");
                String itemStatus = json.getString("itemStatus");
                int quantity = json.getInt("quantity");

                if(availableItemMap.containsKey(itemClassification + itemLocation)) {

                    if (itemStatus.equals("available")) {
                        AvailableItem item = availableItemMap.get(itemClassification + itemLocation);
                        item.setQuantity(quantity);
                        item.setStatus("available");
                    }

                }
                else
                {

                    AvailableItem item = new AvailableItem();
                    item.setQuantity(quantity);
                    item.setItemLocation(itemLocation);
                    item.setClassification(itemClassification);
                    item.setStatus(itemStatus);

                    availableItems.add(item);
                    availableItemMap.put(item.getClassification() + item.getItemLocation(), item);
                }

            }

            setDashboardExpiredPerson();
            Message message = new Message();
            message.what = 2;
            MainActivity.uihander.sendMessage(message);
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void getExpiredItemPersonDatabase_interface(String htmlSource) {

        Log.i("Admin get expire", htmlSource);
        try {
            expiredItems = new ArrayList<ExpiredItem>();
            JSONObject jsonObject = new JSONObject(htmlSource);

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ExpiredItem item = new ExpiredItem(json.getString("itemTag"));
                item.setBorrowedTimeStamp(json.getString("borrowTimestamp").substring(0, 10));
                item.setBorrowedLocation(json.getString("borrowLocation"));
                item.setClassification(json.getString("itemClassification"));
                item.setBorrowPersonEmail(json.getString("email"));
                item.setBorrowPersonID(json.getString("userID"));
                item.setBorrowPersonName(json.getString("userName"));
                expiredItems.add(item);
            }


            setDashboardExpiredPerson();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public boolean inMaintainList(Item item) {
        if(item == null)
            return false;
        if(item.getStatus().equals("maintaining")){
            return true;
        }
        return false;
    }



}
