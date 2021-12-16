package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.Twin
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.ds.heap.ArrayHeap
import io.github.raphaeltarita.util.ds.heap.MutableHeap
import io.github.raphaeltarita.util.inModRange
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day15 : AoCDay {
    override val day: LocalDate = day(15)

    private fun getInput(): List<List<Byte>> {
        return inputPath.readLines()
            .map { line -> line.map { it.digitToInt().toByte() } }
    }

    private fun adjacentTo(vertex: Twin<Int>, xsize: Int, ysize: Int): List<Twin<Int>> {
        val result = ArrayList<Twin<Int>>(4)
        if (vertex.first > 0) result += (vertex.first - 1) to vertex.second
        if (vertex.first < xsize - 1) result += (vertex.first + 1) to vertex.second
        if (vertex.second > 0) result += vertex.first to (vertex.second - 1)
        if (vertex.second < ysize - 1) result += vertex.first to (vertex.second + 1)
        return result
    }

    private fun dijkstra(weights: List<List<Byte>>, start: Twin<Int>, end: Twin<Int>): Int {
        val xsize = weights.first().size
        val ysize = weights.size

        val processed = mutableSetOf(start)
        val minPaths = adjacentTo(start, xsize, ysize).associateWith { (x, y) -> weights[y][x].toInt() }
            .toMutableMap()
            .withDefault { Int.MAX_VALUE }
        minPaths[start] = 0

        val heap: MutableHeap<Twin<Int>> = ArrayHeap(adjacentTo(0 to 0, xsize, ysize)) { o1, o2 ->
            minPaths.getValue(o1) - minPaths.getValue(o2)
        }

        var itr = 0
        while (heap.isNotEmpty()) {
            val current = heap.popMin()
            processed += current
            if (current == end) break
            for (adj in adjacentTo(current, xsize, ysize)) {
                if (adj in processed) continue
                val nextCost = minPaths.getValue(current) + weights[adj.second][adj.first]
                val heapIdx = heap.indexOf(adj)
                if (heapIdx < 0) {
                    minPaths[adj] = nextCost
                    heap += adj
                } else if (nextCost < minPaths.getValue(heap[heapIdx])) {
                    minPaths[adj] = nextCost
                    heap.decreaseKey(heapIdx, adj)
                }
            }
            ++itr
        }

        return minPaths.getValue((xsize - 1) to (ysize - 1))
    }

    override fun executePart1(): Int {
        val grid = getInput()
        val xsize = grid.first().size
        val ysize = grid.size
        return dijkstra(grid, 0 to 0, (xsize - 1) to (ysize - 1))
    }

    override fun executePart2(): Int {
        val grid = getInput()
        val xsize = grid.first().size
        val ysize = grid.size
        val fullGrid = List(5 * ysize) { y ->
            List(5 * xsize) { x ->
                ((grid[y % ysize][x % xsize] + (x / xsize + y / ysize)).inModRange(1, 10)).toByte()
            }
        }
        return dijkstra(fullGrid, 0 to 0, (xsize * 5 - 1) to (ysize * 5 - 1))
    }
}