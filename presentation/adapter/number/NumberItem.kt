package com.lotteryadviser.presentation.adapter.number

import android.os.Parcelable
import com.lotteryadviser.R
import com.lotteryadviser.domain.repository.gamesresults.model.NumberResult
import kotlinx.parcelize.Parcelize

@Parcelize
data class NumberItem(
    val value: Int,
    val extra: Boolean = false
) : Parcelable

fun NumberResult.getColorRes(): Int {
    return if (extra) {
        R.color.number_match_color
    } else {
        R.color.number_color
    }
}

fun NumberResult.getBackgroundRes(): Int {
    return if (extra) {
        if (value < 10) {
            R.drawable.bg_one_digit_match
        } else {
            R.drawable.bg_two_digit_match
        }
    } else {
        if (value < 10) {
            R.drawable.bg_one_digit
        } else {
            R.drawable.bg_two_digit
        }
    }
}

fun NumberResult.convertToParcelable(): NumberItem =
    NumberItem(
        this.value,
        this.extra
    )

fun List<NumberResult>.convertToParcelable(): List<NumberItem> =
    map { it.convertToParcelable() }

fun NumberItem.convertToModel(): NumberResult =
    NumberResult(value, extra)

fun List<NumberItem>.convertToModel(): List<NumberResult> =
    map { it.convertToModel() }