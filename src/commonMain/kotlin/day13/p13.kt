package day13

import print
import readInput

val p13 = suspend {
    val input = readInput("13.txt").lines()
    val numPairs = (input.size + 1) / 3
    (1..numPairs).filter { index ->
        val left = parseLine(input[(index - 1) * 3])
        val right = parseLine(input[(index - 1) * 3 + 1])
        left.compareTo(right) == -1
    }.sum().print { "Part 1: $it" }

    val all = (listOf("[[2]]", "[[6]]") + input.filter(String::isNotEmpty)).map(::parseLine).sorted()
    val index2 = 1 + all.withIndex().find { it.value.toString() == "[[2]]" }!!.index
    val index6 = 1 + all.withIndex().find { it.value.toString() == "[[6]]" }!!.index
    (index2 * index6).print { "Part 2: $it" }
}

fun parseLine(line: String): Part.L {
    val packet = mutableListOf<Part.L>()
    val chars = mutableListOf<Char>()
    line.forEach { c ->
        when (c) {
            '[' -> packet.add(Part.L())
            in '0'..'9' -> chars.add(c)
            ',' -> if (chars.isNotEmpty()) packet.last().add(Part.I(chars.joinToString("").toInt()))
                .also { chars.clear() }
            ']' -> {
                val latest = packet.last()
                if (chars.isNotEmpty()) latest.add(Part.I(chars.joinToString("").toInt())).also { chars.clear() }
                if (packet.size > 1) {
                    packet.removeAt(packet.size - 1)
                    packet.last().add(latest)
                }
            }
        }
    }
    return packet[0]
}

sealed class Part : Comparable<Part> {
    class L(val values: MutableList<Part> = mutableListOf()) : Part() {
        fun add(part: Part) {
            values.add(part)
        }

        override fun toString() = values.toString()
    }

    class I(val value: Int) : Part() {
        override fun toString() = value.toString()
    }

    override fun compareTo(right: Part): Int {
        val left = this
        if (left is I && right is I) {
            return left.value.compareTo(right.value)
        } else if (left is I && right is L) {
            return L(mutableListOf(left)).compareTo(right)
        } else if (left is L && right is I) {
            return left.compareTo(L(mutableListOf(right)))
        } else {
            val pairs = (left as L).values.zip((right as L).values)
            pairs.forEach {
                val order = it.first.compareTo(it.second)
                if (order == -1) {
                    return -1
                } else if (order == 1) {
                    return 1
                }
            }
            return left.values.size.compareTo(right.values.size)
        }
    }
}