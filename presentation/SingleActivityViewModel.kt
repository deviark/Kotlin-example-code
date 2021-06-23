package com.lotteryadviser.presentation

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import com.lotteryadviser.di.Injector
import com.lotteryadviser.domain.model.Result
import com.lotteryadviser.domain.usesase.LaunchCase
import com.lotteryadviser.navigation.Screens
import com.lotteryadviser.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SingleActivityViewModel @Inject constructor(
    private val router: Router,
    private val launchCase: LaunchCase
) : BaseViewModel() {

    private val _hasErrorUpdate: MutableSharedFlow<Boolean?> = MutableSharedFlow()
    val hasErrorUpdate: SharedFlow<Boolean?> = _hasErrorUpdate

    private val _hasSubscription: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val hasSubscription = _hasSubscription.asStateFlow()

    init {
        launchCase.connectBillingManager()
        updateData()

        viewModelScope.launch {
            launchCase.hasSubscription().collect {
                _hasSubscription.value = it
            }
        }
    }

    fun updateData() {
        viewModelScope.launch {
            launchCase.updateData()
                .collect { result ->
                    val hasErrorUpdate = if (result is Result.Success) {
                        if (result.data) {
                            router.newRootScreen(Screens.Countries(true))
                        } else {
                            router.newRootScreen(Screens.Main())
                        }
                        false
                    } else {
                        true
                    }
                    _hasErrorUpdate.emit(hasErrorUpdate)
                }
        }
    }

    override fun onCleared() {
        launchCase.endConnectionBillingManger()
        Injector.clearActivityComponent()
        super.onCleared()
    }

}