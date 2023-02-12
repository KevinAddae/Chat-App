package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartUpActivity extends AppCompatActivity {

    Button loginBtn, registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        loginBtn = findViewById(R.id.start_loginBtn);
        registerBtn = findViewById(R.id.start_regiBtn);

        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(StartUpActivity.this, LoginActivity.class));
        });

        registerBtn.setOnClickListener(v -> {
            startActivity(new Intent(StartUpActivity.this, RegisterActivity.class));
        });
    }
}