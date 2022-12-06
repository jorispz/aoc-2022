package day06

import print
import readInput

val p06 = suspend {
    val input = readInput("06.txt")
    findMarker(input, 4).print { "Part 1: ${it}" }
    findMarker(input, 14).print { "Part 2: ${it}" }
}

// Using input.asSequence here makes it slower, not faster. It is faster though when the answer is
// near the start of the input
fun findMarker(input: String, length: Int) = input.windowed(length).indexOfFirst { it.toSet().size == length } + length