package day04

import containsFully
import overlapsWith
import print
import readInput


// 1: 584
// 2: 933
val p04 = suspend {
    // List of Lists, each containing two IntRanges
    val input = readInput("04.txt").split("\n").map {
        it.split(",").map { it.split("-").map { it.toInt() }.let { it[0]..it[1] } }
    }

    input.count {
        it[0].containsFully(it[1]) || it[1].containsFully(it[0])
    }.print()

    input.count {
        it[0].overlapsWith(it[1])
    }.print()

}

