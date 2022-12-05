package day05

import print
import readInput

private val moveRegex = Regex("""move (\d*) from (\d*) to (\d*)""")
val p05 = suspend {
    val input = readInput("05.txt")
    val (stacksInput, movesInput) = input.split("[\\r?\\n]{2}".toRegex())
    // Part 1
    var stacks = parseStacks(stacksInput)
    movesInput.lines().forEach { move ->
        val (amount, from, to) = moveRegex.matchEntire(move)!!.destructured.toList().map { it.toInt() }
        repeat(amount) { stacks[to - 1].add(stacks[from - 1].removeLast()) }
    }
    stacks.map { it.last() }.joinToString("").print()


    // Part 2
    stacks = parseStacks(stacksInput)
    movesInput.lines().forEach { move ->
        val (amount, from, to) = moveRegex.matchEntire(move)!!.destructured.toList().map { it.toInt() }
        val cratesToMove = stacks[from - 1].takeLast(amount)
        repeat(amount) { stacks[from - 1].removeLast() }
        stacks[to - 1].addAll(cratesToMove)
    }
    stacks.map { it.last() }.joinToString("").print()

}


fun parseStacks(stacksInput: String): List<MutableList<Char>> {
    val lines = stacksInput.lines()
    val numStacks = lines.last().split(" ").last { it != "" }.toInt().print { "Number of cranes: $it" }
    val stacks = (1..numStacks).map { mutableListOf<Char>() }
    lines.dropLast(1).reversed().forEach { line ->
        (0 until numStacks).forEach { stack ->
            val index = 1 + (stack) * 4
            if (line.length > index + 1) {
                val crate = line[index]
                if (crate != ' ') stacks[stack].add(crate)
            }
        }
    }
    return stacks
}