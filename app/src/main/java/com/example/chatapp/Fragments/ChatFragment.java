package com.example.chatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chatapp.Adapter.UserAdapter;
import com.example.chatapp.R;
import com.example.chatapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUser;
    FirebaseUser fUser;
    FirebaseFirestore db;

    private  List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fUser = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();

        usersList = new ArrayList<>();
        usersList.clear();
/**
 * This will collect all the ids of users that have messages with the currently logged in
 * user.
 */
        db.collection("chats").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                if(document.get("sender").toString().equals(fUser.getUid()))
                    usersList.add(document.get("receiver").toString());
                else if(document.get("receiver").toString().equals(fUser.getUid()))
                    usersList.add(document.get("sender").toString());
            }
        });
        readChats();

        return view;
    }

    /**
     * This will get users from Firebase db that have a chat with the user that is currently logged.
     * Also will show the collected users on the recyclerview
     */
    private void readChats() {
        mUser = new ArrayList<>();
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                for (QueryDocumentSnapshot document: task.getResult()){
                    for (String id: usersList)
                        if (document.get("id").toString().equals(id))
                            mUser.add(new User(document.get("id").toString(),document.get("username").toString()
                                    ,document.get("imageURL").toString()));

                }
            }    else {
                Toast.makeText(getActivity(), "Do", Toast.LENGTH_SHORT).show();
            }
            userAdapter = new UserAdapter(getContext(),mUser);
            recyclerView.setAdapter(userAdapter);
        });

    }
}