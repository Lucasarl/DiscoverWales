package com.example.discoverwales;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSettings extends AppCompatActivity {
    private static final connectionPG con = new connectionPG();
    private CircleImageView profilePic;
    //ImageView imageView;
    Uri image;
    Button uploadImage;
    String imageId=null;
    StorageReference storageReference;

    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if(result.getResultCode()==RESULT_OK) {
                if(result.getData()!=null){
                    image=result.getData().getData();
                    Glide.with(getApplicationContext()).load(image).into(profilePic);
                    System.out.println("Check...");
                    uploadImage.setEnabled(true);
                    System.out.println("Check...");
                    uploadImage(image);
                    System.out.println("Check...");
                }
            } else {
                Toast.makeText(ProfileSettings.this,"Please select an image", Toast.LENGTH_SHORT).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        profilePic = findViewById(R.id.profile);
        profilePic.setImageResource(R.drawable.profile_pic);
        ImageButton edit = findViewById(R.id.editPassword);
        edit.setImageResource(R.drawable.edit);
        ImageButton backArrow = findViewById(R.id.back);
        backArrow.setImageResource(R.drawable.back_arrow);
        Bundle extras = getIntent().getExtras();
        backArrow.setOnClickListener(v -> {
            //this.finish(); ///CANT DO THIS, ALSO FIGURE OUT WHY NULL AND UPDATE PASSWORD
            Intent intent;
            if (extras.getString("activity1").equals("museums")) {
                intent = new Intent(ProfileSettings.this, MuseumsActivity.class);
                intent.putExtra("email", extras.getString("email"));
                startActivity((intent));
            }
        });


        TextView textEmail = findViewById(R.id.email);
        TextView textFirstName = findViewById(R.id.firstName);
        TextView textLastName = findViewById(R.id.lastName);
        TextView textPassword = findViewById(R.id.password);

        String queryFirstName = "SELECT * FROM \"Users\" WHERE email = ?";

        edit.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileSettings.this, ChangePassword.class);
            intent.putExtra("email", extras.getString("email"));
            intent.putExtra("activity", "settings");
            intent.putExtra("activity1", extras.getString("activity1"));
            startActivity(intent);
        });


        textEmail.setText(extras.getString("email"));
        try (Connection conn = con.connectionDB();
             PreparedStatement pstmt = conn.prepareStatement(queryFirstName)) {
            pstmt.setString(1, extras.getString("email"));
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            textFirstName.setText(rs.getString("firstName"));
            textLastName.setText(rs.getString("lastName"));

            textPassword.setText(rs.getString("password"));
            if (!StringUtils.isEmpty(rs.getString("profilePicture"))) {
                storageReference = FirebaseStorage.getInstance().getReference().child("images/" + rs.getString("profilePicture"));
                File localFile = File.createTempFile(rs.getString("profilePicture"), "jpeg");
                storageReference.getFile(localFile)
                        .addOnSuccessListener(taskSnapshot -> {
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profilePic.setImageBitmap(bitmap);
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(ProfileSettings.this, "Error loading profile picture", Toast.LENGTH_SHORT).show()
                        );
            }
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error fetching profile picture name", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        uploadImage = findViewById(R.id.changePfpButton);
        uploadImage.setOnClickListener(v -> {
            // Launch the image picker intent
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            activityResultLauncher.launch(intent);

            // Show an AlertDialog to confirm
            new AlertDialog.Builder(ProfileSettings.this)
                    .setTitle("Confirm Upload")
                    .setMessage("Are you sure you want to update your profile picture?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Execute the query after confirmation
                        String query = "UPDATE \"Users\" SET \"profilePicture\" = ? WHERE email = ?";
                        try (Connection conn = con.connectionDB();
                             PreparedStatement pstmt = conn.prepareStatement(query)) {

                            pstmt.setString(1, imageId); // Set the new profile picture ID
                            pstmt.setString(2, extras.getString("email")); // Set the email
                            int rowsAffected = pstmt.executeUpdate();

                            if (rowsAffected > 0) {
                                Toast.makeText(ProfileSettings.this, "Profile picture updated successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ProfileSettings.this, "Failed to update profile picture.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (SQLException e) {
                            Log.e("DatabaseError", "Error updating profile picture", e);
                            Toast.makeText(ProfileSettings.this, "Database error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Dismiss the dialog without doing anything
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    private void uploadImage(Uri image) {
        try{
        imageId= UUID.randomUUID().toString();
        StorageReference reference= FirebaseStorage.getInstance().getReference().child("images/"+ imageId);
            System.out.println(reference.getPath());
            System.out.println(reference.getName());
            reference.putFile(image);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}