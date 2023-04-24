package com.drmontoya.openweather.presentation.fragments.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel
@Inject constructor() : ViewModel() {

    private val _apiLanguage = MutableLiveData<String>()
    val apiLanguage: LiveData<String>
        get() = _apiLanguage

    fun selectApiLanguge(language: String) {
        _apiLanguage.postValue(language)
    }


    fun modifyApiLanguage(context: Context) {
        writeApiLanguageToSharedPreferences(context, apiLanguage.value.toString())
    }

    companion object {
        val SETTINGS_PREFERENCES = "settings_preference"
        val API_LANGUAGE = "api_language"
        val LANGUAGES_MAP = mapOf<String, String>(
            "English" to "en",
            "Spanish" to "es"
        )
        val LANGUAGES = listOf(
            "English",
            "Spanish"
        )

        fun getApiLanguageWithContext(context: Context): String {
            val preferences =
                context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
            return preferences.getString(API_LANGUAGE, null) ?: LANGUAGES_MAP["English"] ?: "en"
        }

        fun writeApiLanguageToSharedPreferences(context: Context, language: String) {
            val preferences =
                context.getSharedPreferences(SETTINGS_PREFERENCES, Context.MODE_PRIVATE)
            val editor = preferences.edit()
            val languageClave = LANGUAGES_MAP[language] ?: "en"
            editor.putString(SETTINGS_PREFERENCES, languageClave)
            editor.apply()
        }
    }

}