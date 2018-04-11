package com.example.max.testjson;

import android.util.Log;

import com.example.max.testjson.dummy.DummyContent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by max on 2018/4/5.
 */

public class Item {

    private String itemTag;
    private String itemLocation;
    private String boughtTime;
    private String classification;
    private int itemPermission;

    int error;

    Item() { }

    Item( String ItemTag) {
        itemTag = ItemTag;
    }

    Item(String aitemTag, String aitemLocation) {
            itemTag = aitemTag;
            itemLocation = aitemLocation;
            itemPermission = 1;
    }



    public String getItemTag() {
        return itemTag;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public String getBoughtTime() {
        return boughtTime;
    }

    public String getClassification() {
        return classification;
    }

    public int getItemPermission() {
        return itemPermission;
    }


    public int register() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", getItemTag());
            postdata.put("itemLocation", getItemLocation());
            postdata.put("boughtTime", getBoughtTime());
            postdata.put("itemClassification", getClassification());
            postdata.put("itemPermission", getItemPermission());

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.registerItemURL, postdata.toString(),new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success","result----"+result);
                    try {
                        JSONObject json = new JSONObject(result);
                        error = json.getInt("error_message");
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
        return error;
    }

    @Override
    public String toString() {
        return "Item{" +
                "Tag='" + itemTag + '\'' +
                ", Location=" + itemLocation +
                ", BoughTime=" + boughtTime +
                ", Classfication=" + classification +
                '}';
    }

}
