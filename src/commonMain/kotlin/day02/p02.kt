package day02


import print
import readInput

enum class Choice { ROCK, PAPER, SCISSOR }
enum class Result { WIN, LOSE, DRAW }

// 1: 15572
// 2: 16098
val p02 = suspend {
    val input = readInput("02.txt")
    val part1 = input.split("\n").map { line ->
        line.split(" ").map {
            when (it) {
                "A", "X" -> Choice.ROCK
                "B", "Y" -> Choice.PAPER
                "C", "Z" -> Choice.SCISSOR
                else -> error("Parse error")
            }
        }
    }.map { Round(it[0], it[1]) }
    part1.sumOf { it.score }.print { "Part 1: $it" }

    val part2 = input.split("\n").map { line ->
        line.split(" ").map {
            when (it) {
                "A" -> Choice.ROCK
                "B" -> Choice.PAPER
                "C" -> Choice.SCISSOR
                "X" -> Result.LOSE
                "Y" -> Result.DRAW
                "Z" -> Result.WIN
                else -> error("Parse error")
            }
        }
    }.map { Round(it[0] as Choice, it[1] as Result) }
    part2.sumOf { it.score }.print { "Part 2: $it" }
}


class Round {
    private val attack: Choice
    private val defense: Choice
    private val result: Result
    val score
        get() = when (result) {
            Result.LOSE -> 0
            Result.DRAW -> 3
            Result.WIN -> 6
        } + when (defense) {
            Choice.ROCK -> 1
            Choice.PAPER -> 2
            Choice.SCISSOR -> 3
        }

    constructor(first: Choice, second: Choice) {
        this.attack = first
        this.defense = second
        this.result = when (first to second) {
            Choice.ROCK to Choice.ROCK -> Result.DRAW
            Choice.ROCK to Choice.PAPER -> Result.WIN
            Choice.ROCK to Choice.SCISSOR -> Result.LOSE
            Choice.PAPER to Choice.ROCK -> Result.LOSE
            Choice.PAPER to Choice.PAPER -> Result.DRAW
            Choice.PAPER to Choice.SCISSOR -> Result.WIN
            Choice.SCISSOR to Choice.ROCK -> Result.WIN
            Choice.SCISSOR to Choice.PAPER -> Result.LOSE
            Choice.SCISSOR to Choice.SCISSOR -> Result.DRAW
            else -> error("")
        }
    }

    constructor(first: Choice, second: Result) {
        this.attack = first
        this.result = second
        this.defense = when (first to second) {
            Choice.ROCK to Result.LOSE -> Choice.SCISSOR
            Choice.ROCK to Result.DRAW -> Choice.ROCK
            Choice.ROCK to Result.WIN -> Choice.PAPER
            Choice.PAPER to Result.LOSE -> Choice.ROCK
            Choice.PAPER to Result.DRAW -> Choice.PAPER
            Choice.PAPER to Result.WIN -> Choice.SCISSOR
            Choice.SCISSOR to Result.LOSE -> Choice.PAPER
            Choice.SCISSOR to Result.DRAW -> Choice.SCISSOR
            Choice.SCISSOR to Result.WIN -> Choice.ROCK
            else -> error("")
        }
    }

}



