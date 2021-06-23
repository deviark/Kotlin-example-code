package com.lotteryadviser.domain.repository.countries.model

data class Country(
    val id: Long,
    val code: String,
    var selected: Boolean
)