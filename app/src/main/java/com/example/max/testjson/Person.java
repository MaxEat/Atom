package com.example.max.testjson;

import android.util.Log;

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
    private String kuleuvenID;
    private String cardID;
    private String email;
    private int userType;
    private String userName;
    private ArrayList<BorrowedItem> borrowedItems;
    private Map<String, BorrowedItem> borrowedItemMAP = new HashMap<String, BorrowedItem>();

    public ArrayList<BorrowedItem> getBorrowedItems() {
        return borrowedItems;
    }


    int error;

    Person() {}

    Person(String aUserName, String aKuleuvenID, String Email ) {
        kuleuvenID = aKuleuvenID;
        userName = aUserName;
        email = Email;
        userType = 2;
        borrowedItems = new ArrayList<BorrowedItem>();
    }


    public void setKuleuvenID(String kuleuvenID) {
        this.kuleuvenID = kuleuvenID;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(int userType) {
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

    public int getUserType() {
        return userType;
    }

    public String getUserName() {
        return userName;
    }



    public int register() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID", kuleuvenID);
            postdata.put("cardID", cardID );
            postdata.put("email", email);
            postdata.put("userName", userName);
            postdata.put("userType", userType);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.registerPersonURL, postdata.toString(),new BackgroundTask.MyCallback() {
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
        return error;
    }

    public void getAllItem() {

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID",  getKuleuvenID());

        } catch(JSONException e){
            e.printStackTrace();
        }

        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getAllBorrowedItemsURL, postdata.toString(),new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success","result----"+result);
                    try {
                        borrowedItems = new ArrayList<BorrowedItem>();
                        borrowedItemMAP = new HashMap<String, BorrowedItem>();

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for(int i=0; i<jsonArray.length(); i++){
                            JSONObject json= jsonArray.getJSONObject(i);
                            BorrowedItem item = new BorrowedItem();
                            item.setBorrowedTimeStamp(json.getString("borrowTimestamp"));
                            item.setBorrwedLocation(json.getString("borrowLocation"));
                            item.setImageURL(json.getString("borrowLocation"));
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

        if(currentLocation == "") currentLocation = "GroepT";

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", this.getCardID());
            postdata.put("itemTag", itemTag);
            postdata.put("borrowLocation", currentLocation);
        } catch(JSONException e){
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.borrowItemURL, postdata.toString(),new BackgroundTask.MyCallback() {
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
        return error;
    }

    public int returnItem(Item item) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", "aaa");
            postdata.put("itemTag", "itemTag1");
            postdata.put("returnLocation", "groept");
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.returnItemURL, postdata.toString(),new BackgroundTask.MyCallback() {
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
        return error;
    }

    public static void duplicatePerson(String cardid){
        JSONObject postdata = new JSONObject();
        try{
            postdata.put("cardID", cardid);
        } catch (JSONException e){
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.duplicatePersonURL, postdata.toString(),new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success","result----"+result);
                    try {
                        JSONObject json = new JSONObject(result);
                        int error = json.getInt("error_message");
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

}
