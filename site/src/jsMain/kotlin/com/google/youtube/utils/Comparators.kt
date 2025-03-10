package com.google.youtube.utils

object StringDurationComparator : Comparator<String> {
    private const val DAYS_IN_YEAR = 365 // Approximate, for simplicity
    private const val DAYS_IN_MONTH = 30  // Approximate, for simplicity
    private const val DAYS_IN_WEEK = 7
    private const val HOURS_IN_DAY = 24

    override fun compare(a: String, b: String): Int {
        val duration1Days = getDurationInDays(a)
        val duration2Days = getDurationInDays(b)
        return duration1Days.compareTo(duration2Days)
    }

    private fun getDurationInDays(durationStr: String): Double { // Return type changed to Double to handle fractional days from hours
        val parts = durationStr.split("\\s+".toRegex())
        if (parts.size != 2) throw IllegalArgumentException("Invalid duration format: $durationStr")

        val value = parts[0].toInt()

        // Case-insensitive unit matching
        return when (val unit = parts[1].lowercase()) {
            "year", "years" -> value * DAYS_IN_YEAR.toDouble() // Convert to Double for calculation
            "month", "months" -> value * DAYS_IN_MONTH.toDouble() // Convert to Double for calculation
            "week", "weeks" -> value * DAYS_IN_WEEK.toDouble()  // Convert to Double for calculation
            "day", "days" -> value.toDouble()                   // Convert to Double for calculation
            "hour", "hours" -> value.toDouble() / HOURS_IN_DAY.toDouble() // Calculate days from hours, use Double division
            else -> throw IllegalArgumentException("Unsupported duration unit: $unit")
        }
    }
}

object DescendingStringDurationComparator : Comparator<String> {
    override fun compare(a: String, b: String): Int {
        // Reverse the result of the ascending comparator
        return StringDurationComparator.compare(b, a)
    }
}

object StringViewCountComparator : Comparator<String> {
    override fun compare(a: String, b: String): Int {
        val value1 = getViewCountValue(b)
        val value2 = getViewCountValue(a)
        return value1.compareTo(value2)
    }

    private fun getViewCountValue(viewStr: String): Double {
        val cleanViewStr = viewStr.trim()
        val suffix = cleanViewStr.takeLast(1).uppercase()
        val numberPart = cleanViewStr.dropLast(1)

        return when (suffix) {
            "K" -> parseNumber(numberPart) * 1000
            "M" -> parseNumber(numberPart) * 1000000
            else -> parseNumber(cleanViewStr) // No suffix, parse the whole string
        }
    }

    private fun parseNumber(numberStr: String): Double {
        return try {
            numberStr.toDouble()
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid view count format: $numberStr", e)
        }
    }
}
