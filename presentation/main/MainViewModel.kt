package com.lotteryadviser.presentation.main

import androidx.lifecycle.viewModelScope
import com.lotteryadviser.billing.BillingManager
import com.lotteryadviser.di.Injector
import com.lotteryadviser.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val billingManager: BillingManager
) : BaseViewModel() {

    private val _stateSubscription: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val stateSubscription = _stateSubscription.asStateFlow()


    init {
        viewModelScope.launch {
            billingManager.getState().collect { state ->
                if (state.status == BillingManager.Status.READY) {
                    _stateSubscription.emit(state.hasSubscription)
                }
            }
        }

    }

    override fun onCleared() {
        Injector.clearMainComponent()
    }
}