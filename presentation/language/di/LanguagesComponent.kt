package com.lotteryadviser.presentation.language.di

import com.lotteryadviser.presentation.language.LanguagesFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        LanguagesModule::class
    ]
)
interface LanguagesComponent {

    fun inject(fragment: LanguagesFragment)
}