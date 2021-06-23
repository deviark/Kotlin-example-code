package com.lotteryadviser.domain.usesase

import com.lotteryadviser.data.language.LanguageManager
import com.lotteryadviser.data.language.Languages
import com.lotteryadviser.domain.model.Error
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.repository.countries.CountriesRepository
import com.lotteryadviser.domain.repository.countries.model.Country
import com.lotteryadviser.domain.repository.lotteries.LotteriesRepository
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LotteriesUseCase @Inject constructor(
    languageManager: LanguageManager,
    private val countriesRepository: CountriesRepository,
    private val lotteriesRepository: LotteriesRepository,
) {

    private val currentLanguage = languageManager.getCurrentLanguage()

    fun getLotteries(): Flow<Result<State>> {
        return flow {
            countriesRepository.getCurrentCountry().collect { country ->
                emit(Result.Loading(State(country, currentLanguage)))
                try {
                    val items = lotteriesRepository.getLotteriesByCountry(country)
                    emit(Result.Success(State(country, currentLanguage, items)))
                } catch (e: Exception) {
                    emit(Result.Failure(State(country, currentLanguage, emptyList()), Error.NetworkError))
                }
            }
        }
    }

    data class State(
        val country: Country,
        val language: Languages,
        val lotteries: List<Lottery> = emptyList()
    )
}