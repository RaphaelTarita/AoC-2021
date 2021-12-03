package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day3 : AoCDay {
    override val day: LocalDate = day(3)

    private fun getInput(): List<String> {
        return inputPath.readLines()
    }

    override fun executePart1(): Int {
        val input = getInput()
        val isize = input.size
        val linesize = input.first().length
        val gamma = StringBuilder()
        val epsilon = StringBuilder()

        for (i in 0 until linesize) {
            val zeros = input.count { it[i] == '0' }
            if (zeros * 2 > isize) {
                gamma.append('0')
                epsilon.append('1')
            } else {
                gamma.append('1')
                epsilon.append('0')
            }
        }

        return gamma.toString().toInt(2) * epsilon.toString().toInt(2)
    }

    private fun doFilter(pos: Int, input: List<String>, majority: Boolean): List<String> {
        val (zeros, ones) = input.partition { it[pos] == '0' }
        return if (zeros.size > ones.size == majority) {
            zeros
        } else {
            ones
        }
    }

    override fun executePart2(): Int {
        val input = getInput()
        var oxygen = input
        var co2 = input
        val linesize = input.first().length

        for (i in 0 until linesize) {
            if (oxygen.size > 1) {
                oxygen = doFilter(i, oxygen, true)
            }
            if (co2.size > 1) {
                co2 = doFilter(i, co2, false)
            }
        }
        return oxygen.single().toInt(2) * co2.single().toInt(2)
    }
}