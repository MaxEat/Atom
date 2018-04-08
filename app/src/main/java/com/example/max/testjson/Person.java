package com.example.max.testjson;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by max on 2018/4/5.
 */

public class Person {
    private String kuleuvenID;
    private String cardID;
    private String email;
    private int userType;
    private String userName;
    private ArrayList<Item> items;
    int error;

    Person() {}

    Person(String aUserName, String aKuleuvenID, String Email ) {
        kuleuvenID = aKuleuvenID;
        userName = aUserName;
        email = Email;
        userType = 2;
        items = new ArrayList<Item>();
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

    public int getPersonByCardID(String cardID) {

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", cardID);
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getInfoByCardURL, postdata.toString(),new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success","result----"+result);
                    try {
                        JSONObject json = new JSONObject(result);
                        error = json.getInt("error_message");
                        kuleuvenID = json.getString("kuleuvenID");
                        email = json.getString("email");
                        userType = json.getInt("userType");
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
            postdata.put("kuleuvenID", "r0609260");

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getAllItemsURL, postdata.toString(),new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success","result----"+result);
                    try {
                        JSONObject json = new JSONObject(result);

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

    public int borrowItem(Item item) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", "aaa");
            postdata.put("itemTag", "itemTag1");
            postdata.put("borrowLocation", "groept");
        } catch(JSONException e){
            // TODO Auto-generated catch block
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
