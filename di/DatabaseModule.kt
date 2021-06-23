package com.lotteryadviser.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lotteryadviser.data.database.AppDatabase
import com.lotteryadviser.data.database.combination.CombinationDao
import com.lotteryadviser.data.database.country.CountryDao
import com.lotteryadviser.data.database.lottery.LotteryDao
import com.lotteryadviser.data.database.recommendnumbers.RecommendNumbersDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDataBase(context: Context, roomCallback: RoomDatabase.Callback): AppDatabase {
        val dataBaseName = "database.sqlite"
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            dataBaseName
        ).addCallback(roomCallback)
            .build()
    }

    @Provides
    @Singleton
    fun provideRoomDatabaseCallback(): RoomDatabase.Callback = object : RoomDatabase.Callback() {}

    @Provides
    @Singleton
    fun provideCountriesDao(db: AppDatabase): CountryDao = db.getCountryDao()

    @Provides
    @Singleton
    fun provideLotteriesDao(db: AppDatabase): LotteryDao = db.getLotteryDao()

    @Provides
    @Singleton
    fun provideCombinationsDao(db: AppDatabase): CombinationDao = db.getCombinationDao()

    @Provides
    @Singleton
    fun provideRecommendNumbersDao(db: AppDatabase): RecommendNumbersDao = db.getRecommendNumbers()

}