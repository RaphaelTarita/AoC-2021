package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import io.github.raphaeltarita.util.minOfBy
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day9 : AoCDay {
    override val day: LocalDate = day(9)

    private fun heightMap(): List<List<Int>> {
        return inputPath.readLines()
            .map { row -> row.map { it.digitToInt() } }
    }

    private fun height(x: Int, y: Int, heightMap: List<List<Int>>): Int {
        if (y < 0 || y > heightMap.lastIndex || x < 0 || x > heightMap.first().lastIndex) return 9
        return heightMap[y][x]
    }

    private fun isLowPoint(x: Int, y: Int, heightMap: List<List<Int>>): Boolean {
        val current = heightMap[y][x]
        if (height(x, y - 1, heightMap) <= current) return false // top
        if (height(x + 1, y, heightMap) <= current) return false // right
        if (height(x, y + 1, heightMap) <= current) return false // bottom
        if (height(x - 1, y, heightMap) <= current) return false // left
        return true
    }

    override fun executePart1(): Int {
        val heightMap = heightMap()
        var risk = 0
        for (y in heightMap.indices) {
            for (x in heightMap.first().indices) {
                if (isLowPoint(x, y, heightMap)) {
                    risk += 1 + heightMap[y][x]
                }
            }
        }
        return risk
    }

    private fun <K, V> MutableMap<K, V>.addAllFrom(keys: Iterable<K>, value: V) {
        for (k in keys) {
            this[k] = value
        }
    }

    private fun findBasin(x: Int, y: Int, heightMap: List<List<Int>>, basinMap: MutableMap<Pair<Int, Int>, Pair<Int, Int>>): Pair<Int, Int> {
        var xCurr = x
        var yCurr = y
        val foundBasinFor = mutableListOf<Pair<Int, Int>>()
        while (true) {
            val cached = basinMap[xCurr to yCurr]
            if (cached != null) {
                basinMap.addAllFrom(foundBasinFor, cached)
                return cached
            }

            val (xNew, yNew) = minOfBy(
                xCurr to yCurr,
                xCurr to yCurr - 1,
                xCurr + 1 to yCurr,
                xCurr to yCurr + 1,
                xCurr - 1 to yCurr
            ) { (x, y) -> height(x, y, heightMap) }

            if (xNew == xCurr && yNew == yCurr) {
                basinMap.addAllFrom(foundBasinFor, xCurr to yCurr)
                return xCurr to yCurr
            } else {
                xCurr = xNew
                yCurr = yNew
                foundBasinFor += xCurr to yCurr
            }
        }
    }

    override fun executePart2(): Int {
        val heightMap = heightMap()
        val basinMap = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        for (y in heightMap.indices) {
            for (x in heightMap.first().indices) {
                if (x to y !in basinMap && heightMap[y][x] < 9) {
                    basinMap[x to y] = findBasin(x, y, heightMap, basinMap)
                }
            }
        }
        return basinMap.values
            .groupingBy { it }
            .eachCount()
            .values
            .sortedDescending()
            .take(3)
            .fold(1) { acc, i -> acc * i }
    }
}