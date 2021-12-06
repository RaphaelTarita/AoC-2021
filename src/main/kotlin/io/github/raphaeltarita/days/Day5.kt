package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.Twin
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day5 : AoCDay {
    override val day: LocalDate = day(5)

    private fun getInput(): List<Twin<Twin<Int>>> {
        return inputPath.readLines()
            .map {
                val (xy1, xy2) = it.split(Regex("\\s*->\\s*"))
                xy1 to xy2
            }
            .map { (xy1, xy2) ->
                val (x1, y1) = xy1.split(',')
                val (x2, y2) = xy2.split(',')
                (x1.toInt() to x2.toInt()) to (y1.toInt() to y2.toInt())
            }
    }

    private fun posRange(a: Int, b: Int): IntProgression {
        return if (a < b) a..b else a downTo b
    }

    private fun getLine(xline: Twin<Int>, yline: Twin<Int>, onlyStraight: Boolean): List<Twin<Int>> {
        return if (xline.first == xline.second) {
            (posRange(yline.first, yline.second)).map { xline.first to it }
        } else if (yline.first == yline.second) {
            (posRange(xline.first, xline.second)).map { it to yline.first }
        } else {
            if (onlyStraight) {
                emptyList()
            } else {
                val ypoints = posRange(yline.first, yline.second).toList()
                posRange(xline.first, xline.second).mapIndexed { idx, x ->
                    x to ypoints[idx]
                }
            }
        }
    }

    private fun countIntersections(onlyStraight: Boolean): Int {
        val intersections = mutableMapOf<Twin<Int>, Int>()
        val input = getInput()
        for ((xline, yline) in input) {
            for (p in getLine(xline, yline, onlyStraight)) {
                intersections[p] = (intersections[p] ?: 0) + 1
            }
        }
        return intersections.count { (_, v) -> v > 1 }
    }

    override fun executePart1(): Int {
        return countIntersections(true)
    }

    override fun executePart2(): Int {
        return countIntersections(false)
    }
}