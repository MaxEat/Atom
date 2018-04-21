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
import android.widget.Toast;
import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.exceptions.NxpNfcLibException;
import com.nxp.nfclib.plus.IPlus;
import com.nxp.nfclib.plus.IPlusSL3;
import com.nxp.nfclib.plus.PlusFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import static com.example.max.testjson.TestJson.wv;

public class MainActivity extends  AppCompatActivity implements BorrowedFragment.OnListFragmentInteractionListener, AvailableItemFragment.OnListFragmentInteractionListener,Admin_AvailableItemFragment.OnListFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener,ScanResultReceiver{

    private android.support.v4.app.Fragment[]mFragments;

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final int STORAGE_PERMISSION_WRITE = 113;
    private static final int STORAGE= 114;
    private static final int CAMERA= 115;


    private static String packageKey = "27a687a3baf16019e54c1f622d814a06";

    private IPlusSL3 plusSL3 = null;
    private NxpNfcLib libInstance = null;
    private CardType mCardType = CardType.UnknownCard; //不能删

    Fragment fragment = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();


        wv = (CustomedWebview) findViewById(R.id.webview);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);
        wv.hide();
        mFragments = DataGenerator.getFragments("BottomNavigationView Tab");
        //getPermission();
        initializeLibrary();
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

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
        cardLogic(intent);
        super.onNewIntent(intent);

    }


//    public void getPermission() {
//        boolean readPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
//
//        if (!readPermission) {
//            ActivityCompat.requestPermissions(MainActivity.this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_WRITE
//            );
//        }
//
//    }

    public void getPermission() {
        boolean storagePermission = (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission_group.STORAGE) == PackageManager.PERMISSION_GRANTED);

        boolean cameraPermission = (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);

        if (!storagePermission) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission_group.STORAGE}, STORAGE
            );
        }

        if (!cameraPermission) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CAMERA}, CAMERA
            );
        }
    }

    @SuppressLint("JavascriptInterface")
    public void checkUserType(String kuleuvenID, String userName, String email, String userType, String id) throws IOException {

        if (userType.equals("Administrator"))
        {
          showDialog(kuleuvenID, userName, email, userType, id);
        }
        else
        {
            Person user = new Student(userName, kuleuvenID, email);
            user.setUserType(userType);
            user.setCardID(id);
            wv.addJavascriptInterface(user, "Person");
            user.getAllItem();
            TestJson.setUser(user);
            wv.hide();
            fragment = mFragments[5];

            if(fragment!=null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.home_container_main,fragment).commit();
            }
        }


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

                wv.addJavascriptInterface(user, "Person");
                try {
                    user.getAllItem();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                    user.getAllItem();
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

    public void checkId(final String id) {
        JSONObject postdata = new JSONObject();
        try {
            postdata.put("cardID", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            BackgroundTask.getInstance().postAsyncJsonn(BackgroundTask.getInfoByCardURL, postdata.toString(), new BackgroundTask.MyCallback() {
                @Override
                public void onSuccess(String result) {
                    Log.i("Success", "result----" + result);
                    try {
                        JSONObject json = new JSONObject(result);
                        if (json.getInt("error_message") == 4) {
                            Toast.makeText(getApplicationContext(), "This person does not exist", Toast.LENGTH_SHORT).show();


                            fragment = mFragments[7];

                            Bundle bundle = new Bundle();
                            bundle.putString("cardID",id);
                            fragment.setArguments(bundle);

                            if(fragment!=null) {
                                getSupportFragmentManager().beginTransaction().replace(R.id.home_container_main,fragment).commit();
                            }
                        } else {
                            String kuleuvenID = json.getString("kuleuvenID");
                            String userName = json.getString("userName");
                            String email = json.getString("email");
                            String userType = json.getString("userType");
                            checkUserType(kuleuvenID, userName, email, userType, id);
                        }
                    }
                     catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
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
            String id = bytesToHex(details.uid);
            checkId(id);
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

    @Override
    protected void onPause() {
        super.onPause();
        libInstance.stopForeGroundDispatch();
    }


    @Override
    protected void onResume() {
        super.onResume();
        libInstance.startForeGroundDispatch();
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
    public void onListFragmentInteraction(Admin_AvailableItem item) {

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


}
