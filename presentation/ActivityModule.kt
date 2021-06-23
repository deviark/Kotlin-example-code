package com.lotteryadviser.presentation

import androidx.lifecycle.ViewModel
import com.lotteryadviser.di.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ActivityModule {

    @Binds
    @IntoMap
    @ViewModelKey(SingleActivityViewModel::class)
    internal abstract fun bindSingleActivityModel(viewModel: SingleActivityViewModel): ViewModel


}