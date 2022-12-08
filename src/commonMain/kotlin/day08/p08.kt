package day08

import Position
import print
import readInput
import takeUntil

data class Tree(val x: Int, val y: Int, val height: Int, var visible: Boolean)

val p08 = suspend {
    val input = readInput("08.txt").lines()
    val map = map2D(input) { it.toString().toInt() }
    map.positions().count { p ->
        if (p.x == 0 || p.x == map.maxX || p.y == 0 || p.y == map.maxY) {
            true
        } else {
            val (left, right) = map.row(p.y).withIndex().filter { (col, _) -> col != p.x }
                .partition { (col, _) -> col < p.x }
            val (up, down) = map.column(p.x).withIndex().filter { (row, _) -> row != p.y }
                .partition { (row, _) -> row < p.y }

            val treeHeight = map[p]
            fun List<IndexedValue<Int>>.isVisible() = this.all { (_, height) -> height < treeHeight }

            left.isVisible() || right.isVisible() || up.isVisible() || down.isVisible()
        }
    }.print { "Part 1: $it" }

    map.positions().maxOf { p ->
        val (left, right) = map.row(p.y).withIndex().filter { (col, _) -> col != p.x }
            .partition { (col, _) -> col < p.x }
        val (up, down) = map.column(p.x).withIndex().filter { (row, _) -> row != p.y }
            .partition { (row, _) -> row < p.y }

        val treeHeight = map[p]
        fun List<IndexedValue<Int>>.viewDistance() = this.takeUntil { (_, height) -> height >= treeHeight }.count()

        left.reversed().viewDistance() * up.reversed().viewDistance() * right.viewDistance() * down.viewDistance()
    }.print { "Part 2: $it" }

}


inline fun <reified T> map2D(
    input: List<String>,
    wrapX: Boolean = false,
    wrapY: Boolean = false,
    noinline convert: (Char) -> T
) = Map2D(input, wrapX, wrapY, convert)

class Map2D<T>(input: List<String>, val wrapX: Boolean = false, val wrapY: Boolean = false, convert: (Char) -> T) {

    private val map: Array<Array<Any?>> = Array(input.size) { y ->
        (0 until input[y].length).map { convert(input[y][it]) }.toTypedArray()
    }

    val height = input.size
    val width by lazy {
        map.maxOf { it.size }
    }

    val maxY = height - 1
    val maxX by lazy { width - 1 }

    fun print() = this.also {
        map.forEach { println(it.joinToString("")) }
    }

    operator fun get(x: Int, y: Int): T {
        return map[if (wrapY) y.rem(height) else y][if (wrapX) x.rem(width) else x] as T
    }

    operator fun get(p: Position) = this[p.x, p.y]

    fun row(index: Int) = sequence {
        (0 until width).forEach { yield(get(it, index)) }
    }

    fun rows(): Sequence<Sequence<T>> = sequence {
        (0 until height).forEach { yield(row(it)) }
    }

    fun column(index: Int) = sequence {
        (0 until height).forEach { yield(get(index, it)) }
    }

    fun columns(): Sequence<Sequence<T>> = sequence {
        (0 until width).forEach { yield(column(it)) }
    }

    fun positions(): Sequence<Position> =
        sequence { (0 until height).forEach { y -> (0 until width).forEach { x -> yield(Position(x, y)) } } }

}


