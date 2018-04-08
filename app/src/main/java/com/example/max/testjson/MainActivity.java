package com.example.max.testjson;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.jcraft.jsch.Session;
import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.exceptions.NxpNfcLibException;
import com.nxp.nfclib.plus.IPlus;
import com.nxp.nfclib.plus.IPlusSL3;
import com.nxp.nfclib.plus.PlusFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView txtJson;
    ProgressDialog pd;
    WebView wv;
    Button nfcBtn;

    // for nfc
    private NxpNfcLib libInstance = null;
    static String packageKey = "8f2a9ad0ff7cc797a1145e5f707c0a47";
    private IPlusSL3 plusSL3 = null;
    private static final int STORAGE_PERMISSION_WRITE = 113;

    private CardType mCardType = CardType.UnknownCard;
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static Connection connection = null;
    private static Session session = null;
    private Person user;


    @SuppressLint("JavascriptInterface")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        nfcBtn = (Button)findViewById(R.id.nfcButton);
//        nfcBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                user = new Person("1234567890");
//                user.duplicatePerson();
//            }
//        });

        Log.i("start","aa");
      //  getPermission();
      //  initializeLibrary();


        //Test: register person
        //check
//        user = new Person("1234567890", "123232","23434");
//        int error = user.register();
//        if(error == 3)
//            Toast.makeText(getApplicationContext(), "This person is already exist", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(getApplicationContext(), "Person registered successfully", Toast.LENGTH_SHORT).show();

        //Test: get all item of certain person
       // user.getAllItem();


        //Test: register item
        Item item = new Item("test1", "Agora");
//        error = item.register();
//        if(error == 2)
//            Toast.makeText(getApplicationContext(), "This item already registered", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(getApplicationContext(), "Item registered successfully", Toast.LENGTH_SHORT).show();
//
//
//        //Test: user borrow item
//        error = user.borrowItem(item);
//        if(error == 4)
//            Toast.makeText(getApplicationContext(), "This person does not exist", Toast.LENGTH_SHORT).show();
//        else
//            Toast.makeText(getApplicationContext(), "Found Person", Toast.LENGTH_SHORT).show();
//
//

        //Test: get person info by card id
        //check
        Person current = new Person();
        int error = current.getPersonByCardID("1234");
        if(error == 4)
            Toast.makeText(getApplicationContext(), "This person does not exist", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Found Person", Toast.LENGTH_SHORT).show();

//        //Test: user return item
        error = current.returnItem(item);
        if(error == 1)
            Toast.makeText(getApplicationContext(), "This item already returned", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Successfully returned", Toast.LENGTH_SHORT).show();


        //Test: duplicate person
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        wv  = (WebView) findViewById(R.id.webview1);
//        wv.getSettings().setJavaScriptEnabled(true);
//        wv.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
//        wv.getSettings().setDomStorageEnabled(true);
//
//        wv.loadUrl("https://labtools.groept.be/inventory/secure"); //加载我的PHP框架代码
//        wv.requestFocus();
//        wv.getSettings().setUseWideViewPort(true);
//        wv.getSettings().setLoadWithOverviewMode(true);
//        wv.getSettings().setSupportZoom(true);
//        WebSettings webSettings = wv.getSettings();
//        webSettings.setBuiltInZoomControls(true);
//
//
//        wv.setWebViewClient(new WebViewClient() {
//
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
//            }
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                if(url.contains("secure")) {
//                    view.loadUrl("javascript:window.local_obj.getHtmlSource(document.body.innerHTML)");
//
//                }
//
//            }
//
//        });
        //  user = new Person("Wang Siyuan","r0609653","r0609653@kuleuven.be");
        //    user.register();
        // user.getItem();

//        user = new Person("aaa");
//
//        Item item = new Item("itemTag3","2017-04-06 00:00:00.000000", "groepT");
//        item.register();
//        Item item2 = new Item("itemTag3","2017-04-06 00:00:00.000000", "groepT");
//        item2.register();
        //user.returnItem(item);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }




    //与NFC登陆相关的


    @TargetApi(19)
    private void initializeLibrary() {
        libInstance = NxpNfcLib.getInstance();
        try {
            libInstance.registerActivity(this, packageKey);
        } catch (NxpNfcLibException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onNewIntent(final Intent intent) {
      //  cardLogic(intent);
        super.onNewIntent(intent);
    }

    public void getPermission(){
        boolean readPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if (!readPermission) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_WRITE
            );
        }

    }
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private void cardLogic(final Intent intent) {
        CardType type = CardType.UnknownCard;
        try {
            type = libInstance.getCardType(intent);
        } catch (NxpNfcLibException ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mCardType = CardType.PlusSL3;
        plusSL3 = PlusFactory.getInstance().getPlusSL3(libInstance.getCustomModules());

        try {
            plusSL3.getReader().connect();
            IPlus.CardDetails details = plusSL3.getCardDetails();
            MyApp.setUserCardID(bytesToHex(details.uid));
            Log.i("Card ID", MyApp.getUserCardID());
            Toast.makeText(getApplicationContext(),bytesToHex(details.uid),Toast.LENGTH_SHORT).show();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_PERMISSION_WRITE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Requested permission granted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(MainActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }

    }


    // lifecycle

    @Override
    protected void onPause() {
        super.onPause();
     //   libInstance.stopForeGroundDispatch();
    }


    @Override
    protected void onResume() {
        super.onResume();
      //  libInstance.startForeGroundDispatch();
    }


    //与webview相关的

    private void showStudentNumber() {
        if (!user.getUserName().equals("None"))
        {
            Toast.makeText(getApplicationContext(), "Welcome! " + user.getUserName(), Toast.LENGTH_LONG).show();
        }
    }

    public final class InJavaScriptLocalObj {
        @JavascriptInterface
        public void getHtmlSource(String htmlSource) {

            Log.i("HTML", htmlSource);
            try {

                String studentNumber = "None";
                String userName = "None";
                String email = "None";

                JSONObject obj = new JSONObject(htmlSource);
                userName = obj.getString("email").split("@")[0].replace(".", " ");
                studentNumber = obj.getString("user").split("@")[0]; Log.i("user", studentNumber);
                email = obj.getString("email");

                user = new Person(userName,studentNumber,email);
                showStudentNumber();

            } catch (Throwable t) {
                Log.e("My info", "Could not parse malformed JSON: \"" + htmlSource + "\"");
            }
        }

    }




}