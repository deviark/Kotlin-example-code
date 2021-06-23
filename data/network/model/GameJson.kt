package com.lotteryadviser.data.network.model

import com.lotteryadviser.domain.repository.gamesresults.model.GameResults
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.Instant

@JsonClass(generateAdapter = true)
data class GameJson(
    @Json(name = "lottery_id")
    val lotteryID: Long,
    @Json(name = "name_lottery")
    val nameLottery: String,
    @Json(name = "game_number")
    val gameNumber: Long?,
    @Json(name = "played_at")
    val playedAt: Instant,
    @Json(name = "jackpot")
    val jackpot: Long?,
    @Json(name = "currency_code")
    val currencyCode: String,
    @Json(name = "result")
    val results: List<GameResultJson>
)

fun GameJson.convertToModel(): GameResults =
    GameResults(
        this.lotteryID,
        this.nameLottery,
        this.gameNumber,
        this.playedAt,
        this.jackpot,
        this.currencyCode,
        this.results.convertToModel().sortedBy { it.extra }
    )

fun List<GameJson>.convertToModel(): List<GameResults> =
    map { it.convertToModel() }