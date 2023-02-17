package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

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

    DatabaseReference reference;

    FirebaseFirestore db;

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
        db = FirebaseFirestore.getInstance();


        btnRegister.setOnClickListener(view -> {
            String txtUsername = username.getText().toString();
            String txtEmail = email.getText().toString();
            String txtPassword = password.getText().toString();

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

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        HashMap<String, String> user = new HashMap<>();
        user.put("id", userID);
        user.put("username", username.getText().toString());
        user.put("imageURL", "default");


        db.collection("users")
                        .add(user).addOnSuccessListener(documentReference ->
                        Toast.makeText(RegisterActivity.this, "DocumentSnapshot added with ID: " + documentReference.getId(), Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(RegisterActivity.this, ""+e, Toast.LENGTH_SHORT).show());

//        reference.setValue(hashMap);

//        reference.setValue(hashMap).addOnCompleteListener(task1 -> {
//            if (task1.isSuccessful()) {
//                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();
//            } else
//                Toast.makeText(this, "The database has not saved user info", Toast.LENGTH_SHORT).show();
//        });

        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}