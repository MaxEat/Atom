package com.example.max.testjson;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    private int itemPermission;
    public static List<Item> sample;

    public static final Map<String, Item> sample_MAP = new HashMap<String, Item>();
    private static Item createItem(int position) {
        return new Item("123",Integer.toString(position));
    }

    private static void addItem(Item item) {
        sample.add(item);
        sample_MAP.put(item.itemTag, item);
    }
    static {
        // Add some sample items.
        for (int i = 1; i <= 4; i++) {
            Item.addItem(Item.createItem(i));
        }
    }

    int error;

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

    public int getItemPermission() {
        return itemPermission;
    }




    public int register() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", getItemTag());
            postdata.put("itemLocation", getItemLocation());
            postdata.put("boughtTime", getBoughtTime());
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
                '}';
    }

}
