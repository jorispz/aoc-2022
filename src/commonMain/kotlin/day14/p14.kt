package day14

import Position
import print
import readInput

enum class Type { AIR, ROCK, SAND }

val p14 = suspend {
    val input = readInput("14.txt").lines()
    val rocks = input
        // Maps to a list of Positions, one for each x,y pair
        .map { line ->
            line.split(" -> ")
                .map { pair -> pair.split(",").map { coordinate -> coordinate.toInt() }.let { Position(it[0], it[1]) } }
        }
        // Add all intermediate positions between line segments and make it a single list
        .flatMap {
            it.windowed(2, 1).flatMap { (from, to) ->
                (from.x interpolate to.x).flatMap { x -> (from.y interpolate to.y).map { y -> Position(x, y) } }
            }
        }
        // Delete the duplicates, because the end of line 1 and the start of line 2 is the same position
        .distinct()

    val maxY = rocks.maxOf { it.y }
    val sands = mutableListOf<Position>()

    fun calculatePath(hasFloor: Boolean = false): Position? {
        val blocked = rocks + sands
        var current = Position(500, 0)

        while (true) {
            val options = listOf(current.down, current.down_left, current.down_right)
            val next = options.firstOrNull { !blocked.contains(it) }
            if (next != null) {
                if (!hasFloor && next.y > maxY) {
                    return null
                }
                if (hasFloor && next.y == maxY + 2) {
                    return current
                }
                current = next
            } else {
                return current
            }
        }
    }

    do {
        val next = calculatePath()?.also { sands.add(it) }
    } while (next != null)
    draw(rocks, sands)
    sands.size.print()

    do {
        val next = calculatePath(true)?.also { sands.add(it) }
    } while (next != Position(500, 0))
    draw(rocks, sands)
    sands.size.print()

}


fun draw(rocks: List<Position>, sand: List<Position>) {
    val allPositions = rocks + sand + Position(500, 0)
    val minX = allPositions.minOf { it.x }
    val maxX = allPositions.maxOf { it.x }
    val minY = allPositions.minOf { it.y }
    val maxY = allPositions.maxOf { it.y }
    val mapString = (0..(maxY - minY)).map { y ->
        (0..(maxX - minX)).map { x ->
            val pos = Position(x + minX, y + minY)
            if (rocks.contains(pos)) '#' else if (sand.contains(pos)) 'o' else '.'
        }.joinToString("")
    }
}


infix fun Int.interpolate(other: Int): IntProgression {
    return if (this <= other) {
        this..other
    } else {
        this downTo other
    }
}