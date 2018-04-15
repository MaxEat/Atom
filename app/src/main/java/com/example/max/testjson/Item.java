package com.example.max.testjson;

import android.util.Log;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Response;

/**
 * Created by max on 2018/4/5.
 */

public class Item implements Serializable {

    private String itemTag;
    private String itemLocation;
    private String boughtTime;
    private String classification;
    private int itemPermission;
    private String imageURL;
    private String status;
    int error;
    private static String[] itemClassifications;
    private static int classificationNumber;
    private String pictureNumberString;
    private int picturNumber;

    Item() { }

    Item( String ItemTag) {
        itemTag = ItemTag;
    }

    Item(String aitemTag, String aitemLocation) {
            itemTag = aitemTag;
            itemLocation = aitemLocation;
            itemPermission = 1;
    }

    public String getItemTag() {
        return itemTag;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public String getBoughtTime() {
        return boughtTime;
    }

    public String getClassification() {
        return classification;
    }

    public int getItemPermission() {
        return itemPermission;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL( String url) {
        imageURL = url;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setItemLocation(String location) {
        itemLocation = location;
    }

    public static String[] getAllClassifications(){

//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("itemTag", getItemTag());
//        } catch(JSONException e){
//            e.printStackTrace();
//        }
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getPictureNumberUrl, postdata.toString(),new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success","result----"+result);
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        pictureNumberString = json.getString("pictureNumber");
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
//
//        picturNumber = Integer.parseInt(pictureNumberString);
//        itemClassifications = new String[picturNumber];
//



        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID",null);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getAllClassificationsURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success get items", "result----" + result);
                    try {

                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray jsonArray = jsonObject.getJSONArray("list");

                        classificationNumber = jsonArray.length();
                        itemClassifications = new String[jsonArray.length()];

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            itemClassifications[i] = json.getString("itemPictureClassification");
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

        return itemClassifications;
    }

    public static int getClassificationNumber(){
        return classificationNumber;
    }


    public void setInfoSyn() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", getItemTag());
        } catch(JSONException e){
            e.printStackTrace();
        }
        try{
            Response response =   BackgroundTask.getInstance().postSyncJson(BackgroundTask.getInfoByItemTagURL, postdata.toString());
            if (response.isSuccessful()){
                String responseStr = response.body().string();
                Log.i("Get item info",responseStr);
            }else{
                Log.i("Get item info","error");
            }
            } catch (IOException e) {
                e.printStackTrace();
        }
    }

    public void setInfos(final TextView view) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", getItemTag());
        } catch(JSONException e){
            e.printStackTrace();
        }

        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getInfoByItemTagURL, postdata.toString(),new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success","result----"+result);
                    try {
                        JSONObject json = new JSONObject(result);
                        itemLocation = json.getString("itemLocation");
                        classification = json.getString("itemClassification");
                        status = json.getString("itemStatus");
                        error = json.getInt("error_message");
                        view.setText(itemLocation + " at " +classification);
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

    public int register() {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", getItemTag());
            postdata.put("itemLocation", getItemLocation());
            postdata.put("boughtTime", getBoughtTime());
            postdata.put("itemClassification", getClassification());
            postdata.put("itemPermission", getItemPermission());

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.registerItemURL, postdata.toString(),new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success","result----"+result);
                    try {
                        JSONObject json = new JSONObject(result);
                        error = json.getInt("error_message");
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

    public boolean checkItemAvailable(){
        if(status!="Borrowing" && status!="Maintaining")
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return "Item{" +
                "Tag='" + itemTag + '\'' +
                ", Location=" + itemLocation +
                ", BoughTime=" + boughtTime +
                ", Classfication=" + classification +
                '}';
    }

}
