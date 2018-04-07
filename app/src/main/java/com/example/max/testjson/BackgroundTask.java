package com.example.max.testjson;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by max on 2018/4/5.
 */

public class BackgroundTask {

    static final OkHttpClient client = new OkHttpClient();
    static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    static private Context context;

    static String registerPersonURL = "https://labtools.groept.be/inventory/sql/php_addPerson.php";
    static String registerItemURL = "https://labtools.groept.be/inventory/sql/php_addItem.php";
    static String getAllItemsURL = "https://labtools.groept.be/inventory/sql/php_selectItemByUser.php";
    static String borrowItemURL = "https://labtools.groept.be/inventory/sql/php_borrowItem.php";
    static String returnItemURL = "https://labtools.groept.be/inventory/sql/php_returnItem.php";
    static String duplicatePersonURL = "https://labtools.groept.be/inventory/sql/php_duplicatePerson.php";


    BackgroundTask(Context context){
        this.context=context;
    }

    private static void formCreateRequest(JSONObject postdata, final String Tag, String url){


        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Your Token")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {
                String mMessage = response.body().string();
                // if return error message = 1, the borrowed item already returned
                // if return error message = 2, the item already exist
                // if return error message = 3, the person already exist
                Log.i(Tag, response.toString());
                if (response.isSuccessful()){
                    try {
                        Log.i(Tag, mMessage);
                        JSONObject json = new JSONObject(mMessage);
                        int error = json.getInt("error_message");
                        Log.i("error", Integer.toString(error));
                        if(error == 1)
                            Toast.makeText(context, "This item is already returned", Toast.LENGTH_SHORT).show();
                        if(error == 2)
                            Toast.makeText(context, "This item is already exist", Toast.LENGTH_SHORT).show();
                        if(error == 3)
                            Toast.makeText(context, "This person is already exist", Toast.LENGTH_SHORT).show();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    private static void formQueryRequest(JSONObject postdata, final String Tag,  String url) {

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Your Token")
                .addHeader("cache-control", "no-cache")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {

                String mMessage = response.body().string();

                if (response.isSuccessful()){
                    try {
                        Log.i(Tag, mMessage);
                        JSONObject json = new JSONObject(mMessage);
                        String error = json.getString("error_message");

                        Log.i("error", error);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });

    }

    public static void addPerson(Person person) {

        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID", person.getKuleuvenID());
            postdata.put("cardID", person.getCardID());
            postdata.put("email", person.getEmail());
            postdata.put("userType", person.getUserType());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        formCreateRequest(postdata, "Add Person", registerPersonURL);


    }

    public static void addItem(Item item) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("itemTag", item.getItemTag());
            postdata.put("itemLocation", item.getItemLocation());
            postdata.put("boughtTime", item.getBoughtTime());
            postdata.put("itemPermission", item.getItemPermission());
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        formCreateRequest(postdata, "Add Item", registerItemURL);

    }

    public static void receiveItems(Person person) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID", person.getKuleuvenID());

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        formQueryRequest(postdata, "All items", getAllItemsURL); // change latter


    }

    public static void borrowItem(Person person, Item item) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", "aaa");
            postdata.put("itemTag", "itemTag1");
            postdata.put("borrowLocation", "groept");
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        formCreateRequest(postdata, "Borrow item", borrowItemURL);

    }

    public static void returnItem(Person person, Item item) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", "aaa");
            postdata.put("itemTag", "itemTag1");
            postdata.put("returnLocation", "groept");
        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        formCreateRequest(postdata, "Return item", returnItemURL);

    }

    public static void duplicatePerson(String cardID){
        JSONObject postdata = new JSONObject();
        try{
            postdata.put("cardID", cardID);
        } catch (JSONException e){
            e.printStackTrace();
        }
        formCreateRequest(postdata, "Decide person duplicate", duplicatePersonURL);

    }

}
