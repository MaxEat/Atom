package com.example.max.testjson;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Animatable;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.nxp.nfclib.CardType;
import com.nxp.nfclib.NxpNfcLib;
import com.nxp.nfclib.exceptions.NxpNfcLibException;
import com.nxp.nfclib.plus.IPlus;
import com.nxp.nfclib.plus.IPlusSL3;
import com.nxp.nfclib.plus.PlusFactory;
import com.squareup.leakcanary.RefWatcher;

public class SplashActivity extends AppCompatActivity {

    /** Splash screen timer.*/
    public static final int SPLASH_TIME_OUT = 5000;
    public ImageView mIVTapLinxLogo = null;

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static final int STORAGE_PERMISSION_WRITE = 113;
    private static final int STORAGE= 114;

    private static String packageKey = "27a687a3baf16019e54c1f622d814a06";

    private IPlusSL3 plusSL3 = null;
    private NxpNfcLib libInstance = null;
    private CardType mCardType = CardType.UnknownCard; //不能删

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initializeUI();
        getPermission();
        initializeLibrary();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),"Please scan your card to log in", Toast.LENGTH_SHORT).show();
            }
        }, SPLASH_TIME_OUT);
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

    public void getPermission() {
        boolean storagePermission = (ContextCompat.checkSelfPermission(SplashActivity.this,
                Manifest.permission_group.STORAGE) == PackageManager.PERMISSION_GRANTED);


        if (!storagePermission) {
            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{Manifest.permission_group.STORAGE}, STORAGE
            );
        }


    }
    private void initializeUI() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mIVTapLinxLogo = (ImageView) findViewById(R.id.imgTapLinx);
//        mIVTapLinxLogo.getLayoutParams().width = (size.x) ;
//        mIVTapLinxLogo.getLayoutParams().height =(size.y) ;


        RotateAnimation rotateAni = new RotateAnimation(30, 50, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAni.setDuration(2000);
        rotateAni.setRepeatMode(Animation.REVERSE);
        rotateAni.setRepeatCount(Animation.INFINITE);
        mIVTapLinxLogo.startAnimation(rotateAni);

//        if(linear) rotateAni.setInterpolator(new LinearInterpolator());	// 匀速旋转
//
//        view.startAnimation(rotateAni);
//        Animation anim = new RotateAnimation(0f, 360f,
//                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
//                0.5f);
//        anim.setInterpolator(new LinearInterpolator());
//        anim.setRepeatCount(Animation.INFINITE);
//        anim.setDuration(4000);

        // Start animating the image


    }

    @Override
    public void onNewIntent(final Intent intent) {
        cardLogic(intent);
        super.onNewIntent(intent);

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
            Log.i("Scanned card ID", id);
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("cardID", id);
            startActivity(i);
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
                    Toast.makeText(SplashActivity.this, "Requested permission granted", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(SplashActivity.this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
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
       RefWatcher refWatcher = TestJson.getRefWatcher(this);
       refWatcher.watch(this);
    }

}
