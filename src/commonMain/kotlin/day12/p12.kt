package day12

import Map2D
import Position
import print
import readInput

val p012 = suspend {
    val input = readInput("12-test.txt").lines()
    val goals = Array(2) { Position(0, 0) }
    val map = Map2D(input) { c, p ->
        when (c) {
            'S' -> 'a'.also { goals[0] = p }
            'E' -> 'z'.also { goals[1] = p }
            else -> c
        } - 'a'
    }.print()
    val (start, goal) = goals

    val openVertices = mutableMapOf<Position, Int>().also { it[start] = 0 }
    val closedVertices = mutableMapOf<Position, Int>()
    val cameFrom = mutableMapOf<Position, Position>()

    while (openVertices.isNotEmpty()) {
        val (currentPos, currentValue) = openVertices.minBy { it.value }

        if (currentPos == goal) {
            val path = mutableListOf(currentPos)
            var current = currentPos
            while (cameFrom.containsKey(current)) {
                current = cameFrom.getValue(current)
                path.add(0, current)
            }
            path.toList().print()
            break
        }

        openVertices.remove(currentPos)
        closedVertices[currentPos] = currentValue
        (map.neighbors(currentPos) - closedVertices.keys).forEach { neighbor ->

        }


    }


}