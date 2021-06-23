package com.lotteryadviser.presentation.language.model

import com.lotteryadviser.data.language.Languages

data class WrapperLanguage(
    val language: Languages,
    var selected: Boolean
)