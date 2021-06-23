package com.lotteryadviser.di

import com.lotteryadviser.presentation.ActivityComponent
import com.lotteryadviser.presentation.App
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

object Injector {

    lateinit var appComponent: AppComponent
        private set

    private var activityComponent: ActivityComponent? = null
    private var countriesComponent: CountriesComponent? = null
    private var languagesComponent: LanguagesComponent? = null
    private var mainComponent: MainComponent? = null
    private var dialogComponent: DialogComponent? = null
    private var lotteryComponent: LotteryComponent? = null
    private var historyInfoComponent: HistoryInfoComponent? = null
    private var historyGameSystemComponent: HistoryGameSystemComponent? = null
    private var statisticGameResultComponent: StatisticGameResultComponent? = null
    private var gameResultsComponent: GameResultsComponent? = null
    private var subscriptionComponent: SubscriptionComponent? = null


    fun init(app: App): AppComponent {
        appComponent = DaggerAppComponent.factory().create(app)
        return appComponent
    }

    fun activityComponent(): ActivityComponent {
        if (activityComponent == null) {
            activityComponent = appComponent.plusActivityComponent()
        }
        return activityComponent!!
    }

    fun clearActivityComponent() {
        activityComponent = null
    }

    fun countriesComponent(): CountriesComponent {
        if (countriesComponent == null) {
            countriesComponent = activityComponent().plusCountriesComponent()
        }
        return countriesComponent!!
    }

    fun clearCountriesComponent() {
        countriesComponent = null
    }

    fun languagesComponent(): LanguagesComponent {
        if (languagesComponent == null) {
            languagesComponent = activityComponent().plusLanguagesComponent()
        }
        return languagesComponent!!
    }

    fun clearLanguagesComponent() {
        languagesComponent = null
    }

    fun mainComponent(): MainComponent {
        if (mainComponent == null) {
            mainComponent = activityComponent().plusMainComponent()
        }
        return mainComponent!!
    }

    fun clearMainComponent() {
        mainComponent = null
    }

    fun dialogComponent(): DialogComponent {
        if (dialogComponent == null) {
            dialogComponent = activityComponent().plusDialogComponent()
        }
        return dialogComponent!!
    }

    fun clearDialogComponent() {
        dialogComponent = null
    }


    fun lotteryComponent(): LotteryComponent {
        if (lotteryComponent == null) {
            lotteryComponent = activityComponent().plusLotteryComponent()
        }
        return lotteryComponent!!
    }

    fun clearLotteryComponent() {
        lotteryComponent = null
    }


    fun historyInfoComponent(): HistoryInfoComponent {
        if (historyInfoComponent == null) {
            historyInfoComponent = activityComponent().plusHistoryInfoComponent()
        }
        return historyInfoComponent!!
    }

    fun clearHistoryInfoComponent() {
        historyInfoComponent = null
    }

    fun gameResultsComponent(): GameResultsComponent {
        if (gameResultsComponent == null) {
            gameResultsComponent = activityComponent().plusGameResultComponent()
        }
        return gameResultsComponent!!
    }

    fun clearGameResultsComponent() {
        gameResultsComponent = null
    }

    fun historyGameSystemComponent(): HistoryGameSystemComponent {
        if (historyGameSystemComponent == null) {
            historyGameSystemComponent = activityComponent().plusHistoryGameSystemComponent()
        }
        return historyGameSystemComponent!!
    }

    fun clearHistoryGameSystemComponent() {
        historyGameSystemComponent = null
    }

    fun statisticGameResultComponent(): StatisticGameResultComponent {
        if (statisticGameResultComponent == null) {
            statisticGameResultComponent = activityComponent().plusStatisticGameResultComponent()
        }
        return statisticGameResultComponent!!
    }

    fun clearStatisticGameResultComponent() {
        statisticGameResultComponent = null
    }

    fun subscriptionComponent(): SubscriptionComponent {
        if (subscriptionComponent == null) {
            subscriptionComponent = activityComponent().plusSubscriptionComponent()
        }
        return subscriptionComponent!!
    }

    fun clearSubscriptionComponent() {
        subscriptionComponent = null
    }
}