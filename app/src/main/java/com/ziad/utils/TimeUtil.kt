package com.ziad.utils

import java.time.Duration
import java.time.Instant

object TimeUtil {
     fun computeRelativeTime(past: Instant): String {
        val now = Instant.now()
        val dur = Duration.between(past, now)
        return when {
            dur.isNegative -> "just now"
            dur.toMinutes() < 1 -> "just now"
            dur.toMinutes() < 60 -> "${dur.toMinutes()} minute${if (dur.toMinutes() == 1L) "" else "s"} ago"
            dur.toHours() < 24 -> "${dur.toHours()} hour${if (dur.toHours() == 1L) "" else "s"} ago"
            dur.toDays() < 7 -> "${dur.toDays()} day${if (dur.toDays() == 1L) "" else "s"} ago"
            else -> {
                val dt = java.time.ZonedDateTime.ofInstant(past, java.time.ZoneId.systemDefault())
                val fmt = java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")
                "on ${dt.format(fmt)}"
            }
        }
    }
}