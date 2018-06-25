package com.example.max.testjson;

import android.os.Bundle;
import android.os.Message;
import android.util.ArrayMap;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.max.testjson.dashboard.News;

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
 * Created by max on 2018/4/5.
 */

public abstract class Person {
    protected int error;
    public ArrayList<AvailableItem> wishItems;
    protected String kuleuvenID;
    protected String cardID;
    protected String email;
    protected String userType;
    protected String userName;
    protected String alertEmail;
    protected String headshotUrl;
    protected ArrayList<News> dashboard;
    protected Map<String, News> dashboardMap;
    public List<AvailableItem> availableItems = new ArrayList<AvailableItem>();
    public Map<String, AvailableItem> availableItemMap = new HashMap<String, AvailableItem>();
    //  key: getClassification()+newItem.getItemLocation()

    public List<Item> itemList = new ArrayList<Item>();


    Person(String CardID) {
        cardID = CardID;
    }

    Person(String aUserName, String aKuleuvenID, String Email) {
        kuleuvenID = aKuleuvenID;
        userName = aUserName;
        email = Email;
        alertEmail = Email;
        dashboard = new ArrayList<News>();
        dashboardMap = new HashMap<>();
    }

    public void setHeadshotUrl(String headshotUrl){
        this.headshotUrl = headshotUrl;
    }

    public String getHeadshotUrl(){
        return headshotUrl;
    }

    public void setAlertEmail(String alertEmail) {
        this.alertEmail = alertEmail;
    }

    public void setKuleuvenID(String kuleuvenID) {
        this.kuleuvenID = kuleuvenID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setCardID(String CardID) {
        cardID = CardID;
    }

    public String getKuleuvenID() {
        return kuleuvenID;
    }

    public String getCardID() {
        return cardID;
    }

    public String getEmail() {
        return email;
    }

    public String getAlertEmail() {
        return alertEmail;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setAvailableItems(List<AvailableItem> items) {
        availableItems = items;
    }

    public void setAvailableItemMap(Map<String, AvailableItem> map) {
        availableItemMap = map;
    }

    public ArrayList<News> getDashboard(){
        return dashboard;
    }

    public abstract void setDashboard();


    public void updateItemState(String itemTag) throws IOException {
        byte[] array = updateItemState_createJson(itemTag);
        wv.postUrl(CustomedWebview.updateItemStateUrl,array);
    }

    public void updateAlertEmail(String email) throws IOException {
        byte[] array = updateAlertEmail_createJson(email);
        wv.postUrl(CustomedWebview.updateAlertEmail, array);
    }

    public void addToAvailableList(String itemLocation, String itemClassification) {
        boolean exist = false;
        for(AvailableItem item:availableItems){
            if(item.getItemLocation().equals(itemLocation) && item.getClassification().equals(itemClassification)){
                item.increaseQuantity();
                exist = true;
                Log.i("item", itemClassification + " at " + itemLocation + ", quantity: " + item.getQuantity());
                break;
            }
        }
        if(!exist)
        {
            AvailableItem item = new AvailableItem();
            item.setItemLocation(itemLocation);
            item.setClassification(itemClassification);
            availableItems.add(item);
            availableItemMap.put(Integer.toString(item.getId()), item);
            Log.i("id", Integer.toString(item.getId()));
        }


    }

    public void getAllAvailableItems() throws IOException {
        byte[] array = getAllAvailableItems_createJson();
        wv.postUrl(CustomedWebview.getAllAvailableItemsURL, array);
    }

    public void getItemsOfSameKind() throws IOException {
        byte[] array = getItemsOfSameKind_createJson();
        wv.postUrl(CustomedWebview.getItemsOfSameKindURL, array);
    }



    public void administratorAddItem( String itemTag, String currentLocation, String timestamp, String itemClassification) throws IOException {
        byte[] array = administratorAddItem_createJson(itemTag, currentLocation, timestamp, itemClassification);
        wv.postUrl(CustomedWebview.addNewItemURL1, array);
    }

    public void administratorAddItemNew(String itemTag, String currentLocation, String timestamp, String itemClassification, int permissionDay) throws IOException{
        byte[] array = administratorAddItemNew_createJson(itemTag, currentLocation, timestamp, itemClassification, permissionDay);
        wv.postUrl(CustomedWebview.addNewItemURL2, array);
    }

    public byte[] getAllAvailableItems_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }

    // create internet json
    protected byte[] createJson(JSONObject postdata) throws IOException {
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }

    protected byte[] updateItemState_createJson(String itemTag) throws IOException {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", itemTag);
        } catch(JSONException e){
            e.printStackTrace();
        }
        return createJson(postdata);
    }

    protected byte[] getItemsOfSameKind_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        return createJson(postdata);
    }

    private byte[] updateAlertEmail_createJson(String email) throws IOException {
        JSONObject postdata = new JSONObject();
        try{
            postdata.put("cardID", cardID);
            postdata.put("preferedEmail", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }


    protected byte[] administratorAddItem_createJson(String itemTag, String currentLocation, String timestamp, String itemClassification) throws IOException {
        if (currentLocation == "") currentLocation = "GroepT";

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", itemTag);
            postdata.put("itemLocation", currentLocation);
            postdata.put("boughtTime", timestamp);
            postdata.put("itemClassification", itemClassification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }

    protected byte[] administratorAddItemNew_createJson(String itemTag, String currentLocation,
                                                        String timestamp, String itemClassification, int permissionDay) throws IOException{
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", itemTag);
            postdata.put("itemLocation", currentLocation);
            postdata.put("boughtTime", timestamp);
            postdata.put("itemClassification", itemClassification);
            postdata.put("permissionDay",permissionDay);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }

    public abstract void formPage();

    @JavascriptInterface
    public void login_interface(String htmlSource){
        Log.i("get register info", htmlSource);
        try {
            JSONObject obj = new JSONObject(htmlSource);
            userName = obj.getString("email").split("@")[0].replace(".", " ");
            kuleuvenID = obj.getString("user").split("@")[0];
            email = obj.getString("email");

        } catch (Throwable t) {
            Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
        }
    }

    @JavascriptInterface
    public void getItemsSameKind_interface(String htmlSource){
        Log.i("get same kind info", htmlSource);
        try {

            JSONObject jsonObject = new JSONObject(htmlSource);
            JSONArray sorting = jsonObject.getJSONArray("list");
            availableItems = new ArrayList<AvailableItem>();
            availableItemMap = new HashMap<String, AvailableItem>();

            for (int i = 0; i < sorting.length(); i++) {
                JSONObject json = sorting.getJSONObject(i);
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


                //刚加的
                //formPage();

            }

        } catch (Throwable t) {
            Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
        }
    }


    @JavascriptInterface
    public void updateItemStatus_Interface(String htmlSource) {
        Log.i("update item status",htmlSource);
        try {
            JSONObject json = new JSONObject(htmlSource);
            error = json.getInt("error_message");
            Log.i("error", Integer.toString(error));

            Message m = new Message();
            Bundle b = new Bundle();
            b.putString("title", "Maintain/UnMaintain");

            if(error == 21)
            {
                b.putString("result", "This item is being borrowed, cannot maintain/unmaintain it");
            }
            else
            {
                b.putString("result", "You have successfully update the item's status");
            }

            m.setData(b);
            m.what = 4;
            AddItemFragment.handler.sendMessage(m);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void administratorAddItem_interface(String htmlSource) {
        Log.i("admin add item", htmlSource);
        try {
            JSONObject json = new JSONObject(htmlSource);
            error = json.getInt("error_message");
            Log.i("error", Integer.toString(error));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void administratorAddItemNew_interface(String htmlSource) {
        Log.i("add new class item", htmlSource);
        try{
            JSONObject json = new JSONObject(htmlSource);
            error = json.getInt("error_message");
            Log.i("error", Integer.toString(error));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void getAllAvailableItems_interface(String htmlSource) {
        Log.i("Success get available", htmlSource);
        try {
            availableItems = new ArrayList<AvailableItem>();
            availableItemMap = new HashMap<String, AvailableItem>();
            itemList = new ArrayList<Item>();
            JSONObject jsonObject =  new JSONObject(htmlSource);;

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                Item  item = new Item(json.getString("itemTag"), json.getString("itemLocation"));
                item.setClassification(json.getString("itemClassification"));
                item.setStatus(json.getString("itemStatus"));
                itemList.add(item);
            }
            formPage();
        }catch (Throwable t) {
            Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
        }

    }

    @Override
    public String toString() {
        return "Person{" +
                "kuleuvenid='" + kuleuvenID +
                ", cardId=" + cardID +
                ", email=" + email +
                ", username=" + userName +
                ", userType=" + userType +
                '}';
    }



}

