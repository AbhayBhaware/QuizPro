package com.example.quizpro;

import java.security.PublicKey;

public class User {

   public String name, number, email, password;

    public User() { }

    public User(String name, String number, String email, String password)
    {
        this.name=name;
        this.number=number;
        this.email=email;
        this.password=password;
    }
}
