package com.lotteryadviser.data.repositories.lotteries

import com.lotteryadviser.data.database.country.isAllCountry
import com.lotteryadviser.data.database.lottery.LotteryDao
import com.lotteryadviser.data.database.lottery.convertToFavoriteEntity
import com.lotteryadviser.data.network.RestApi
import com.lotteryadviser.data.network.model.convertToModel
import com.lotteryadviser.data.network.request.LotteryIdsRequest
import com.lotteryadviser.domain.repository.countries.model.Country
import com.lotteryadviser.domain.repository.lotteries.LotteriesRepository
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LotteriesRepositoryImp @Inject constructor(
    private val restApi: RestApi,
    private val lotteriesDao: LotteryDao
) : LotteriesRepository {


    override suspend fun getLotteriesByCountry(country: Country): List<Lottery> {
        return restApi.getLotteries(country.code).data.convertToModel()
            .sortedBy { it.playedAt }
    }

    override suspend fun getFavoriteLotteriesByCountry(country: Country): List<Lottery> {
        val favoriteLotteryIds = if (country.isAllCountry()) {
            lotteriesDao.findFavoritesAll()
        } else {
            lotteriesDao.findFavoritesByCountry(country.code)
        }.map { it.id }

        return if (favoriteLotteryIds.isEmpty()) {
            emptyList()
        } else {
            restApi.getFavoriteLotteries(
                LotteryIdsRequest(
                    favoriteLotteryIds
                )
            ).data.convertToModel()
                .sortedBy { it.playedAt }
        }
    }

    override suspend fun addFavorite(lottery: Lottery) {
        lottery.isFavorite = true
        lotteriesDao.updateFavorite(lottery.convertToFavoriteEntity())
    }

    override suspend fun removeFavorite(lottery: Lottery) {
        lottery.isFavorite = false
        lotteriesDao.updateFavorite(lottery.convertToFavoriteEntity())
    }

    override suspend fun removeFavorites(lotteries: List<Lottery>) {
        lotteries.forEach { it.isFavorite = false }
        lotteriesDao.updateFavorite(lotteries.convertToFavoriteEntity())
    }

    override fun isFavorite(lottery: Lottery): Flow<Boolean> {
        return flow {
            lotteriesDao.findFavoriteUntilChange(lottery.id).collect { isFavorite ->
                emit(isFavorite?.isFavorite ?: false)
            }
        }
    }

}