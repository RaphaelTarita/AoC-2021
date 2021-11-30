package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import kotlinx.datetime.LocalDate

object Day0 : AoCDay {
    override val day: LocalDate = day(0)

    override fun execute() {
        println("Test day executed successfully (this is Day \"0\", which is intended either for testing the framework or as a fallback)")
    }
}