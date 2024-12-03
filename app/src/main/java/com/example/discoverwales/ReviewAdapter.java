package com.example.discoverwales;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Reviews.Review> reviewList;

    // Constructor
    public ReviewAdapter(List<Reviews.Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Reviews.Review review = reviewList.get(position);
        holder.reviewerName.setText(review.getReviewerName());
        holder.reviewDate.setText(review.getDate());
        holder.reviewRating.setRating(review.getRating());
        holder.reviewText.setText(review.getReviewText());
        if (review.getProfilePictureName() != null && !review.getProfilePictureName().isEmpty()) {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + review.getProfilePictureName());
            try {
                File localFile = File.createTempFile(review.getProfilePictureName(), "jpeg");
                storageRef.getFile(localFile)
                        .addOnSuccessListener(taskSnapshot -> {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            holder.profilePicture.setImageBitmap(bitmap);
                        });

            } catch (Exception e) {
                Log.e("FileError", "Error creating temp file for profile picture", e);
            }
        } else {
            holder.profilePicture.setImageResource(R.drawable.profile_pic);
        }
    }



    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    // ViewHolder class
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerName, reviewDate, reviewText;
        CircleImageView profilePicture;
        RatingBar reviewRating;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewerName = itemView.findViewById(R.id.reviewerName);
            reviewDate = itemView.findViewById(R.id.reviewDate);
            reviewRating = itemView.findViewById(R.id.reviewRating);
            profilePicture = itemView.findViewById(R.id.profilePicture);
            reviewText = itemView.findViewById(R.id.reviewText);
        }
    }

    public void updateData(List<Reviews.Review> newReviews) {
        this.reviewList = newReviews;
        notifyDataSetChanged();  // Notify adapter to update the UI
    }

    public void loadProfilePicture(String pictureName, Context context, CircleImageView profilePic) {
        String picName = pictureName;

        if (picName != null) {
            downloadAndSetProfilePicture(picName, context, profilePic);
        }
    }


    private void downloadAndSetProfilePicture(String picName, Context context, CircleImageView profilePic) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + picName);
        try {
            File localFile = File.createTempFile(picName, "jpeg");
            storageRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        profilePic.setImageBitmap(bitmap);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Error loading profile picture", Toast.LENGTH_SHORT).show()
                    );
        } catch (Exception e) {
            Log.e("FileError", "Error creating temp file for profile picture", e);
        }
    }

}
