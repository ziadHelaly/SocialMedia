package com.ziad.view.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.ziad.utils.TimeUtil
import java.time.Instant

@Composable
fun rememberRelativeTime(isoTime: String): String {
    val formatter = remember {
        java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME
    }
    val updatedInstant = remember(isoTime) {
        try {
            java.time.OffsetDateTime.parse(isoTime, formatter).toInstant()
        } catch (e: Exception) {
            Instant.EPOCH
        }
    }

    val ticker = remember {
        kotlinx.coroutines.flow.flow {
            while (true) {
                emit(Unit)
                kotlinx.coroutines.delay(60_000L)
            }
        }
    }

    LaunchedEffect(updatedInstant) {
    }

    val relative = remember(updatedInstant) {
        mutableStateOf(TimeUtil.computeRelativeTime(updatedInstant))
    }

    LaunchedEffect(ticker, updatedInstant) {
        ticker.collect {
            relative.value = TimeUtil.computeRelativeTime(updatedInstant)
        }
    }

    return relative.value
}

