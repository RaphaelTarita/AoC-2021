package io.github.raphaeltarita.structure

import io.github.raphaeltarita.days.Day0
import io.github.raphaeltarita.days.Day1
import io.github.raphaeltarita.days.Day10
import io.github.raphaeltarita.days.Day11
import io.github.raphaeltarita.days.Day12
import io.github.raphaeltarita.days.Day13
import io.github.raphaeltarita.days.Day14
import io.github.raphaeltarita.days.Day15
import io.github.raphaeltarita.days.Day16
import io.github.raphaeltarita.days.Day17
import io.github.raphaeltarita.days.Day18
import io.github.raphaeltarita.days.Day19
import io.github.raphaeltarita.days.Day2
import io.github.raphaeltarita.days.Day20
import io.github.raphaeltarita.days.Day21
import io.github.raphaeltarita.days.Day22
import io.github.raphaeltarita.days.Day23
import io.github.raphaeltarita.days.Day24
import io.github.raphaeltarita.days.Day25
import io.github.raphaeltarita.days.Day3
import io.github.raphaeltarita.days.Day4
import io.github.raphaeltarita.days.Day5
import io.github.raphaeltarita.days.Day6
import io.github.raphaeltarita.days.Day7
import io.github.raphaeltarita.days.Day8
import io.github.raphaeltarita.days.Day9
import io.github.raphaeltarita.structure.fmt.IndentConfig
import io.github.raphaeltarita.util.Twin
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.mapPair
import io.github.raphaeltarita.util.outputPathOfDay
import io.github.raphaeltarita.util.today
import kotlinx.datetime.LocalDate
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.Path
import kotlin.io.path.bufferedWriter
import kotlin.io.path.deleteIfExists
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

object AoC {
    private val DAYS_OUTPUT_PATH = Path("output/days.txt")
    private val FALLBACK = Day0
    private val executionlist = mapOf(
        Day1.mapPair,
        Day2.mapPair,
        Day3.mapPair,
        Day4.mapPair,
        Day5.mapPair,
        Day6.mapPair,
        Day7.mapPair,
        Day8.mapPair,
        Day9.mapPair,
        Day10.mapPair,
        Day11.mapPair,
        Day12.mapPair,
        Day13.mapPair,
        Day14.mapPair,
        Day15.mapPair,
        Day16.mapPair,
        Day17.mapPair,
        Day18.mapPair,
        Day19.mapPair,
        Day20.mapPair,
        Day21.mapPair,
        Day22.mapPair,
        Day23.mapPair,
        Day24.mapPair,
        Day25.mapPair,
    )

    private fun printFile(result: AoCResult, path: Path, config: IndentConfig, append: Boolean = false) {
        path.bufferedWriter(
            options = arrayOf(
                StandardOpenOption.CREATE,
                StandardOpenOption.WRITE,
                if (append) StandardOpenOption.APPEND else StandardOpenOption.TRUNCATE_EXISTING
            )
        )
            .append(result.formatResult(config)).close()
    }

    private fun printAll(cfg: IndentConfig, mapper: (LocalDate) -> AoCResult) {
        DAYS_OUTPUT_PATH.deleteIfExists()
        executionlist.keys
            .sorted()
            .map(mapper)
            .forEach { printFile(it, DAYS_OUTPUT_PATH, cfg, true) }
    }

    fun executeSimple(d: LocalDate): AoCResult {
        val day = executionlist[d] ?: FALLBACK
        return AoCResult(
            day,
            day.executePart1(),
            day.executePart2(),
            day.outputFormatter1,
            day.outputFormatter2
        )
    }

    @OptIn(ExperimentalTime::class)
    fun executeTimed(d: LocalDate, iterations: Int = 100, warmups: Int = 0): AoCTimedResult {
        val day = executionlist[d] ?: FALLBACK

        repeat(warmups) {
            day.executePart1()
            day.executePart2()
        }

        val totalResults = mutableListOf<Twin<Pair<Any?, Duration>>>()

        repeat(iterations) {
            val (res1, timing1) = measureTimedValue { day.executePart1() }
            val (res2, timing2) = measureTimedValue { day.executePart2() }
            totalResults.add((res1 to timing1) to (res2 to timing2))
        }

        val expected1 = totalResults.first().first.first
        val expected2 = totalResults.first().second.first
        if (!totalResults.all { it.first.first == expected1 && it.second.first == expected2 }) {
            throw IllegalStateException("Results in multiple timing runs were not the same!")
        }

        val timings: Twin<Duration> = totalResults.map { it.first.second to it.second.second }
            .foldIndexed(Duration.ZERO to Duration.ZERO) { n, cma, x ->
                val x1 = (x.first + cma.first * n) / (n + 1)
                val x2 = (x.second + cma.second * n) / (n + 1)
                x1 to x2
            }

        return AoCTimedResult(
            day,
            totalResults.first().first.first,
            totalResults.first().second.first,
            timings.first,
            timings.second,
            day.outputFormatter1,
            day.outputFormatter2
        )
    }

    fun executeSimple(num: Int): AoCResult = executeSimple(day(num))
    fun executeTimed(num: Int, iterations: Int = 100, warmups: Int = 0): AoCTimedResult = executeTimed(day(num), iterations, warmups)
    fun executeSimpleToday(): AoCResult = executeSimple(today())
    fun executeTimedToday(iterations: Int = 100, warmups: Int = 0): AoCTimedResult = executeTimed(today(), iterations, warmups)

    fun executeAllSimple(): List<AoCResult> {
        return executionlist.keys
            .sorted()
            .map { executeSimple(it) }
    }

    fun executeAllTimed(iterations: Int = 100, warmups: Int = 0): List<AoCTimedResult> {
        return executionlist.keys
            .sorted()
            .map { executeTimed(it, iterations, warmups) }
    }

    fun printSimple(d: LocalDate, cfg: IndentConfig = IndentConfig()) = printFile(executeSimple(d), outputPathOfDay(d), cfg)
    fun printSimple(num: Int, cfg: IndentConfig = IndentConfig()) = printSimple(day(num), cfg)
    fun printSimpleToday(cfg: IndentConfig = IndentConfig()) = printSimple(today(), cfg)

    fun printTimed(
        d: LocalDate,
        iterations: Int = 100,
        warmups: Int = 0,
        cfg: IndentConfig = IndentConfig()
    ) = printFile(executeTimed(d, iterations, warmups), outputPathOfDay(d), cfg)

    fun printTimed(
        num: Int,
        iterations: Int = 0,
        warmups: Int = 0,
        cfg: IndentConfig = IndentConfig()
    ) = printTimed(day(num), iterations, warmups, cfg)

    fun printTimedToday(
        iterations: Int = 100,
        warmups: Int = 0,
        cfg: IndentConfig = IndentConfig()
    ) = printTimed(today(), iterations, warmups, cfg)

    fun printAllSimple(cfg: IndentConfig = IndentConfig()) {
        printAll(cfg) { executeSimple(it) }
    }

    fun printAllTimed(iterations: Int = 100, warmups: Int = 0, cfg: IndentConfig = IndentConfig()) {
        printAll(cfg) { executeTimed(it, iterations, warmups) }
    }
}