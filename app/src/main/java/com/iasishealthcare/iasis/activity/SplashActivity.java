package com.iasishealthcare.iasis.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.iasishealthcare.iasis.AppData;
import com.iasishealthcare.iasis.R;

public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (AppData.getInstance().getFavouriteLocation().length() < 1) {
                    Intent intent = new Intent(SplashActivity.this, SettingActivity.class);
                    intent.putExtra("first", true);
                    startActivity(intent);
                }
                else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                }
                finish();
            }
        }, SPLASH_TIME);
    }

}
