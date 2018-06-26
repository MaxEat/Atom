package com.example.max.testjson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.webkit.JavascriptInterface;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static com.example.max.testjson.TestJson.wv;

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
    private int borrowDays;
    private int permissionDays;
    private String status;
    private Bitmap bitmap;
    private int error;

    public List<String> itemClassifications = new ArrayList<String>();

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

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getStatus() { return status;}

    public void setImageURL( String url) {
        imageURL = url;
    }

    public void setClassification(String classification) {
        this.classification = classification;
    }

    public void setItemLocation(String location) {
        itemLocation = location;
    }

    public void setStatus(String Status) { status = Status; }

    public boolean getAvailability() {
        if(status.equals("Returned")){
            return true;
        }
        return false;
    }

    public boolean checkItemAvailable(){
        if(status!="Borrowing" && status!="Maintaining")
            return true;
        else
            return false;
    }

    public void setBitmap() {
        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            bitmap = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            e.printStackTrace();
        }
    }

    public void setInfos() throws IOException {
        byte[] array = setInfos_createJson();
        wv.postUrl(CustomedWebview.getInfoByItemTagURL, array);
    }

    public void setImageFromDataBase() throws IOException {
        byte[] array = setInfos_createJson();
        wv.postUrl(CustomedWebview.getItemPictureURL, array);
    }


    // create internet json
    protected byte[] createJson(JSONObject postdata) throws IOException {
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }


    protected byte[] setInfos_createJson() throws IOException {

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", getItemTag());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }

    public byte[] setImageFromDatabase_createJson() throws IOException {
        JSONObject postdata = new JSONObject();

        try {
            postdata.put("itemClassification", getClassification());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return createJson(postdata);
    }



    @JavascriptInterface
    public void setInfos_interface(String htmlSource) {
        Log.i("set item info", htmlSource);
        try {
            JSONObject json = new JSONObject(htmlSource);
            itemLocation = json.getString("itemLocation");
            classification = json.getString("itemClassification");
            status = json.getString("itemStatus");

            error = json.getInt("error_message");
            Message m = new Message();
            Bundle b = new Bundle();
            b.putString("classification", classification);
            m.setData(b);
            m.what = 3;
            AddItemFragment.handler.sendMessage(m);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void setImageFromDatabase_interface(String htmlSource) {
        Log.i("Get item info", htmlSource);
        try{
            JSONObject json = new JSONObject(htmlSource);
            imageURL = json.getString("pictureUrl");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            InputStream in = new java.net.URL(imageURL).openStream();
            bitmap = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            Log.e("Error Message", e.getMessage());
            e.printStackTrace();
        }
    }

//    public void setInfos(final TextView view) {
//        JSONObject postdata = new JSONObject();
//        try {
//            postdata.put("itemTag", getItemTag());
//        } catch(JSONException e){
//            e.printStackTrace();
//        }
//
//        try {
//            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getInfoByItemTagURL, postdata.toString(),new BackgroundTask.MyCallback() {
//                @Override
//                public void onSuccess(String result) {
//                    Log.i("Success","result----"+result);
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        itemLocation = json.getString("itemLocation");
//                        classification = json.getString("itemClassification");
//                        status = json.getString("itemStatus");
//                        error = json.getInt("error_message");
//                        view.setText(itemLocation + " at " +classification);
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


//    public void setImageFromDatabase() {
//        final JSONObject postdata = new JSONObject();
//
//        try {
//            postdata.put("itemClassification", getClassification());
//            Response response = BackgroundTask.getInstance().postSyncJson(BackgroundTask.getItemPictureURL, postdata.toString());
//            if (response.isSuccessful()) {
//                String responseStr = response.body().string();
//                JSONObject json = new JSONObject(responseStr);
//                imageURL = json.getString("pictureUrl");
//                Log.i("Get item info", imageURL);
//
//                try {
//                    InputStream in = new java.net.URL(imageURL).openStream();
//                    bitmap = BitmapFactory.decodeStream(in);
//
//                } catch (Exception e) {
//                    Log.e("Error Message", e.getMessage());
//                    e.printStackTrace();
//                }
//
//            } else {
//                Log.i("Get item info", "error");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public String toString() {
        return "Item{ Tag='" + itemTag +
                ", Location=" + itemLocation +
                ", BoughTime=" + boughtTime +
                ", Classfication=" + classification +
                '}';
    }

}
