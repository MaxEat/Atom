package com.example.max.testjson;

import android.util.Log;
import com.example.max.testjson.dashboard.Wish_Item_Available_News;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by max on 2018/4/14.
 */

public class Student extends Person {
    public ArrayList<Item> wishItems;

    Student(String aUserName, String aKuleuvenID, String Email){
        super(aUserName, aKuleuvenID, Email);
        wishItems = new ArrayList<Item>();
    }

    @Override
    public void getAllItem(){
        super.getAllItem();
        getWishListFromDatabase();
        AvailableItem.getAllAvailableItems();
    }

    public void getWishListFromDatabase() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getAllWishListItemsURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success get wish list", "result----" + result);
                    try {
                        wishItems = new ArrayList<Item>();
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("wishlist");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            Item item = new Item();
                            item.setItemLocation(json.getString("itemLocation"));
                            item.setClassification(json.getString("itemClassification"));
                            wishItems.add(item);
                        }
                        setDashboardWishItems();
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

    public void addItemToWish(final Item item) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
            postdata.put("itemLocation", item.getItemLocation());
            postdata.put("itemClassification", item.getClassification());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.addItemToWishListURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success", "result----" + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        error = json.getInt("error_message");
                        if(error == 0)
                            wishItems.add(item);
                        else
                            Log.i("error", "You have already added this before");

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

    public void removeItemFromWish(final Item item) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
            postdata.put("itemLocation", item.getItemLocation());
            postdata.put("itemClassification", item.getClassification());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.removeItemFromWishListURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success", "result----" + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        error = json.getInt("error_message");
                        if(error == 0)
                            wishItems.remove(item);
                        if(error == 8)
                            Log.i("error", "You wish list doesn't have such item");

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

    public void setDashboardWishItems() {
        for (Item wish : wishItems) {
            if (wish.checkItemAvailable()) {
                Wish_Item_Available_News news = new Wish_Item_Available_News(wish);
                dashboard.add(news);
            }
        }
    }

    public boolean inWishList(Item item) {
        for(Item i:wishItems){
            if(i.getItemLocation().equals(item.getItemLocation()) && i.getClassification().equals(item.getClassification())){
                return true;
            }
        }
        return false;
    }
}
