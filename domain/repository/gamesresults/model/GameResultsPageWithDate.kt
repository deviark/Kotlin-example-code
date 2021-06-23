package com.lotteryadviser.domain.repository.gamesresults.model

import java.time.Instant

data class GameResultsPageWithDate(
    val playedAt: Instant,
    val page: Int,
    val countPage: Int,
    val gamesResult: List<GameResults>
)