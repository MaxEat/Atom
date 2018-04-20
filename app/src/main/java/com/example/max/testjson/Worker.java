package com.example.max.testjson;

import android.util.Log;

import com.example.max.testjson.dashboard.News;
import com.example.max.testjson.dashboard.Remind_Expired_Student_News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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

    @Override
    public void getAllItem() throws IOException {
        super.getAllItem();
    }

    public void getExpiredItemPersonDatabase() {
        JSONObject postdata = new JSONObject();
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getExpiredItemURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success", "result----" + result);
                    try {
                        expiredItems = new ArrayList<ExpiredItem>();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            ExpiredItem item = new ExpiredItem(json.getString("itemTag"));
                            item.setBorrowedTimeStamp(json.getString("borrowTimestamp").substring(0,10));
                            item.setBorrowedLocation(json.getString("borrowLocation"));
                            item.setClassification(json.getString("itemClassification"));
                            item.setBorrowPersonEmail(json.getString("Email"));
                            item.setBorrowPersonID(json.getString("userID"));
                            item.setBorrowPersonName(json.getString("person name"));
                            expiredItems.add(item);
                        }
                        setDashboardExpiredPerson();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailture() {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<News> getDashboard(){
        getExpiredItemPersonDatabase();
        return dashboard;
    }

    public void setDashboardExpiredPerson() {
        for(ExpiredItem expiredItem:expiredItems){
            Remind_Expired_Student_News news = new Remind_Expired_Student_News(expiredItem);
            dashboard.add(news);
        }
    }

}
