package com.example.admin.oneclickwash;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.example.admin.oneclickwash.Configuration.Util;
import com.example.admin.oneclickwash.Constant.Constant;
import com.google.android.gms.analytics.HitBuilders;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SplashScreen extends AppCompatActivity {

    String userId;
    String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView splashImage= (ImageView) findViewById(R.id.splash_logo);

        userId= Util.getSharedPrefrenceValue(getApplicationContext(), Constant.SP_USER_ID);
        deviceId=Util.getSharedPrefrenceValue(getApplicationContext(),Constant.SP_DEVICE_ID);

        try {
            Application.tracker().setScreenName("Splash Screen");
            Application.tracker().send(new HitBuilders.EventBuilder()
                    .setLabel("Splash Screen")
                    .setCategory("Splash Screen")
                    .setAction("App Launch")
                    .build());

        } catch (Exception e) {

        }

        if(deviceId==null){
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

            Util.setSharedPrefrenceValue(getApplicationContext(),Constant.PREFS_NAME,Constant.SP_DEVICE_ID,android_id);

        }

        Animation animation = new ScaleAnimation((float) 5.0, (float) 1.0,
                (float) 6.0, (float) 1.0);
        animation.setDuration(1500);
        animation.setRepeatCount(0);
        animation.setRepeatMode(1);
        animation.setFillAfter(true);
        splashImage.startAnimation(animation);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(userId==null) {
                    Intent intent = new Intent(getApplicationContext(), Tutorial.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), HomeScreen.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 3000);

    }



}
