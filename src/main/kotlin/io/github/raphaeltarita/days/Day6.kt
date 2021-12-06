package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readText

object Day6 : AoCDay {
    override val day: LocalDate = day(6)

    private fun getInput(): List<Int> {
        return inputPath.readText()
            .substringBeforeLast('\n')
            .split(Regex(",\\s*"))
            .map { it.toInt() }
    }

    private fun populate(iterations: Int): Long {
        val input = getInput().groupBy { it }
            .mapValues { (_, v) -> v.size.toLong() }

        val fish = LongArray(9) { input[it] ?: 0L }

        repeat(iterations) {
            val newFish = fish[0]
            for (age in 1..8) {
                fish[age - 1] = fish[age]
            }
            fish[8] = newFish
            fish[6] = fish[6] + newFish
        }
        return fish.sum()
    }

    override fun executePart1(): Long {
        return populate(80)
    }

    override fun executePart2(): Long {
        return populate(256)
    }
}