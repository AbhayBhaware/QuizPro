package com.example.quizpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

public class SingupActivity extends AppCompatActivity {

    EditText Ename, Enumber, Eemail, Epassword;
    Button signupBTN;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        Ename=findViewById(R.id.name);
        Enumber=findViewById(R.id.number);
        Eemail=findViewById(R.id.email);
        Epassword=findViewById(R.id.password);

        signupBTN=findViewById(R.id.signupBTN);


    }
}