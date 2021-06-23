package com.lotteryadviser.data

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.lotteryadviser.data.language.Languages
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Preferences @Inject constructor(context: Context) {

    companion object {
        private const val PREFERENCE_NAME = "preference"

        private const val IS_FIRST_LAUNCH = "is_first_launch"
        private const val LANGUAGE = "language"
        private const val THEME_MODE = "theme_mode"
        private const val DATE_LAST_COUNTRIES_UPDATE_COUNTRY = "date_last_countries_update_country"

        fun geCurrentLanguage(context: Context): Languages {
            val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            return Languages.getLanguage(
                preference.getInt(
                    LANGUAGE,
                    Languages.getDefaultLanguage().ordinal
                )
            )
        }
    }

    private val preference: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)


    var isFirstLaunch: Boolean
        set(value) = preference.edit().putBoolean(IS_FIRST_LAUNCH, value).apply()
        get() = preference.getBoolean(IS_FIRST_LAUNCH, true)


    var language: Languages
        set(value) = preference.edit().putInt(LANGUAGE, value.ordinal).apply()
        get() = Languages.getLanguage(
            preference.getInt(
                LANGUAGE,
                Languages.getDefaultLanguage().ordinal
            )
        )

    var themeMode: Int
        set(value) = preference.edit().putInt(THEME_MODE, value).apply()
        get() = preference.getInt(THEME_MODE, AppCompatDelegate.MODE_NIGHT_UNSPECIFIED)

    var dateLastCountriesUpdate: Instant?
        set(value) = preference.edit()
            .putString(DATE_LAST_COUNTRIES_UPDATE_COUNTRY, value.toString()).apply()
        get() {
            val value = preference.getString(DATE_LAST_COUNTRIES_UPDATE_COUNTRY, null)
            return if (value.isNullOrEmpty()) {
                null
            } else {
                Instant.parse(value)
            }
        }

}