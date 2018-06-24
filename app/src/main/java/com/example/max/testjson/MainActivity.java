package com.example.max.testjson;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.max.testjson.dashboard.News;
import com.squareup.leakcanary.RefWatcher;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.max.testjson.CustomedWebview.baseURL;
import static com.example.max.testjson.CustomedWebview.duplicatePersonURL;
import static com.example.max.testjson.TestJson.wv;

public class MainActivity extends  AppCompatActivity implements BorrowedFragment.OnListFragmentInteractionListener, AvailableItemFragment.OnListFragmentInteractionListener,Admin_AvailableItemFragment.OnListFragmentInteractionListener,SettingFragment.OnFragmentInteractionListener, DashBoardFragment.OnListFragmentInteractionListener,ScanResultReceiver{

    private android.support.v4.app.Fragment[]mFragments;
    private static final int CAMERA= 115;
    Fragment fragment = null;
    private String cardID;
    private String kuleuvenID = "None";
    private String userName = "None";
    private String email = "None";
    private String userType = "";
    private String blacklist;
    private String role;
    public List<AvailableItem> availableItems = new ArrayList<AvailableItem>();
    public Map<String, AvailableItem> availableItemMap = new HashMap<String, AvailableItem>();
    public static Handler uihander;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cardID = intent.getStringExtra("cardID");

        setContentView(R.layout.activity_main);
        getPermission();

        mFragments = DataGenerator.getFragments("BottomNavigationView Tab");



        uihander = new Handler() {
            @Override
            public void handleMessage(Message inputMessage) {
                Fragment fragment;
                // Gets the image task from the incoming Message object.
                switch (inputMessage.what) {
                    case 1:
                        Log.i("ui hander 1", "go to student's dashboard");
                        fragment = mFragments[5];
                        if (fragment != null) {
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.home_container_main, fragment).commit();
                            break;
                        }
                        break;
                    case 2:
                        Log.i("ui hander 2", "go to administrator's dashboard");
                        fragment = mFragments[6];
                        if (fragment != null) {
                            getSupportFragmentManager().beginTransaction().replace(
                                    R.id.home_container_main, fragment).commit();
                            break;
                        }
                        break;
                }
            }
        };


        wv = (CustomedWebview) findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);

        wv.addJavascriptInterface(new MainActivity.getFormData(),"local");
        wv.show();
        wv.loadUrl(CustomedWebview.baseURL);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }



    public void getPermission() {

        boolean cameraPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

        if (!cameraPermission) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA
            );
        }
    }

    @SuppressLint("JavascriptInterface")
    public void checkUserType(final String kuleuvenID, final String userName, final String email, final String userType, final String id, final String blacklist) throws IOException {

        runOnUiThread(new Runnable() {
            @Override
            public void run(){

                if(userType.equals("Administrator"))

                {
                    showDialog(kuleuvenID, userName, email, userType, id);
                }
                else

                {
                    Student user = new Student(userName, kuleuvenID, email, blacklist);
                    user.setUserType(userType);
                    user.setCardID(id);
                    wv.addJavascriptInterface(user, "Person");
                    TestJson.setUser(user);

                    try {

                        ((Student)TestJson.getUser()).getAllItem();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fragment = mFragments[5];

                    if (fragment != null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_container_main, fragment).commit();
                    }
                }
            }
            });

    }
    public void showDialog(final String kuleuvenID, final String userName, final String email, final String userType, final String cardid) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.administrator, new DialogInterface.OnClickListener() {
            @SuppressLint("JavascriptInterface")
            public void onClick(DialogInterface dialog, int id) {

                Person user = new Worker(userName, kuleuvenID, email);
                user.setCardID(cardid);
                user.setUserType("Administrator");
                user.setAvailableItems(availableItems);
                user.setAvailableItemMap(availableItemMap);
                TestJson.setUser(user);

                wv.addJavascriptInterface(TestJson.getUser(), "Person");
                try {
                    ((Worker)TestJson.getUser()).initializeWorker();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });
        builder.setNegativeButton(R.string.user, new DialogInterface.OnClickListener() {
            @SuppressLint("JavascriptInterface")
            public void onClick(DialogInterface dialog, int id) {

                Person user = new Student(userName, kuleuvenID, email, blacklist);
                user.setAvailableItems(availableItems);
                user.setAvailableItemMap(availableItemMap);
                user.setCardID(cardid);
                user.setUserType("Student");
                TestJson.setUser(user);

                wv.addJavascriptInterface(user, "Person");
                try {

                    ((Student)TestJson.getUser()).getPersonalItems();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
}

    public void check() throws IOException {
        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                wv.hide();
                JSONObject postdata = new JSONObject();
                try {
                    postdata.put("cardID", cardID);
                    postdata.put("kuleuvenID", kuleuvenID);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                StringEntity se = null;
                byte[] array = null;
                try {
                    se = new StringEntity(postdata.toString(),"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                se.setContentType("application/json");
                try {
                    array = EntityUtils.toByteArray(se);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                wv.postUrl(duplicatePersonURL, array);
            }
        });
    }

    public void cleanSession() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Log.d("clear wv", "Using clearCookies code for API >=" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieManager.getInstance().removeAllCookies(null);
            CookieManager.getInstance().flush();
        } else
        {
            Log.d("clear wv", "Using clearCookies code for API <" + String.valueOf(Build.VERSION_CODES.LOLLIPOP_MR1));
            CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(this);
            cookieSyncMngr.startSync();
            CookieManager cookieManager=CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieManager.removeSessionCookie();
            cookieSyncMngr.stopSync();
            cookieSyncMngr.sync();
        }
        TestJson.resetWebView();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
      //  libInstance.startForeGroundDispatch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = TestJson.getRefWatcher(this);
        refWatcher.watch(this);

        //自动logout

    }

    @Override
    public void onListFragmentInteraction(BorrowedItem borrowedItem) {
        Intent intent = new Intent(this, Borrowed_Item_Detail.class);
        intent.putExtra("item",borrowedItem);
        startActivity(intent);
    }

    @Override
    public void onListFragmentInteraction(AvailableItem item) {

    }


    @Override
    public void scanResultData(String codeFormat, String codeContent) {
        Toast.makeText(this, "FORMAT: " + codeFormat, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "CONTENT: " + codeContent, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void scanResultData(NoScanResultException noScanData) {
        Toast toast = Toast.makeText(this,noScanData.getMessage(), Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(News item) {

    }


    private final class getFormData {
        @JavascriptInterface
        public void getRegisterInfo(String htmlSource) {

            Log.i("Get kuleuven info", htmlSource);
            try {
                JSONObject obj = new JSONObject(htmlSource);
                userName = obj.getString("email").split("@")[0].replace(".", " ");
                kuleuvenID = obj.getString("user").split("@")[0];
                email = obj.getString("email");
                role = obj.getString("role");


                check();
            } catch (Throwable t) {
                Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
            }
        }

        @JavascriptInterface
        public void checkPersonInfo(String htmlSource) {

            Log.i("Check person", htmlSource);
            try {


                JSONObject jsonObject = new JSONObject(htmlSource);

                JSONObject jsonUser = jsonObject.getJSONObject("user");
                int error = jsonUser.getInt("error_message");



                if(error == 0) {
                    userType = jsonUser.getString("userType");
                    blacklist = jsonUser.getString("state");


                    JSONArray permissionTypeArray = jsonObject.getJSONArray("permissionTypeList");
                    JSONArray pictureArray = jsonObject.getJSONArray("pictureList");
                    JSONArray locationArray = jsonObject.getJSONArray("locationList");
                    JSONArray classificationArray = jsonObject.getJSONArray("classificationList");

                    JSONArray sorting = jsonObject.getJSONArray("sorting");

                   // TestJson.permissionArray = new String[permissionTypeArray.length()];
                    TestJson.permission_days = new HashMap<>();
                    TestJson.classificationArray = new String[classificationArray.length()];
                    TestJson.locationArray = new String[locationArray.length()];
                    TestJson.classificationPictureArray = new String[pictureArray.length()];
                    availableItems = new ArrayList<AvailableItem>();
                    availableItemMap = new HashMap<String, AvailableItem>();

                    for (int i = 0; i < permissionTypeArray.length(); i++) {
                        JSONObject json = permissionTypeArray.getJSONObject(i);
                        TestJson.permission_days.put(json.getString("permissionItemClassification"), json.getInt("permissionDay"));
                        Log.i("permission value " + i, json.toString() + "\"");
                    }

                    for (int i = 0; i < pictureArray.length(); i++) {
                        JSONObject json = pictureArray.getJSONObject(i);
                        TestJson.classificationPictureArray[i] = json.getString("itemPictureClassification");
                        TestJson.pictureMap.put(json.getString("itemPictureClassification"), json.getString("pictureUrl"));
                        Log.i("picture classification " + i, TestJson.classificationPictureArray[i] + "\"" + json.getString("pictureUrl"));
                    }

                    for (int i = 0; i < locationArray.length(); i++) {
                        JSONObject json = locationArray.getJSONObject(i);
                        TestJson.locationArray[i] = json.getString("itemLocation");
                        Log.i("location " + i, TestJson.locationArray[i] + "\"");
                    }

                    for (int i = 0; i < classificationArray.length(); i++) {
                        JSONObject json = classificationArray.getJSONObject(i);
                        TestJson.classificationArray[i] = json.getString("itemClassification");
                        Log.i("classification " + i, TestJson.classificationArray[i] + "\"");
                    }

                    for (int i = 0; i < sorting.length(); i++) {
                        JSONObject json = sorting.getJSONObject(i);
                        String itemClassification = json.getString("itemClassification");
                        String itemLocation = json.getString("itemLocation");
                        String itemStatus = json.getString("itemStatus");
                        int quantity = json.getInt("quantity");

                        if(availableItemMap.containsKey(itemClassification + itemLocation)) {

                            if (itemStatus.equals("available")) {
                                AvailableItem item = availableItemMap.get(itemClassification + itemLocation);
                                item.setQuantity(quantity);
                                item.setStatus("available");
                            }

                        }
                        else
                        {

                            AvailableItem item = new AvailableItem();
                            item.setQuantity(quantity);
                            item.setItemLocation(itemLocation);
                            item.setClassification(itemClassification);
                            item.setStatus(itemStatus);

                            availableItems.add(item);
                            availableItemMap.put(item.getClassification() + item.getItemLocation(), item);
                        }

                    }


                    checkUserType(kuleuvenID, userName, email, userType, cardID, blacklist);
                }
                if(error == 4){

                    fragment = mFragments[7];
                    Bundle bundle = new Bundle();
                    bundle.putString("cardID",cardID);
                    bundle.putString("userName",userName);
                    bundle.putString("email",email);
                    bundle.putString("kuleuvenID", kuleuvenID);
                    bundle.putString("role", role);
                    fragment.setArguments(bundle);

                    if(fragment!=null) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.home_container_main,fragment).commit();
                    }
                }
                if(error == 11){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            wv.hide();
                            cleanSession();

                            wv = (CustomedWebview) findViewById(R.id.webview);
                            wv.getSettings().setJavaScriptEnabled(true);
                            wv.getSettings().setDomStorageEnabled(true);

                            wv.addJavascriptInterface(new MainActivity.getFormData(),"local");
                            wv.show();
                            wv.loadUrl(CustomedWebview.baseURL);
                            Toast.makeText(getApplication(), "Please log in the correct username and password", Toast.LENGTH_SHORT);
                        }
                    });

                }
            } catch (Throwable t) {
                Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
            }
        }

    }



    public void getPermissionClassification() throws IOException {
        byte[] array = getPermissionClassification_createJson();
        wv.postUrl(CustomedWebview.getPermissionClassificationURL, array);
    }

    protected byte[] getPermissionClassification_createJson() throws IOException {
        JSONObject postdata = new JSONObject();
        StringEntity se = new StringEntity(postdata.toString(),"UTF-8");
        se.setContentType("application/json");
        byte[] array = EntityUtils.toByteArray(se);
        return array;
    }


}
