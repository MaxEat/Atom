package com.example.max.testjson;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

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

    static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    //代表的就是请求包体内容的类型。
    static private Context context;

    static String registerPersonURL = "https://labtools.groept.be/inventory/sql/php_addPerson.php";
    static String registerItemURL = "https://labtools.groept.be/inventory/sql/php_addItem.php";
    static String getAllBorrowedItemsURL = "https://labtools.groept.be/inventory/sql/php_selectItemByUser.php";
    static String borrowItemURL = "https://labtools.groept.be/inventory/sql/php_borrowItem.php";
    static String returnItemURL = "https://labtools.groept.be/inventory/sql/php_returnItem.php";
    static String duplicatePersonURL = "https://labtools.groept.be/inventory/sql/php_duplicatePerson.php";
    static String getInfoByCardURL = "https://labtools.groept.be/inventory/sql/php_getUserInfoByCard.php";
    static String getAllAvailableItemsURL = "https://labtools.groept.be/inventory/sql/php_selectAvailableItem.php";

    private Handler okHttpHandler;
    private OkHttpClient mOkHttpClient;

    private BackgroundTask() {
        this.mOkHttpClient = TestJson.getInstance().getOkHttpClient();
        this.okHttpHandler = new Handler(Looper.getMainLooper());
    }


    public static final BackgroundTask getInstance(){
        return SingleFactory.manger;
    }
    private static final class SingleFactory{
        private static final  BackgroundTask manger = new BackgroundTask();
    }

    interface MyCallback{
        void onSuccess(String result);
        void onFailture();
    }

    public void postAsyncJsonn(String url, String json, MyCallback mCallback) throws IOException {
        final RequestBody requestBody = RequestBody.create(MEDIA_TYPE, json);
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        deliveryResult(mOkHttpClient.newCall(request),mCallback);
    }

    private void deliveryResult(final Call call, final  MyCallback mCallback) {
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                okHttpHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallback != null) {
                            mCallback.onFailture();
                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String responseStr = response.body().string();

                okHttpHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallback != null) {
                            mCallback.onSuccess(responseStr);
                        }
                    }
                });

            }
        });
    }

}
