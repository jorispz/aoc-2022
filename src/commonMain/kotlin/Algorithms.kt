import kotlin.math.min

private data class Vertex<T>(val node: T, var distance: Int = Int.MAX_VALUE)

/**
 * Implementation of Dijkstra's algorithm to find all shortest distances between
 *
 *
 */
fun <T> shortestPathLengths(origin: T, nodes: Collection<T>, adjacentTo: T.(t: T) -> Boolean): List<Pair<T, Int>> {

    val shortest = mutableListOf<Vertex<T>>()
    val source = nodes.mapTo(mutableListOf()) { Vertex(it) }
    source.find { it.node == origin }?.distance = 0

    while (source.isNotEmpty() && source.any { it.distance < Int.MAX_VALUE }) {
        source.sortBy { it.distance }
        val next = source.removeAt(0)
        shortest.add(next)
        source.filter { it.node.adjacentTo(next.node) }.forEach {
            it.distance = min(next.distance + 1, it.distance)
        }
    }
    return shortest.map { Pair(it.node, it.distance) }
}

interface Graph<V : Graph.Vertex> {
    interface Vertex

    fun heuristicDistance(a: V, b: V): Long
    fun weight(a: V, b: V): Long

    fun getNeighbors(v: V): Set<V>
}


/*
This implements the A* algorithm to find the shortest route in a graph of vertices with weighted edges. Note that
this becomes Dijkstra's algorithm when the heuristic function is set to constant 0

Hasn't been properly tested accept for AoC 2022 Day 12, which is Dijkstra
 */
fun <V : Graph.Vertex> findShortestPath(graph: Graph<V>, start: V, end: V): List<V> {

    val cameFrom = mutableMapOf<V, V>()
    val openVertices = mutableSetOf(start)
    val closedVertices = mutableSetOf<V>()
    val costFromStart = mutableMapOf(start to 0L)
    val estimatedTotalCost = mutableMapOf(start to graph.heuristicDistance(start, end))

    while (openVertices.isNotEmpty()) {
        val currentPos = openVertices.minBy { estimatedTotalCost.getValue(it) }
        if (currentPos == end) {
            val path = mutableListOf(currentPos)
            var current = currentPos
            while (cameFrom.containsKey(current)) {
                current = cameFrom.getValue(current)
                path.add(0, current)
            }
            return path.toList()
        }
        openVertices.remove(currentPos)
        closedVertices.add(currentPos)
        graph.getNeighbors(currentPos).filterNot { closedVertices.contains(it) }  // Exclude previous visited vertices
            .forEach { neighbour ->
                val score = costFromStart.getValue(currentPos) + graph.weight(currentPos, neighbour)
                if (score < costFromStart.getOrElse(neighbour) { Long.MAX_VALUE }) {
                    if (!openVertices.contains(neighbour)) {
                        openVertices.add(neighbour)
                    }
                    cameFrom.put(neighbour, currentPos)
                    costFromStart[neighbour] = score
                    estimatedTotalCost[neighbour] = score + graph.heuristicDistance(neighbour, end)
                }
            }
    }
    return emptyList<V>()
}
