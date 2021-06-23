package com.lotteryadviser.presentation.main.di

import com.lotteryadviser.presentation.main.MainFragment
import com.lotteryadviser.presentation.main.combinations.CombinationsFragment
import com.lotteryadviser.presentation.main.favorite.FavoritesFragment
import com.lotteryadviser.presentation.main.lotteries.LotteriesFragment
import com.lotteryadviser.presentation.main.lotteryresults.LotteriesResultsFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [
        MainModule::class,
    ]
)
interface MainComponent {

    fun inject(fragment: MainFragment)

    fun inject(fragment: LotteriesFragment)
    fun inject(fragment: FavoritesFragment)
    fun inject(fragment: CombinationsFragment)
    fun inject(fragment: LotteriesResultsFragment)
}