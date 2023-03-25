package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    ImageView profile_image, sendBtn;
    TextView username;
    EditText sendTxt;
    FirebaseUser fUser;
    DocumentReference reference;
    FirebaseFirestore fStore;
    Intent intent;

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

        intent = getIntent();
        User user = (User) intent.getSerializableExtra("user");

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        Log.i("MessageActivity","before reference lines");

        username.setText(user.getUsername());
        if (user.getImageURL().equals("default"))
            profile_image.setImageResource(R.mipmap.ic_launcher_round);
        else
            Glide.with(MessageActivity.this).load(user.getImageURL());


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendTxt.getText().toString();
//                if (!msg.equals(""))
//                    sendMessage(fUser.getUid(),user.getUsername(),msg);
//                else
//                    Toast.makeText(MessageActivity.this, "Cannot send nothing", Toast.LENGTH_SHORT).show();
//                sendTxt.setText("");
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
        String userID = (String) intent.getSerializableExtra("id");
        reference = fStore.collection("chats").document(userID);
        HashMap<String, Object> interaction = new HashMap<>();
        interaction.put("sender", sender);
        interaction.put("receiver", receiver);
        interaction.put("message", message);

        reference.set(interaction);
    }
}