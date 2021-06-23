package com.lotteryadviser.presentation.dialog.di

import com.lotteryadviser.presentation.dialog.CombinationOptionDialog
import com.lotteryadviser.presentation.dialog.DatePickerDialog
import com.lotteryadviser.presentation.dialog.invite.InviteSubscriptionDialog
import com.lotteryadviser.presentation.dialog.invite.di.InviteSubscriptionModule
import dagger.Subcomponent

@Subcomponent(
    modules = [
        InviteSubscriptionModule::class,
    ]
)
interface DialogComponent {

    fun inject(dialog: DatePickerDialog)

    fun inject(dialog: CombinationOptionDialog)

    fun inject(dialog: InviteSubscriptionDialog)
}