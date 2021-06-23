package com.lotteryadviser.di

import com.lotteryadviser.di.viewmodel.ViewModelModule
import com.lotteryadviser.presentation.ActivityComponent
import com.lotteryadviser.presentation.App
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        NavigationModule::class,
        ViewModelModule::class,
        DatabaseModule::class,
        RepositoryModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: App): AppComponent
    }

    fun plusActivityComponent(): ActivityComponent

    fun inject(app: App)

}