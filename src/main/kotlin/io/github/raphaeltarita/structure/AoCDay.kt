package io.github.raphaeltarita.structure

import io.github.raphaeltarita.structure.fmt.Formatter
import io.github.raphaeltarita.structure.fmt.GenericFormatter
import kotlinx.datetime.LocalDate

interface AoCDay {
    val day: LocalDate
    fun executePart1(): Any?
    fun executePart2(): Any?

    val outputFormatter1: Formatter<Any?>
        get() = GenericFormatter
    val outputFormatter2: Formatter<Any?>
        get() = GenericFormatter
}