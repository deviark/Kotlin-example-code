package com.lotteryadviser.presentation.base

import androidx.lifecycle.ViewModel
import com.lotteryadviser.analytic.AnalyticManager
import javax.inject.Inject

open class BaseViewModel: ViewModel() {

    @Inject
    lateinit var analyticManager: AnalyticManager

    fun onResume() {
        analyticManager.logScreen(getScreenName(), getClassName(), getParams())
    }

    protected open fun getScreenName(): String {
        return ""
    }

    protected open fun getClassName(): String {
        return ""
    }

    protected open fun getParams(): MutableMap<String, String> {
        return mutableMapOf()
    }
}