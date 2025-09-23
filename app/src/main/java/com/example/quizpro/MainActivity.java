package com.example.quizpro;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView questionText, timerText;
    RadioButton opA, opB, opC, opD;
    Button nextBtn;
    CountDownTimer countDownTimer;
    int timePerQuestion = 30;

    DatabaseReference dbRef;
    List<Question> questionList = new ArrayList<>();
    int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionText = findViewById(R.id.question);
        timerText = findViewById(R.id.timertext);
        opA = findViewById(R.id.optionA);
        opB = findViewById(R.id.optionB);
        opC = findViewById(R.id.optionC);
        opD = findViewById(R.id.optionD);
        nextBtn = findViewById(R.id.nextBtn);

        dbRef= FirebaseDatabase.getInstance().getReference("Programming");

        fetchQuestion();
    }

    public void fetchQuestion() {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                questionList.clear();
                for (DataSnapshot data: snapshot.getChildren()){
                    Question question=data.getValue(Question.class);
                    if (question!=null)
                    {
                        questionList.add(question);
                    }
                }
                if (!questionList.isEmpty())
                {
                    showQuestion(currentIndex);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed To Load Question", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void showQuestion(int index) {
        Question question=questionList.get(index);

        questionText.setText(question.getQuestion());
        opA.setText(question.getOptions().get(0));
        opB.setText(question.getOptions().get(1));
        opC.setText(question.getOptions().get(2));
        opD.setText(question.getOptions().get(3));

        // Reset colors and selections
        opA.setTextColor(getResources().getColor(android.R.color.black));
        opB.setTextColor(getResources().getColor(android.R.color.black));
        opC.setTextColor(getResources().getColor(android.R.color.black));
        opD.setTextColor(getResources().getColor(android.R.color.black));
        opA.setChecked(false);
        opB.setChecked(false);
        opC.setChecked(false);
        opD.setChecked(false);

        // Start timer for this question
        startTimer(question.getAnswer());

        nextBtn.setOnClickListener(v -> {
            countDownTimer.cancel(); // Stop timer when answer is chosen
            checkAnswer(question.getAnswer());
        });
    }

    private void startTimer(String correctAnswer) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        countDownTimer = new CountDownTimer(timePerQuestion * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText((millisUntilFinished / 1000) + "s");
            }

            @Override
            public void onFinish() {
                timerText.setText("Time's up!");
                // Auto move to next question if time runs out
                checkAnswer(correctAnswer);
            }
        }.start();
    }

    private void checkAnswer(String correctAnswer) {
        String selectedAnswer = "";
        if (opA.isChecked()) selectedAnswer = opA.getText().toString();
        else if (opB.isChecked()) selectedAnswer = opB.getText().toString();
        else if (opC.isChecked()) selectedAnswer = opC.getText().toString();
        else if (opD.isChecked()) selectedAnswer = opD.getText().toString();

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
                timerText.setVisibility(View.GONE);
            }
        }, 1000);
    }

    private void highlightCorrect(String correctAnswer, boolean isCorrect) {
        int colorGreen = getResources().getColor(android.R.color.holo_green_dark);
        int colorRed = getResources().getColor(android.R.color.holo_red_dark);

        RadioButton correctOption = null;

        if (opA.getText().toString().equals(correctAnswer)) correctOption = opA;
        else if (opB.getText().toString().equals(correctAnswer)) correctOption = opB;
        else if (opC.getText().toString().equals(correctAnswer)) correctOption = opC;
        else if (opD.getText().toString().equals(correctAnswer)) correctOption = opD;

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
