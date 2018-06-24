package com.example.max.testjson;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.webkit.JavascriptInterface;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.max.testjson.dashboard.Item_Expired_News;
import com.example.max.testjson.dashboard.Item_Expiring_News;
import com.example.max.testjson.dashboard.News;
import com.example.max.testjson.dashboard.Wish_Item_Available_News;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.max.testjson.TestJson.wv;

/**
 * Created by max on 2018/4/14.
 */

public class Student extends Person {
    public ArrayList<AvailableItem> wishItems;
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
        wishItems = new ArrayList<AvailableItem>();
        borrowedItems = new ArrayList<BorrowedItem>();
        blacklistItems = new ArrayList<BorrowedItem>();
        itemList = new ArrayList<>();
        blacklist = "normal";
    }

    Student(String aUserName, String aKuleuvenID, String Email, String Blacklist){
        super(aUserName, aKuleuvenID, Email);
        wishItems = new ArrayList<AvailableItem>();
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
            if(blacklist.equals("normal")) {
                blacklist = "blacklist";
                changeBlacklist();
            }
            return true;
        }

    }

    @Override
    public void setDashboard() {
        dashboard = new ArrayList<News>();
        blacklistItems = new ArrayList<BorrowedItem>();
        setDashboardWishItems();
        setDashboardAlertItems();
        setDashboardExpiredItems();

    }

    public void setDashboardWishItems() {
        for (AvailableItem wish : availableItems) {
            if (wish.getInWishList()) {
                Wish_Item_Available_News news = new Wish_Item_Available_News(wish);
                dashboard.add(news);
            }
        }
//        for (AvailableItem wish : wishItems) {
//            if (wish.checkItemAvailable()) {
//                Wish_Item_Available_News news = new Wish_Item_Available_News(wish);
//                dashboard.add(news);
//            }
//        }
    }

    public void printAvailable(){
        for(AvailableItem item:availableItems){
            Log.i("info", "location: "+item.getItemLocation() + ", " +
                    "classification: "+ item.getClassification() + "in wishlist: " + item.getInWishList());
        }
    }

    @Override
    public void formPage()
    {
        Log.i("formpage", "worker");

        for(Item item:itemList){
            if(availableItemMap.containsKey(item.getClassification()+item.getItemLocation())){
                availableItemMap.get(item.getClassification()+item.getItemLocation()).increaseQuantity();
                continue;
            }

            AvailableItem newItem = new AvailableItem();
            newItem.setItemLocation(item.getItemLocation());
            newItem.setClassification(item.getClassification());

            availableItems.add(newItem);
            availableItemMap.put(newItem.getClassification()+newItem.getItemLocation(), newItem);
        }
        setWishMark();

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
                Item_Expired_News news = new Item_Expired_News(borrowedItem);
                blacklistItems.add(borrowedItem);
                dashboard.add(news);

            }
        }
        for(BorrowedItem borrowedItem:blacklistItems) {
            Log.i("expired item", borrowedItem.toString());
        }
    }

    public void addToDashBoard(AvailableItem item) throws IOException {
        if(availableItemMap.get(item.getClassification()+item.getItemLocation()).getInWishList())
            return;
        addItemToWish(item);
        Wish_Item_Available_News news = new Wish_Item_Available_News(item);
        dashboard.add(news);
    }

    public void removeWishFromDashBoard(AvailableItem item) throws IOException {
        availableItemMap.get(item.getClassification()+item.getItemLocation()).setInWishList(false);
        for(AvailableItem wishItem:wishItems){
            if(wishItem.getItemLocation().equals(item.getItemLocation()) && wishItem.getClassification().equals(item.getClassification())){
                wishItems.remove(wishItem);
                break;
            }
        }
        setDashboard();
        removeItemFromWish(item);

    }

    //internet request
    public void borrowItem( String itemTag, String currentLocation) throws IOException {


        byte[] array = borrowItem_createJson(itemTag, currentLocation);
        wv.postUrl(CustomedWebview.borrowItemURL, array);
    }

    public void returnItem( String itemTag, String currentLocation) throws IOException {

        for(BorrowedItem borrowedItem:borrowedItems){
            if(borrowedItem.getItemTag().equals(itemTag))
            {
                borrowedItems.remove(borrowedItem);
                break;
            }
        }
        for(BorrowedItem blacklistItem:blacklistItems){
            if(blacklistItem.getItemTag().equals(itemTag))
            {
                blacklistItems.remove(blacklistItem);
                break;
            }
        }
        if(blacklistItems.isEmpty())
            blacklist = "normal";
        else
            blacklist = "blacklist";
        Log.i("here", "return item");
        byte[] array = returnItem_createJson(itemTag, currentLocation);
        wv.postUrl(CustomedWebview.returnItemURL, array);
    }

    protected byte[] borrowItem_createJson(String itemTag, String currentLocation) throws IOException {

        if (currentLocation == "") currentLocation = "GroepT";

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
            postdata.put("itemTag", itemTag);
            postdata.put("borrowLocation", currentLocation);
            Log.i("post", postdata.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }

    protected byte[] returnItem_createJson(String itemTag, String currentLocation) throws IOException {

        if (currentLocation == "") currentLocation = "GroepT";

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
            postdata.put("itemTag", itemTag);
            postdata.put("returnLocation", currentLocation);
            postdata.put("blacklist", blacklist);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }

    public void getPersonalItems() throws IOException {
        byte[] array = getPersonalItems_createJson();
        wv.postUrl(CustomedWebview.getPersonalItemsURL,array);
    }

    public void getWishListFromDatabase() throws IOException {
        byte[] array = getWishListFromDatabase_createJson();
        wv.postUrl(CustomedWebview.getAllWishListItemsURL, array);
    }

    public void addItemToWish(AvailableItem item) throws IOException {
        availableItemMap.get(item.getClassification()+item.getItemLocation()).setInWishList(true);
       // item.setInWishList(true);
        wishItems.add(item);
        byte[] array = addItemToWish_createJson(item);
        wv.postUrl(CustomedWebview.addItemToWishListURL, array);
    }

    public void removeItemFromWish(AvailableItem item) throws IOException {
        item.setInWishList(false);


        byte[] array = removeItemFromWish_createJson(item);
        wv.postUrl(CustomedWebview.removeItemFromWishListURL, array);
    }

    public void changeBlacklist() throws IOException {
        byte[] array = changeBlacklist_createJson();
        wv.postUrl(CustomedWebview.changeBlackListStateURL, array);
    }


    private byte[] getPersonalItems_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
            postdata.put("kuleuvenID", getKuleuvenID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
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

    public byte[] addItemToWish_createJson(AvailableItem item) throws IOException {

      //  wishItems.add(item);

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
    public void getAllItem() throws IOException {
        byte[] array = getAllItem_createJson();
        wv.postUrl(CustomedWebview.getAllBorrowedItemsURL,array);
    }

    @JavascriptInterface
    public void getPersonalItems_interface(String htmlSource) {
        Log.i("get personal items", htmlSource);
        try {
            borrowedItems = new ArrayList<BorrowedItem>();
            borrowedItemMAP = new HashMap<String, BorrowedItem>();
            wishItems = new ArrayList<AvailableItem>();

            JSONObject jsonObject = new JSONObject(htmlSource);
            JSONArray jsonArray = jsonObject.getJSONArray("list");



            for (String keys : TestJson.permission_days.keySet())
            {
                System.out.println(keys + ":"+ TestJson.permission_days.get(keys));
            }

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                BorrowedItem item = new BorrowedItem(json.getString("itemTag"));
                item.setBorrowedTimeStamp(json.getString("borrowTimestamp").substring(0,10));
                item.setBorrowedLocation(json.getString("borrowLocation"));
                item.setImageURL(json.getString("borrowLocation"));
                item.setClassification(json.getString("itemClassification"));

                item.setLeftDays();
                borrowedItems.add(item);
                borrowedItemMAP.put(Integer.toString(i),item);
                Log.i("borrowed:"+Integer.toString(i),item.toString());
            }

            JSONArray jsonArray2 = jsonObject.getJSONArray("wishlist");
            for (int i = 0; i < jsonArray2.length(); i++) {
                JSONObject json = jsonArray2.getJSONObject(i);
                AvailableItem item = new AvailableItem();
                item.setItemLocation(json.getString("itemLocation"));
                item.setClassification(json.getString("itemClassification"));
                wishItems.add(item);
                Log.i("wish:"+Integer.toString(i),item.toString());
            }
            formPage();

            Message message = new Message();
            message.what = 1;
            MainActivity.uihander.sendMessage(message);



        } catch (Throwable t) {
            Log.i("get all item", t.getMessage());
            Log.e("My info", "Could not parse malformed JSON for get all item: \"" + htmlSource + "\"");
        }
    }

    private void setWishMark() {
        for(AvailableItem item:wishItems){
            String string = item.getClassification()+item.getItemLocation();
            if(availableItemMap.containsKey(string)){
                AvailableItem wishItem = availableItemMap.get(string);
                wishItem.setInWishList(true);
            }
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
                Log.i("error", "You have already removed this before");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void getWishListFromDatabase_interface(String htmlSource) {
        Log.i("Success get wish list", htmlSource);
        try {
            //wishItems = new ArrayList<AvailableItem>();
            JSONObject jsonObject = new JSONObject(htmlSource);
            JSONArray jsonArray = jsonObject.getJSONArray("wishlist");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                AvailableItem item = new AvailableItem();
                item.setItemLocation(json.getString("itemLocation"));
                item.setClassification(json.getString("itemClassification"));
             //   wishItems.add(item);
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

    @JavascriptInterface
    public void borrowItem_Interface(String htmlSource) {
        Log.i("borrow item", htmlSource);
        try {
            JSONObject json = new JSONObject(htmlSource);
            error = json.getInt("error_message");

            if(error == 0)
            {
                String itemTag = json.getString("itemTag");
                String itemLocation = json.getString("itemLocation");
                String itemClassification = json.getString("itemClassification");
                int permissionDays = TestJson.permission_days.get(itemClassification);


                Calendar c = Calendar.getInstance();
                Date currentDate = new Date();
                c.setTime(currentDate);
                c.add(Calendar.DATE, permissionDays); //same with c.add(Calendar.DAY_OF_MONTH, 1);
                Date currentDatePlusOne = c.getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                String dates = dateFormat.format(currentDatePlusOne);

                Message m = new Message();
                Bundle b = new Bundle();

                b.putString("title", "Borrowing");
                b.putString("result", "You have successfully borrowed this item, please return it before " + dates);
                b.putInt("permissionDays", permissionDays);
                m.setData(b);
                m.what = 4;
                AddItemFragment.handler.sendMessage(m);

                BorrowedItem borrowedItem = new BorrowedItem(itemTag, itemLocation);
                borrowedItem.setClassification(itemClassification);
                borrowedItems.add(borrowedItem);

                Log.i("new borrowing item", itemTag + " " + itemLocation);

            }
            else
            {
                Message m = new Message();
                Bundle b = new Bundle();
                b.putString("title", "Borrowing");
                b.putString("result", "You cannot borrow this item, it is already been borrowed");
                m.setData(b);
                m.what = 4;
                AddItemFragment.handler.sendMessage(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void returnItem_Interface(String htmlSource) {
        Log.i("return item", htmlSource);
        try {
            JSONObject json = new JSONObject(htmlSource);
            error = json.getInt("error_message");
            Log.i("error", Integer.toString(error));

            if(error == 0)
            {
                Message m = new Message();
                Bundle b = new Bundle();
                b.putString("title", "Returning");
                b.putString("result", "You have successfully returned this item");
                m.setData(b);
                m.what = 4;
                AddItemFragment.handler.sendMessage(m);
            }
            else
            {
                Message m = new Message();
                Bundle b = new Bundle();
                b.putString("title", "Returning");
                b.putString("result", "You cannot return this item");
                m.setData(b);
                m.what = 4;
                AddItemFragment.handler.sendMessage(m);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
