package com.lotteryadviser.navigation

import android.content.Intent
import com.github.terrakok.cicerone.androidx.ActivityScreen
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.lotteryadviser.domain.repository.gamesresults.model.GameResults
import com.lotteryadviser.domain.repository.gamesystems.model.GameSystem
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import com.lotteryadviser.presentation.SingleActivity
import com.lotteryadviser.presentation.combinationsnumbers.CombinationsNumbersInfoFragment
import com.lotteryadviser.presentation.country.CountriesFragment
import com.lotteryadviser.presentation.gameresults.combinations.GameResultsCombinationsFragment
import com.lotteryadviser.presentation.gameresults.results.GameResultsFragment
import com.lotteryadviser.presentation.gameresults.statistic.StatisticGameResultFragment
import com.lotteryadviser.presentation.language.LanguagesFragment
import com.lotteryadviser.presentation.lottery.combinations.RecommendCombinationsFragment
import com.lotteryadviser.presentation.lottery.gamesystems.GameSystemsFragment
import com.lotteryadviser.presentation.lottery.info.LotteryFragment
import com.lotteryadviser.presentation.main.MainFragment
import com.lotteryadviser.presentation.subscription.SubscriptionFragment

object Screens {

    fun MainActivity() = ActivityScreen { context ->
        Intent(context, SingleActivity::class.java)
    }

    fun Countries(firstLaunch: Boolean = false) =
        FragmentScreen(key = "CountriesScreen") {
            CountriesFragment.newInstance(firstLaunch)
        }

    fun Langugages() =
        FragmentScreen(key = "LanguagesScreen") { LanguagesFragment() }

    fun Main() = FragmentScreen(key = "MainScreen") { MainFragment() }

    fun Lottery(lottery: Lottery) = FragmentScreen(key = "LotteryScreen") {
        LotteryFragment.newInstance(lottery)
    }

    fun GameSystems(lottery: Lottery, countRecommendNumbers: Int) =
        FragmentScreen(key = "GameSystemsScreen") {
            GameSystemsFragment.newInstance(lottery, countRecommendNumbers)
        }

    fun Combinations(
        lottery: Lottery,
        gameSystem: GameSystem? = null,
        countCombinations: Int? = null,
        sizeCombination: Int? = null,
        recommendNumbers: List<Int> = emptyList(),
    ) =
        FragmentScreen(key = "RecommendCombinationsScreen") {
            RecommendCombinationsFragment.newInstance(
                lottery,
                gameSystem,
                countCombinations,
                sizeCombination,
                recommendNumbers
            )
        }

    fun GameResults(lottery: Lottery) = FragmentScreen(key = "GameResultsScreen") {
        GameResultsFragment.newInstance(lottery)
    }

    fun CombinationNumbersInfo(combinationID: Long) =
        FragmentScreen(key = "CombinationsNumbersInfoScreen") {
            CombinationsNumbersInfoFragment.newInstance(combinationID)
        }

    fun GameResultsCombinations(item: GameResults) =
        FragmentScreen(key = "GameResultsCombinationsScreen") {
            GameResultsCombinationsFragment.newInstance(item)
        }

    fun StatisticGameResult(item: GameResults, combinationID: Long) =
        FragmentScreen(key = "StatisticGameResultScreen") {
            StatisticGameResultFragment.newInstance(item, combinationID)
        }

    fun Subscriptions() = FragmentScreen(key = "SubscriptionScreen") {
        SubscriptionFragment()
    }

}