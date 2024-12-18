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
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MuseumInfo1 extends AppCompatActivity  implements WebViewTouchListener{

    private static final connectionPG con = new connectionPG();
    private CircleImageView profilePic;
    private TranslatorHelper translatorHelper;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapter viewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.museum_info_1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String selectedLanguage = LanguagePreferences.getLanguage(this);
        translatorHelper = TranslatorManager.getTranslator(selectedLanguage);

        TextView title=findViewById(R.id.title);

        TextView menuTitle=findViewById(R.id.textView14);
        translatorHelper.translateTextView(menuTitle);

        Bundle extras = getIntent().getExtras();
        if (extras.getString("museum").equals("national_museum")) {
            title.setText("National Museum Cardiff");
        } else if (extras.getString("museum").equals("cardiff_museum")) {
            title.setText("Cardiff Museum");
        } else if (extras.getString("museum").equals("st_fagans")) {
            title.setText("St Fagans Museum");
        } else if (extras.getString("museum").equals("coal_museum")) {
            title.setText("National Coal Museum");
        } else if (extras.getString("museum").equals("legion_museum")) {
            title.setText("National Legion Museum");
        }

        translatorHelper.translateTextView(title);

        TextView nRev=findViewById(R.id.numberReviews);

        Connection conn= con.connectionDB();
        String query = "SELECT COUNT(*) FROM \"Reviews\" WHERE museum=?";
        int numberOfReviews=0;
        try {
            PreparedStatement pstmt=conn.prepareStatement(query);
            pstmt.setString(1,extras.getString("museum"));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                numberOfReviews=rs.getInt(1);
                String numberReviews = "(" +numberOfReviews  + ") Reviews";
                nRev.setText(numberReviews);
            } else {
                nRev.setText("(0) Reviews");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        translatorHelper.translateTextView(nRev);

        RatingBar ratingBar = findViewById(R.id.ratingBar3);
        String query2 = "SELECT rating FROM \"Reviews\" WHERE museum=?";
        float totalStars = 0;
        try {
            PreparedStatement pstmt=conn.prepareStatement(query2);
            pstmt.setString(1,extras.getString("museum"));
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                totalStars+=rs.getFloat(1);
            }
            ratingBar.setRating(totalStars/numberOfReviews);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        profilePic = findViewById(R.id.profile);
        profilePic.setImageResource(R.drawable.profile_pic);
        ImageButton menuButton = findViewById(R.id.menu);
        menuButton.setImageResource(R.drawable.menu);

        ImageButton share = findViewById(R.id.share);
        share.setImageResource(R.drawable.share);

        setupShare(share);

        if (extras != null && extras.getString("email") != null) {
            loadProfilePicture(extras.getString("email"));
        }

        setupMenu(menuButton, "Museums");
        setupProfileMenu(profilePic, extras.getString("email"));


        ImageButton backArrow = findViewById(R.id.back2);
        backArrow.setImageResource(R.drawable.back_arrow);
        backArrow.setOnClickListener(v -> {
            Intent i = new Intent(MuseumInfo1.this, MuseumsActivity.class);
            i.putExtra("email", extras.getString("email"));
            startActivity(i);
        });

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        for(int i=0; i<tabLayout.getTabCount()-1;i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null && tab.getText() != null) {
                String originalText = tab.getText().toString();
                System.out.println(originalText);
                translatorHelper.translate(originalText, translatedText -> {
                    tab.setText(translatedText);
                }, e -> System.err.println("Translation error: " + e.getMessage()));
            } else {
                System.err.println("Tab or tab text is null.");
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
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
                            Toast.makeText(MuseumInfo1.this, "Error loading profile picture", Toast.LENGTH_SHORT).show()
                    );
        } catch (Exception e) {
            Log.e("FileError", "Error creating temp file for profile picture", e);
        }
    }

    private void setupMenu(ImageButton menuButton, String highlightedItemId) {
        menuButton.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MuseumInfo1.this, menuButton);
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
                Toast.makeText(MuseumInfo1.this, "You clicked: " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });
            popup.show();
        });
    }

    @Override
    public void onWebViewTouched(boolean isTouched) {
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        viewPager2.setUserInputEnabled(!isTouched); // Disable or enable swiping
    }

    private void setupProfileMenu(CircleImageView profilePicView, String email) {
        profilePicView.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MuseumInfo1.this, profilePicView);
            popup.getMenuInflater().inflate(R.menu.popup_menu2, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                if ("Log Out".contentEquals(item.getTitle())) {
                    navigateToMainActivity();
                } else if ("Profile Settings".contentEquals(item.getTitle())) {
                    Intent intent = new Intent(MuseumInfo1.this, ProfileSettings.class);
                    intent.putExtra("email", email);
                    intent.putExtra("activity1", "museums");
                    startActivity(intent);
                }
                return true;
            });
            popup.show();
        });
    }

    private void setupShare(ImageButton share) {
        // Retrieve the selected museum from the Intent extra
        Intent intent = getIntent();
        String selectedMuseum = intent.getStringExtra("museum");

        // Define the social media links for each museum
        Map<String, Map<String, String>> museumLinks = new HashMap<>();
        museumLinks.put("cardiff_castle", Map.of(
                "Facebook", "https://www.facebook.com/officialcardiffcastle/?locale=en_GB",
                "Instagram", "https://www.instagram.com/cardiff_castle/",
                "Twitter", "https://x.com/cardiff_castle?lang=es"
        ));
        museumLinks.put("national_museum", Map.of(
                "Facebook", "https://www.facebook.com/museumcardiff/?locale=en_GB",
                "Instagram", "https://www.instagram.com/cardiffnationalmuseum/",
                "Twitter", "https://x.com/Museum_Cardiff?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor"
        ));

        museumLinks.put("cardiff_museum", Map.of(
                "Facebook", "https://www.facebook.com/cardiffstory/?locale=en_GB",
                "Instagram", "https://www.instagram.com/museumofcardiff/",
                "Twitter", "https://x.com/TheCardiffStory?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor"
        ));

        museumLinks.put("st_fagans", Map.of(
                "Facebook", "https://www.facebook.com/stfagansmuseum/?locale=en_GB",
                "Instagram", "https://www.instagram.com/stfagansmakersmarket/",
                "Twitter", "https://x.com/StFagans_Museum?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor"
        ));

        museumLinks.put("coal_museum", Map.of(
                "Facebook", "https://www.facebook.com/bigpitmuseum/?locale=en_GB",
                "Instagram", "https://www.instagram.com/ncmme_miningmuseum/",
                "Twitter", "https://x.com/BigPitMuseum?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor"
        ));

        museumLinks.put("legion_museum", Map.of(
                "Facebook", "https://www.facebook.com/romanlegionmuseum/?locale=en_GB",
                "Instagram", "https://www.instagram.com/explore/locations/254715255/national-roman-legion-museum/",
                "Twitter", "https://x.com/RomanCaerleon?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor"
        ));
        // Add links for all 6 museums similarly...

        share.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MuseumInfo1.this, share);
            popup.getMenuInflater().inflate(R.menu.popup_menu3, popup.getMenu());

            popup.setOnMenuItemClickListener(item -> {
                // Retrieve the selected platform
                String selectedPlatform = item.getTitle().toString();

                // Get the appropriate link for the selected museum and platform
                Map<String, String> linksForMuseum = museumLinks.get(selectedMuseum);
                if (linksForMuseum != null) {
                    String socialMediaUrl = linksForMuseum.get(selectedPlatform);
                    if (socialMediaUrl != null) {
                        shareLinkOnPlatform(socialMediaUrl);
                    } else {
                        Toast.makeText(this, "Link not available for this platform.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Museum links not found.", Toast.LENGTH_SHORT).show();
                }
                return true;
            });
            popup.show();
        });
    }

    private void shareLinkOnPlatform(String url) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);

        // Let the user choose their preferred app
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }


    private void highlightMenuItem(MenuItem menuItem) {
        SpannableString styledTitle = new SpannableString(menuItem.getTitle());
        styledTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#9c0084")), 0, styledTitle.length(), 0);
        menuItem.setTitle(styledTitle);
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(MuseumInfo1.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
