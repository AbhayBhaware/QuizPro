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
import com.google.firebase.firestore.FirebaseFirestore;

public class SingupActivity extends AppCompatActivity {

    EditText Ename, Enumber, Eemail, Epassword;
    Button signupBTN;

    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singup);

        Ename=findViewById(R.id.name);
        Enumber=findViewById(R.id.number);
        Eemail=findViewById(R.id.email);
        Epassword=findViewById(R.id.password);

        signupBTN=findViewById(R.id.signupBTN);
        dbref= FirebaseDatabase.getInstance().getReference("Users");

        signupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=Ename.getText().toString();
                String number=Enumber.getText().toString();
                String email=Eemail.getText().toString();
                String password=Epassword.getText().toString();

                if (name.isEmpty() || number.isEmpty() || email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(SingupActivity.this, "Fill All Fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                User user=new User(name, number, email, password);

                dbref.child(number).setValue(user).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(SingupActivity.this, "Signup Successfully", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(SingupActivity.this, LoginActivity.class);
                        i.putExtra("number", number);
                        startActivity(i);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(SingupActivity.this, "Signup Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}