package com.example.max.testjson;

import android.util.Log;
import android.widget.Toast;

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
    private int error;
    private String kuleuvenID;
    private String cardID;
    private String email;
    private String userType;
    private String userName;
    private String alertEmail;
    private ArrayList<BorrowedItem> borrowedItems;
    private ArrayList<Item> wishItems;
    private Map<String, BorrowedItem> borrowedItemMAP = new HashMap<String, BorrowedItem>();
    private ArrayList<News> dashboard;

    Person(String aUserName, String aKuleuvenID, String Email) {
        kuleuvenID = aKuleuvenID;
        userName = aUserName;
        email = Email;
        alertEmail = Email;
        borrowedItems = new ArrayList<BorrowedItem>();
        wishItems = new ArrayList<Item>();


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

    public ArrayList<Item> getWishItems() {
        return wishItems;
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
                    Log.i("Success", "result----" + result);
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
                    Log.i("Success", "result----" + result);
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

    public ArrayList<News> getDashboard(){
        Log.i("number of wish", Integer.toString(wishItems.size()));
        dashboard = News.createDashBoard(this);
        Log.i("dashboard", Integer.toString(dashboard.size()));
        return dashboard;
    }

}

