package com.example.max.testjson;

import android.util.Log;
import android.widget.Toast;

import com.example.max.testjson.dashboard.Item_Expiring_News;
import com.example.max.testjson.dashboard.News;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by max on 2018/4/5.
 */

public class Person {
    protected int error;
    protected String kuleuvenID;
    protected String cardID;
    protected String email;
    protected String userType;
    protected String userName;
    protected String alertEmail;
    protected ArrayList<BorrowedItem> borrowedItems;
    protected Map<String, BorrowedItem> borrowedItemMAP = new HashMap<String, BorrowedItem>();
    protected ArrayList<News> dashboard;

    Person(String aUserName, String aKuleuvenID, String Email) {
        kuleuvenID = aKuleuvenID;
        userName = aUserName;
        email = Email;
        alertEmail = Email;
        borrowedItems = new ArrayList<BorrowedItem>();
        dashboard = new ArrayList<News>();
    }

    public ArrayList<BorrowedItem> getBorrowedItems() {

        return borrowedItems;
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

    public String getUserType() {
        return userType;
    }

    public String getUserName() {
        return userName;
    }



    public int register() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID", kuleuvenID);
            postdata.put("cardID", cardID);
            postdata.put("email", email);
            postdata.put("userName", userName);
            postdata.put("userType", userType);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.registerPersonURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success", "result----" + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        error = json.getInt("error_message");
                        Log.i("error", Integer.toString(error));

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

    public String getAlertEmail() {
        return alertEmail;
    }

    public void getAllItem() {

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID", getKuleuvenID());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getAllBorrowedItemsURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success get items", "result----" + result);
                    try {
                        borrowedItems = new ArrayList<BorrowedItem>();
                        borrowedItemMAP = new HashMap<String, BorrowedItem>();

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            BorrowedItem item = new BorrowedItem(json.getString("itemTag"));
                            item.setBorrowedTimeStamp(json.getString("borrowTimestamp").substring(0,10));
                            item.setBorrowedLocation(json.getString("borrowLocation"));
                            item.setImageURL(json.getString("borrowLocation"));
                            item.setClassification(json.getString("itemClassification"));
                            borrowedItems.add(item);
                            borrowedItemMAP.put(Integer.toString(item.getId()), item);
                        }
                        setDashboardAlertItems();
                        setDashboardExpiredItems();

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

    public void updateItemState(String itemTag){
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", itemTag);
        } catch(JSONException e){
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.updateItemStateUrl, postdata.toString(),new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success","result----"+result);
                    try {
                        JSONObject json = new JSONObject(result);
                        error = json.getInt("error_message");
                        Log.i("error", Integer.toString(error));
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

    public int borrowItem(String itemTag, String currentLocation) {

        if (currentLocation == "") currentLocation = "GroepT";

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", this.getCardID());
            postdata.put("itemTag", itemTag);
            postdata.put("borrowLocation", currentLocation);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.borrowItemURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success", "result----" + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        error = json.getInt("error_message");
                        Log.i("error", Integer.toString(error));
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

    public int returnItem(Item item) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", getCardID());
            postdata.put("itemTag", item.getItemTag());
            postdata.put("returnLocation", item.getItemLocation());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.returnItemURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success", "result----" + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        error = json.getInt("error_message");
                        Log.i("error", Integer.toString(error));
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

    public ArrayList<News> getDashboard(){
        return dashboard;
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
            if (leftDays<0) {
                Item_Expiring_News news = new Item_Expiring_News(borrowedItem);
                dashboard.add(news);
            }
        }
    }
}

