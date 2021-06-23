package com.lotteryadviser.data.language

import android.app.Application
import android.content.Context
import com.lotteryadviser.data.Preferences
import com.yariksoffice.lingver.Lingver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageManager @Inject constructor(
    private val context: Context,
    private val preferences: Preferences
) {

    fun init(app: Application) {
        val locale = preferences.language.getLocale()
        Lingver.init(app, locale)
    }

    fun setLanguage(language: Languages) {
        preferences.language = language
        Lingver.getInstance().setLocale(context, language.getLocale())
    }

    fun getCurrentLanguage(): Languages {
        return preferences.language
    }
}