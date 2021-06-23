package com.lotteryadviser.presentation.country.di

import androidx.lifecycle.ViewModel
import com.lotteryadviser.di.viewmodel.ViewModelKey
import com.lotteryadviser.presentation.country.CountriesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class  CountriesModule {

    @Binds
    @IntoMap
    @ViewModelKey(CountriesViewModel::class)
    internal abstract fun bindCountriesViewModel(viewModel: CountriesViewModel): ViewModel
}