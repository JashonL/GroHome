package com.growatt.grohome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.growatt.grohome.module.login.RegisterLoginActivity;
import com.growatt.grohome.utils.ActivityUtils;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ActivityUtils.startActivity(SplashActivity.this,new Intent(SplashActivity.this, RegisterLoginActivity.class),ActivityUtils.ANIMATE_FORWARD,true);
            }
        }, 3000);
    }
}
