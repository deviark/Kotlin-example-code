package com.lotteryadviser.domain.usesase

import com.lotteryadviser.domain.repository.countries.CountriesRepository
import com.lotteryadviser.domain.repository.countries.model.Country
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CountriesUseCase @Inject constructor(
    private val repository: CountriesRepository
) {

    fun getCountries(): Flow<List<Country>> {
        return repository.getCountries()
    }

    suspend fun setCurrentCountry(country: Country) {
        repository.setCurrentCountry(country)
    }
}