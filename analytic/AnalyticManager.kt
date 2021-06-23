package com.lotteryadviser.analytic

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.lotteryadviser.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticManager @Inject constructor(
    context: Context,
) {

    companion object {
        const val EVENT_CLICK_OPEN_SUBSCRIPTION_SCREEN = "click_open_subscription_screen"
        const val PARAM_LOTTERY_NAME = "lottery_name"
        const val PARAM_LOTTERY_BRAND = "lottery_brand"
        const val PARAM_IS_FAVORITE = "is_favorite"
        private const val PARAM_FLAVOR = "flavor"
    }

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun logEvent(event: String, params: MutableMap<String, String> = mutableMapOf()) {
        if (isEnable()) {
            addFlavorParam(params)
            fireBaseLogEvent(event, params)
        }
    }

    fun logScreen(screenName: String, className: String, params: MutableMap<String, String>) {
        if (isEnable()) {
            if (screenName.isNotEmpty() && className.isNotEmpty()) {
                addFlavorParam(params)
                val bundle = mapToBundle(params)
                bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, className)
                firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
            }
        }
    }

    private fun addFlavorParam(params: MutableMap<String, String>): MutableMap<String, String> {
        params[PARAM_FLAVOR] = BuildConfig.FLAVOR
        return params
    }

    private fun fireBaseLogEvent(event: String, params: MutableMap<String, String>) {
        firebaseAnalytics.logEvent(event, mapToBundle(params))
    }

    private fun mapToBundle(params: MutableMap<String, String>): Bundle {
        val bundle = Bundle()
        for (entry in params.entries) {
            bundle.putString(entry.key, entry.value)
        }
        return bundle
    }

    private fun isEnable(): Boolean {
        return !BuildConfig.DEBUG
    }
}