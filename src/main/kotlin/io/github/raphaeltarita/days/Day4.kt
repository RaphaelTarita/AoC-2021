package io.github.raphaeltarita.days

import io.github.raphaeltarita.structure.AoCDay
import io.github.raphaeltarita.util.day
import io.github.raphaeltarita.util.inputPath
import io.github.raphaeltarita.util.without
import kotlinx.datetime.LocalDate
import kotlin.io.path.readLines

object Day4 : AoCDay {
    override val day: LocalDate = day(4)

    private fun getCalls(): List<Int> {
        return inputPath.readLines()
            .first()
            .split(',')
            .map { it.toInt() }
    }

    private fun getBoards(): List<List<List<Int>>> {
        val boards = mutableListOf<List<List<Int>>>()
        var currentBoard = mutableListOf<List<Int>>()
        for (line in inputPath.readLines().drop(2)) {
            if (line.isBlank()) {
                boards += currentBoard
                currentBoard = mutableListOf()
            } else {
                currentBoard += line.trim().split(Regex("\\s+"))
                    .map { it.toInt() }
            }
        }
        boards += currentBoard
        return boards
    }

    private fun markBoard(call: Int, board: List<List<Int>>, hitlist: List<MutableList<Boolean>>) {
        for ((x, row) in board.withIndex()) {
            for ((y, num) in row.withIndex()) {
                if (call == num) {
                    hitlist[x][y] = true
                }
            }
        }
    }

    private fun checkHitlist(hitlist: List<List<Boolean>>): Boolean {
        for (diag in hitlist.indices) {
            if (hitlist[diag].all { it }) return true
            if (hitlist.all { it[diag] }) return true
        }
        return false
    }

    private fun getResult(call: Int, board: List<List<Int>>, hitlist: List<List<Boolean>>): Int {
        var sum = 0
        for ((x, row) in board.withIndex()) {
            for ((y, num) in row.withIndex()) {
                if (!hitlist[x][y]) sum += num
            }
        }
        return sum * call
    }

    override fun executePart1(): Int {
        val calls = getCalls()
        val boards = getBoards()
        val hitlists = List(boards.size) {
            List(5) {
                MutableList(5) { false }
            }
        }

        for (call in calls) {
            for ((idx, board) in boards.withIndex()) {
                markBoard(call, board, hitlists[idx])
                if (checkHitlist(hitlists[idx])) {
                    return getResult(call, board, hitlists[idx])
                }
            }
        }
        error("no bingo happened")
    }

    override fun executePart2(): Int {
        val calls = getCalls()
        var boards = getBoards()
        var hitlists = List(boards.size) {
            List(5) {
                MutableList(5) { false }
            }
        }
        var lastBingoCall: Int = -1
        var lastBoard: List<List<Int>> = emptyList()
        var lastHitlist: List<List<Boolean>> = emptyList()

        for (call in calls) {
            var idx = 0
            while (idx < boards.size) {
                val board = boards[idx]
                val hitlist = hitlists[idx]
                markBoard(call, board, hitlist)
                if (checkHitlist(hitlist)) {
                    lastBingoCall = call
                    lastBoard = board
                    lastHitlist = hitlist

                    boards = boards.without(idx)
                    hitlists = hitlists.without(idx)
                } else {
                    ++idx
                }
            }
        }

        return getResult(lastBingoCall, lastBoard, lastHitlist)
    }
}