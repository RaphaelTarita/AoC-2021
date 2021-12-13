package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.Graph
import io.github.raphaeltarita.util.buildGraph
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day12 : AoCDay {
    private object VertexPool {
        private val vertices: MutableMap<String, Cave> = mutableMapOf()

        fun get(forString: String): Cave {
            return vertices.getOrPut(forString) { Cave(forString) }
        }
    }

    private data class Cave(val label: String, val isSmall: Boolean) {
        constructor(label: String) : this(label, label.first().isLowerCase())

        override fun equals(other: Any?): Boolean {
            return this === other // all instances pooled, see VertexPool
        }

        override fun hashCode(): Int {
            return label.hashCode()
        }
    }

    override val day: LocalDate = day(12)

    private fun getGraph(): Graph<Cave> {
        val edges = inputPath.readLines()
            .map {
                val (v1, v2) = it.split(Regex("\\s*-\\s*"))
                VertexPool.get(v1) to VertexPool.get(v2)
            }

        return buildGraph {
            for ((v1, v2) in edges) {
                addUndirectedEdge(v1, v2)
            }
        }
    }

    private fun dfs(graph: Graph<Cave>, currentPath: List<Cave>, start: Cave, end: Cave, duplicateUsed: Boolean = false): Int {
        var result = 0
        for (adjacent in graph.adjacentVertices(currentPath.last())) {
            var usesDuplication = duplicateUsed
            if (adjacent == end) {
                ++result
                continue
            }
            if (adjacent.isSmall && adjacent in currentPath) {
                if (duplicateUsed || adjacent == start) continue else usesDuplication = true
            }
            result += dfs(graph, currentPath + adjacent, start, end, usesDuplication)
        }
        return result
    }

    private fun calculatePaths(allowOneDuplicate: Boolean): Int {
        val graph = getGraph()
        val start = graph.vertices.find { it.label == "start" } ?: error("no 'start' node")
        val end = graph.vertices.find { it.label == "end" } ?: error("no 'end' node")
        return dfs(graph, listOf(start), start, end, !allowOneDuplicate)
    }

    override fun executePart1(): Int = calculatePaths(false)
    override fun executePart2(): Int = calculatePaths(true)
}