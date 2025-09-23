package com.example.quizpro;

import java.util.List;

public class Question {
    private String question;
    private String answer;
    private List<String> options;  // store options as a list

    public Question() {
        // Empty constructor required for Firebase
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getOptions() {
        return options;
    }
}
