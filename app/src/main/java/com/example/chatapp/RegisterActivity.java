package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * This activity focuses on the users ability to register to the app.
 * The activity utilises firebase auth to register a new user account using their email, username and password.
 *
 */

public class RegisterActivity extends AppCompatActivity {
    EditText username, password, email;
    Button btnRegister;
    FirebaseAuth auth;

    ProgressDialog progressDialog;

    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Register");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
        /**
         * firebase firestore original rule after if
         */
//        request.time < timestamp.date(2023, 2, 12);

        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        email = findViewById(R.id.register_email);
        btnRegister = findViewById(R.id.registerBtn);
        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        btnRegister.setOnClickListener(view -> {
            String txtUsername = username.getText().toString().trim();
            String txtEmail = email.getText().toString().trim();
            String txtPassword = password.getText().toString().trim();

            if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)) {
                Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_LONG).show();
            } else if (txtPassword.length() < 6) {
                Toast.makeText(RegisterActivity.this, "Password must at least be 6 characters long", Toast.LENGTH_SHORT).show();
            } else
                auth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sendUserToNext();
                        Toast.makeText(RegisterActivity.this, "auth Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "auth failed", Toast.LENGTH_SHORT).show();
                    }
                });
            //register(txtUsername,txtEmail,txtPassword);
        });
    }

    private void sendUserToNext() {
        String userID = auth.getCurrentUser().getUid();
        DocumentReference documentReference = fStore.collection("users").document(userID);


        //reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        HashMap<String, String> user = new HashMap<>();
        user.put("id", userID);
        user.put("username", username.getText().toString());
        user.put("imageURL", "default");

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(RegisterActivity.this, "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_LONG).show();
            }
        });

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}