package com.example.max.testjson;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by ASUS on 2018/4/11.
 */

public class Admin_Overview extends Item {
    public static List<Admin_Overview> admin_Overviews = new ArrayList<Admin_Overview>();
    public static Map<String, Admin_Overview> admin_OverviewMap = new HashMap<String, Admin_Overview>();

    private int quantity;
    private String type;
    private int id;




    Admin_Overview(String aitemTag, String aitemLocation) {
        super(aitemTag,aitemLocation);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public static void getAllAdmin_Overviews() {
        JSONObject postdata = new JSONObject();

        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getAllAvailableItemsURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success", "result----" + result);
                    try {
                        admin_Overviews = new ArrayList<Admin_Overview>();
                        admin_OverviewMap = new HashMap<String, Admin_Overview>();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            Log.i("tag", json.getString("itemTag"));
                            Log.i("location", json.getString("itemLocation"));
                            Admin_Overview item = new Admin_Overview(json.getString("itemTag"), json.getString("itemLocation"));
                            item.setType(json.getString("itemClassification"));
                            item.setId(i);
                            admin_Overviews.add(item);
                            admin_OverviewMap.put(Integer.toString(item.getId()), item);
                        }

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
    public String toString() {
        return "Item{" +
                ", Tag=" + getItemTag() +
                ", Id=" + getId() +
                ", Location=" + getItemLocation() +
                ", Classfication=" + getClassification() +
                '}';
    }
}

