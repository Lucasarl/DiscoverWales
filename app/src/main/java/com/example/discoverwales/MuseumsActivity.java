package com.example.discoverwales;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MuseumsActivity extends AppCompatActivity {
    Object menuHelper;
    Class[] argTypes;
    private static connectionPG con=new connectionPG();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_museums);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseApp.initializeApp(this);
        ImageButton menu=findViewById(R.id.menu);
        menu.setImageResource(R.drawable.menu);

        Bundle extras = getIntent().getExtras();

        CircleImageView profPic = findViewById(R.id.profile);
        profPic.setImageResource(R.drawable.profile_pic);

        Connection conn = con.connectionDB();;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String picName="SELECT \"profilePicture\" FROM \"Users\" WHERE email = ?";
        boolean next=false;
        try {
            pstmt = conn.prepareStatement(picName);
            pstmt.setString(1,extras.getString("email"));
            rs = pstmt.executeQuery();
            next=rs.next();
            if(next){
            picName=rs.getString("profilePicture");}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if(next) {
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference().child("images/"+picName);
        try {
            final File localFile = File.createTempFile(picName, "jpeg");

            mStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(MuseumsActivity.this, "Picture Retrieved", Toast.LENGTH_SHORT).show();
                            //System.out.println("Hello");
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profPic.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MuseumsActivity.this, "Error Occurred with Profile Picture", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }}

        menu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(MuseumsActivity.this, menu);
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());
                MenuItem specificItem = popup.getMenu().findItem(R.id.museums);

                SpannableString styledTitle = new SpannableString(specificItem.getTitle());
                styledTitle.setSpan(new ForegroundColorSpan(Color.parseColor("#9c0084")), 0, styledTitle.length(), 0);
                specificItem.setTitle(styledTitle);

                popup.setOnMenuItemClickListener( (MenuItem item) -> {
                        Toast.makeText(
                                MuseumsActivity.this,
                                "You Clicked : " + item.getTitle(),
                                Toast.LENGTH_SHORT
                        ).show();
                        return true;
                });
                popup.show();
        });
        profPic.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(MuseumsActivity.this, profPic);
            popup.getMenuInflater()
                    .inflate(R.menu.popup_menu2, popup.getMenu());

            popup.setOnMenuItemClickListener( (MenuItem item) -> {
                if(item.getTitle().equals("Log Out")) {
                    Intent i = new Intent(MuseumsActivity.this, MainActivity.class);
                    startActivity(i);
                }
                return true;
            });
            popup.show();
        });

    }


}