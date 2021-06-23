package com.lotteryadviser.data.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LotteryIdsRequest(
    @Json(name = "ids")
    val lotteryIds: List<Long>
)