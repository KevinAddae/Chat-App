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

import com.example.chatapp.Adapter.UserAdapter;
import com.example.chatapp.R;
import com.example.chatapp.model.Chat;
import com.example.chatapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    DatabaseReference reference;
    FirebaseFirestore fStore;

    private  List<String> usersList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        fStore = FirebaseFirestore.getInstance();
        usersList.clear();

        fStore.collection("chats").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                if(document.get("sender").toString().equals(fUser.getUid()))
                    usersList.add(document.get("sender").toString());
                if(document.get("receiver").toString().equals(fUser.getUid()))
                    usersList.add(document.get("receiver").toString());
            }
        });

        Log.e("ChatFragment",usersList.size() + "Is the size");

        readChats();
        return view;

    }

    /**
     * This will get users
     */
    private void readChats() {
        mUser = new ArrayList<>();

        fStore.collection("users").get().addOnCompleteListener(task -> {
            mUser.clear();

            for (QueryDocumentSnapshot document : task.getResult()) {
                for (String id: usersList) {
                    if (document.get("id").equals(id))
                        if (mUser.size() != 0) {
                            Log.e("ChatFragment",usersList.size() + "Is the ID");
                            Log.e("ChatFragment",id);
                            if(id!= fUser.getUid());
                            for (User user: mUser)  {
                                if (!document.get(id).toString().equals(user.getId()))
                                    mUser.add(new User(document.get("id").toString()
                                            ,document.get("imageURL").toString()
                                            ,document.get("username").toString()));
                            }
                        } else
                            mUser.add(new User(document.get("id").toString()
                                    ,document.get("imageURL").toString()
                                    ,document.get("username").toString()));

                }
            }
        });
        userAdapter = new UserAdapter(getContext(),mUser);
        recyclerView.setAdapter(userAdapter);
    }
}