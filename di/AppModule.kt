package com.lotteryadviser.di

import android.content.Context
import com.lotteryadviser.presentation.App
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(application: App): Context = application.applicationContext!!

    @Provides
    @Singleton
    fun provideGlobalScope(): CoroutineScope = GlobalScope
}