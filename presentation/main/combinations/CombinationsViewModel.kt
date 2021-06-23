package com.lotteryadviser.presentation.main.combinations

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.lotteryadviser.domain.model.Filter
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.repository.combinations.model.CombinationNumbers
import com.lotteryadviser.domain.usesase.CombinationsUseCase
import com.lotteryadviser.navigation.Screens
import com.lotteryadviser.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject


class CombinationsViewModel @Inject constructor(
    private val router: Router,
    private val useCase: CombinationsUseCase,
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<Result<CombinationsUseCase.State>?> =
        MutableStateFlow(null)
    val uiState: StateFlow<Result<CombinationsUseCase.State>?> = _uiState

    private lateinit var filter: Filter
    private lateinit var maxDate: Instant

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            useCase.getCombinations().collect { result ->
                filter = result.data.filter
                maxDate = result.data.maxDate
                _uiState.value = result
            }
        }
    }

    fun changeDate(date: Instant) {
        useCase.changeDate(date)
    }

    fun selectedDate(): Instant {
        return filter.date
    }

    fun maxDate(): Instant {
        return maxDate
    }

    fun changeCountry() {
        router.navigateTo(Screens.Countries())
    }

    fun forwardToHistoryItem(item: CombinationNumbers) {
        router.navigateTo(Screens.CombinationNumbersInfo(item.id))
    }

    override fun getScreenName(): String {
        return "Combinations"
    }

    override fun getClassName(): String {
        return "CombinationsFragment"
    }

}