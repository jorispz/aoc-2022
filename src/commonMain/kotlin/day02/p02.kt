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
        }.let { Pair(it[0], it[1]) }
    }
    part1.sumOf { score(it) }.print { "Part 1: $it" }

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
        }.let { Pair(it[0], it[1]) }
    }
    part2.sumOf { score2(it as Pair<Choice, Result>) }.print { "Part 2: $it" }
}

class Round {
    val attack: Choice
    val defense: Choice
    val result: Result
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
            Choice.PAPER to Choice.ROCK -> Result.DRAW
            Choice.PAPER to Choice.PAPER -> Result.WIN
            Choice.PAPER to Choice.SCISSOR -> Result.LOSE
            Choice.SCISSOR to Choice.ROCK -> Result.DRAW
            Choice.SCISSOR to Choice.PAPER -> Result.WIN
            Choice.SCISSOR to Choice.SCISSOR -> Result.LOSE
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

private fun score2(round: Pair<Choice, Result>): Int = when (round) {
    Pair(Choice.ROCK, Result.WIN) -> score(Pair(Choice.ROCK, Choice.PAPER))
    Pair(Choice.ROCK, Result.LOSE) -> score(Pair(Choice.ROCK, Choice.SCISSOR))
    Pair(Choice.ROCK, Result.DRAW) -> score(Pair(Choice.ROCK, Choice.ROCK))
    Pair(Choice.PAPER, Result.WIN) -> score(Pair(Choice.PAPER, Choice.SCISSOR))
    Pair(Choice.PAPER, Result.LOSE) -> score(Pair(Choice.PAPER, Choice.ROCK))
    Pair(Choice.PAPER, Result.DRAW) -> score(Pair(Choice.PAPER, Choice.PAPER))
    Pair(Choice.SCISSOR, Result.WIN) -> score(Pair(Choice.SCISSOR, Choice.ROCK))
    Pair(Choice.SCISSOR, Result.LOSE) -> score(Pair(Choice.SCISSOR, Choice.PAPER))
    Pair(Choice.SCISSOR, Result.DRAW) -> score(Pair(Choice.SCISSOR, Choice.SCISSOR))
    else -> error("Can't happen")
}

private fun score(round: Pair<Choice, Choice>) = when (round) {
    Pair(Choice.ROCK, Choice.ROCK) -> 1 + 3
    Pair(Choice.ROCK, Choice.PAPER) -> 2 + 6
    Pair(Choice.ROCK, Choice.SCISSOR) -> 3 + 0
    Pair(Choice.PAPER, Choice.ROCK) -> 1 + 0
    Pair(Choice.PAPER, Choice.PAPER) -> 2 + 3
    Pair(Choice.PAPER, Choice.SCISSOR) -> 3 + 6
    Pair(Choice.SCISSOR, Choice.ROCK) -> 1 + 6
    Pair(Choice.SCISSOR, Choice.PAPER) -> 2 + 0
    Pair(Choice.SCISSOR, Choice.SCISSOR) -> 3 + 3
    else -> error("Can't happen")
}


