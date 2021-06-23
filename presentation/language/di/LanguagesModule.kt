package com.lotteryadviser.presentation.language.di

import androidx.lifecycle.ViewModel
import com.lotteryadviser.di.viewmodel.ViewModelKey
import com.lotteryadviser.presentation.language.LanguagesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class LanguagesModule {

    @Binds
    @IntoMap
    @ViewModelKey(LanguagesViewModel::class)
    internal abstract fun bindLanguagesViewModel(viewModel: LanguagesViewModel): ViewModel
}