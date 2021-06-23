package com.lotteryadviser.domain.repository.gamesresults

import com.lotteryadviser.domain.repository.gamesresults.model.GameResultsPageWithDate
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import java.time.Instant

interface GamesResultsRepository {
    suspend fun getGamesResultsWithDate(lottery: Lottery, page: Int, playedAt: Instant?): GameResultsPageWithDate
}