package com.lotteryadviser.presentation.main.lotteries

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.lotteryadviser.data.Preferences
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import com.lotteryadviser.domain.usesase.LotteriesUseCase
import com.lotteryadviser.navigation.Screens
import com.lotteryadviser.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class LotteriesViewModel @Inject constructor(
    private val router: Router,
    private val preferences: Preferences,
    private val userCase: LotteriesUseCase
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<Result<LotteriesUseCase.State>?> = MutableStateFlow(null)
    val usState: StateFlow<Result<LotteriesUseCase.State>?> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            userCase.getLotteries().collect { state ->
                _uiState.value = state
            }
        }
    }

    fun toggleThemeLightDark() {
        val nightMode = when (AppCompatDelegate.getDefaultNightMode()) {
            AppCompatDelegate.MODE_NIGHT_NO -> AppCompatDelegate.MODE_NIGHT_YES
            AppCompatDelegate.MODE_NIGHT_YES -> AppCompatDelegate.MODE_NIGHT_NO
            else -> AppCompatDelegate.MODE_NIGHT_YES
        }
        preferences.themeMode = nightMode
        AppCompatDelegate.setDefaultNightMode(nightMode)
    }

    fun changeLanguage() {
        router.navigateTo(Screens.Langugages())
    }

    fun forwardToLottery(lottery: Lottery) {
        router.navigateTo(Screens.Lottery(lottery))
    }

    fun changeCountry() {
        router.navigateTo(Screens.Countries())
    }

    override fun getScreenName(): String {
        return "Lotteries"
    }

    override fun getClassName(): String {
        return "LotteriesFragment"
    }
}