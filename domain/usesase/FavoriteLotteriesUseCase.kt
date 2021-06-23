package com.lotteryadviser.domain.usesase

import com.lotteryadviser.domain.model.Error
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.repository.countries.CountriesRepository
import com.lotteryadviser.domain.repository.countries.model.Country
import com.lotteryadviser.domain.repository.lotteries.LotteriesRepository
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoriteLotteriesUseCase @Inject constructor(
    private val coroutineScope: CoroutineScope,
    private val countriesRepository: CountriesRepository,
    private val lotteriesRepository: LotteriesRepository
) {

    private val stateFlow: MutableSharedFlow<Result<State>> = MutableSharedFlow()

    private var stateSuccess: Result<State>? = null

    init {
        updateData()
    }

    private fun updateData() {
        coroutineScope.launch {
            countriesRepository.getCurrentCountry().collect { country ->
                stateFlow.emit(Result.Loading(State(country)))
                try {
                    val items = lotteriesRepository.getFavoriteLotteriesByCountry(country)
                    val localStateSuccess = Result.Success(State(country, items))
                    stateSuccess = localStateSuccess
                    stateFlow.emit(localStateSuccess)
                } catch (e: Exception) {
                    stateFlow.emit(Result.Failure(State(country), Error.NetworkError))
                }
            }
        }
    }

    suspend fun removeFavorites(lotteries: List<Lottery>) {
        lotteriesRepository.removeFavorites(lotteries)
        val localStateSuccess = stateSuccess
        if (localStateSuccess != null) {
            val newFavoriteLotteries = localStateSuccess.data.lotteries.toMutableList()
            newFavoriteLotteries.removeAll(lotteries)
            stateFlow.emit(
                Result.Success(
                    State(
                        localStateSuccess.data.country,
                        newFavoriteLotteries
                    )
                )
            )
        }
    }

    fun getFavoriteLotteries(): Flow<Result<State>> {
        updateData()
        return stateFlow
    }

    data class State(
        val country: Country,
        val lotteries: List<Lottery> = emptyList()
    )
}