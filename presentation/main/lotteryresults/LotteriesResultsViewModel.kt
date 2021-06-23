package com.lotteryadviser.presentation.main.lotteryresults

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import com.lotteryadviser.domain.usesase.LotteriesUseCase
import com.lotteryadviser.navigation.Screens
import com.lotteryadviser.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class LotteriesResultsViewModel @Inject constructor(
    private val router: Router,
    private val useCase: LotteriesUseCase,
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<Result<LotteriesUseCase.State>?> =
        MutableStateFlow(null)
    val uiState = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            useCase.getLotteries().collect { state ->
                _uiState.value = state
            }
        }
    }


    fun changeCountry() {
        router.navigateTo(Screens.Countries())
    }

    fun forwardGameResults(lottery: Lottery) {
        router.navigateTo(Screens.GameResults(lottery))
    }

    override fun getScreenName(): String {
        return "LotteriesResults"
    }

    override fun getClassName(): String {
        return "LotteriesResultsFragment"
    }
}