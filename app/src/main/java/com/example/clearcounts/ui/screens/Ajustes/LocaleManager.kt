package com.example.clearcounts.ui.screens.Ajustes

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleManager {

    private const val PREF_NAME = "settings"
    private const val KEY_LOCALE = "app_locale"

    fun applyLocale(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val tag = prefs.getString(KEY_LOCALE, "es") ?: "es"
        val locale = Locale.forLanguageTag(tag)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
    }

    fun setLocale(context: Context, languageTag: String) {
        Log.d("IDIOMA", "setLocale llamado con: $languageTag")
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LOCALE, languageTag)
            .apply()
        Log.d("IDIOMA", "SharedPreferences guardado")
        val locale = Locale.forLanguageTag(languageTag)
        Log.d("IDIOMA", "Locale creado: $locale")
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
        Log.d("IDIOMA", "setApplicationLocales ejecutado")
    }

    fun getCurrentLocaleTag(context: Context): String {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getString(KEY_LOCALE, "es") ?: "es"
    }
}