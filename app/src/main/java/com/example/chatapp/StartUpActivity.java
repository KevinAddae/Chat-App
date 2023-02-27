package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class StartUpActivity extends AppCompatActivity {

    Button loginBtn, registerBtn;

    FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if (firebaseUser != null) {
            startActivity(new Intent(StartUpActivity.this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);

        loginBtn = findViewById(R.id.start_loginBtn);
        registerBtn = findViewById(R.id.start_regiBtn);
        // firebaseUser is to allow for auto login.
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        loginBtn.setOnClickListener(v -> {
            startActivity(new Intent(StartUpActivity.this, LoginActivity.class));
        });

        registerBtn.setOnClickListener(v -> {
            startActivity(new Intent(StartUpActivity.this, RegisterActivity.class));
        });
    }
}