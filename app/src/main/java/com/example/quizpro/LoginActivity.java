package com.example.quizpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    Button createAcBTN, loginBTN;

    EditText numberEDT, passwordEDT;

    DatabaseReference dbref;
    MaterialCardView m1, m2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAcBTN=findViewById(R.id.createAcBTN);
        numberEDT=findViewById(R.id.numberEDT);
        passwordEDT = findViewById(R.id.passwordEDT);
        loginBTN=findViewById(R.id.loginBTN);

        m1=findViewById(R.id.mobileCard);
        m2=findViewById(R.id.passCard);
        List<MaterialCardView> allCards=Arrays.asList(m1,m2);
        List<EditText> allEdits = Arrays.asList(numberEDT, passwordEDT);

        dbref= FirebaseDatabase.getInstance().getReference("Users");

        String numberFromSignup=getIntent().getStringExtra("number");
        if (numberFromSignup !=null)
        {
            numberEDT.setText(numberFromSignup);
        }



        View.OnClickListener selectCardListener = v->{
            for (MaterialCardView c:allCards)
            {
                c.setStrokeColor(Color.TRANSPARENT);
            }

            if (v instanceof EditText)
            {
                MaterialCardView parent =(MaterialCardView) v.getParent();

                parent.setStrokeColor(Color.parseColor("#60CDC2"));
            }

            else if (v instanceof MaterialCardView)
            {
                ((MaterialCardView) v).setStrokeColor(Color.parseColor("#60CDC2"));
            }
        };

        for (MaterialCardView card :allCards)
        {
            card.setOnClickListener(selectCardListener);
        }
        for (EditText editText:allEdits)
        {
            editText.setOnFocusChangeListener((v, hasFocus)->{
                if (hasFocus)
                {
                    for (MaterialCardView c:allCards)
                    {
                        c.setStrokeColor(Color.TRANSPARENT);
                    }
                    MaterialCardView parent =(MaterialCardView) v.getParent();
                    parent.setStrokeColor(Color.parseColor("#60CDC2"));
                }
            });

            editText.setOnClickListener(selectCardListener);
        }


        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number =numberEDT.getText().toString().trim();
                String password=passwordEDT.getText().toString().trim();
                if (number.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                dbref.child(number).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        if (task.getResult().exists())
                        {
                            String dbpassword=task.getResult().child("password").getValue(String.class);

                            if (dbpassword.equals(password))
                            {
                                Toast.makeText(LoginActivity.this, "Login Succesfully", Toast.LENGTH_SHORT).show();

                                getSharedPreferences("QuizProPrefs",MODE_PRIVATE).edit().putBoolean("isLoggedIn",true).putString("userNumber",number).apply();

                                Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "User not Found", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this, "Error :"+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        createAcBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, SingupActivity.class);
                startActivity(intent);

            }
        });
    }
}