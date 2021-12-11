package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day11 : AoCDay {
    override val day: LocalDate = day(11)

    private fun getInputField(): List<MutableList<Pair<Int, Boolean>>> {
        return inputPath.readLines()
            .map { line -> line.map { it.digitToInt() to false }.toMutableList() }
    }

    private fun processOctopus(x: Int, y: Int, field: List<MutableList<Pair<Int, Boolean>>>): Int {
        if (y < 0 || y > field.lastIndex || x < 0 || x > field.first().lastIndex) return 0
        val octopus = field[y][x].let { it.first + 1 to it.second }

        if (octopus.second) return 0  // already flashed

        return if (octopus.first > 9) {
            field[y][x] = 0 to true
            return processOctopus(x - 1, y - 1, field) +  // upper left
                    processOctopus(x, y - 1, field) +        // upper
                    processOctopus(x + 1, y - 1, field) + // upper right
                    processOctopus(x + 1, y, field) +        // right
                    processOctopus(x + 1, y + 1, field) + // lower right
                    processOctopus(x, y + 1, field) +        // lower
                    processOctopus(x - 1, y + 1, field) + // lower left
                    processOctopus(x - 1, y, field) +        // left
                    1                                           // center
        } else {
            field[y][x] = octopus.first to false
            0
        }
    }

    private fun reset(field: List<MutableList<Pair<Int, Boolean>>>) {
        field.forEach { line ->
            line.replaceAll { (energy, _) -> energy to false }
        }
    }

    override fun executePart1(): Int {
        val dumbos = getInputField()
        var result = 0
        repeat(100) {
            for (y in dumbos.indices) {
                for (x in dumbos[y].indices) {
                    result += processOctopus(x, y, dumbos)
                }
            }
            reset(dumbos)
        }
        return result
    }

    override fun executePart2(): Int {
        val dumbos = getInputField()
        var counter = 0
        while (true) {
            for (y in dumbos.indices) {
                for (x in dumbos[y].indices) {
                    processOctopus(x, y, dumbos)
                }
            }
            ++counter
            if (dumbos.all { line -> line.all { it.second } }) return counter
            reset(dumbos)
        }
    }
}