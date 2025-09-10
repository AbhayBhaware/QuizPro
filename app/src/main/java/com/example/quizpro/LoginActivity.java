package com.example.quizpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    Button createAcBTN;

    EditText numberEDT, passwordEDT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAcBTN=findViewById(R.id.createAcBTN);
        numberEDT=findViewById(R.id.numberEDT);
        passwordEDT = findViewById(R.id.passwordEDT);

        String numberFromSignup=getIntent().getStringExtra("number");

        if (numberFromSignup !=null)
        {
            numberEDT.setText(numberFromSignup);
        }

        createAcBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, SingupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}