package com.lotteryadviser.domain.repository.lotteries

import com.lotteryadviser.domain.repository.countries.model.Country
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import kotlinx.coroutines.flow.Flow

interface LotteriesRepository {

    suspend fun getLotteriesByCountry(country: Country): List<Lottery>

    suspend fun getFavoriteLotteriesByCountry(country: Country): List<Lottery>

    suspend fun addFavorite(lottery: Lottery)

    suspend fun removeFavorite(lottery: Lottery)

    suspend fun removeFavorites(lotteries: List<Lottery>)

    fun isFavorite(lottery: Lottery): Flow<Boolean>

}