package com.example.quizpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    Button createAcBTN, loginBTN;

    EditText numberEDT, passwordEDT;

    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        createAcBTN=findViewById(R.id.createAcBTN);
        numberEDT=findViewById(R.id.numberEDT);
        passwordEDT = findViewById(R.id.passwordEDT);
        loginBTN=findViewById(R.id.loginBTN);

        dbref= FirebaseDatabase.getInstance().getReference("Users");

        String numberFromSignup=getIntent().getStringExtra("number");

        if (numberFromSignup !=null)
        {
            numberEDT.setText(numberFromSignup);
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
                                Intent i=new Intent(LoginActivity.this,HomeActivity.class);
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
                finish();
            }
        });
    }
}