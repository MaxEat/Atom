package com.example.max.testjson;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.example.max.testjson.dashboard.Wish_Item_Available_News;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import static com.example.max.testjson.TestJson.wv;

/**
 * Created by max on 2018/4/14.
 */

public class Student extends Person {
    public ArrayList<Item> wishItems;

    Student(String CardID) {
        super(CardID);
    }

    Student(String aUserName, String aKuleuvenID, String Email){
        super(aUserName, aKuleuvenID, Email);
        wishItems = new ArrayList<Item>();
    }

    @Override
    public void getAllItem() throws IOException {
        super.getAllItem();
        getWishListFromDatabase();
    }

    public void getWishListFromDatabase() throws IOException {
        byte[] array = getWishListFromDatabase_createJson();
        wv.postUrl(CustomedWebview.getAllWishListItemsURL, array);
    }

    public byte[] getWishListFromDatabase_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }

    public byte[] addItemToWish_createJson(Item item) throws IOException {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
            postdata.put("itemLocation", item.getItemLocation());
            postdata.put("itemClassification", item.getClassification());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }

    public byte[] removeItemFromWish_createJson(Item item) throws IOException {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
            postdata.put("itemLocation", item.getItemLocation());
            postdata.put("itemClassification", item.getClassification());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }

    public void addItemToWish(final Item item) throws IOException {
        byte[] array = addItemToWish_createJson(item);
        wv.postUrl(CustomedWebview.addItemToWishListURL, array);
    }

    public void removeItemFromWish(final Item item) throws IOException {
        byte[] array = removeItemFromWish_createJson(item);
        wv.postUrl(CustomedWebview.removeItemFromWishListURL, array);
    }

    @JavascriptInterface
    public void addItemToWish_interface(String htmlSource) {
        Log.i("Add item to wish", htmlSource);
        try {
            JSONObject json = new JSONObject(htmlSource);
            error = json.getInt("error_message");
            if(error == 0)
                Log.i("success", "You have added");
                //wishItems.add(item);
            else
                Log.i("error", "You have already added this before");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void removeItemFromWish_interface(String htmlSource) {
        Log.i("Remove item from wish", htmlSource);
        try {
            JSONObject json = new JSONObject(htmlSource);
            error = json.getInt("error_message");
            if(error == 0)
                Log.i("success", "You have removed");
                //wishItems.remove(item);
            else
                Log.i("error", "You have already added this before");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void getWishListFromDatabase_interface(String htmlSource) {
        Log.i("Success get wish list", htmlSource);
        try {
            wishItems = new ArrayList<Item>();
            JSONObject jsonObject = new JSONObject(htmlSource);
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



//    public void addItemToWish(final Item item) {
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("cardID", getCardID());
//            postdata.put("itemLocation", item.getItemLocation());
//            postdata.put("itemClassification", item.getClassification());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.addItemToWishListURL, postdata.toString(), new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success", "result----" + result);
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        error = json.getInt("error_message");
//                        if(error == 0)
//                            wishItems.add(item);
//                        else
//                            Log.i("error", "You have already added this before");
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

//    public void removeItemFromWish(final Item item) {
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("cardID", getCardID());
//            postdata.put("itemLocation", item.getItemLocation());
//            postdata.put("itemClassification", item.getClassification());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.removeItemFromWishListURL, postdata.toString(), new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success", "result----" + result);
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        error = json.getInt("error_message");
//                        if(error == 0)
//                            wishItems.remove(item);
//                        if(error == 8)
//                            Log.i("error", "You wish list doesn't have such item");
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
//    public void getWishListFromDatabase() {
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("cardID", getCardID());
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getAllWishListItemsURL, postdata.toString(), new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success get wish list", "result----" + result);
//                    try {
//                        wishItems = new ArrayList<Item>();
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray jsonArray = jsonObject.getJSONArray("wishlist");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject json = jsonArray.getJSONObject(i);
//                            Item item = new Item();
//                            item.setItemLocation(json.getString("itemLocation"));
//                            item.setClassification(json.getString("itemClassification"));
//                            wishItems.add(item);
//                        }
//                        setDashboardWishItems();
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
//
//
