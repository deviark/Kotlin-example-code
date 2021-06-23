package com.lotteryadviser.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lotteryadviser.data.database.combination.CombinationDao
import com.lotteryadviser.data.database.combination.CombinationEntity
import com.lotteryadviser.data.database.combination.numbers.CombinationNumbersEntity
import com.lotteryadviser.data.database.country.CountryDao
import com.lotteryadviser.data.database.country.CountryEntity
import com.lotteryadviser.data.database.lottery.LotteryDao
import com.lotteryadviser.data.database.lottery.LotteryFavoriteEntity
import com.lotteryadviser.data.database.recommendnumbers.RecommendNumbersDao
import com.lotteryadviser.data.database.recommendnumbers.RecommendNumbersEntity
import com.lotteryadviser.data.database.recommendnumbers.groupnumebers.GroupNumbersEntity

@Database(
    entities = [
        CountryEntity::class,
        LotteryFavoriteEntity::class,
        CombinationEntity::class,
        CombinationNumbersEntity::class,
        RecommendNumbersEntity::class,
        GroupNumbersEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    Converters::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getCountryDao(): CountryDao
    abstract fun getLotteryDao(): LotteryDao
    abstract fun getCombinationDao(): CombinationDao
    abstract fun getRecommendNumbers(): RecommendNumbersDao
}