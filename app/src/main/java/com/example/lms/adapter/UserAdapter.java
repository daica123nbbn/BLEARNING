package com.example.lms.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.R;

import com.example.lms.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);

        // Bind data to the views
        holder.userName.setText(user.getName());
        holder.userJobTitle.setText(user.getJobTitle());
        holder.ratingBar.setRating(user.getRating());
        holder.userReview.setText(user.getReview());
        holder.userImage.setImageResource(Integer.parseInt(user.getImageUrl()));

        // Handle "Read more" click to expand the review text
        holder.readMore.setOnClickListener(v -> {
            holder.userReview.setMaxLines(Integer.MAX_VALUE);
            holder.readMore.setVisibility(View.GONE);
        });


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView userName;
        TextView userJobTitle;
        RatingBar ratingBar;
        TextView userReview;
        TextView readMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            userName = itemView.findViewById(R.id.userName);
            userJobTitle = itemView.findViewById(R.id.userJobTitle);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            userReview = itemView.findViewById(R.id.userReview);
            readMore = itemView.findViewById(R.id.readMore);
        }
    }
}