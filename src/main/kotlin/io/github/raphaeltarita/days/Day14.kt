package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import io.github.raphaeltarita.util.splitAt
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day14 : AoCDay {
    override val day: LocalDate = day(14)

    private fun getAllInput(): Triple<Map<Pair<Char, Char>, Long>, Map<Pair<Char, Char>, Char>, Char> {
        val (firstLine, other) = inputPath.readLines().splitAt(1)
        val template = firstLine.single()
            .zipWithNext()
            .groupingBy { it }
            .eachCount()
            .mapValues { (_, v) -> v.toLong() }

        val instructions = other.drop(1)
            .map { it.split(Regex("\\s*->\\s*")) }
            .associate { (pair, result) -> (pair[0] to pair[1]) to result[0] }

        val lastChar = firstLine.single().last()
        return Triple(template, instructions, lastChar)
    }

    private fun polymerization(
        template: Map<Pair<Char, Char>, Long>,
        instructions: Map<Pair<Char, Char>, Char>,
        iterations: Int
    ): Map<Pair<Char, Char>, Long> {
        var original = template
        var workingCopy = template.toMutableMap()
        repeat(iterations) {
            for ((pair, freq) in original) {
                val insert = instructions[pair]
                if (insert != null) {
                    val newCount = (workingCopy[pair] ?: 0L) - freq
                    if (newCount <= 0) {
                        workingCopy.remove(pair)
                    } else {
                        workingCopy[pair] = newCount
                    }
                    val left = pair.first to insert
                    val right = insert to pair.second
                    workingCopy[left] = (workingCopy[left] ?: 0L) + freq
                    workingCopy[right] = (workingCopy[right] ?: 0L) + freq
                }
            }
            original = workingCopy
            workingCopy = original.toMutableMap()
        }
        return original
    }

    private fun calculateResult(forIterations: Int): Long {
        val (template, instructions, lastChar) = getAllInput()
        val letterCounts = polymerization(template, instructions, forIterations).entries
            .groupingBy { (k, _) -> k.first }
            .aggregate { _, sum: Long?, count, _ -> (sum ?: 0L) + count.value }
            .toMutableMap()

        letterCounts[lastChar] = (letterCounts[lastChar] ?: 0) + 1

        return (letterCounts.values.maxOrNull() ?: 0) - (letterCounts.values.minOrNull() ?: 0)
    }

    override fun executePart1(): Long {
        return calculateResult(10)
    }

    override fun executePart2(): Long {
        return calculateResult(40)
    }
}