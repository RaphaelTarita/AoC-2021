package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import io.github.raphaeltarita.util.intersect
import io.github.raphaeltarita.util.negate
import io.github.raphaeltarita.util.pow
import kotlinx.datetime.LocalDate
import java.util.EnumSet
import kotlin.io.path.readLines

object Day8 : AoCDay {
    private enum class Segment {
        UPPER,
        U_LEFT,
        U_RIGHT,
        MIDDLE,
        L_LEFT,
        L_RIGHT,
        LOWER;

        companion object {
            private val set = EnumSet.allOf(Segment::class.java)
            fun valueSet(): EnumSet<Segment> {
                return EnumSet.copyOf(set)
            }
        }
    }

    private enum class Digit(val segments: Set<Segment>, val corresponding: Int, val offSegments: Set<Segment> = segments.negate()) {
        ZERO(setOf(Segment.UPPER, Segment.U_LEFT, Segment.U_RIGHT, Segment.L_LEFT, Segment.L_RIGHT, Segment.LOWER), 0),
        ONE(setOf(Segment.U_RIGHT, Segment.L_RIGHT), 1),
        TWO(setOf(Segment.UPPER, Segment.U_RIGHT, Segment.MIDDLE, Segment.L_LEFT, Segment.LOWER), 2),
        THREE(setOf(Segment.UPPER, Segment.U_RIGHT, Segment.MIDDLE, Segment.L_RIGHT, Segment.LOWER), 3),
        FOUR(setOf(Segment.U_LEFT, Segment.U_RIGHT, Segment.MIDDLE, Segment.L_RIGHT), 4),
        FIVE(setOf(Segment.UPPER, Segment.U_LEFT, Segment.MIDDLE, Segment.L_RIGHT, Segment.LOWER), 5),
        SIX(setOf(Segment.UPPER, Segment.U_LEFT, Segment.MIDDLE, Segment.L_LEFT, Segment.L_RIGHT, Segment.LOWER), 6),
        SEVEN(setOf(Segment.UPPER, Segment.U_RIGHT, Segment.L_RIGHT), 7),
        EIGHT(setOf(Segment.UPPER, Segment.U_LEFT, Segment.U_RIGHT, Segment.MIDDLE, Segment.L_LEFT, Segment.L_RIGHT, Segment.LOWER), 8),
        NINE(setOf(Segment.UPPER, Segment.U_LEFT, Segment.U_RIGHT, Segment.MIDDLE, Segment.L_RIGHT, Segment.LOWER), 9);

        companion object {
            private val set = EnumSet.allOf(Digit::class.java)
            fun valueSet(): EnumSet<Digit> {
                return EnumSet.copyOf(set)
            }

            fun fromSegments(segments: Set<Segment>): Digit {
                for (d in values()) {
                    if (d.segments == segments) return d
                }
                error("unknown segment combination: $segments")
            }
        }
    }

    override val day: LocalDate = day(8)

    private fun isUniqueNumberLength(pattern: String): Boolean {
        return pattern.length == 2 || pattern.length == 3 || pattern.length == 4 || pattern.length == 7
    }

    private val DIGITS_LEN_5 = EnumSet.of(Digit.TWO, Digit.THREE, Digit.FIVE)
    private val DIGITS_LEN_6 = EnumSet.of(Digit.ZERO, Digit.SIX, Digit.NINE)

    private fun getInput(): List<Pair<List<String>, List<String>>> {
        return inputPath.readLines()
            .map {
                val (patterns, output) = it.split(Regex("\\s*\\|\\s*"))
                patterns.split(' ') to output.split(' ')
            }
    }

    override fun executePart1(): Int {
        return getInput().sumOf { output -> output.second.count(this::isUniqueNumberLength) }
    }

    private fun deduceSegments(from: List<String>): Map<Char, Segment> {
        val digitsLeft = Digit.valueSet()
        val possibilities = mapOf(
            'a' to Segment.valueSet(),
            'b' to Segment.valueSet(),
            'c' to Segment.valueSet(),
            'd' to Segment.valueSet(),
            'e' to Segment.valueSet(),
            'f' to Segment.valueSet(),
            'g' to Segment.valueSet()
        )

        fun narrow(deducedDigit: Digit, input: String) {
            for (c in possibilities.keys) {
                if (c in input) {
                    possibilities.getValue(c).retainAll(deducedDigit.segments)
                } else {
                    possibilities.getValue(c).removeAll(deducedDigit.segments)
                }
            }
            digitsLeft.remove(deducedDigit)
        }

        fun narrowMultiple(possibleDigits: Set<Digit>, input: String) {
            if (possibleDigits.size == 1) {
                narrow(possibleDigits.single(), input)
            }

            var possibleSegments = input.map { possibilities.getValue(it) }
            possibleSegments = possibleSegments.partition { segments -> possibleSegments.count { it == segments } == segments.size }
                .let { (fixed, other) -> other + fixed.distinct().flatten().map { EnumSet.of(it) } }

            val narrowed = possibleDigits.filter { digit ->
                digit.segments.all { segment -> possibleSegments.any { segment in it } }
                        && possibleSegments.none { segments -> digit.offSegments.containsAll(segments) }
            }
            if (narrowed.size == 1) {
                narrow(narrowed.single(), input)
            }
        }

        val (uniqueLength, other) = from.partition(this::isUniqueNumberLength)

        for (segments in uniqueLength.sortedBy { it.length }) {
            if (possibilities.values.all { it.size == 1 }) break
            when (segments.length) {
                2 -> narrow(Digit.ONE, segments)
                3 -> narrow(Digit.SEVEN, segments)
                4 -> narrow(Digit.FOUR, segments)
                7 -> narrow(Digit.EIGHT, segments)
            }
        }
        for (segments in other.sortedBy { it.length }) {
            if (possibilities.values.all { it.size == 1 }) break
            when (segments.length) {
                5 -> narrowMultiple(digitsLeft intersect DIGITS_LEN_5, segments)
                6 -> narrowMultiple(digitsLeft intersect DIGITS_LEN_6, segments)
            }
        }

        return possibilities.mapValues { (_, v) -> v.single() }
    }

    private fun toNumber(digits: List<Digit>): Int {
        var res = 0
        for ((idx, d) in digits.withIndex()) {
            res += 10.pow(digits.size - idx - 1).toInt() * d.corresponding
        }
        return res
    }

    override fun executePart2(): Int {
        return getInput().sumOf { line ->
            val segmentMap = deduceSegments(line.first)
            toNumber(line.second.map { segment -> Digit.fromSegments(segment.map { segmentMap.getValue(it) }.toSet()) })
        }
    }
}