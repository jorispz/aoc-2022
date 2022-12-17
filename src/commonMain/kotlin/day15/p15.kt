package day15

import Position
import print
import readInput
import without
import kotlin.math.absoluteValue

private val lineRegex = Regex("""Sensor at x=(-?\d*), y=(-?\d*): closest beacon is at x=(-?\d*), y=(-?\d*)""")
val p15 = suspend {
    val input = readInput("15.txt")
    val pairs = input.lines().map { line ->
        val (sensorX, sensorY, beaconX, beaconY) = lineRegex.matchEntire(line)!!.destructured.toList()
            .map { it.toInt() }
        Pair(Position(sensorX, sensorY), Position(beaconX, beaconY))
    }


    //val measureLine = 10
    val measureLine = 2000000

    pairs.map {
        val distanceBetweenSensorAndBeacon = it.first.distanceTo(it.second)
        val distanceToMeasureLine = (it.first.y - measureLine).absoluteValue
        val radiusOnLine = distanceBetweenSensorAndBeacon - distanceToMeasureLine
        if (radiusOnLine < 0) {
            emptySet()
        } else {
            it.print()
            val proposal = ((it.first.x - radiusOnLine)..(it.first.x + radiusOnLine)).toSet()
            (if (it.second.y == measureLine) proposal.without(it.second.x) else proposal)
        }
    }.also { println() }.reduce { acc, intRange -> acc.union(intRange) }.size.print()


}