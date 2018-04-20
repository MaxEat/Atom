package com.example.max.testjson;

import android.util.Log;
import android.webkit.JavascriptInterface;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.max.testjson.TestJson.wv;

/**
 * Created by max on 2018/4/10.
 */

public class AvailableItem extends Item {


    private int quantity;
    private int id;

    AvailableItem() {}

    AvailableItem(String aitemTag, String aitemLocation) {
        super(aitemTag,aitemLocation);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }




//
//    public static void getAllAvailableItems() {
//        JSONObject postdata = new JSONObject();
//
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getAllAvailableItemsURL, postdata.toString(), new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success get available", "result----" + result);
//                    try {
//                        availableItems = new ArrayList<AvailableItem>();
//                        availableItemMap = new HashMap<String, AvailableItem>();
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray jsonArray = jsonObject.getJSONArray("list");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject json = jsonArray.getJSONObject(i);
//                            AvailableItem item = new AvailableItem(json.getString("itemTag"), json.getString("itemLocation"));
//                            item.setClassification(json.getString("itemClassification"));
//                            item.setId(i);
//                            availableItems.add(item);
//                            availableItemMap.put(Integer.toString(item.getId()), item);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//                @Override
//                public void onFailture() {
//
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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
