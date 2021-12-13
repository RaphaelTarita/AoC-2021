package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readText

object Day13 : AoCDay {
    override val day: LocalDate = day(13)

    private fun getInput(): Pair<Set<Pair<Int, Int>>, List<Pair<Char, Int>>> {
        val (coordinates, folds) = inputPath.readText().split("\n\n")
        return coordinates.lines()
            .map {
                val (x, y) = it.split(',')
                x.toInt() to y.toInt()
            }.toSet() to folds.lines()
            .dropLast(1)
            .map {
                val (dir, value) = it.split('=')
                dir.last() to value.toInt()
            }
    }

    private fun foldPoint(value: Int, axis: Int): Int {
        return if (value >= axis) 2 * axis - value else value
    }

    private fun foldSheet(coordinates: Set<Pair<Int, Int>>, folds: List<Pair<Char, Int>>): Set<Pair<Int, Int>> {
        var res = coordinates
        for ((dir, axis) in folds) {
            res = when (dir) {
                'x' -> res.map { (x, y) -> foldPoint(x, axis) to y }.toSet()
                'y' -> res.map { (x, y) -> x to foldPoint(y, axis) }.toSet()
                else -> error("unknown direction: '$dir'")
            }
        }
        return res
    }

    private fun rangeFor(coords: Set<Pair<Int, Int>>, selector: (Pair<Int, Int>) -> Int): IntRange {
        return coords.minOf(selector)..coords.maxOf(selector)
    }

    private fun printSheet(coordinates: Set<Pair<Int, Int>>): String {
        val sb = StringBuilder()
        for (y in rangeFor(coordinates) { (_, y) -> y }) {
            for (x in rangeFor(coordinates) { (x, _) -> x }) {
                sb.append(if (x to y in coordinates) '#' else '.')
            }
            sb.append('\n')
        }
        return sb.toString()
    }

    override fun executePart1(): Int {
        val (coordinates, folds) = getInput()
        val foldedCoordinates = foldSheet(coordinates, listOf(folds.first()))
        return foldedCoordinates.size
    }

    override fun executePart2(): String {
        val (coordinates, folds) = getInput()
        val foldedCoordinates = foldSheet(coordinates, folds)
        return printSheet(foldedCoordinates)
    }
}