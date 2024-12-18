package com.example.discoverwales;

public class TranslatorManager {
    private static TranslatorHelper translatorHelper;

    public static TranslatorHelper getTranslator(String languageCode) {
        if (translatorHelper != null) {
            translatorHelper.closeTranslator();
        }
        translatorHelper = new TranslatorHelper(languageCode);
        return translatorHelper;
    }
}
