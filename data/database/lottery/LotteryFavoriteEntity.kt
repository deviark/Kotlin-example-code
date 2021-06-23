package com.lotteryadviser.data.database.lottery

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lotteryadviser.data.database.COLUMN_ID

@Entity(tableName = TABLE_NAME)
data class LotteryFavoriteEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Long,
    @ColumnInfo(name = COLUMN_COUNTRY_CODE)
    val countryCode: String,
    @ColumnInfo(name = COLUMN_IS_FAVORITE)
    var isFavorite: Boolean = false
)