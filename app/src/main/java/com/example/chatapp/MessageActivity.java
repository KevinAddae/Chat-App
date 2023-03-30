package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MessageActivity extends AppCompatActivity {
    ImageView profile_image, sendBtn;
    TextView username;
    EditText sendTxt;
    FirebaseUser loggedInUser;
    DocumentReference reference;
    FirebaseFirestore fStore;
    Intent intent;
    User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        // adds the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sendBtn = findViewById(R.id.btn_send);
        sendTxt = findViewById(R.id.txt_send);
        profile_image = findViewById(R.id.mainProfile_image);
        username = findViewById(R.id.main_username);

        fStore = FirebaseFirestore.getInstance();
        intent = getIntent();
        selectedUser = (User) intent.getSerializableExtra("user");

        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.i("MessageActivity","before reference lines");

        username.setText(selectedUser.getUsername());
        if (selectedUser.getImageURL().equals("default"))
            profile_image.setImageResource(R.mipmap.ic_launcher_round);
        else
            Glide.with(MessageActivity.this).load(selectedUser.getImageURL());


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendTxt.getText().toString();
                if (!msg.equals(""))
                    //Log.i("MessageActivity", "This is the Username " + user.getUsername());
                    sendMessage(loggedInUser.getUid(), selectedUser.getId(),msg);
                else
                    Toast.makeText(MessageActivity.this, "Cannot send nothing", Toast.LENGTH_SHORT).show();
                sendTxt.setText("");
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(i,0);
        return true;
    }

    public void sendMessage(String sender, String receiver, String message) {
        reference = fStore.collection("chats").document(sender);
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> interaction = new HashMap<>();
        interaction.put("sender", sender);
        interaction.put("receiver", receiver);
        interaction.put("message", message);

        //ref.child("chats").push().setValue(interaction);
        reference.set(interaction);
    }
}