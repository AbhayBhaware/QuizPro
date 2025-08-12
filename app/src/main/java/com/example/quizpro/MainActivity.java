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

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    TextView questionText;
    RadioButton opA, opB, opC, opD;
    Button nextBtn;

    FirebaseFirestore db;

    List<DocumentSnapshot> questionList =new ArrayList<>();
    int currentIndex=0;

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
        db.collection("Questions").get().addOnCompleteListener(task -> {
           if (task.isSuccessful())
           {
               questionList=task.getResult().getDocuments();
               if (!questionList.isEmpty())
               {
                   showQuestion(currentIndex);
               }
           }
        });
    }

    public void showQuestion(int index) {
        DocumentSnapshot document = questionList.get(index);

        String question = document.getString("question");
        String optionA = document.getString("optionA");
        String optionB = document.getString("optionB");
        String optionC = document.getString("optionC");
        String optionD = document.getString("optionD");
        String correctAnswer = document.getString("currectAnswer");


        questionText.setText(question);
        opA.setText(optionA);
        opB.setText(optionB);
        opC.setText(optionC);
        opD.setText(optionD);

        opA.setTextColor(getResources().getColor(android.R.color.black));
        opB.setTextColor(getResources().getColor(android.R.color.black));
        opC.setTextColor(getResources().getColor(android.R.color.black));
        opD.setTextColor(getResources().getColor(android.R.color.black));
        opA.setChecked(false);
        opB.setChecked(false);
        opC.setChecked(false);
        opD.setChecked(false);

        nextBtn.setOnClickListener(v -> {
            String selectedAnswer = "";
            if (opA.isChecked()) selectedAnswer = "optionA";
            else if (opB.isChecked()) selectedAnswer = "optionB";
            else if (opC.isChecked()) selectedAnswer = "optionC";
            else if (opD.isChecked()) selectedAnswer = "optionD";

            if (selectedAnswer.equals(correctAnswer)) {
                highlightCorrect(correctAnswer, true);
            } else {
                highlightCorrect(correctAnswer, false);
            }


            nextBtn.postDelayed(() -> {
                currentIndex++;
                if (currentIndex < questionList.size()) {
                    showQuestion(currentIndex);
                } else {
                    questionText.setText("Quiz Finished!");
                    opA.setVisibility(View.GONE);
                    opB.setVisibility(View.GONE);
                    opC.setVisibility(View.GONE);
                    opD.setVisibility(View.GONE);
                    nextBtn.setVisibility(View.GONE);
                }
            }, 1000);
        });
    }



    private void highlightCorrect(String correctAnswer, boolean isCorrect) {
        int colorGreen = getResources().getColor(android.R.color.holo_green_dark);
        int colorRed = getResources().getColor(android.R.color.holo_red_dark);

        RadioButton correctOption = null;

        if (correctAnswer.equals("optionA")) correctOption = opA;
        else if (correctAnswer.equals("optionB")) correctOption = opB;
        else if (correctAnswer.equals("optionC")) correctOption = opC;
        else if (correctAnswer.equals("optionD")) correctOption = opD;

        if (isCorrect && correctOption != null) {
            correctOption.setTextColor(colorGreen);
        } else {
            if (opA.isChecked()) opA.setTextColor(colorRed);
            else if (opB.isChecked()) opB.setTextColor(colorRed);
            else if (opC.isChecked()) opC.setTextColor(colorRed);
            else if (opD.isChecked()) opD.setTextColor(colorRed);

            if (correctOption != null) correctOption.setTextColor(colorGreen);
        }
    }
}

