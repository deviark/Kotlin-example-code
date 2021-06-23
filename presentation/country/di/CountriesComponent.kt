package com.lotteryadviser.presentation.country.di

import com.lotteryadviser.presentation.country.CountriesFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        CountriesModule::class
    ]
)
interface CountriesComponent {

    fun inject(fragment: CountriesFragment)
}