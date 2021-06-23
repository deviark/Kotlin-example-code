package com.lotteryadviser.presentation.country

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.lotteryadviser.di.Injector
import com.lotteryadviser.domain.repository.countries.model.Country
import com.lotteryadviser.domain.usesase.CountriesUseCase
import com.lotteryadviser.navigation.Screens
import com.lotteryadviser.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


class CountriesViewModel @Inject constructor(
    private val router: Router,
    private val countriesUseCase: CountriesUseCase,
) : BaseViewModel() {

    private var firstLaunch: Boolean = false

    private val _countries: MutableStateFlow<List<Country>> = MutableStateFlow(emptyList())

    val countries: StateFlow<List<Country>> = _countries

    init {
        viewModelScope.launch {
            countriesUseCase.getCountries().collect { items ->
                _countries.value = items
            }
        }
    }

    fun init(firstLaunch: Boolean) {
        this.firstLaunch = firstLaunch
    }

    fun changeCountry(country: Country) {
        viewModelScope.launch {
            countriesUseCase.setCurrentCountry(country)
            if (firstLaunch) {
                router.newRootScreen(Screens.Main())
            } else {
                router.backTo(Screens.Main())
            }
        }
    }

    override fun onCleared() {
        Injector.clearCountriesComponent()
    }

    override fun getScreenName(): String {
        return "Countries"
    }

    override fun getClassName(): String {
        return "CountriesFragment"
    }
}