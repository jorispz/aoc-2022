package day08

import Map2D
import print
import readInput

data class Tree(val x: Int, val y: Int, val height: Int, var visible: Boolean)

val p08 = suspend {
    val input = readInput("08.txt").lines()
    val map = Map2D(input) { it.digitToInt() }

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

// Takes items from the list up to and including the first item for which the predicate fails
private inline fun <T> Iterable<T>.takeUntil(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (predicate(item)) {
            break
        }
    }
    return list
}

