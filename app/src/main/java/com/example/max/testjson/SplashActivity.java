package com.example.max.testjson;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.widget.ImageView;

public class SplashActivity extends AppCompatActivity {

    /** Splash screen timer.*/
    public static final int SPLASH_TIME_OUT = 1500;
    public ImageView mIVTapLinxLogo = null;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initializeUI();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
    /**
     * Initializing the UI thread.
     */
    private void initializeUI() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        mIVTapLinxLogo = (ImageView) findViewById(R.id.imgTapLinx);
        mIVTapLinxLogo.getLayoutParams().width = (size.x) ;
        mIVTapLinxLogo.getLayoutParams().height =(size.y) ;

    }
}
