package com.lotteryadviser.domain.repository.gamesresults.model

import java.time.Instant

data class GameResults(
    val lotteryID: Long,
    val lotteryName: String,
    val gameNumber: Long?,
    val playedAt: Instant,
    val jackPot: Long?,
    val currencyCode: String,
    val results: List<NumberResult>
)