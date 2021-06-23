package com.lotteryadviser.data.network.adapter

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.Instant

class InstantAdapter {

    @FromJson
    fun fromJson(timestamp: String?): Instant? {
        return if (timestamp == null) {
            null
        } else {
            Instant.parse(timestamp)
        }
    }

    @ToJson
    fun toJson(date: Instant?): String? {
        return date?.toString()
    }
}