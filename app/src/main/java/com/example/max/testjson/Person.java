package com.example.max.testjson;

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
    protected String kuleuvenID;
    protected String cardID;
    protected String email;
    protected String userType;
    protected String userName;
    protected String alertEmail;
    protected ArrayList<News> dashboard;
    public List<AvailableItem> availableItems = new ArrayList<AvailableItem>();
    public Map<String, AvailableItem> availableItemMap = new HashMap<String, AvailableItem>();


    Person(String CardID) {
        cardID = CardID;
    }

    Person(String aUserName, String aKuleuvenID, String Email) {
        kuleuvenID = aKuleuvenID;
        userName = aUserName;
        email = Email;
        alertEmail = Email;
        dashboard = new ArrayList<News>();
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

    public ArrayList<News> getDashboard(){
        return dashboard;
    }

    public abstract void setDashboard();


    public void updateItemState(String itemTag) throws IOException {
        byte[] array = updateItemState_createJson(itemTag);
        wv.postUrl(CustomedWebview.updateItemStateUrl,array);
    }

    public void register() throws IOException {
        byte[] array = register_createJson();
        wv.postUrl(CustomedWebview.registerPersonURL, array);
    }

    public void borrowItem( String itemTag, String currentLocation) throws IOException {
        byte[] array = borrowItem_createJson(itemTag, currentLocation);
        wv.postUrl(CustomedWebview.borrowItemURL, array);
    }

    public void returnItem( String itemTag, String currentLocation) throws IOException {
        byte[] array = returnItem_createJson(itemTag, currentLocation);
        wv.postUrl(CustomedWebview.returnItemURL, array);
    }
    public void getAllAvailableItems() throws IOException {
        byte[] array = getAllAvailableItems_createJson();
        wv.postUrl(CustomedWebview.getAllAvailableItemsURL, array);
    }

    public void administratorAddItem( String itemTag, String currentLocation, String timestamp, String itemClassification,
                                      String itemPermission) throws IOException {
        byte[] array = administratorAddItem_createJson(itemTag, currentLocation, timestamp, itemClassification,
                itemPermission);
        wv.postUrl(CustomedWebview.addNewItemURL, array);
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

    protected byte[] borrowItem_createJson(String itemTag, String currentLocation) throws IOException {

        if (currentLocation == "") currentLocation = "GroepT";

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
            postdata.put("itemTag", itemTag);
            postdata.put("borrowLocation", currentLocation);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }

    protected byte[] register_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID", kuleuvenID);
            postdata.put("cardID", cardID);
            postdata.put("email", email);
            postdata.put("userName", userName);
            postdata.put("userType", userType);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }

    protected byte[] administratorAddItem_createJson(String itemTag, String currentLocation, String timestamp, String itemClassification,
                                                     String itemPermission) throws IOException {
        if (currentLocation == "") currentLocation = "GroepT";

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", itemTag);
            postdata.put("itemLocation", currentLocation);
            postdata.put("boughtTime", timestamp);
            postdata.put("itemClassification", itemClassification);
            postdata.put("itemPermission", itemPermission);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }

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
    public void registerPerson_interface(String htmlSource) {
        Log.i("register", htmlSource);
        try {
            JSONObject json = new JSONObject(htmlSource);
            error = json.getInt("error_message");
            Log.i("error", Integer.toString(error));

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
            Log.i("error", Integer.toString(error));
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void updateItemStatus_Interface(String htmlSource) {
        Log.i("update item status",htmlSource);
        try {
            JSONObject json = new JSONObject(htmlSource);
            error = json.getInt("error_message");
            Log.i("error", Integer.toString(error));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//
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
    public void getAllAvailableItems_interface(String htmlSource) {
        Log.i("Success get available", htmlSource);
        try {
            availableItems = new ArrayList<AvailableItem>();
            availableItemMap = new HashMap<String, AvailableItem>();
            JSONObject jsonObject =  new JSONObject(htmlSource);;

            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                AvailableItem item = new AvailableItem(json.getString("itemTag"), json.getString("itemLocation"));
                item.setClassification(json.getString("itemClassification"));
                item.setStatus(json.getString("itemStatus"));
                item.setId(i);
                availableItems.add(item);
                availableItemMap.put(Integer.toString(item.getId()), item);
            }
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

//    public void getAllItem() {
//
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("kuleuvenID", getKuleuvenID());
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getAllBorrowedItemsURL, postdata.toString(), new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success get items", "result----" + result);
//                    try {
//                        borrowedItems = new ArrayList<BorrowedItem>();
//                        borrowedItemMAP = new HashMap<String, BorrowedItem>();
//
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray jsonArray = jsonObject.getJSONArray("list");
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject json = jsonArray.getJSONObject(i);
//                            BorrowedItem item = new BorrowedItem(json.getString("itemTag"));
//                            item.setBorrowedTimeStamp(json.getString("borrowTimestamp").substring(0,10));
//                            item.setBorrowedLocation(json.getString("borrowLocation"));
//                            item.setImageURL(json.getString("borrowLocation"));
//                            item.setClassification(json.getString("itemClassification"));
//                            borrowedItems.add(item);
//                            borrowedItemMAP.put(Integer.toString(item.getId()), item);
//                        }
//                        setDashboardAlertItems();
//                        setDashboardExpiredItems();
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
//
//    }

//    public byte updateItemState(String itemTag){
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("itemTag", itemTag);
//        } catch(JSONException e){
//            e.printStackTrace();
//        }
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.updateItemStateUrl, postdata.toString(),new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success","result----"+result);
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        error = json.getInt("error_message");
//                        Log.i("error", Integer.toString(error));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }
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
//    public int borrowItem(String itemTag, String currentLocation) {
//
//        if (currentLocation == "") currentLocation = "GroepT";
//
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("cardID", this.getCardID());
//            postdata.put("itemTag", itemTag);
//            postdata.put("borrowLocation", currentLocation);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.borrowItemURLSecure, postdata.toString(), new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success", "result----" + result);
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        error = json.getInt("error_message");
//                        Log.i("error", Integer.toString(error));
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
//        return error;
//    }
//
//    public int returnItem(String itemTag, String currentLocation) {
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("cardID", getCardID());
//            postdata.put("itemTag", itemTag);
//            postdata.put("returnLocation", currentLocation);
//        } catch (JSONException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.returnItemURL, postdata.toString(), new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success return item", "result----" + result);
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        error = json.getInt("error_message");
//                        Log.i("error", Integer.toString(error));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
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
//        return error;
//    }
//


//    public int administratorAddItem(String itemTag, String currentLocation, String timestamp, String itemClassification,
//                                    String itemPermission) {
//
//        if (currentLocation == "") currentLocation = "GroepT";
//
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("itemTag", itemTag);
//            postdata.put("itemLocation", currentLocation);
//            postdata.put("boughtTime", timestamp);
//            postdata.put("itemClassification", itemClassification);
//            postdata.put("itemPermission", itemPermission);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.addNewItemURL, postdata.toString(), new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success", "result----" + result);
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        error = json.getInt("error_message");
//                        Log.i("error", Integer.toString(error));
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
//        return error;
//    }
//


}

