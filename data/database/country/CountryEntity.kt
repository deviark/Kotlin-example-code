package com.lotteryadviser.data.database.country

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lotteryadviser.data.database.COLUMN_ID

@Entity(tableName = TABLE_NAME)
open class CountryEntity(
    @PrimaryKey
    @ColumnInfo(name = COLUMN_ID)
    val id: Long,
    @ColumnInfo(name = COLUMN_COUNTRY_CODE)
    val code: String,
    @ColumnInfo(name = COLUMN_SELECTED)
    var selected: Boolean = false
) {
    companion object {
        const val ID_ALL_COUNTRY = -1L
        fun instanceAllCountries(selected: Boolean = false) = CountryEntity(ID_ALL_COUNTRY, "", selected)
    }
}