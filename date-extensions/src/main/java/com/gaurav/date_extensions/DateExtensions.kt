package com.gaurav.date_extensions

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private fun Long.changeToMilliSec(isEpochTime:Boolean):Long {
    return if(isEpochTime) this*1000 else this
}

fun Long.changeToDateFormat(isEpochTime:Boolean = false,
                            format: String = "yyyy-MM-dd HH:mm:ss"): String {
    val date = java.util.Date(this.changeToMilliSec(isEpochTime))
    val dateStr = SimpleDateFormat(format)
    return dateStr.format(date)
}

fun Long.getDayFromTimestamp(isEpochTime:Boolean = false): Int {
    val cal = Calendar.getInstance()
    cal.timeInMillis = this.changeToMilliSec(isEpochTime)
    return cal[Calendar.DAY_OF_MONTH]
}

fun getDayOfMonthSuffix(day: Int): String? {
    return if (day >= 11 && day <= 13) {
        "th"
    } else when (day % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}

fun Long.parseTimeTo_dd_MMM(isEpochTime:Boolean = false): String {
    if (this == 0L)
        return ""

    var format = ""
    val milliSeconds = this.changeToMilliSec(isEpochTime)
    try {
        val pattern = "d'" + getDayOfMonthSuffix(milliSeconds.getDayFromTimestamp()) + "' MMM"
        val formatter = SimpleDateFormat(pattern, Locale.getDefault())
        format = formatter.format(milliSeconds)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return format
}

private fun getStringFormatted(str: String, vararg values: String): String {
    val format = str.replace("%d", values[0])
    return format
}

fun Long.parseRemainingTimeDifference(isEpochTime:Boolean = false): String {
    val timeInMillis = this.changeToMilliSec(isEpochTime)
    val seconds = timeInMillis / 1000
    if (seconds < 60) {
        return "Latest"
    }
    val minutes = seconds / 60
    if (minutes < 60) {
        return getStringFormatted(
            "%d minutes",
            java.lang.Long.toString(minutes)
        )
    }
    val hours = minutes / 60
    if (hours < 24) {
        return getStringFormatted(
            "%d hours",
            java.lang.Long.toString(hours)
        )
    }
    val days = hours / 24
    if (days < 30) {
        return getStringFormatted(
            "%d days",
            java.lang.Long.toString(days)
        )
    }
    val months = days / 30
    if (months < 12) {
        return getStringFormatted(
            "%d months",
            java.lang.Long.toString(months)
        )
    }
    val years = months / 12
    return getStringFormatted(
        "%d years",
        java.lang.Long.toString(years)
    )
}

fun Long.parseTimeDifference(isEpochTime:Boolean = false): String {

    val seconds = this.changeToMilliSec(isEpochTime) / 1000
    if (seconds < 60) {
        return "Latest"
    }
    val minutes = seconds / 60
    if (minutes < 60) {
        return getStringFormatted(
            "%d minutes ago",
            java.lang.Long.toString(minutes)
        )
    }
    val hours = minutes / 60
    if (hours < 24) {
        return getStringFormatted(
            "%d hours ago",
            java.lang.Long.toString(hours)
        )
    }
    val days = hours / 24
    if (days < 30) {
        return getStringFormatted(
            "%d days ago",
            java.lang.Long.toString(days)
        )
    }
    val months = days / 30
    if (months < 12) {
        return getStringFormatted(
            "%d months ago",
            java.lang.Long.toString(months)
        )
    }
    val years = months / 12
    return getStringFormatted(
        "%d years ago",
        java.lang.Long.toString(years)
    )
}

fun Long.isDateToday(isEpochTime:Boolean = false): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.changeToMilliSec(isEpochTime)
    val todayCalendar = Calendar.getInstance()
    todayCalendar.timeInMillis = System.currentTimeMillis()
    return (
            calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                    calendar.get(Calendar.DAY_OF_MONTH) == todayCalendar.get(Calendar.DAY_OF_MONTH)
            )
}

fun Long.isDateYesterday(isEpochTime:Boolean = false): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.changeToMilliSec(isEpochTime)
    val todayCalendar = Calendar.getInstance()
    todayCalendar.timeInMillis = System.currentTimeMillis()

    return (
            calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR) - 1
            )
}

fun Long.isDateYesterdayOrBefore(isEpochTime: Boolean= false): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.changeToMilliSec(isEpochTime)
    val todayCalendar = Calendar.getInstance()
    todayCalendar.timeInMillis = System.currentTimeMillis()

    return (
            calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                    calendar.get(Calendar.DAY_OF_YEAR) < todayCalendar.get(Calendar.DAY_OF_YEAR)
            )
}

fun Long.isDateLastMinuteOrBefore(isEpochTime: Boolean= false): Boolean {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.changeToMilliSec(isEpochTime)
    val todayCalendar = Calendar.getInstance()
    todayCalendar.timeInMillis = System.currentTimeMillis()

    return (
            calendar.get(Calendar.YEAR) == todayCalendar.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == todayCalendar.get(Calendar.MONTH) &&
                    calendar.get(Calendar.DAY_OF_YEAR) == todayCalendar.get(Calendar.DAY_OF_YEAR) &&
                    calendar.get(Calendar.HOUR_OF_DAY) == todayCalendar.get(Calendar.HOUR_OF_DAY) &&
                    calendar.get(Calendar.MINUTE) < todayCalendar.get(Calendar.MINUTE)
            )
}

fun Long.timeDayOfWeek(isEpochTime: Boolean= false): String {
    return SimpleDateFormat("EEEE hh:mm a", Locale.getDefault()).format(this.changeToMilliSec(isEpochTime))
}

fun Long.isMoreThanSevenDays(isEpochTime: Boolean= false): Boolean {
    return getDiffDate(this.changeToMilliSec(isEpochTime), System.currentTimeMillis()).days > 7
}

private fun getDiffDate(startDateInMillis: Long, endDateInMillis: Long): CustomDate {
    val diff = endDateInMillis - startDateInMillis
    val seconds = diff / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return CustomDate(days, hours % 24, minutes % 60, seconds % 60)
}

fun Long.isDateThisWeek(isEpochTime: Boolean= false): Boolean {
    val currentCalendar = Calendar.getInstance()
    val week = currentCalendar[Calendar.WEEK_OF_YEAR]
    val year = currentCalendar[Calendar.YEAR]
    val targetCalendar = Calendar.getInstance()
    targetCalendar.timeInMillis = this.changeToMilliSec(isEpochTime)
    val targetWeek = targetCalendar[Calendar.WEEK_OF_YEAR]
    val targetYear = targetCalendar[Calendar.YEAR]
    return week == targetWeek && year == targetYear
}

fun Long.getTimeInString(isEpochTime: Boolean = false): String {
    val minutes = this.changeToMilliSec(isEpochTime) / 60
    val seconds = this.changeToMilliSec(isEpochTime) % 60
    return "$minutes : $seconds"
}

fun Long.getHourMinFormatString(isEpochTime: Boolean = false): String {
    val minutes = this.changeToMilliSec(isEpochTime) / (1000 * 60) % 60
    val hours = this.changeToMilliSec(isEpochTime) / (1000 * 60 * 60) % 24
    return "${if (hours < 10) "0" else ""}$hours:${if (minutes < 10) "0" else ""}$minutes"
}

// Sample date 12:50 P.M
fun Long.getTime(isEpochTime: Boolean = false): String {
    return SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(this.changeToMilliSec(isEpochTime)).toUpperCase()
}

fun Long.getDayWithSuffix(isEpochTime: Boolean = false): String {
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = this.changeToMilliSec(isEpochTime)

    val day = calendar[Calendar.DAY_OF_MONTH]

    val dayWithSuffix = "$day${getDayOfMonthSuffix(day)}"

    val formatter = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    val formattedDate = formatter.format(this.changeToMilliSec(isEpochTime))
    return "$dayWithSuffix $formattedDate"
}