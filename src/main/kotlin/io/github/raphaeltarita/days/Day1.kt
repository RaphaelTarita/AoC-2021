package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.pathOfDay
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day1 : AoCDay {
    override val day: LocalDate = day(1)

    private fun getInts(): Sequence<Int> {
        return pathOfDay(1).readLines()
            .asSequence() // not strictly necessary but might increase performance
            .map { it.toInt() }
    }

    override fun executePart1(): Int {
        return getInts().zipWithNext()
            .count { (a, b) -> b > a }
    }

    override fun executePart2(): Int {
        return getInts().windowed(3)
            .map { it.sum() }
            .zipWithNext()
            .count { (a, b) -> b > a }
    }
}