package com.lotteryadviser.presentation.language

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.lotteryadviser.data.language.LanguageManager
import com.lotteryadviser.data.language.Languages
import com.lotteryadviser.di.Injector
import com.lotteryadviser.navigation.Screens
import com.lotteryadviser.presentation.base.BaseViewModel
import com.lotteryadviser.presentation.language.model.WrapperLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LanguagesViewModel @Inject constructor(
    private val router: Router,
    private val languageManager: LanguageManager
) : BaseViewModel() {

    private val _languages: MutableStateFlow<List<WrapperLanguage>> = MutableStateFlow(emptyList())

    val languages = _languages.asStateFlow()

    private var selectedLanguage: Languages = languageManager.getCurrentLanguage()

    init {
        viewModelScope.launch {
            val wrapperLanguages: MutableList<WrapperLanguage> = ArrayList()
            val sortedLanguages = Languages.values().sortedBy { it.name }
            for (language in sortedLanguages) {
                wrapperLanguages.add(
                    WrapperLanguage(
                        language, selectedLanguage == language
                    )
                )
            }
            _languages.value = wrapperLanguages
        }
    }

    fun selectedLanguage(language: Languages) {
        selectedLanguage = language
    }

    fun saveLanguage() {
        if (selectedLanguage != languageManager.getCurrentLanguage()) {
            languageManager.setLanguage(selectedLanguage)
            router.newRootScreen(Screens.MainActivity())
        } else {
            router.backTo(Screens.Main())
        }
    }

    override fun onCleared() {
        Injector.clearLanguagesComponent()
    }

    override fun getScreenName(): String {
        return "Languages"
    }

    override fun getClassName(): String {
        return "LanguagesFragment"
    }
}