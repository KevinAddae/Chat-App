package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chatapp.Adapter.MessageAdapter;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessageActivity extends AppCompatActivity {
    ImageView profile_image, sendBtn;
    TextView username;
    EditText sendTxt;
    FirebaseUser loggedInUser;
    DocumentReference reference;
    FirebaseFirestore fStore;
    Intent intent;
    User selectedUser;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<Chat> mChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        // adds the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.getStackFromEnd();
        recyclerView.setLayoutManager(linearLayoutManager);

        sendBtn = findViewById(R.id.btn_send);
        sendTxt = findViewById(R.id.txt_send);
        profile_image = findViewById(R.id.mainProfile_image);
        username = findViewById(R.id.main_username);

        fStore = FirebaseFirestore.getInstance();
        intent = getIntent();
        selectedUser = (User) intent.getSerializableExtra("user");

        loggedInUser = FirebaseAuth.getInstance().getCurrentUser();




        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = sendTxt.getText().toString();
                Log.i("MessageActivity","selected user id" + selectedUser.getId());
                Log.i("MessageActivity","current user id" + loggedInUser.getUid());

                if (!msg.equals(""))
                    //Log.i("MessageActivity", "This is the Username " + user.getUsername());
                    sendMessage(loggedInUser.getUid(), selectedUser.getId(),msg);
                else
                    Toast.makeText(MessageActivity.this, "Cannot send nothing", Toast.LENGTH_SHORT).show();

                sendTxt.setText("");
                readMessage(loggedInUser.getUid(), selectedUser.getId(), selectedUser.getImageURL());

            }
        });

        username.setText(selectedUser.getUsername());
        if (selectedUser.getImageURL().equals("default"))
            profile_image.setImageResource(R.mipmap.ic_launcher_round);
        else
            Glide.with(MessageActivity.this).load(selectedUser.getImageURL());

        readMessage(loggedInUser.getUid(), selectedUser.getId(), selectedUser.getImageURL());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(i,0);
        return true;
    }

    public void sendMessage(String sender, String receiver, String message) {
        reference = fStore.collection("chats").document();
        //DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> interaction = new HashMap<>();
        interaction.put("sender", sender);
        interaction.put("receiver", receiver);
        interaction.put("message", message);

        //ref.child("chats").push().setValue(interaction);
        reference.set(interaction);
    }

    private void readMessage (String myId, String userId, String imageURL) {
        mChat = new ArrayList<>();

        fStore.collection("chats").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                if (myId.equals(document.get("sender")) && userId.equals(document.get("receiver"))){
                    mChat.add(new Chat(document.get("sender").toString(),
                            document.get("receiver").toString(),
                            document.get("message").toString()));
                }
            }
            Log.i("MessageAdapter", "The size of mChat : " + mChat.size());
            messageAdapter = new MessageAdapter(MessageActivity.this,mChat,imageURL);
            messageAdapter.notifyItemInserted(mChat.size()-1);
            recyclerView.setAdapter(messageAdapter);
        });
        }

}