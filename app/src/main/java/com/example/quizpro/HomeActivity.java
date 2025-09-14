package com.example.quizpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {

    CardView card1, card2, card3, card4;
    Button logoutBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        card1=findViewById(R.id.card1);
        card2=findViewById(R.id.card2);
        card3=findViewById(R.id.card3);
        card4=findViewById(R.id.card4);
        logoutBTN=findViewById(R.id.logoutBTN);

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSharedPreferences("QuizProPrefs",MODE_PRIVATE).edit().clear().apply();

                Toast.makeText(HomeActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();

                Intent i=new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        card1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HomeActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });


    }
}