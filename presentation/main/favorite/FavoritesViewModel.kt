package com.lotteryadviser.presentation.main.favorite

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.usesase.FavoriteLotteriesUseCase
import com.lotteryadviser.navigation.Screens
import com.lotteryadviser.presentation.base.BaseViewModel
import com.lotteryadviser.presentation.main.favorite.model.WrapperLotteryItem
import com.lotteryadviser.presentation.main.favorite.model.convertToModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class FavoritesViewModel
@Inject constructor(
    private val router: Router,
    private val useCase: FavoriteLotteriesUseCase
) : BaseViewModel() {

    private val _uiState: MutableStateFlow<Result<FavoriteLotteriesUseCase.State>?> =
        MutableStateFlow(null)
    val uiState: StateFlow<Result<FavoriteLotteriesUseCase.State>?> = _uiState

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            useCase.getFavoriteLotteries().collect { state ->
                _uiState.value = state
            }
        }
    }

    fun changeCountry() {
        router.navigateTo(Screens.Countries())
    }

    fun removeFromFavorites(items: List<WrapperLotteryItem>) {
        viewModelScope.launch {
            useCase.removeFavorites(items.convertToModel())
        }
    }

    fun forwardLottery(item: WrapperLotteryItem) {
        router.newChain(Screens.Lottery(item.lottery))
    }

    override fun getScreenName(): String {
        return "Favorites"
    }

    override fun getClassName(): String {
        return "FavoritesFragment"
    }
}