package com.example.discoverwales;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MuseumsActivity extends AppCompatActivity {

    private static final connectionPG con = new connectionPG();
    private CircleImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_museums);

        initializeWindowInsets();
        FirebaseApp.initializeApp(this);

        ImageButton cardiffCastle=findViewById(R.id.imageButton2);
        cardiffCastle.setImageResource(R.drawable.cardiff_castle);

        LinearLayout layout1= findViewById(R.id.layout1);

        profilePic = findViewById(R.id.profile);
        profilePic.setImageResource(R.drawable.profile_pic);
        ImageButton menuButton = findViewById(R.id.menu);
        menuButton.setImageResource(R.drawable.menu);



        Bundle extras = getIntent().getExtras();

        cardiffCastle.setOnClickListener( v -> {
            Intent intent = new Intent(MuseumsActivity.this, MuseumInfo1.class);
            intent.putExtra("email", extras.getString("email"));
            intent.putExtra("museum", "cardiff_castle");
            startActivity(intent);
        });

        layout1.setOnClickListener( v -> {
            Intent intent = new Intent(MuseumsActivity.this, MuseumInfo1.class);
            intent.putExtra("email", extras.getString("email"));
            intent.putExtra("museum", "cardiff_castle");
            startActivity(intent);
        });



        if (extras != null && extras.getString("email") != null) {
            loadProfilePicture(extras.getString("email"));
        }

        setupMenu(menuButton, "Museums");
        setupProfileMenu(profilePic,extras.getString("email"));

    }

    private void initializeWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void loadProfilePicture(String email) {
        String picName = fetchProfilePictureName(email);

        if (picName != null) {
            downloadAndSetProfilePicture(picName);
        }
    }

    private String fetchProfilePictureName(String email) {
        String query = "SELECT \"profilePicture\" FROM \"Users\" WHERE email = ?";
        try (Connection conn = con.connectionDB();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("profilePicture");
            }
        } catch (SQLException e) {
            Log.e("DatabaseError", "Error fetching profile picture name", e);
        }
        return null;
    }

    private void downloadAndSetProfilePicture(String picName) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/" + picName);
        try {
            File localFile = File.createTempFile(picName, "jpeg");
            storageRef.getFile(localFile)
                    .addOnSuccessListener(taskSnapshot -> {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        profilePic.setImageBitmap(bitmap);
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(MuseumsActivity.this, "Error loading profile picture", Toast.LENGTH_SHORT).show()
                    );
        } catch (Exception e) {
            Log.e("FileError", "Error creating temp file for profile picture", e);
        }
    }

    private void setupMenu(ImageButton menuButton, String highlightedItemId) {
        menuButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MuseumsActivity.this, menuButton);
            popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

            System.out.println(popup.getMenu().size());
            for (int i = 0; i < popup.getMenu().size(); i++) {
                MenuItem item = popup.getMenu().getItem(i);
                if (item.getTitle().toString().equals(highlightedItemId)) {
                    highlightMenuItem(item);
                    break;
                }
            }

            popup.setOnMenuItemClickListener(item -> {
                Toast.makeText(MuseumsActivity.this, "You clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });
            popup.show();
        });
    }

    private void setupProfileMenu(CircleImageView profilePicView, String email) {
        profilePicView.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MuseumsActivity.this, profilePicView);
            popup.getMenuInflater().inflate(R.menu.popup_menu2, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if ("Log Out".contentEquals(item.getTitle())) {
                    navigateToMainActivity();
                } else if ("Profile Settings".contentEquals(item.getTitle())) {
                    Intent intent = new Intent(MuseumsActivity.this, ProfileSettings.class);
                    intent.putExtra("email", email);
                    intent.putExtra("activity1", "museums");
                    startActivity(intent);
                }
                return true;
            });
            popup.show();
        });
    }

    private void highlightMenuItem(MenuItem menuItem) {
        SpannableString styledTitle = new SpannableString(menuItem.getTitle());
        styledTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#9c0084")), 0, styledTitle.length(), 0);
        menuItem.setTitle(styledTitle);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(MuseumsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}