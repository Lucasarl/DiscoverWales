package com.example.discoverwales;

import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.concurrent.CompletableFuture;

public class TranslatorHelper {
    private Translator translator;

    public TranslatorHelper(String targetLanguageCode) {
        TranslatorOptions options = new TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(targetLanguageCode)
                .build();

        translator = Translation.getClient(options);
        downloadModel();
    }

    // Download the translation model
    private void downloadModel() {
        translator.downloadModelIfNeeded()
                .addOnSuccessListener(unused -> Log.d("TranslatorHelper", "Model downloaded"))
                .addOnFailureListener(e -> Log.e("TranslatorHelper", "Model download failed", e));
    }

    // Translate text
    public void translate(String text, OnSuccessListener<String> onSuccess, OnFailureListener onFailure) {
        translator.translate(text)
                .addOnSuccessListener(onSuccess)
                .addOnFailureListener(onFailure);
    }

    public String translate(String text) {
        CompletableFuture<String> future = new CompletableFuture<>();

        // Use your asynchronous translation method
        this.translate(text, future::complete, e -> {
            System.err.println(e.getMessage());
            future.completeExceptionally(e);
        });

        try {
            // Wait for the translation result (blocks the thread temporarily)
            return future.get(); // You can use a timeout with future.get(timeout, unit) if needed
        } catch (Exception e) {
            throw new RuntimeException("Translation failed", e);
        }
    }

    public void translateTextView(TextView view) {
        this.translate(view.getText().toString(),   translatedText -> {
            view.setText(translatedText);}, e -> System.err.println(e.getMessage()));
    }

    public void closeTranslator() {
        if (translator != null) {
            translator.close();
        }
    }
}



