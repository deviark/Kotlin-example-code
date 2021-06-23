package com.lotteryadviser.domain.model

import com.lotteryadviser.domain.repository.countries.model.Country
import java.time.Instant

data class Filter(
    val date: Instant,
    val country: Country,
    val lotteryID: Long = - 1
)