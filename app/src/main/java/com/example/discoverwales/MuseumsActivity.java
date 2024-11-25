package com.example.discoverwales;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.hdodenhof.circleimageview.CircleImageView;

public class MuseumsActivity extends AppCompatActivity {
    Object menuHelper;
    Class[] argTypes;

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
        ImageButton menu=findViewById(R.id.menu);
        menu.setImageResource(R.drawable.menu);

        Bundle extras = getIntent().getExtras();

        CircleImageView profPic = findViewById(R.id.profile);

        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference().child("images/04431fe1-f7a0-4c26-823a-b10e815c2271");
        System.out.println(FirebaseStorage.getInstance().getReference().getName());
        System.out.println(FirebaseStorage.getInstance().getReference().getBucket());
        System.out.println(FirebaseStorage.getInstance().getReference().toString());
        try {
            final File localFile = File.createTempFile("04431fe1-f7a0-4c26-823a-b10e815c2271", "jpeg");
            System.out.println(localFile.getAbsolutePath());
            System.out.println(localFile.canExecute());
            System.out.println(localFile.exists());
            System.out.println(localFile.canRead());
            System.out.println(localFile.isFile());
            mStorageReference.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(MuseumsActivity.this, "Picture Retrieved", Toast.LENGTH_SHORT).show();
                            Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                            profPic.setImageBitmap(bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MuseumsActivity.this, "Error Occurred with Profile Picture", Toast.LENGTH_SHORT).show();
                            profPic.setImageResource(R.drawable.profile_pic);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

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

    }


}