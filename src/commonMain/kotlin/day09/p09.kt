package day09

import Heading
import Position
import print
import readInput

val p09 = suspend {
    val input = readInput("09.txt")
    solve(input, 2).print { "Part 1: $it" }
    solve(input, 10).print { "Part 2: $it" }
}

private fun solve(input: String, numKnots: Int): Int {
    val knots = Array(numKnots) { Position(0, 0) }
    val visited = hashSetOf(knots[0])
    input.lines().forEach { step ->
        val heading = when (step.substring(0, 1)) {
            "U" -> Heading.N
            "L" -> Heading.W
            "R" -> Heading.E
            "D" -> Heading.S
            else -> error("")
        }

        val numSteps = step.substring(2).toInt()
        repeat(numSteps) {
            knots[numKnots - 1] = knots[numKnots - 1].move(heading)
            (numKnots - 2 downTo 0).forEach {
                if (knots[it + 1].notAdjacentTo(knots[it])) {
                    knots[it] = knots[it] + (knots[it + 1] - knots[it]).sign
                }
            }
            visited.add(knots[0])
        }
    }
    return visited.size

}