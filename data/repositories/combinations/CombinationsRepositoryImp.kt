package com.lotteryadviser.data.repositories.combinations

import com.lotteryadviser.data.database.combination.CombinationDao
import com.lotteryadviser.data.database.combination.convertToEntity
import com.lotteryadviser.data.database.combination.convertToModel
import com.lotteryadviser.data.database.combination.numbers.convertToEntity
import com.lotteryadviser.data.database.country.isAllCountry
import com.lotteryadviser.domain.model.Filter
import com.lotteryadviser.domain.repository.combinations.CombinationsRepository
import com.lotteryadviser.domain.repository.combinations.model.CombinationNumbers
import com.lotteryadviser.domain.repository.countries.model.Country
import com.lotteryadviser.domain.repository.gamesystems.model.GameSystem
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import com.lotteryadviser.domain.utils.DateTimeUtils
import java.time.Instant
import javax.inject.Inject

class CombinationsRepositoryImp @Inject constructor(
    private val dao: CombinationDao
) : CombinationsRepository {

    override suspend fun getCombinationNumbers(filter: Filter): List<CombinationNumbers> {
        val dateFrom = DateTimeUtils.flushTime(filter.date)
        val dateTo = DateTimeUtils.nextDay(dateFrom)
            .minusSeconds(1)

        return if (filter.country.isAllCountry()) {
            dao.findAllBetweenCreateAt(dateFrom, dateTo).convertToModel()
        } else {
            dao.findAllBetweenCreateAtAndCountry(filter.country.code, dateFrom, dateTo)
                .convertToModel()
        }
    }


    override suspend fun getCombinations(
        lottery: Lottery,
        gameSystem: GameSystem
    ): CombinationNumbers? {
        val gameNumber = lottery.gameNumber
        return if (gameNumber == null) {
            dao.findByLotteryAndGameSystem(
                lottery.playedAt,
                gameSystem.countNumbers,
                gameSystem.countCombinations,
                gameSystem.version,
                gameSystem.sizeCombination
            )
        } else {
            dao.findByLotteryAndGameSystem(
                gameNumber,
                lottery.playedAt,
                gameSystem.countNumbers,
                gameSystem.countCombinations,
                gameSystem.version,
                gameSystem.sizeCombination
            )
        }?.convertToModel()
    }

    override suspend fun getLastDateCombination(country: Country): Instant? {
        val lastDyCombination = if (country.isAllCountry()) {
            return dao.findLastDate()
        } else {
            dao.findLastDate(country.code)
        }
        return if (lastDyCombination != null) {
            DateTimeUtils.flushTime(lastDyCombination)
        } else {
            lastDyCombination
        }
    }

    override suspend fun getLastDateCombinationByLotteryId(lotteryID: Long): Instant? {
        return dao.findLastDateByLotteryId(lotteryID)
    }

    override suspend fun getCombinationsNumbers(combinationID: Long): CombinationNumbers? {
        return dao.findById(combinationID)?.convertToModel()
    }

    override suspend fun getCombinationsNumbersByLottery(
        lotteryID: Long,
        playedAt: Instant?
    ): List<CombinationNumbers> {
        return if (playedAt != null) {
            val dateTo = DateTimeUtils.nextDay(playedAt)
                .minusSeconds(1)
            dao.findAllByLotteryIDAndBetweenCreateAt(lotteryID, playedAt, dateTo)
                .convertToModel()
        } else {
            dao.findAllByLotteryID(lotteryID).convertToModel()
        }
    }

    override suspend fun save(model: CombinationNumbers) {
        val entity = model.convertToEntity()
        val combinationsNumbers = model.numbers.convertToEntity()
        dao.save(entity, combinationsNumbers)
    }
}