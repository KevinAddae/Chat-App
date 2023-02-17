package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText password, email;

    Button loginBtn;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        getSupportActionBar().setTitle("Login");
        // Sets up the action bar with bar_layout.xml
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Login");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        password = findViewById(R.id.login_password);
        email = findViewById(R.id.login_email);
        loginBtn = findViewById(R.id.login_Btn);
        auth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(view -> {
            String txtEmail = email.getText().toString();
            String txtPassword = password.getText().toString();

            //adds the condition when the both edit texts are empty
            if(TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                Toast.makeText(LoginActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
            } else {
                auth.signInWithEmailAndPassword(txtEmail,txtPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    } else
                        Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}