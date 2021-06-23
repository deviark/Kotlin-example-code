package com.lotteryadviser.data.repositories.countries

import com.lotteryadviser.data.database.country.CountryDao
import com.lotteryadviser.data.database.country.CountryEntity
import com.lotteryadviser.data.database.country.convertToEntity
import com.lotteryadviser.data.database.country.convertToModel
import com.lotteryadviser.data.network.RestApi
import com.lotteryadviser.domain.repository.countries.CountriesRepository
import com.lotteryadviser.domain.repository.countries.model.Country
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class CountriesRepositoryImp @Inject constructor(
    private val restApi: RestApi,
    private val countriesDao: CountryDao,
) : CountriesRepository {

    override fun getCountries(): Flow<List<Country>> {
        return countriesDao.findAll().map { it.convertToModel() }
    }

    override fun getCurrentCountry(): Flow<Country> {
        return countriesDao.findSelectedCountryUntilChange().transform { entity ->
            val country = entity?.convertToModel() ?: CountryEntity.instanceAllCountries(true)
                .convertToModel()
            emit(country)
        }
    }

    override suspend fun update() {
        val remoteCountriesData = restApi.getCountries().data
            .convertToEntity().toMutableList()
        val allCountries = CountryEntity.instanceAllCountries(false)
        remoteCountriesData.add(0, allCountries)
        val selectedCountry = countriesDao.findSelectedCountry().first()
        val selectedCountryInRemote = remoteCountriesData.find { it.id == selectedCountry?.id }
        if (selectedCountryInRemote == null) {
            allCountries.selected = true
        } else {
            selectedCountryInRemote.selected = true
        }
        countriesDao.update(remoteCountriesData)
    }


    override suspend fun setCurrentCountry(country: Country) {
        countriesDao.updateSelected(country.convertToEntity())
    }
}