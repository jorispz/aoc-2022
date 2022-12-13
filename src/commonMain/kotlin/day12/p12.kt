package day12

import Graph
import Map2D
import Position
import findShortestPath
import print
import readInput

val p012 = suspend {
    val input = readInput("12.txt").lines()
    val goals = Array(2) { Position(0, 0) }
    val map = Map2D(input) { c, p ->
        when (c) {
            'S' -> 'a'.also { goals[0] = p }
            'E' -> 'z'.also { goals[1] = p }
            else -> c
        }
    }

    val graph: Graph<Position> = object : Graph<Position> {
        // Make it Dijkstra instead of A*
        override fun heuristicDistance(a: Position, b: Position) = 0L
        override fun weight(a: Position, b: Position) = if ((map[b] - map[a]) <= 1) 0 else Long.MAX_VALUE
        override fun getNeighbors(v: Position) = map.neighbors(v)
    }

    val path = findShortestPath(graph, goals[0], goals[1])
    path.size.print { "Part 1: ${it - 1}" }

    map.find('a')
        .map { findShortestPath(graph, it, goals[1]).size }
        .filter { it > 0 }
        .min()
        .print { "Part 2: ${it - 1}" }

}

