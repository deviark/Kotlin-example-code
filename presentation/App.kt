package com.lotteryadviser.presentation

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.lotteryadviser.data.Preferences
import com.lotteryadviser.data.language.LanguageManager
import com.lotteryadviser.di.Injector
import javax.inject.Inject

class App: Application() {

    @Inject
    lateinit var languageManager: LanguageManager

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate() {
        Injector.init(this)
            .inject(this)
        languageManager.init(this)
        initThemeMode()
        super.onCreate()
    }

    private fun initThemeMode() {
        val nightMode = preferences.themeMode
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

}