package board

import board.Direction.*
import java.util.*
import kotlin.collections.LinkedHashMap

fun createBoard(width: Int): Map<Int, List<Cell>> {
    val map = LinkedHashMap<Int, List<Cell>>()
    for (i in 1..width) {
        val list = LinkedList<Cell>()
        for (j in 1..width) {
            list.add(Cell(i, j))
        }
        map[i] = list
    }
    return map
}

fun createSquareBoard(width: Int): SquareBoard {
    return SquareBoardImp(width, createBoard(width))
}

fun <T> createGameBoard(width: Int): GameBoard<T> {
    return GameBoardImp(width, createBoard(width))
}

open class SquareBoardImp(override val width: Int, val map: Map<Int, List<Cell>>) : SquareBoard {

    override fun getCellOrNull(i: Int, j: Int): Cell? {
        return map[i]?.firstOrNull { it.j == j }
    }

    override fun getCell(i: Int, j: Int): Cell {
        return getCellOrNull(i, j) ?: throw IllegalArgumentException("index out of range.")
    }

    override fun getAllCells(): Collection<Cell> {
        return map.flatMap { it.value }
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
//        return map[i]?.filter{ jRange.contains(it.j)}.orEmpty()
        return jRange.mapNotNull { getCellOrNull(i, it) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        return iRange.mapNotNull { getCellOrNull(it, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            UP -> getCellOrNull(this.i - 1, this.j)
            LEFT -> getCellOrNull(this.i, this.j - 1)
            DOWN -> getCellOrNull(this.i + 1, this.j)
            RIGHT -> getCellOrNull(this.i, this.j + 1)
        }
    }
}

class GameBoardImp<T>(width: Int, map: Map<Int, List<Cell>>) : GameBoard<T>, SquareBoardImp(width, map) {
    private val lhm = LinkedHashMap<Cell, T?>()

    init {
        map.values.flatten().forEach {
            lhm[it] = null
        }
    }

    override fun get(cell: Cell): T? {
        return lhm[cell]
    }

    override fun set(cell: Cell, value: T?) {
        lhm[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> {
        return lhm.filterValues(predicate).keys
    }

    override fun find(predicate: (T?) -> Boolean): Cell? {
        return filter(predicate).first()
    }

    override fun any(predicate: (T?) -> Boolean): Boolean {
        return lhm.values.any(predicate)
    }

    override fun all(predicate: (T?) -> Boolean): Boolean {
        return lhm.values.all(predicate)
    }

}

