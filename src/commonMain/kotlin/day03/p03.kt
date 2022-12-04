package day03

import print
import readInput;

val p03 = suspend {

    val input = readInput("03.txt")
    input.split("\n")
        .sumOf { it.firstHalf().toSet().intersect(it.secondHalf().toSet()).single().priority() }
        .print { "Part 1: $it" }
    input.split("\n").windowed(3, 3)
        .sumOf { it.map { it.toSet() }.reduce { x, y -> x.intersect(y) }.single().priority() }
        .print { "Part 2: $it" }
}

fun String.firstHalf() = this.substring(0 until this.length / 2)
fun String.secondHalf() = this.substring(this.length / 2)
fun Char.priority() = 1 + (this.lowercaseChar() - 'a') + if (this.isUpperCase()) 26 else 0