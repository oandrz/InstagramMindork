package com.mindorks.bootcamp.instagram.utils.common

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/** Converting from String to Date **/
fun String.getDateWithServerTimeStamp(): Date {
    val dateFormat = SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        Locale.getDefault()
    )
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    return dateFormat.parse(this)
}

