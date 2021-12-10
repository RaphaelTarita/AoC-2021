package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day10 : AoCDay {
    private sealed class CheckResult {
        abstract fun getScore(): Long
        class Invalid(val type: CharType) : CheckResult() {
            override fun getScore(): Long = type.invalidScore.toLong()
        }

        class Incomplete(val types: List<CharType>) : CheckResult() {
            override fun getScore(): Long = types.asReversed().fold(0L) { acc, type -> acc * 5 + type.incompleteScore }
        }
    }

    private enum class CharType(val opening: Char, val closing: Char, val invalidScore: Int, val incompleteScore: Int) {
        PARENTHESIS('(', ')', 3, 1),
        BRACKET('[', ']', 57, 2),
        BRACE('{', '}', 1197, 3),
        ANGLE_BRACKET('<', '>', 25137, 4);

        companion object {
            fun opened(c: Char): CharType = values().find { it.opening == c } ?: error("unknown opening character: $c")
            fun closed(c: Char): CharType = values().find { it.closing == c } ?: error("unknown closing character: $c")
        }
    }

    override val day: LocalDate = day(10)

    private fun getInput(): List<String> = inputPath.readLines()

    private fun isOpening(c: Char): Boolean = c == '(' || c == '[' || c == '{' || c == '<'
    private fun isClosing(c: Char): Boolean = c == ')' || c == ']' || c == '}' || c == '>'

    private fun checkLine(line: String): CheckResult {
        val stack = mutableListOf<CharType>()
        for (c in line) {
            when {
                isOpening(c) -> stack += CharType.opened(c)
                isClosing(c) -> if (stack.removeLastOrNull() != CharType.closed(c)) return CheckResult.Invalid(CharType.closed(c))
                else -> error("unknown symbol: '$c'")
            }
        }
        return CheckResult.Incomplete(stack)
    }

    override fun executePart1(): Long {
        var result = 0L
        for (line in getInput()) {
            result += when (val res = checkLine(line)) {
                is CheckResult.Incomplete -> continue
                is CheckResult.Invalid -> res.getScore()
            }
        }
        return result
    }

    override fun executePart2(): Long {
        val allScores = buildList {
            for (line in getInput()) {
                this += when (val res = checkLine(line)) {
                    is CheckResult.Invalid -> continue
                    is CheckResult.Incomplete -> res.getScore()
                }
            }
        }.sorted()
        return allScores[allScores.size / 2]
    }
}