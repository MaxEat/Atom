package com.example.max.testjson;

import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.example.max.testjson.dashboard.News;
import com.example.max.testjson.dashboard.Remind_Expired_Student_News;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.max.testjson.TestJson.wv;

/**
 * Created by max on 2018/4/13.
 */

public class Worker extends Person{

    private ArrayList<ExpiredItem> expiredItems;
    private ArrayList<ManageItem> manageItems;
    private HashMap<String, ManageItem> manageItemHashMap;

    Worker(String CardID) {super(CardID);}

    Worker(String aUserName, String aKuleuvenID, String Email){
        super(aUserName, aKuleuvenID, Email);
        expiredItems = new ArrayList<ExpiredItem>();
        manageItems = new ArrayList<ManageItem>();
        manageItemHashMap = new HashMap<>();

    }

    public ArrayList<ManageItem> getManageItems() {
        return manageItems;
    }

    public void setManageItems(ArrayList<ManageItem> manageItems) {
        this.manageItems = manageItems;
    }

    public HashMap<String, ManageItem> getManageItemHashMap() {
        return manageItemHashMap;
    }

    public void setManageItemHashMap(HashMap<String, ManageItem> manageItemHashMap) {
        this.manageItemHashMap = manageItemHashMap;
    }

    public ArrayList<ExpiredItem> getExpiredItems() {
        return expiredItems;
    }

    public void initializeWorker() throws IOException {
        byte[] array = initializeWorker_createJson();
        wv.postUrl(wv.initializeWorkerURL, array);

    }

    public byte[] initializeWorker_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }

    public void setDashboard() {
        try {
            getExpiredItemPersonDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void formPage() {
        Log.i("itemlist size", Integer.toString(itemList.size()));

        for(Item item:itemList){

            if(!item.getStatus().equals("available")) {
                if (manageItemHashMap.containsKey(item.getClassification() + item.getItemLocation())) {
                    ManageItem manageItem = manageItemHashMap.get(item.getClassification() + item.getItemLocation());
                    if (item.getStatus().equals("maintaining"))
                        manageItem.increaseMaintainNr();
                    if (item.getStatus().equals("borrowing"))
                        manageItem.increaseBorrwoNr();
                }
                else{
                    ManageItem newItem = new ManageItem();
                    newItem.setItemLocation(item.getItemLocation());
                    newItem.setClassification(item.getClassification());
                    if (item.getStatus().equals("maintaining"))
                        newItem.increaseMaintainNr();
                    if (item.getStatus().equals("borrowing"))
                        newItem.increaseBorrwoNr();
                    manageItems.add(newItem);
                    manageItemHashMap.put(newItem.getClassification() + newItem.getItemLocation(), newItem);
                }
            }
        }
        Message message = new Message();
        message.what = 5;
        Admin_AvailableItemFragment.handler.sendMessage(message);
//        for(Item item:itemList){
//            boolean exist = false;
//            for(AvailableItem i: availableItems){
//                if(item.getItemLocation().equals(i.getItemLocation()) &&
//                        item.getClassification().equals(i.getClassification())){
//                    i.increaseQuantity();
//                    exist = true;
//                    break;
//                }
//            }
//            if(!exist){
//                AvailableItem newItem = new AvailableItem();
//                newItem.setItemLocation(item.getItemLocation());
//                newItem.setClassification(item.getClassification());
//
//                availableItems.add(newItem);
//                availableItemMap.put(newItem.getClassification()+newItem.getItemLocation(), newItem);
//            }
//        }
//        Log.i("avail itemlist size", Integer.toString(availableItems.size()));
//        setMaintainingMark();

    }


    public void getItemsOfSameKind_Worker() throws IOException {
        byte[] array = getItemsOfSameKind_createJson();
        wv.postUrl(CustomedWebview.getItemsOfSameKindWorkerURL, array);
    }

    public void setDashboardExpiredPerson() {

        for(ExpiredItem expiredItem:expiredItems){
            Remind_Expired_Student_News news = new Remind_Expired_Student_News(expiredItem);
            if(dashboardMap.containsKey(expiredItem.getItemTag()))
                continue;
            dashboard.add(news);
            dashboardMap.put(expiredItem.getItemTag(), news);
        }
        Log.i("dash info size", Integer.toString(dashboard.size()));

    }


    public void getExpiredItemPersonDatabase() throws IOException {
        byte[] array = getExpiredItemPersonDatabase_createJson();
        wv.postUrl(CustomedWebview.getExpiredItemURL,array);
    }

    private byte[] getExpiredItemPersonDatabase_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }

    @JavascriptInterface
    public void initializeWorker_interface(String htmlSource) {
        Log.i("Admin_get initialize", htmlSource);
        try {
            expiredItems = new ArrayList<ExpiredItem>();
            manageItems = new ArrayList<ManageItem>();
            manageItemHashMap = new HashMap<>();
            availableItems = new ArrayList<AvailableItem>();
            availableItemMap = new HashMap<String, AvailableItem>();

            JSONObject jsonObject = new JSONObject(htmlSource);
            JSONArray jsonArrayExpire = jsonObject.getJSONArray("expire");
            JSONArray maintain = jsonObject.getJSONArray("maintainlist");
            JSONArray borrow = jsonObject.getJSONArray("borrowlist");
//
            for (int i = 0; i < jsonArrayExpire.length(); i++) {
                JSONObject json = jsonArrayExpire.getJSONObject(i);
                ExpiredItem item = new ExpiredItem(json.getString("itemTag"));
                item.setBorrowedTimeStamp(json.getString("borrowTimestamp").substring(0, 10));
                item.setBorrowedLocation(json.getString("borrowLocation"));
                item.setClassification(json.getString("itemClassification"));
                if(json.getString("preferedEmail").equals("null"))
                    item.setBorrowPersonEmail(json.getString("email"));
                else
                    item.setBorrowPersonEmail(json.getString("preferedEmail"));
                item.setBorrowPersonID(json.getString("userID"));
                item.setBorrowPersonName(json.getString("userName"));
                expiredItems.add(item);
                Log.i("person email", item.getBorrowPersonEmail());
            }

            for (int i = 0; i < maintain.length(); i++) {

                    JSONObject json = maintain.getJSONObject(i);
                    String itemClassification = json.getString("itemClassification");
                    String itemLocation = json.getString("itemLocation");
                    int quantity = json.getInt("quantity");

                    ManageItem newItem = new ManageItem();
                    newItem.setItemLocation(itemLocation);
                    newItem.setClassification(itemClassification);
                    newItem.setMaintainNr(quantity);
                    manageItems.add(newItem);
                    manageItemHashMap.put(newItem.getClassification() + newItem.getItemLocation(), newItem);

                }

            for(int i = 0; i<borrow.length(); i++) {
                JSONObject json = borrow.getJSONObject(i);
                String itemClassification = json.getString("itemClassification");
                String itemLocation = json.getString("itemLocation");
                int quantity = json.getInt("quantity");

                if(manageItemHashMap.containsKey(itemClassification+itemLocation)){
                    ManageItem item = manageItemHashMap.get(itemClassification+itemLocation);
                    item.setBorrowNr(quantity);
                }
                else {
                    ManageItem newItem = new ManageItem();
                    newItem.setItemLocation(itemLocation);
                    newItem.setClassification(itemClassification);
                    newItem.setBorrowNr(quantity);
                    manageItems.add(newItem);
                        manageItemHashMap.put(newItem.getClassification() + newItem.getItemLocation(), newItem);
                    }
            }


//            for (int i = 0; i < jsonArrayOverview.length(); i++) {
//                JSONObject json = jsonArrayOverview.getJSONObject(i);
//                String itemClassification = json.getString("itemClassification");
//                String itemLocation = json.getString("itemLocation");
//                String itemStatus = json.getString("itemStatus");
//                int quantity = json.getInt("quantity");
//
//                if(availableItemMap.containsKey(itemClassification + itemLocation)) {
//
//                    if (itemStatus.equals("available")) {
//                        AvailableItem item = availableItemMap.get(itemClassification + itemLocation);
//                        item.setQuantity(quantity);
//                        item.setStatus("available");
//                    }
//
//                }
//                else
//                {
//
//                    AvailableItem item = new AvailableItem();
//                    item.setQuantity(quantity);
//                    item.setItemLocation(itemLocation);
//                    item.setClassification(itemClassification);
//                    item.setStatus(itemStatus);
//
//                    availableItems.add(item);
//                    availableItemMap.put(item.getClassification() + item.getItemLocation(), item);
//                }
//
//            }

            setDashboardExpiredPerson();
            Message message = new Message();
            message.what = 2;
            MainActivity.uihander.sendMessage(message);

        }catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void getItemsSameKind_interface_Worker(String htmlSource){
        Log.i("get same kind info", htmlSource);
        try {

            JSONObject jsonObject = new JSONObject(htmlSource);
//            JSONArray sorting = jsonObject.getJSONArray("maintainlist");
            JSONArray maintain = jsonObject.getJSONArray("maintainlist");
            JSONArray borrow = jsonObject.getJSONArray("borrowlist");

            availableItems = new ArrayList<AvailableItem>();
            availableItemMap = new HashMap<String, AvailableItem>();
            manageItemHashMap = new HashMap<String, ManageItem>();
            manageItems = new ArrayList<ManageItem>();

            for (int i = 0; i < maintain.length(); i++) {

                JSONObject json = maintain.getJSONObject(i);
                String itemClassification = json.getString("itemClassification");
                String itemLocation = json.getString("itemLocation");
                int quantity = json.getInt("quantity");

                ManageItem newItem = new ManageItem();
                newItem.setItemLocation(itemLocation);
                newItem.setClassification(itemClassification);
                newItem.setMaintainNr(quantity);
                manageItems.add(newItem);
                manageItemHashMap.put(newItem.getClassification() + newItem.getItemLocation(), newItem);

            }

            for(int i = 0; i<borrow.length(); i++) {
                JSONObject json = borrow.getJSONObject(i);
                String itemClassification = json.getString("itemClassification");
                String itemLocation = json.getString("itemLocation");
                int quantity = json.getInt("quantity");

                if(manageItemHashMap.containsKey(itemClassification+itemLocation)){
                    ManageItem item = manageItemHashMap.get(itemClassification+itemLocation);
                    item.setBorrowNr(quantity);
                }
                else {
                    ManageItem newItem = new ManageItem();
                    newItem.setItemLocation(itemLocation);
                    newItem.setClassification(itemClassification);
                    newItem.setBorrowNr(quantity);
                    manageItems.add(newItem);
                    manageItemHashMap.put(newItem.getClassification() + newItem.getItemLocation(), newItem);
                }
            }
            Message message = new Message();
            message.what = 5;
            Admin_AvailableItemFragment.handler.sendMessage(message);

//            for (int i = 0; i < sorting.length(); i++) {
//                JSONObject json = sorting.getJSONObject(i);
//                String itemClassification = json.getString("itemClassification");
//                String itemLocation = json.getString("itemLocation");
//                String itemStatus = json.getString("itemStatus");
//                int quantity = json.getInt("quantity");
//
//                if(availableItemMap.containsKey(itemClassification + itemLocation)) {
//
//                    if (itemStatus.equals("available")) {
//                        AvailableItem item = availableItemMap.get(itemClassification + itemLocation);
//                        item.setQuantity(quantity);
//                        item.setStatus("available");
//                    }
//
//                }
//                else
//                {
//
//                    AvailableItem item = new AvailableItem();
//                    item.setQuantity(quantity);
//                    item.setItemLocation(itemLocation);
//                    item.setClassification(itemClassification);
//                    item.setStatus(itemStatus);
//
//
//                    availableItems.add(item);
//                    availableItemMap.put(item.getClassification() + item.getItemLocation(), item);
//                }
//
//            }

        } catch (Throwable t) {
            Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
        }
    }




    @JavascriptInterface
    public void getExpiredItemPersonDatabase_interface(String htmlSource) {

        Log.i("Admin get expire", htmlSource);
        try {
            expiredItems = new ArrayList<ExpiredItem>();
            JSONObject jsonObject = new JSONObject(htmlSource);

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                ExpiredItem item = new ExpiredItem(json.getString("itemTag"));
                item.setBorrowedTimeStamp(json.getString("borrowTimestamp").substring(0, 10));
                item.setBorrowedLocation(json.getString("borrowLocation"));
                item.setClassification(json.getString("itemClassification"));
                item.setBorrowPersonEmail(json.getString("email"));
                item.setBorrowPersonID(json.getString("userID"));
                item.setBorrowPersonName(json.getString("userName"));
                expiredItems.add(item);
            }


            setDashboardExpiredPerson();
        }catch (JSONException e) {
            e.printStackTrace();
        }
    }




    public boolean inMaintainList(Item item) {
        if(item == null)
            return false;
        if(item.getStatus().equals("maintaining")){
            return true;
        }
        return false;
    }



}
