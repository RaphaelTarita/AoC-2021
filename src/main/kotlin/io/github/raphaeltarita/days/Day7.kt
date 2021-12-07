package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readText
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.roundToInt

object Day7 : AoCDay {
    override val day: LocalDate = day(7)

    private fun getPositions(): List<Int> {
        return inputPath.readText()
            .substringBeforeLast('\n')
            .split(Regex(",\\s*"))
            .map { it.toInt() }
    }

    private fun median(of: List<Int>): Int {
        val sorted = of.sorted()
        return if (sorted.size % 2 != 0) {
            sorted[sorted.size / 2]
        } else {
            ((sorted[sorted.size / 2 - 1] + sorted[sorted.size / 2]) / 2.0).roundToInt()
        }
    }

    private fun sumUp(x: Int): Int {
        return (x * (x + 1)) / 2
    }

    override fun executePart1(): Int {
        val positions = getPositions()
        val ideal = median(positions)
        return positions.sumOf { abs(ideal - it) }
    }

    override fun executePart2(): Int {
        val positions = getPositions()
        val ideal = positions.average()
        val lower = ideal.toInt()
        val upper = ceil(ideal).toInt()
        return min(positions.sumOf { sumUp(abs(lower - it)) }, positions.sumOf { sumUp(abs(upper - it)) })
    }
}