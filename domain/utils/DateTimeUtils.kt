package com.lotteryadviser.domain.utils

import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateTimeUtils {


    fun getStartOfCurrentDay(): Instant {
        return LocalDate
            .now()
            .atStartOfDay()
            .toInstant(getZoneOffset())
    }

    fun getCurrentDateTime(): Instant {
        return LocalDateTime
            .now()
            .toInstant(getZoneOffset())
    }

    private fun getZoneOffset(localeDateTime: LocalDateTime = LocalDateTime.now()): ZoneOffset {
        val zoneID = ZoneOffset.systemDefault()
        return zoneID.rules.getOffset(localeDateTime)
    }

    fun flushTime(date: Instant): Instant {
        return LocalDateTime
            .ofInstant(date, getDefaultZoneID())
            .toLocalDate()
            .atStartOfDay()
            .toInstant(getZoneOffset())
    }

    fun covertTimestampToString(date: Instant, hasTime: Boolean = false): String {
        val localDateTime = LocalDateTime.ofInstant(date, getDefaultZoneID())
        return if (hasTime) {
            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(localDateTime)
        } else {
            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).format(localDateTime)
        }
    }

    fun createDate(year: Int, month: Int, dayOfMonth: Int): Instant {
        val localeDateTime = LocalDate.of(year, month, dayOfMonth)
            .atStartOfDay()
        return localeDateTime
            .toInstant(getZoneOffset(localeDateTime))
    }

    fun nextDay(date: Instant): Instant {
        val localDateTime = LocalDateTime.ofInstant(date, getDefaultZoneID())
        return localDateTime.plusDays(1).toInstant(getZoneOffset(localDateTime))
    }

    private fun getDefaultZoneID(): ZoneId {
        return ZoneId.systemDefault()
    }

}