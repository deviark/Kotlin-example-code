package com.lotteryadviser.presentation.main.favorite.model

import com.lotteryadviser.domain.repository.lotteries.model.Lottery


data class WrapperLotteryItem(
    val lottery: Lottery,
    var selected: Boolean = false
)

fun WrapperLotteryItem.convertToModel(): Lottery = this.lottery

fun List<WrapperLotteryItem>.convertToModel(): List<Lottery> = map { it.convertToModel() }

fun Lottery.convertToUiModel(): WrapperLotteryItem =
    WrapperLotteryItem(this)

fun List<Lottery>.convertToUiModel(): List<WrapperLotteryItem> = map { it.convertToUiModel() }
