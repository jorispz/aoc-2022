package day1

import print
import readInput

// 1:  74394
// 2: 212836
val p01 = suspend {
    val input = readInput("01.txt")

    val weightsPerElf = input.split("""[\r?\n]{2}""".toRegex()).map { it.split("\n").map { it.toInt() } }
    weightsPerElf.maxOf { it.sum() }.print { "Part 1: $it" }
    weightsPerElf.map { it.sum() }.sorted().takeLast(3).sum().print { "Part 2: $it" }
}
