package com.google.youtube.utils

import kotlin.js.Date

fun getCurrentDateFormatted(): String {
    val currentDate = Date()
    val day = currentDate.getDate()
    val monthIndex = currentDate.getMonth() // Month is 0-indexed
    val year = currentDate.getFullYear()

    val dayString = if (day < 10) "0${day}" else day.toString()

    val monthNames = arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val monthName = monthNames[monthIndex]

    val yearShort = year % 100
    val yearString = if (yearShort < 10) "0${yearShort}" else yearShort.toString()

    return "$dayString $monthName $yearString"
}
