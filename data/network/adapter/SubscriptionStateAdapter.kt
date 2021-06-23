package com.lotteryadviser.data.network.adapter

import com.lotteryadviser.domain.model.SubscriptionState
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class SubscriptionStateAdapter {

    @FromJson
    fun fromJson(nameState: String?): SubscriptionState? {
        return if (nameState == null) {
            null
        } else {
            try {
                SubscriptionState.valueOf(nameState)
            } catch (e: Exception) {
                SubscriptionState.UNKNOWN
            }

        }
    }

    @ToJson
    fun toJson(state: SubscriptionState?): String {
        return state?.name ?: SubscriptionState.UNKNOWN.name
    }
}