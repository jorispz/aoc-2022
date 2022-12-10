package day10

import print
import readInput
import kotlin.math.absoluteValue

val p10 = suspend {
    val input = readInput("10.txt").lineSequence()

    val output = sequence {
        var cycle = 1
        var register = 1
        for (instruction in input) {
            when (instruction.substring(0, 4)) {
                "noop" -> yield(Pair(cycle++, register))
                "addx" -> {
                    yield(Pair(cycle++, register))
                    yield(Pair(cycle++, register))
                    register += instruction.substring(5).toInt()
                }
                else -> error("")
            }
        }
    }

    output
        .filter { it.first in listOf(20, 60, 100, 140, 180, 220) }
        .sumOf { it.first * it.second }
        .print { "Part 1: $it" }

    output.chunked(40).joinToString("\n") { state ->
        state.joinToString("") {
            val pixelIndex = (it.first - 1) % 40
            if ((pixelIndex - it.second).absoluteValue <= 1) "⚫️" else "⚪️"
        }
    }.print { "Part 2: \n\n$it\n" }

}
