package io.github.raphaeltarita.structure

import io.github.raphaeltarita.structure.fmt.Formatter
import io.github.raphaeltarita.structure.fmt.GenericFormatter
import io.github.raphaeltarita.structure.fmt.IndentConfig
import io.github.raphaeltarita.structure.fmt.Indenter

open class AoCResult(
    val aocday: AoCDay,
    val result1: Any?,
    val result2: Any?,
    val fmt1: Formatter<Any?> = GenericFormatter,
    val fmt2: Formatter<Any?> = fmt1,
) {
    open fun formatResult(config: IndentConfig = IndentConfig()): String {
        val indenter = Indenter(config)
        return indenter.append("Day ${aocday.day.dayOfMonth}:")
            .increaseLevel()
            .append("Part 1:")
            .increaseLevel()
            .append(result1, fmt1)
            .decreaseLevel()
            .append("Part 2:")
            .increaseLevel()
            .append(result2, fmt2)
            .resetLevel()
            .getText()
    }
}