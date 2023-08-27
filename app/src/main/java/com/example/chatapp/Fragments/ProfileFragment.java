 package com.example.chatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Adapter.UserAdapter;
import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
import com.example.chatapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import org.w3c.dom.Text;

import java.util.concurrent.Executor;


 public class ProfileFragment extends Fragment {

    ImageView image_profile;
    TextView username;
    FirebaseUser fUser;
    FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile = view.findViewById(R.id.profile_image);
        username = view.findViewById(R.id.username);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        db.collection("users").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                if (document.get("id").toString().equals(fUser.getUid())) {
                    username.setText(document.get("username").toString());
                    if (document.get("imageURL").equals("default"))
                        image_profile.setImageResource(R.mipmap.ic_launcher);
                }
            }
        });


        return view;
    }
}