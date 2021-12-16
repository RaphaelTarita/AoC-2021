package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.HEX_INV
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import kotlinx.datetime.LocalDate
import kotlin.io.path.readText

// perform everything using chars because using bitops is agonizing
object Day16 : AoCDay {
    sealed class Packet(val version: Int) {
        abstract val typeId: Byte
        abstract fun calculate(): Long
    }

    class LiteralPacket(version: Int, val value: Long) : Packet(version) {
        override val typeId: Byte = 4
        override fun calculate(): Long = value
    }

    sealed class OperatorPacket(version: Int, val value: List<Packet>) : Packet(version)

    class SumPacket(version: Int, value: List<Packet>) : OperatorPacket(version, value) {
        override val typeId: Byte = 0
        override fun calculate(): Long = value.sumOf { it.calculate() }
    }

    class ProductPacket(version: Int, value: List<Packet>) : OperatorPacket(version, value) {
        override val typeId: Byte = 1
        override fun calculate(): Long = value.fold(1L) { acc, packet -> acc * packet.calculate() }
    }

    class MinimumPacket(version: Int, value: List<Packet>) : OperatorPacket(version, value) {
        override val typeId: Byte = 2
        override fun calculate(): Long = value.minOf { it.calculate() }
    }

    class MaximumPacket(version: Int, value: List<Packet>) : OperatorPacket(version, value) {
        override val typeId: Byte = 3
        override fun calculate(): Long = value.maxOf { it.calculate() }
    }

    class GreaterThanPacket(version: Int, value: List<Packet>) : OperatorPacket(version, value) {
        override val typeId: Byte = 5
        override fun calculate(): Long = if (value[0].calculate() > value[1].calculate()) 1L else 0L
    }

    class LessThanPacket(version: Int, value: List<Packet>) : OperatorPacket(version, value) {
        override val typeId: Byte = 6
        override fun calculate(): Long = if (value[0].calculate() < value[1].calculate()) 1L else 0L
    }

    class EqualToPacket(version: Int, value: List<Packet>) : OperatorPacket(version, value) {
        override val typeId: Byte = 7
        override fun calculate(): Long = if (value[0].calculate() == value[1].calculate()) 1L else 0L
    }

    override val day: LocalDate = day(16)

    private fun getInput(): CharArray {
        return inputPath.readText()
            .dropLast(1)
            .flatMap { HEX_INV[it.digitToInt(16)].asIterable() }
            .toCharArray()
    }

    private fun CharArray.sliceToInt(indices: IntRange): Int = slice(indices).joinToString("").toInt(2)

    private fun StringBuilder.appendChars(chars: Iterable<Char>): StringBuilder {
        for (c in chars) {
            append(c)
        }
        return this
    }

    private fun parseLiteralPayload(input: CharArray, pos: Int): Pair<Long, Int> {
        val resultNum = StringBuilder()
        var current = pos
        do {
            val lastBlock = input[current]
            resultNum.appendChars(input.slice(current + 1 until current + 5))
            current += 5
        } while (lastBlock != '0')
        return resultNum.toString().toLong(2) to current
    }

    private fun parseOperatorPayload(input: CharArray, pos: Int, isBitLength: Boolean): Pair<List<Packet>, Int> {
        val lenOffset = if (isBitLength) 15 else 11
        val len = input.sliceToInt(pos until pos + lenOffset)
        val res = mutableListOf<Packet>()
        var current = pos + lenOffset
        var count = 0
        while (if (isBitLength) current - (pos + lenOffset) < len else count < len) {
            val (nextPacket, nextPos) = parsePackets(input, current)
            res += nextPacket
            current = nextPos
            ++count
        }
        return res to current
    }

    private fun parseOperatorPayload(input: CharArray, pos: Int, lenType: Char): Pair<List<Packet>, Int> {
        return parseOperatorPayload(input, pos, lenType == '0')
    }

    private fun parsePackets(input: CharArray, pos: Int = 0): Pair<Packet, Int> {
        val version = input.sliceToInt(pos until pos + 3)
        return when (val typeId = input.sliceToInt(pos + 3 until pos + 6)) {
            4 -> {
                val (literal, endPos) = parseLiteralPayload(input, pos + 6)
                LiteralPacket(version, literal) to endPos
            }
            else -> {
                val (payload, endPos) = parseOperatorPayload(input, pos + 7, input[pos + 6])
                when (typeId) {
                    0 -> SumPacket(version, payload)
                    1 -> ProductPacket(version, payload)
                    2 -> MinimumPacket(version, payload)
                    3 -> MaximumPacket(version, payload)
                    5 -> GreaterThanPacket(version, payload)
                    6 -> LessThanPacket(version, payload)
                    7 -> EqualToPacket(version, payload)
                    else -> error("unknown packet type ID: '$typeId")
                } to endPos
            }
        }
    }

    private fun versionSum(rootPacket: Packet): Int {
        return when (rootPacket) {
            is LiteralPacket -> rootPacket.version
            is OperatorPacket -> rootPacket.version + rootPacket.value.sumOf { versionSum(it) }
        }
    }

    override fun executePart1(): Int {
        val (rootPacket, _) = parsePackets(getInput())
        return versionSum(rootPacket)
    }

    override fun executePart2(): Long {
        return parsePackets(getInput()).first.calculate()
    }
}