package com.drmontoya.openweather.data.local.entity

import androidx.room.TypeConverter

class IntListConverter {

    @TypeConverter
    fun fromString(string: String): List<Int> {
        return string.split(",").map { it.toInt() }
    }

    @TypeConverter
    fun toString(list: List<Int>): String {
        return list.joinToString(",")
    }
}

class StringListConverter {

    @TypeConverter
    fun fromString(string: String): List<String> {
        return string.split(",")
    }

    @TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString(",")
    }
}

class DoubleListConverter {

    @TypeConverter
    fun fromString(string: String): List<Double> {
        return string.split(",").map { it.toDouble() }
    }

    @TypeConverter
    fun toString(list: List<Double>): String {
        return list.joinToString(",")
    }
}
