package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day2 : AoCDay {
    override val day: LocalDate = day(2)

    private fun getPairs(): Sequence<Pair<String, Int>> {
        return inputPath.readLines()
            .asSequence()
            .map { it.split(' ') }
            .map { (dir, v) -> dir to v.toInt() }
    }

    override fun executePart1(): Int {
        val pair = getPairs().fold(0 to 0) { (horizontal, depth), (dir, v) ->
            when (dir) {
                "forward" -> (horizontal + v) to depth
                "down" -> horizontal to (depth + v)
                "up" -> horizontal to (depth - v)
                else -> error("unknown direction: $dir")
            }
        }

        return pair.first * pair.second
    }

    override fun executePart2(): Int {
        val pair = getPairs()
            .fold(Triple(0, 0, 0)) { (horizontal, depth, aim), (dir, v) ->
                when (dir) {
                    "forward" -> Triple(horizontal + v, depth + aim * v, aim)
                    "down" -> Triple(horizontal, depth, aim + v)
                    "up" -> Triple(horizontal, depth, aim - v)
                    else -> error("unknown direction: $dir")
                }
            }

        return pair.first * pair.second
    }
}