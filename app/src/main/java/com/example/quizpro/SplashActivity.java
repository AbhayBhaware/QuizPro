package com.example.quizpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

       Handler h=new Handler();
       h.postDelayed(new Runnable() {
           @Override
           public void run() {
               Intent i=new Intent(SplashActivity.this, LoginActivity.class);
               startActivity(i);
               finish();
           }
       },3000);
    }
}