package com.lotteryadviser.data.database.lottery

import com.lotteryadviser.domain.repository.lotteries.model.Lottery

internal const val TABLE_NAME = "lottery"

internal const val COLUMN_COUNTRY_CODE = "country_code"
internal const val COLUMN_IS_FAVORITE = "is_favorite"


fun Lottery.convertToFavoriteEntity(): LotteryFavoriteEntity =
    LotteryFavoriteEntity(
        this.id,
        this.countryCode,
        this.isFavorite,
    )

fun List<Lottery>.convertToFavoriteEntity(): List<LotteryFavoriteEntity> =
    map { it.convertToFavoriteEntity() }