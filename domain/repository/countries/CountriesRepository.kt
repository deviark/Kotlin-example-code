package com.lotteryadviser.domain.repository.countries

import com.lotteryadviser.domain.repository.countries.model.Country
import kotlinx.coroutines.flow.Flow

interface CountriesRepository {

    fun getCountries(): Flow<List<Country>>

    fun getCurrentCountry(): Flow<Country>

    suspend fun update()

    suspend fun setCurrentCountry(country: Country)
}