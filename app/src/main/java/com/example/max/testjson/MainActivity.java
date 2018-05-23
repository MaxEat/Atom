package com.example.max.testjson;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static com.example.max.testjson.CustomedWebview.duplicatePersonURL;
import static com.example.max.testjson.TestJson.wv;

public class MainActivity extends  AppCompatActivity implements BorrowedFragment.OnListFragmentInteractionListener, AvailableItemFragment.OnListFragmentInteractionListener,Admin_AvailableItemFragment.OnListFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener,ScanResultReceiver{

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        cardID = intent.getStringExtra("cardID");

        setContentView(R.layout.activity_main);
        getPermission();

        mFragments = DataGenerator.getFragments("BottomNavigationView Tab");
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

                try {
                    getPermissionClassification();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
                user.setUserType(userType);
                TestJson.setUser(user);


                fragment = mFragments[6];
                if(fragment!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container_main,fragment).commit();
                }
            }
        });
        builder.setNegativeButton(R.string.user, new DialogInterface.OnClickListener() {
            @SuppressLint("JavascriptInterface")
            public void onClick(DialogInterface dialog, int id) {

                Person user = new Student(userName, kuleuvenID, email);
                user.setCardID(cardid);
                user.setUserType(userType);
                TestJson.setUser(user);

                wv.addJavascriptInterface(user, "Person");

                try {

                    ((Student)TestJson.getUser()).getAllItem();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                fragment = mFragments[5];
                if(fragment!=null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container_main,fragment).commit();
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

    @Override
    protected void onPause() {
        super.onPause();
      //  libInstance.stopForeGroundDispatch();
    }


    @Override
    protected void onResume() {
        super.onResume();
      //  libInstance.startForeGroundDispatch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
                JSONObject obj = new JSONObject(htmlSource);
                int error = obj.getInt("error_message");

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
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //Toast.makeText(getApplicationContext(), "This person does not exist", Toast.LENGTH_SHORT).show();
//                            fragment = mFragments[7];
//                            Bundle bundle = new Bundle();
//                            bundle.putString("cardID",cardID);
//                            bundle.putString("userName",userName);
//                            bundle.putString("email",email);
//                            fragment.setArguments(bundle);
//
//                            if(fragment!=null) {
//                                getSupportFragmentManager().beginTransaction().replace(R.id.home_container_main,fragment).commit();
//                            }
//                        }
//                    });
                }
                if(error == 0){
                    userType = obj.getString("userType");
                    blacklist = obj.getString("state");

                    checkUserType(kuleuvenID, userName, email, userType,cardID, blacklist);
                }
            } catch (Throwable t) {
                Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
            }
        }
        
        @JavascriptInterface
        public void getPermissionClassification_interface(final String htmlSource) {

            Log.i("Get classifications", htmlSource);
            try {
                JSONObject jsonObject = new JSONObject(htmlSource);
                JSONArray jsonArray = jsonObject.getJSONArray("list");
                JSONArray jsonArray2 = jsonObject.getJSONArray("list2");
                JSONArray jsonArray3 = jsonObject.getJSONArray("list3");

                TestJson.permissionArray = new String[jsonArray.length()];
                TestJson.classificationArray = new String[jsonArray2.length()];
                TestJson.locationArray = new String[jsonArray3.length()];

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    TestJson.permissionArray[i] = json.getString("permissionType");
                    Log.i("permission value",  TestJson.permissionArray[i] + "\"");
                }

                for (int i = 0; i < jsonArray2.length(); i++) {
                    JSONObject json = jsonArray2.getJSONObject(i);
                    TestJson.classificationArray[i] = json.getString("itemPictureClassification");
                    Log.i("classification "+i,  TestJson.classificationArray[i] + "\"");
                }

                for (int i = 0; i<jsonArray3.length(); i++) {
                    JSONObject json = jsonArray3.getJSONObject(i);
                    TestJson.locationArray[i] = json.getString("itemLocation");
                    Log.i("location "+i,  TestJson.classificationArray[i] + "\"");
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
