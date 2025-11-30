package com.sebastianvm.bgcomp.ui

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import java.time.format.FormatStyle
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate

@ContributesBinding(AppScope::class)
@Inject
class JavaDateTimeFormatter : DateTimeFormatter {

    override fun format(date: LocalDate): String {
        val dateFormatter = java.time.format.DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        val localizedDate = date.toJavaLocalDate().format(dateFormatter)
        return localizedDate
    }
}
