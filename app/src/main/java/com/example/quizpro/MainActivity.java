package com.example.quizpro;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity {

    TextView questionText;
    RadioButton opA, opB, opC, opD;
    Button nextBtn;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText=findViewById(R.id.question);
        opA=findViewById(R.id.optionA);
        opB=findViewById(R.id.optionB);
        opC=findViewById(R.id.optionC);
        opD=findViewById(R.id.optionD);
        nextBtn=findViewById(R.id.nextBtn);

        db=FirebaseFirestore.getInstance();


        fetchQuestion();
    }

    public void fetchQuestion()
    {
        db.collection("Questions").document("VQ3QhFkisPoTrEJzwMp9").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document =task.getResult();
                    if (document.exists())
                    {
                        String question = document.getString("question");
                        String optionA = document.getString("optionA");
                        String optionB = document.getString("optionB");
                        String optionC = document.getString("optionC");
                        String optionD = document.getString("optionD");
                        String correctAnswer = document.getString("correctAnswer");


                        questionText.setText(question);
                        opA.setText(optionA);
                        opB.setText(optionB);
                        opC.setText(optionC);
                        opD.setText(optionD);

                        nextBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String selectedAnswer = "";
                                if (opA.isChecked()) selectedAnswer = "optionA";
                                else if (opB.isChecked()) selectedAnswer = "optionB";
                                else if (opC.isChecked()) selectedAnswer = "optionC";
                                else if (opD.isChecked()) selectedAnswer = "optionD";

                                if (selectedAnswer.equals(correctAnswer))
                                {
                                        if (opA.isChecked()) opA.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                        else if (opB.isChecked()) opB.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                        else if (opC.isChecked()) opC.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                                        else if (opD.isChecked()) opD.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                                }
                                else
                                {
                                    if (opA.isChecked()) opA.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                                    else if (opB.isChecked()) opB.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                                    else if (opC.isChecked()) opC.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                                    else if (opD.isChecked()) opD.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

                                }
                            }
                        });
                    }
                }
            }
        });
    }
}