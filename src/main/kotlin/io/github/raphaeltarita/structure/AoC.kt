package io.github.raphaeltarita.structure

import io.github.raphaeltarita.days.Day0
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.today

object AoC {
    private val FALLBACK = Day0
    private val executionlist = mapOf(
        Day0.day to Day0
    )

    fun execute(num: Int) {
        (executionlist[day(num)] ?: FALLBACK).execute()
    }

    fun executeToday() {
        (executionlist[today()] ?: FALLBACK).execute()
    }

    fun executeAll() {
        for ((_, day) in executionlist) {
            if (day === Day0) continue
            day.execute()
        }
    }
}