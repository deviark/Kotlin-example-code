package com.lotteryadviser.data.database.lottery

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lotteryadviser.data.database.COLUMN_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface LotteryDao {

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_IS_FAVORITE = 1")
    suspend fun findFavoritesAll(): List<LotteryFavoriteEntity>

    @Query("SELECT * FROM  $TABLE_NAME WHERE $COLUMN_COUNTRY_CODE = :countryCode AND $COLUMN_IS_FAVORITE = 1")
    suspend fun findFavoritesByCountry(countryCode: String): List<LotteryFavoriteEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavorite(lottery: LotteryFavoriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavorite(lotteries: List<LotteryFavoriteEntity>)

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :lotteryId ")
    fun findFavorite(lotteryId: Long): Flow<LotteryFavoriteEntity?>

    fun findFavoriteUntilChange(lotteryId: Long) = findFavorite(lotteryId).distinctUntilChanged()


}