package com.lotteryadviser.utils

import android.content.Context
import com.lotteryadviser.R
import com.lotteryadviser.data.database.country.CountryEntity
import com.lotteryadviser.domain.repository.combinations.model.CombinationNumbers
import com.lotteryadviser.domain.repository.countries.model.Country
import com.lotteryadviser.domain.repository.gamesresults.model.GameResults
import com.lotteryadviser.domain.repository.gamesystems.model.GameSystem
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import com.lotteryadviser.domain.repository.recommendnumbers.model.RecommendGroupNumbers
import com.lotteryadviser.domain.utils.DateTimeUtils
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

fun Country.displayName(context: Context): String {
    return if (id == CountryEntity.ID_ALL_COUNTRY) {
        context.getString(R.string.label_country_all)
    } else {
        Locale("", code).getDisplayName(Locale.getDefault())
    }
}

fun Lottery.displayJackpot(): String {
    val numberFormat: NumberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.minimumFractionDigits = 0
    numberFormat.currency = Currency.getInstance(currencyCode)
    return if (jackPot != null) {
        numberFormat.format(jackPot)
    } else {
        ""
    }
}

fun Lottery.displayGameDraw(): String {
    return gameNumber?.toString() ?: DateTimeUtils.covertTimestampToString(playedAt)
}

fun GameSystem?.displayName(context: Context): String {
    return if (isOwnCombination()) {
        context.getString(R.string.label_name_custom_system)
    } else {
        context.getString(
            R.string.label_name_system_formatted,
            this?.countNumbers ?: -1,
            this?.countCombinations ?: -1
        )

    }
}

fun GameSystem?.isOwnCombination(): Boolean {
    return this == null || countNumbers == 0
}

fun GameResults.displayGameDraw(prefix: String = ""): String {
    return if (gameNumber != null) {
        "$prefix$gameNumber"
    } else {
        DateTimeUtils.covertTimestampToString(playedAt)
    }
}

fun CombinationNumbers.getGameSystem(): GameSystem {
    return GameSystem(0, systemCountNumbers ?: 0, countCombinations, 0, 0)
}

fun CombinationNumbers.getDrawGameDisplay(): String {
    return if (hasGameDraw()) {
        "$gameNumber"
    } else {
        DateTimeUtils.covertTimestampToString(playedAt)
    }
}

fun CombinationNumbers.hasGameDraw(): Boolean = gameNumber != null

fun RecommendGroupNumbers.displayChancePercent(): String {
    val df = DecimalFormat()
    df.maximumFractionDigits = 2
    df.isDecimalSeparatorAlwaysShown
    val percent = chance * 100
    return if (percent < 1) {
        "<1%"
    } else {
        df.format(chance * 100) + "%"
    }
}