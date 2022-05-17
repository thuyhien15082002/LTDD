package com.example.ltdd;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {
    DataBase dataBase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        init();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
    void init(){
        dataBase = new DataBase(SplashActivity.this, "database.sqlite", null, 1);
        dataBase.QueryData("CREATE TABLE IF NOT EXISTS like (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title VARCHAR, " +
                "thumbnail VARCHAR, " +
                "idVideo VARCHAR )");
    }
}