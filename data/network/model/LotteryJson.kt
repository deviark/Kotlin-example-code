package com.lotteryadviser.data.network.model

import com.lotteryadviser.BuildConfig
import com.lotteryadviser.domain.repository.lotteries.model.Lottery
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class LotteryJson(
    @Json(name = "id")
    val id: Long,
    @Json(name = "name")
    val name: String,
    @Json(name = "brand")
    val brand: String,
    @Json(name = "country_code")
    val countryCode: String,
    @Json(name = "jack_pot")
    val jackPot: Long?,
    @Json(name = "currency_code")
    val currencyCode: String,
    @Json(name = "played_at")
    val playedAt: Instant,
    @Json(name = "game_number")
    val gameNumber: Long?,
    @Json(name = "image_url")
    val imageUrl: String?,
    @Json(name = "main_count_numbers")
    var mainCountNumbers: Int,
    @Json(name = "main_count_numbers_combination")
    var mainCountNumbersCombination: Int,
    @Json(name = "bonus_count_numbers")
    var bonusCountNumbers: Int?,
    @Json(name = "bonus_count_numbers_combination")
    var bonusCountNumbersCombination: Int?,
    @Json(name = "bonus_zero_status")
    var bonusZeroStatus: Boolean?,
    @Json(name = "min_count_number_combination")
    var minCountNumberCombination: Int?,
    @Json(name = "zero_status")
    var zeroStatus: Boolean,
)

fun LotteryJson.convertToModel(): Lottery =
    Lottery(
        id,
        name,
        brand,
        imageUrl,
        jackPot,
        currencyCode,
        playedAt,
        gameNumber,
        countryCode,
        mainCountNumbers,
        mainCountNumbersCombination,
        bonusCountNumbers,
        bonusCountNumbersCombination,
        bonusZeroStatus,
        minCountNumberCombination,
        this.zeroStatus,
        isFavorite = false,
    ).apply {
        if (BuildConfig.FLAVOR == "local") {
            imageUrl = imageUrl?.replace("http://localhost", "http://192.168.1.40")
        }
    }

fun List<LotteryJson>.convertToModel(): List<Lottery> =
    map { it.convertToModel() }