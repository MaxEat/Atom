package com.example.max.testjson;

import android.util.Log;

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

    final OkHttpClient client = new OkHttpClient();
    static final MediaType MEDIA_TYPE = MediaType.parse("application/json");

    String registerPersonURL = "https://labtools.groept.be/inventory/sql/php_addPerson.php";
    String registerItemURL = "https://labtools.groept.be/inventory/sql/php_addPerson.php";
    String getAllItemsURL = "https://labtools.groept.be/inventory/sql/php_selectItemByUser.php";
    String borrowItemURL = "https://labtools.groept.be/inventory/sql/php_borrowItem.php";
    String returnItemURL = "https://labtools.groept.be/inventory/sql/php_returnItem.php";



    private void formCreateRequest(JSONObject postdata, final String Tag, String url){

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
                Log.i(Tag, response.toString());
            }
        });

    }
    private Request formQueryRequest(JSONObject postdata, String url) {

        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postdata.toString());
        final Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Your Token")
                .addHeader("cache-control", "no-cache")
                .build();
        return request;

    }

    public void addPerson(Person person) {

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

    public void addItem(Item item) {
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

    public void receiveItems(Person person) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("kuleuvenID", person.getKuleuvenID());

        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Request request = formQueryRequest(postdata, getAllItemsURL); // change latter

        // asynchronous call
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
                        Log.i("Items", mMessage);
                        JSONObject json = new JSONObject(mMessage);
                        final String serverResponse = json.getString("Your Index");
                        Log.i("Get item response", serverResponse);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    public void borrowItem(Person person, Item item) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("borrowLocation", "groept");
            postdata.put("cardID", "aaa");
            postdata.put("itemTag", "itemTag1");


        } catch(JSONException e){
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        formCreateRequest(postdata, "Borrow item", borrowItemURL);

    }

    public void returnItem(Person person, Item item) {
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

}
