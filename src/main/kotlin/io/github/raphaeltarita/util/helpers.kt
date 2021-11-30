package io.github.raphaeltarita.util

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayAt

fun day(num: Int): LocalDate {
    return if (num == 0) {
        LocalDate(1970, 1, 1)
    } else {
        return LocalDate(2021, Month.DECEMBER, num)
    }
}

fun today(): LocalDate = Clock.System.todayAt(TimeZone.EST)

val TimeZone.Companion.EST: TimeZone
    get() = of("UTC-5")