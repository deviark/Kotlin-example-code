package com.lotteryadviser.domain.repository.lotteries.model

import java.time.Instant

data class Lottery(
    val id: Long,
    val name: String,
    val brand: String,
    var imageUrl: String?,
    val jackPot: Long?,
    val currencyCode: String,
    val playedAt: Instant,
    var gameNumber: Long?,
    val countryCode: String,
    var mainCountNumbers: Int,
    var mainCountNumbersCombination: Int,
    var bonusCountNumbers: Int?,
    var bonusCountNumbersCombination: Int?,
    var bonusZeroStatus: Boolean?,
    var minCountNumberCombination: Int?,
    var zeroStatus: Boolean,
    var isFavorite: Boolean
)