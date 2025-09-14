package com.example.quizpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
               SharedPreferences prefs=getSharedPreferences("QuizProPrefs",MODE_PRIVATE);
               boolean isLoggedIn=prefs.getBoolean("isLoggedIn",false);

               if (isLoggedIn)
               {
                   Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
                   startActivity(intent);
                   finish();
               }
               else
               {
                   Intent i=new Intent(SplashActivity.this,LoginActivity.class);
                   startActivity(i);
                   finish();
               }
           }
       },3000);


    }
}