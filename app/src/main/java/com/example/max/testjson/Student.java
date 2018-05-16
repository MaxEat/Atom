package com.example.max.testjson;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;

import com.example.max.testjson.dashboard.Item_Expiring_News;
import com.example.max.testjson.dashboard.Wish_Item_Available_News;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.max.testjson.TestJson.wv;

/**
 * Created by max on 2018/4/14.
 */

public class Student extends Person {
    public ArrayList<Item> wishItems;
    public ArrayList<BorrowedItem> borrowedItems;
    public Map<String, BorrowedItem> borrowedItemMAP = new HashMap<String, BorrowedItem>();
    public ArrayList<BorrowedItem> blacklistItems;
    public String blacklist;


    Student(String CardID) {
        super(CardID);
        borrowedItems = new ArrayList<BorrowedItem>();
        blacklist = "normal";
    }

    Student(String aUserName, String aKuleuvenID, String Email){
        super(aUserName, aKuleuvenID, Email);
        wishItems = new ArrayList<Item>();
        borrowedItems = new ArrayList<BorrowedItem>();
        blacklistItems = new ArrayList<BorrowedItem>();
        blacklist = "normal";
    }

    Student(String aUserName, String aKuleuvenID, String Email, String Blacklist){
        super(aUserName, aKuleuvenID, Email);
        wishItems = new ArrayList<Item>();
        borrowedItems = new ArrayList<BorrowedItem>();
        blacklistItems = new ArrayList<BorrowedItem>();
        blacklist = Blacklist;
    }
    public void setBorrowedItems(ArrayList<BorrowedItem> aBorrowedItems) {
        borrowedItems = aBorrowedItems;
    }

    public ArrayList<BorrowedItem> getBorrowedItems() {

        return borrowedItems;
    }

    public void setBlacklist(String Blacklist){
        blacklist = Blacklist;
    }

    public String getBlacklist(){
        return blacklist;
    }

    public boolean checkBlacklistItem() throws IOException {
        if(blacklistItems.isEmpty())
        {
            if(blacklist.equals("blacklist"))
            {
                blacklist = "normal";
                changeBlacklist();
            }
            return false;

        }
        else
        {
            blacklist = "blacklist";
            return true;
        }

    }

    @Override
    public void setDashboard() {
        try {
            getAllItem();
          //  getWishListFromDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setDashboardWishItems();
        setDashboardAlertItems();
        setDashboardExpiredItems();
    }

    public void setDashboardAlertItems() {
        for (BorrowedItem borrowedItem : borrowedItems) {
            int leftDays = Integer.parseInt(borrowedItem.getLeftDays());
            if ( leftDays <= TestJson.alertDay && leftDays >=0) {
                Item_Expiring_News news = new Item_Expiring_News(borrowedItem);
                dashboard.add(news);
            }
        }
    }

    public void setDashboardExpiredItems() {
        for (BorrowedItem borrowedItem : borrowedItems) {
            int leftDays = Integer.parseInt(borrowedItem.getLeftDays());
            if (leftDays<=0) {
                Item_Expiring_News news = new Item_Expiring_News(borrowedItem);
                blacklistItems.add(borrowedItem);

                if(blacklist.equals("normal")){
                    blacklist = "blacklist";
                    try {
                        changeBlacklist();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                dashboard.add(news);

            }
        }
        for(BorrowedItem borrowedItem:blacklistItems) {
            Log.i("expired item", borrowedItem.toString());
        }
    }

    //internet request
    public void getAllItem() throws IOException {
        byte[] array = getAllItem_createJson();
        wv.postUrl(CustomedWebview.getAllBorrowedItemsURL,array);
    }

    public void getWishListFromDatabase() throws IOException {
        byte[] array = getWishListFromDatabase_createJson();
        wv.postUrl(CustomedWebview.getAllWishListItemsURL, array);
    }

    public void addItemToWish(final Item item) throws IOException {
        byte[] array = addItemToWish_createJson(item);
        wv.postUrl(CustomedWebview.addItemToWishListURL, array);
    }

    public void removeItemFromWish(final Item item) throws IOException {
        byte[] array = removeItemFromWish_createJson(item);
        wv.postUrl(CustomedWebview.removeItemFromWishListURL, array);
    }

    public void changeBlacklist() throws IOException {
        byte[] array = changeBlacklist_createJson();
        wv.postUrl(CustomedWebview.changeBlackListStateURL, array);
    }


    private byte[] changeBlacklist_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
            postdata.put("state", blacklist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
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

    protected byte[] getAllItem_createJson() throws IOException {

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID", getKuleuvenID());
            postdata.put("cardID", getCardID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }

    @JavascriptInterface
    public void getAllItem_Interface(String htmlSource) {
        Log.i("get all items", htmlSource);
        try {
            borrowedItems = new ArrayList<BorrowedItem>();
            borrowedItemMAP = new HashMap<String, BorrowedItem>();
            wishItems = new ArrayList<Item>();

            JSONObject jsonObject = new JSONObject(htmlSource);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                BorrowedItem item = new BorrowedItem(json.getString("itemTag"));
                item.setBorrowedTimeStamp(json.getString("borrowTimestamp").substring(0,10));
                item.setBorrowedLocation(json.getString("borrowLocation"));
                item.setImageURL(json.getString("borrowLocation"));
                item.setClassification(json.getString("itemClassification"));
                borrowedItems.add(item);
                borrowedItemMAP.put(Integer.toString(i),item);
                Log.i(Integer.toString(i),item.toString());
            }
            JSONArray jsonArray2 = jsonObject.getJSONArray("wishlist");
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject json = jsonArray2.getJSONObject(i);
                Item item = new Item();
                item.setItemLocation(json.getString("itemLocation"));
                item.setClassification(json.getString("itemClassification"));
                wishItems.add(item);
                Log.i(Integer.toString(i),item.toString());
            }

        } catch (Throwable t) {
            Log.e("My info", "Could not parse malformed JSON for get all item: \"" + htmlSource + "\"");
        }
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

    @JavascriptInterface
    public void changeBlacklist_interface(String htmlSource) {
        Log.i("Update blacklist", htmlSource);
        try{
            JSONObject jsonObject = new JSONObject(htmlSource);
            error = jsonObject.getInt("error_message");
            if(error == 0)
                Log.i("success", "Success remove from blacklist");
            else if(error == 8)
                Log.i("error", "State remains the same");
            else
                Log.i("error", "Error update blacklist");
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
