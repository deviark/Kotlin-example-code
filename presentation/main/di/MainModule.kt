package com.lotteryadviser.presentation.main.di

import androidx.lifecycle.ViewModel
import com.lotteryadviser.di.viewmodel.ViewModelKey
import com.lotteryadviser.presentation.main.MainViewModel
import com.lotteryadviser.presentation.main.combinations.CombinationsViewModel
import com.lotteryadviser.presentation.main.favorite.FavoritesViewModel
import com.lotteryadviser.presentation.main.lotteries.LotteriesViewModel
import com.lotteryadviser.presentation.main.lotteryresults.LotteriesResultsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class MainModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LotteriesViewModel::class)
    internal abstract fun bindLotteryViewModel(viewModel: LotteriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    internal abstract fun bindFavoritesViewModel(viewModel: FavoritesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CombinationsViewModel::class)
    internal abstract fun bindHistoryViewModel(viewModel: CombinationsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LotteriesResultsViewModel::class)
    internal abstract fun bindHistoryGamesViewModel(viewModel: LotteriesResultsViewModel): ViewModel
}