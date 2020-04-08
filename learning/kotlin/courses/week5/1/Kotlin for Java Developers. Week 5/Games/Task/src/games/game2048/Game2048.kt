package games.game2048

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Your task is to implement the game 2048 https://en.wikipedia.org/wiki/2048_(video_game).
 * Implement the utility methods below.
 *
 * After implementing it you can try to play the game running 'PlayGame2048'.
 */
fun newGame2048(initializer: Game2048Initializer<Int> = RandomGame2048Initializer): Game =
    Game2048(initializer)

class Game2048(private val initializer: Game2048Initializer<Int>) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        repeat(2) {
            board.addNewValue(initializer)
        }
    }

    override fun canMove() = board.any { it == null }

    override fun hasWon() = board.any { it == 2048 }

    override fun processMove(direction: Direction) {
        if (board.moveValues(direction)) {
            board.addNewValue(initializer)
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}

/*
 * Add a new value produced by 'initializer' to a specified cell in a board.
 */
fun GameBoard<Int?>.addNewValue(initializer: Game2048Initializer<Int>) {
    val a = initializer.nextValue(this) ?: throw Exception("new elements can't be added to a full board")
    set(a.first, a.second)
//    print()
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" in a specified rowOrColumn only.
 * Use the helper function 'moveAndMergeEqual' (in Game2048Helper.kt).
 * The values should be moved to the beginning of the row (or column),
 * in the same manner as in the function 'moveAndMergeEqual'.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValuesInRowOrColumn(rowOrColumn: List<Cell>): Boolean {
//    println("$rowOrColumn")
    val old = rowOrColumn.map { get(it) }
//    print()
//    println("----------------")
    val a = old.moveAndMergeEqual { it * 2 }

    (1..4).map {
        if (it <= a.count()) {
            set(rowOrColumn[it - 1], a[it - 1])
        } else {
            set(rowOrColumn[it - 1], null)
        }
    }
    val new = rowOrColumn.map { get(it) }

//    print()
//    println("$old == $new")
//    println("===============")
    return old != new
}

/*
 * Update the values stored in a board,
 * so that the values were "moved" to the specified direction
 * following the rules of the 2048 game .
 * Use the 'moveValuesInRowOrColumn' function above.
 * Return 'true' if the values were moved and 'false' otherwise.
 */
fun GameBoard<Int?>.moveValues(direction: Direction): Boolean {
    return when (direction) {
        Direction.UP -> {
//            moveValuesInRowOrColumn(getColumn(1..4, 1)) || moveValuesInRowOrColumn(getColumn(1..4, 2)) || moveValuesInRowOrColumn(getColumn(1..4, 3)) || moveValuesInRowOrColumn(getColumn(1..4, 4))
            val a = moveValuesInRowOrColumn(getColumn(1..4, 1))
            val b = moveValuesInRowOrColumn(getColumn(1..4, 2))
            val c = moveValuesInRowOrColumn(getColumn(1..4, 3))
            val d = moveValuesInRowOrColumn(getColumn(1..4, 4))
            a || b || c || d
        }
        Direction.LEFT -> {
//            moveValuesInRowOrColumn(getRow(1, 1..4)) || moveValuesInRowOrColumn(getRow(2, 1..4)) || moveValuesInRowOrColumn(getRow(3, 1..4)) || moveValuesInRowOrColumn(getRow(4, 1..4))
            val a = moveValuesInRowOrColumn(getRow(1, 1..4))
            val b = moveValuesInRowOrColumn(getRow(2, 1..4))
            val c = moveValuesInRowOrColumn(getRow(3, 1..4))
            val d = moveValuesInRowOrColumn(getRow(4, 1..4))
            a || b || c || d
        }
        Direction.DOWN -> {
//            moveValuesInRowOrColumn(getColumn(4 downTo 1, 1)) || moveValuesInRowOrColumn(getColumn(4 downTo 1, 2)) || moveValuesInRowOrColumn(getColumn(4 downTo 1, 3)) || moveValuesInRowOrColumn(getColumn(4 downTo 1, 4))
            val a = moveValuesInRowOrColumn(getColumn(4 downTo 1, 1))
            val b = moveValuesInRowOrColumn(getColumn(4 downTo 1, 2))
            val c = moveValuesInRowOrColumn(getColumn(4 downTo 1, 3))
            val d = moveValuesInRowOrColumn(getColumn(4 downTo 1, 4))
            a || b || c || d
        }
        Direction.RIGHT -> {
//            moveValuesInRowOrColumn(getRow(1, 4 downTo 1)) || moveValuesInRowOrColumn(getRow(2, 4 downTo 1)) || moveValuesInRowOrColumn(getRow(3, 4 downTo 1)) || moveValuesInRowOrColumn(getRow(4, 4 downTo 1))
            val a = moveValuesInRowOrColumn(getRow(1, 4 downTo 1))
            val b = moveValuesInRowOrColumn(getRow(2, 4 downTo 1))
            val c = moveValuesInRowOrColumn(getRow(3, 4 downTo 1))
            val d = moveValuesInRowOrColumn(getRow(4, 4 downTo 1))
            a || b || c || d
        }
    }
}

fun GameBoard<Int?>.print() {
    this.getAllCells().forEachIndexed { i, v ->
        if ((i + 1) != 1 && (i + 1) % 4 == 0) {
            println("${get(v)}")
        } else {
            print("${get(v)} ")
        }
    }
}