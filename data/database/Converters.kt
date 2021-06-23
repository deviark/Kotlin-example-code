package com.lotteryadviser.data.database

import androidx.room.TypeConverter
import com.lotteryadviser.data.database.combination.numbers.NumberValue
import java.time.Instant

class Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let { Instant.ofEpochMilli(value) }
    }

    @TypeConverter
    fun instantToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilli()
    }

    @TypeConverter
    fun numberValueToString(list: List<NumberValue>): String {
        val txtList = StringBuilder()
        val lastIndex = list.lastIndex
        list.forEachIndexed { index, item ->
            txtList.append(item.value)
                .append(",")
                .append(item.extra)
            if (index < lastIndex) {
                txtList.append(";")
            }
        }
        return txtList.toString()
    }

    @TypeConverter
    fun stringToNumberValueList(textIntList: String): List<NumberValue> {
        val intList: MutableList<NumberValue> = mutableListOf()
        if (textIntList.isNotBlank()) {
            val textValues = textIntList.split(";")
            for (txtValue in textValues) {
                val item = txtValue.split(",")
                val value = item[0].toInt()
                val extra = item[1].toBoolean()
                intList.add(NumberValue(
                    value,
                    extra
                ))
            }
        }
        return intList
    }

    @TypeConverter
    fun intListToString(list: List<Int>): String {
        val txtList = StringBuilder()
        val lastIndex = list.lastIndex
        list.forEachIndexed { index, value ->
            txtList.append(value)
            if (index < lastIndex) {
                txtList.append(",")
            }
        }
        return txtList.toString()
    }

    @TypeConverter
    fun stringToIntList(textIntList: String): List<Int> {
        val intList: MutableList<Int> = mutableListOf()
        if (textIntList.isNotBlank()) {
            val textValues = textIntList.split(",")
            for (txtValue in textValues) {
                val value = Integer.parseInt(txtValue)
                intList.add(value)
            }
        }
        return intList
    }


}