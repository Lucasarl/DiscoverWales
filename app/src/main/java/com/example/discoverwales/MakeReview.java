package com.example.discoverwales;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;

public class MakeReview extends AppCompatActivity {
    private static final connectionPG con = new connectionPG();
    private TranslatorHelper translatorHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.make_review);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String selectedLanguage = LanguagePreferences.getLanguage(this);
        translatorHelper = TranslatorManager.getTranslator(selectedLanguage);

        TextView textView1=findViewById(R.id.textView12);
        translatorHelper.translateTextView(textView1);
        TextView textView2=findViewById(R.id.textView13);
        translatorHelper.translateTextView(textView2);
        TextView textView3=findViewById(R.id.textView15);
        translatorHelper.translateTextView(textView3);



        Button cancel = findViewById(R.id.cancel);

        translatorHelper.translate(cancel.getText().toString(),   translatedText -> {
            cancel.setText(translatedText);}, e -> System.err.println(e.getMessage()));
        Bundle extras = getIntent().getExtras();
        Button submit = findViewById(R.id.submit);
        translatorHelper.translate(submit.getText().toString(),   translatedText -> {
            submit.setText(translatedText);}, e -> System.err.println(e.getMessage()));
        cancel.setOnClickListener(v -> {
            Intent intent = new Intent(MakeReview.this, MuseumInfo1.class);
            intent.putExtra("email", extras.getString("email"));
            intent.putExtra("museum", extras.getString("museum"));
            startActivity(intent);
        });

            EditText reviewText = findViewById(R.id.review);
            RatingBar bar = findViewById(R.id.ratingBar);

            submit.setOnClickListener(v -> {
                Connection conn = con.connectionDB();
                String query = "INSERT INTO \"Reviews\" (museum, reviewer_id, date, rating, review_text) VALUES (?, ?, ?, ?, ?)";

                if(reviewText.getText().toString().isEmpty()) {
                        reviewText.setError("Email is required");
                        reviewText.requestFocus();
                } else if (bar.getRating()==0) {
                    Toast.makeText(this, "Please provide a rating", Toast.LENGTH_SHORT).show();
                    bar.requestFocus();
                } else {

                try {
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                    //Validation, Averages
                    String currentDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, extras.getString("museum"));
                    pstmt.setString(2, extras.getString("email"));
                    pstmt.setString(3, currentDate);
                    pstmt.setFloat(4, bar.getRating());
                    pstmt.setString(5, reviewText.getText().toString());
                    int rs = pstmt.executeUpdate();
                    Intent intent = new Intent(MakeReview.this, MuseumInfo1.class);
                    intent.putExtra("email", extras.getString("email"));
                    intent.putExtra("museum", extras.getString("museum"));
                    startActivity(intent);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }}

            });
        }
    }