package com.lotteryadviser.data.database.country

import androidx.room.*
import com.lotteryadviser.data.database.COLUMN_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged

@Dao
interface CountryDao {

    @Query("SELECT * FROM $TABLE_NAME")
    fun findAll(): Flow<List<CountryEntity>>

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :id LIMIT 1")
    suspend fun findByID(id: Long): CountryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(countries: List<CountryEntity>)

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_SELECTED = 1")
    fun findSelectedCountry(): Flow<CountryEntity?>

    fun findSelectedCountryUntilChange() = findSelectedCountry().distinctUntilChanged()

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Transaction
    suspend fun update(entities: List<CountryEntity>) {
        deleteAll()
        insertAll(entities)
    }

    @Update
    fun update(entity: CountryEntity): Int

    @Query("UPDATE $TABLE_NAME SET $COLUMN_SELECTED = 0")
    fun unselectedAll()


    @Transaction
    suspend fun updateSelected(entity: CountryEntity) {
        unselectedAll()
        update(entity)
    }
}