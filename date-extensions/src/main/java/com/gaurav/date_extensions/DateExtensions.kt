package com.gaurav.date_extensions

fun Long.changeToDateFormat(format:String = "yyyy-MM-dd HH:mm:ss"):String {
    val date = java.util.Date(this)
    val dateStr = java.text.SimpleDateFormat(format)
    return dateStr.format(date)
}