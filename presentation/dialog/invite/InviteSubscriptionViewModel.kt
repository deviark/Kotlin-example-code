package com.lotteryadviser.presentation.dialog.invite

import com.github.terrakok.cicerone.Router
import com.lotteryadviser.navigation.Screens
import com.lotteryadviser.presentation.base.BaseViewModel
import javax.inject.Inject

class InviteSubscriptionViewModel @Inject constructor(
    private val router: Router
): BaseViewModel() {

    fun forwardSubscription() {
        router.navigateTo(Screens.Subscriptions())
    }
}