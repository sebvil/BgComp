package com.sebastianvm.bgcomp.common.util.extensions

import kotlinx.datetime.LocalDate

fun LocalDate.toEpochMilliseconds(): Long =
    toEpochDays() * HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLIS_PER_SECOND

fun LocalDate.Companion.fromEpochMilliseconds(millis: Long): LocalDate =
    fromEpochDays(
        (millis / (HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE * MILLIS_PER_SECOND))
            .toInt()
    )

private const val HOURS_PER_DAY = 24L
private const val MINUTES_PER_HOUR = 60L
private const val SECONDS_PER_MINUTE = 60L
private const val MILLIS_PER_SECOND = 1000L
