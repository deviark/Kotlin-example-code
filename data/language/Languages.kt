package com.lotteryadviser.data.language

import com.lotteryadviser.R
import java.util.*

enum class Languages(val code: String, val country: String, val label: Int) {
    ENGLISH("en", "US", R.string.language_english),
    UKRAINE("uk", "UA", R.string.language_ukraine),
    RUSSIA("ru", "RU", R.string.language_russian),
    GERMAN("de", "DE", R.string.language_german),
    FRANCE("fr", "FR", R.string.language_french);

    fun getLocale(): Locale = Locale(code, country)

    companion object {
        fun getDefaultLanguage(): Languages {
            val locale = Locale.getDefault()
            var language: Languages = ENGLISH
            for (item in values()) {
                if (item.code.equals(locale.language, true)) {
                    language = item
                }
            }
            return language
        }

        fun getLanguage(ordinal: Int): Languages {
            return try {
                values()[ordinal]
            } catch (e: Exception) {
                getDefaultLanguage()
            }
        }
    }
}