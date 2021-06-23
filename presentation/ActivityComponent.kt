package com.lotteryadviser.presentation

import com.lotteryadviser.presentation.combinationsnumbers.di.HistoryInfoComponent
import com.lotteryadviser.presentation.country.di.CountriesComponent
import com.lotteryadviser.presentation.dialog.di.DialogComponent
import com.lotteryadviser.presentation.gameresults.combinations.di.HistoryGameSystemComponent
import com.lotteryadviser.presentation.gameresults.results.di.GameResultsComponent
import com.lotteryadviser.presentation.gameresults.statistic.di.StatisticGameResultComponent
import com.lotteryadviser.presentation.language.di.LanguagesComponent
import com.lotteryadviser.presentation.lottery.di.LotteryComponent
import com.lotteryadviser.presentation.main.di.MainComponent
import com.lotteryadviser.presentation.subscription.di.SubscriptionComponent
import dagger.Subcomponent

@Subcomponent(
    modules = [
        ActivityModule::class
    ]
)
interface ActivityComponent {

    fun inject(activity: SingleActivity)

    fun plusLotteryComponent(): LotteryComponent

    fun plusCountriesComponent(): CountriesComponent

    fun plusLanguagesComponent(): LanguagesComponent

    fun plusMainComponent(): MainComponent

    fun plusDialogComponent(): DialogComponent

    fun plusHistoryInfoComponent(): HistoryInfoComponent

    fun plusHistoryGameSystemComponent(): HistoryGameSystemComponent

    fun plusStatisticGameResultComponent(): StatisticGameResultComponent

    fun plusGameResultComponent(): GameResultsComponent

    fun plusSubscriptionComponent(): SubscriptionComponent
}