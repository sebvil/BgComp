package com.sebastianvm.bgcomp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import kotlinx.datetime.LocalDate

interface DateTimeFormatter {

    fun format(date: LocalDate): String
}

val LocalDateTimeFormatter =
    compositionLocalOf<DateTimeFormatter> { error("No datetime formatter provided!") }

@Composable fun LocalDate.format(): String = LocalDateTimeFormatter.current.format(this)
