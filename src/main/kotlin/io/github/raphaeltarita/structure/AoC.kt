package io.github.raphaeltarita.structure

import io.github.raphaeltarita.days.Day0
import io.github.raphaeltarita.days.Day1
import io.github.raphaeltarita.days.Day10
import io.github.raphaeltarita.days.Day11
import io.github.raphaeltarita.days.Day12
import io.github.raphaeltarita.days.Day13
import io.github.raphaeltarita.days.Day14
import io.github.raphaeltarita.days.Day15
import io.github.raphaeltarita.days.Day16
import io.github.raphaeltarita.days.Day17
import io.github.raphaeltarita.days.Day18
import io.github.raphaeltarita.days.Day19
import io.github.raphaeltarita.days.Day2
import io.github.raphaeltarita.days.Day20
import io.github.raphaeltarita.days.Day21
import io.github.raphaeltarita.days.Day22
import io.github.raphaeltarita.days.Day23
import io.github.raphaeltarita.days.Day24
import io.github.raphaeltarita.days.Day25
import io.github.raphaeltarita.days.Day3
import io.github.raphaeltarita.days.Day4
import io.github.raphaeltarita.days.Day5
import io.github.raphaeltarita.days.Day6
import io.github.raphaeltarita.days.Day7
import io.github.raphaeltarita.days.Day8
import io.github.raphaeltarita.days.Day9
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.mapPair
import io.github.raphaeltarita.util.today
import kotlinx.datetime.LocalDate

object AoC {
    private fun separator(part: Int): String = "============================================ Part $part ============================================"
    private val FALLBACK = Day0
    private val executionlist = mapOf(
        Day0.mapPair,
        Day1.mapPair,
        Day2.mapPair,
        Day3.mapPair,
        Day4.mapPair,
        Day5.mapPair,
        Day6.mapPair,
        Day7.mapPair,
        Day8.mapPair,
        Day9.mapPair,
        Day10.mapPair,
        Day11.mapPair,
        Day12.mapPair,
        Day13.mapPair,
        Day14.mapPair,
        Day15.mapPair,
        Day16.mapPair,
        Day17.mapPair,
        Day18.mapPair,
        Day19.mapPair,
        Day20.mapPair,
        Day21.mapPair,
        Day22.mapPair,
        Day23.mapPair,
        Day24.mapPair,
        Day25.mapPair,
    )

    private fun execute(day: AoCDay) {
        println(separator(1))
        day.executePart1()
        println(separator(2))
        day.executePart2()
    }

    fun execute(d: LocalDate) = execute(executionlist[d] ?: FALLBACK)
    fun execute(num: Int) = execute(day(num))
    fun executeToday() = execute(today())

    fun executeAll() {
        for ((_, day) in executionlist) {
            if (day === Day0) continue
            execute(day)
        }
    }
}