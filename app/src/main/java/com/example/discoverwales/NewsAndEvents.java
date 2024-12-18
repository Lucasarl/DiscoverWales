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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsAndEvents extends AppCompatActivity {
    private static final connectionPG con = new connectionPG();
    private CircleImageView profilePic;
    private TranslatorHelper translatorHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_news_and_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String selectedLanguage = LanguagePreferences.getLanguage(this);
        translatorHelper = TranslatorManager.getTranslator(selectedLanguage);

        TextView menuTitle=findViewById(R.id.textView14);
        translatorHelper.translateTextView(menuTitle);

        TextView title=findViewById(R.id.title);
        translatorHelper.translateTextView(title);

        TextView newsTitle=findViewById(R.id.newsTitle);
        translatorHelper.translateTextView(newsTitle);

        TextView newsSubtitle=findViewById(R.id.newsSubtitle);
        translatorHelper.translateTextView(newsSubtitle);

        TextView newsDetails=findViewById(R.id.newsDetails);
        translatorHelper.translateTextView(newsDetails);


        Bundle extras = getIntent().getExtras();
        profilePic = findViewById(R.id.profile);
        profilePic.setImageResource(R.drawable.profile_pic);
        if (extras != null && extras.getString("email") != null) {
            loadProfilePicture(extras.getString("email"));
        }
        ImageButton menuButton = findViewById(R.id.menu);
        menuButton.setImageResource(R.drawable.menu);
        setupMenu(menuButton, "News and Events", extras.getString("email"));
        setupProfileMenu(profilePic,extras.getString("email"));
        Button readMore=findViewById(R.id.readMoreButton);
        System.out.println(readMore.getText().toString());
        //readMore.setText(translatorHelper.translate(readMore.getText().toString()));
        translatorHelper.translate(readMore.getText().toString(),   translatedText -> {
            readMore.setText(translatedText);}, e -> System.err.println(e.getMessage()));
        readMore.setOnClickListener(v->{
                Intent intent = new Intent(NewsAndEvents.this, Library_info1.class);
                intent.putExtra("email", extras.getString("email"));
                intent.putExtra("museum", "news1");
                startActivity(intent);}
                );
        ImageView backArrow= findViewById(R.id.backArrow);
        backArrow.setOnClickListener(v -> {
            Intent i = new Intent(NewsAndEvents.this, MuseumsActivity.class);
            i.putExtra("email", extras.getString("email"));
            startActivity(i);
        });


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
                            Toast.makeText(NewsAndEvents.this, "Error loading profile picture", Toast.LENGTH_SHORT).show()
                    );
        } catch (Exception e) {
            Log.e("FileError", "Error creating temp file for profile picture", e);
        }
    }

    private void setupMenu(ImageButton menuButton, String highlightedItemId, String email) {
        menuButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(NewsAndEvents.this, menuButton);
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
                if(item.getTitle().equals("Museums")){
                    Intent i = new Intent(NewsAndEvents.this, MuseumsActivity.class);
                    i.putExtra("email", email);
                    startActivity(i);
                } else if (item.getTitle().equals("Cultural Library")) {
                    Intent i = new Intent(NewsAndEvents.this, CulturalLibrary.class);
                    i.putExtra("email", email);
                    startActivity(i);
                }
                return true;
            });
            popup.show();
        });
    }

    private void setupProfileMenu(CircleImageView profilePicView, String email) {
        profilePicView.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(NewsAndEvents.this, profilePicView);
            popup.getMenuInflater().inflate(R.menu.popup_menu2, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if ("Log Out".contentEquals(item.getTitle())) {
                    navigateToMainActivity();
                } else if ("Profile Settings".contentEquals(item.getTitle())) {
                    Intent intent = new Intent(NewsAndEvents.this, ProfileSettings.class);
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
        Intent intent = new Intent(NewsAndEvents.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}