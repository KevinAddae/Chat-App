package com.example.chatapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chatapp.MessageActivity;
import com.example.chatapp.R;
import com.example.chatapp.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUser;

    public UserAdapter( Context mContext, List<User> mUser) {
        this.mContext = mContext;
        this.mUser = mUser;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = mUser.get(position);
        TextView username = holder.itemView.findViewById(R.id.single_username);
        ImageView profile_image = holder.itemView.findViewById(R.id.single_profile_image);
        username.setText(user.getUsername());
        if (user.getImageURL().equals("default"))
            profile_image.setImageResource(R.mipmap.ic_launcher);
        else
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent (mContext, MessageActivity.class);
            intent.putExtra("userID",user);
            mContext.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return mUser.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            username = itemView.findViewById(R.id.username);
//            profile_image = itemView.findViewById(R.id.profile_image);


        }
    }

}
