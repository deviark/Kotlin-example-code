package com.lotteryadviser.data.database.country

import com.lotteryadviser.data.network.model.CountryJson
import com.lotteryadviser.domain.repository.countries.model.Country

internal const val TABLE_NAME = "country"

internal const val COLUMN_COUNTRY_CODE = "code"
internal const val COLUMN_SELECTED = "selected"

fun CountryJson.convertToEntity(): CountryEntity =
    CountryEntity(
        this.id,
        this.code
    )

fun List<CountryJson>.convertToEntity(): List<CountryEntity> =
    map { it.convertToEntity() }


fun CountryEntity.convertToModel(): Country =
    Country(
        this.id,
        this.code,
        selected
    )

fun List<CountryEntity>.convertToModel(): List<Country> =
    map { it.convertToModel() }

fun Country.convertToEntity() = CountryEntity(
    this.id,
    this.code,
    this.selected
)

fun Country.isAllCountry(): Boolean = id == CountryEntity.ID_ALL_COUNTRY
