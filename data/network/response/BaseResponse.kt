package com.lotteryadviser.data.network.response

import com.squareup.moshi.Json


abstract class BaseResponse<T>(
    @Json(name = "data")
    val data: T
)