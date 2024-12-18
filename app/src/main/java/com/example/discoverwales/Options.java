package com.example.discoverwales;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.mlkit.nl.translate.TranslateLanguage;

public class Options extends AppCompatActivity {
    private TextView textView;
    private Button buttonEnglish, buttonWelsh;
    private RadioGroup radioGroupTextSize;
    private SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.options);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        Bundle extras = getIntent().getExtras();

        Button openTalkBackButton = findViewById(R.id.openTalkBackButton);

        openTalkBackButton.setOnClickListener(v -> {
            try {
                // Open TalkBack settings
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });



        textView = findViewById(R.id.textView);
        buttonEnglish = findViewById(R.id.buttonEnglish);
        buttonWelsh = findViewById(R.id.buttonWelsh);
        // Load the saved language
        String selectedLanguage = LanguagePreferences.getLanguage(this);
        System.out.println(selectedLanguage);

        // Create a TranslatorHelper for the saved language
        TranslatorHelper translatorHelper = TranslatorManager.getTranslator(selectedLanguage);


        // Language selection buttons
        buttonEnglish.setOnClickListener(v -> {
            LanguagePreferences.saveLanguage(this, TranslateLanguage.ENGLISH);
            recreate(); // Restart activity to apply changes
        });

        buttonWelsh.setOnClickListener(v -> {
            LanguagePreferences.saveLanguage(this, TranslateLanguage.WELSH);
            recreate(); // Restart activity to apply changes
        });

        translatorHelper.translate(buttonEnglish.getText().toString(),   translatedText -> {
            buttonEnglish.setText(translatedText);}, e -> System.err.println(e.getMessage()));

        translatorHelper.translate(buttonWelsh.getText().toString(),   translatedText -> {
            buttonWelsh.setText(translatedText);}, e -> System.err.println(e.getMessage()));

        radioGroupTextSize = findViewById(R.id.radioGroupTextSize);
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE);

        // Load saved preference
        String savedSize = sharedPreferences.getString("textSize", "Medium");
        if (savedSize != null) {
            switch (savedSize) {
                case "Small":
                    radioGroupTextSize.check(R.id.radioSmall);
                    break;
                case "Medium":
                    radioGroupTextSize.check(R.id.radioMedium);
                    break;
                case "Large":
                    radioGroupTextSize.check(R.id.radioLarge);
                    break;
            }
        }

        // Save preference on change
        radioGroupTextSize.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedSize = "Medium";
            if (checkedId == R.id.radioSmall) {
                selectedSize = "Small";
            } else if (checkedId == R.id.radioMedium) {
                selectedSize = "Medium";
            } else if (checkedId == R.id.radioLarge) {
                selectedSize = "Large";
            }

            sharedPreferences.edit().putString("textSize", selectedSize).apply();
        });


        ImageButton backArrow= findViewById(R.id.imageButton);
        backArrow.setOnClickListener(v -> {
            Intent i = new Intent(Options.this, MuseumsActivity.class);
            i.putExtra("email", extras.getString("email"));
            startActivity(i);
        });

        translatorHelper.translate(textView.getText().toString(), translatedText -> {
            textView.setText(translatedText); // Update TextView with the translation
        }, e -> System.err.println(e.getMessage()));
    }


    }
