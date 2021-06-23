package com.lotteryadviser.presentation.dialog.invite.di

import androidx.lifecycle.ViewModel
import com.lotteryadviser.di.viewmodel.ViewModelKey
import com.lotteryadviser.presentation.dialog.invite.InviteSubscriptionViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class InviteSubscriptionModule {

    @Binds
    @IntoMap
    @ViewModelKey(InviteSubscriptionViewModel::class)
    internal abstract fun bindInviteSubscriptionViewModelViewModel(viewModel: InviteSubscriptionViewModel): ViewModel
}