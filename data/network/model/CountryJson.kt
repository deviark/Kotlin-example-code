package com.lotteryadviser.data.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CountryJson(
    @Json(name = "id")
    val id: Long,
    @Json(name = "code")
    val code: String
)