package io.github.raphaeltarita.structure

import kotlinx.datetime.LocalDate

interface AoCDay {
    val day: LocalDate
    fun execute()
}