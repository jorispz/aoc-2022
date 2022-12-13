package day11

import print
import readInput

sealed class Operation {
    class Multiply(val multiplier: Long) : Operation() {
        override operator fun invoke(i: Long) = multiplier * i
        override fun toString() = "times $multiplier"
    }

    class Add(val amount: Long) : Operation() {
        override fun invoke(i: Long) = i + amount
        override fun toString() = "plus $amount"
    }

    object Square : Operation() {
        override fun invoke(i: Long) = i * i
        override fun toString() = "squared"
    }

    abstract operator fun invoke(i: Long): Long
}

data class Monkey(
    val items: MutableList<Long>,
    val operation: Operation,
    val test: Long,
    val trueMonkey: Int,
    val falseMonkey: Int,
    var numInspections: Long = 0L
)

val p11 = suspend {
    val input = readInput("11.txt")
    val monkeys = input.split("""[\r?\n]{2}""".toRegex()).map {
        val lines = it.lines()
        val items = lines[1].substring(18).split(", ").map { it.toLong() }.toMutableList()
        val operation = lines[2].substring(23).let {
            val (op, operand) = it.split(" ")
            when (op) {
                "+" -> Operation.Add(operand.toLong())
                "*" -> if (operand == "old") Operation.Square else Operation.Multiply(operand.toLong())
                else -> error("")
            }
        }
        val test = lines[3].substring(21).toLong()
        val trueMonkey = lines[4].substring(29).toInt()
        val falseMonkey = lines[5].substring(30).toInt()
        Monkey(items, operation, test, trueMonkey, falseMonkey)
    }

//    repeat(20) {
//        monkeys.forEach { monkey ->
//            monkey.items.forEach { item ->
//                val nextItem = monkey.operation(item) / 3
//                if (nextItem % monkey.test == 0L) monkeys[monkey.trueMonkey].items.add(nextItem) else monkeys[monkey.falseMonkey].items.add(nextItem)
//                monkey.numInspections++
//            }
//            monkey.items.clear()
//        }
//
//    }
//    monkeys.map { it.numInspections }.sorted().reversed().take(2).reduce(Long::times).print {"Part 1: $it"}

    val modValue = monkeys.map { it.test }.reduce(Long::times)
    repeat(10_000) {
        monkeys.forEach { monkey ->
            monkey.items.forEach { item ->
                val nextItem = monkey.operation(item) % modValue
                if (nextItem % monkey.test == 0L) monkeys[monkey.trueMonkey].items.add(nextItem) else monkeys[monkey.falseMonkey].items.add(
                    nextItem
                )
                monkey.numInspections++
            }
            monkey.items.clear()
        }

    }
    monkeys.map { it.numInspections }.sorted().reversed().take(2).reduce(Long::times).print { "Part 2: $it" }
}
