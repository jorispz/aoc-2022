import kotlin.math.abs
import kotlin.math.sign

inline fun <T> T.print(msg: (T) -> Any? = { it }): T = this.apply { println(msg(this)) }

fun String.sorted(): String = this.toList().sorted().joinToString()


fun <T> Sequence<T>.without(element: T): Sequence<T> = this.filter { it != element }
fun <T> Sequence<T>.pairs(pairWithSelf: Boolean = true, includeMirrors: Boolean = true): Sequence<Pair<T, T>> {
    var indexFirst = 0
    var indexSecond: Int
    return sequence {
        this@pairs.iterator().forEach { first ->
            val other = if (includeMirrors) {
                indexSecond = 0
                this@pairs.iterator()
            } else {
                indexSecond = indexFirst
                this@pairs.drop(indexFirst).iterator()
            }

            other.iterator().forEach { second ->
                if (indexFirst != indexSecond || pairWithSelf) {
                    yield(Pair(first, second))
                }
                indexSecond++
            }
            indexFirst++
        }
    }
}

fun <T> Iterable<T>.pairs(pairWithSelf: Boolean = true, includeMirrors: Boolean = true): Sequence<Pair<T, T>> {
    var indexFirst = 0
    var indexSecond: Int
    return sequence {
        this@pairs.iterator().forEach { first ->
            val other = if (includeMirrors) {
                indexSecond = 0
                this@pairs.iterator()
            } else {
                indexSecond = indexFirst
                this@pairs.drop(indexFirst).iterator()
            }

            other.iterator().forEach { second ->
                if (indexFirst != indexSecond || pairWithSelf) {
                    yield(Pair(first, second))
                }
                indexSecond++
            }
            indexFirst++
        }
    }
}

inline fun <T> Sequence<T>.countLong(predicate: (T) -> Boolean): Long {
    var count = 0L
    for (element in this) if (predicate(element)) ++count
    return count
}

fun <T> Collection<T>.without(element: T) = this.filter { it != element }

fun <T> List<T>.replaceElementAt(index: Int, newValue: T): List<T> =
    this.toMutableList().apply { this[index] = newValue }

fun <T> Collection<T>.whenNotEmpty(block: (Collection<T>) -> Unit) {
    if (this.isNotEmpty()) {
        block(this)
    }
}

fun <T : Comparable<T>> Iterable<T>.maxWithIndex(): IndexedValue<T>? = this.withIndex().maxByOrNull { it.value }

fun <T> MutableSet<T>.takeFirst(): T = this.first().also { remove(it) }
//fun <T> MutableList<T>.takeFirst(): T = this.first().also { removeAt(0) }

data class Vector(val dx: Int, val dy: Int) {
    val sign by lazy {
        Vector(dx.sign, dy.sign)
    }
}

data class Position(val x: Int, val y: Int) : Graph.Vertex {

    companion object {
        val ORIGIN = Position(0, 0)
    }

    val up by lazy { Position(x, y - 1) }
    val up_right by lazy { Position(x + 1, y - 1) }
    val right by lazy { Position(x + 1, y) }
    val down_right by lazy { Position(x + 1, y + 1) }
    val down by lazy { Position(x, y + 1) }
    val down_left by lazy { Position(x - 1, y + 1) }
    val left by lazy { Position(x - 1, y) }
    val up_left by lazy { Position(x - 1, y - 1) }
    val adjacent by lazy { setOf(left, up, right, down) }
    val adjacentWithDiagonals by lazy { setOf(left, up_left, up, up_right, right, down_right, down, down_left) }

    fun move(dx: Int = 0, dy: Int = 0) = Position(this.x + dx, this.y + dy)
    operator fun plus(v: Vector) = Position(this.x + v.dx, this.y + v.dy)
    operator fun minus(other: Position) = Vector(this.x - other.x, this.y - other.y)

    fun headingTo(other: Position) = when (other) {
        this.up -> Heading.N
        this.down -> Heading.S
        this.left -> Heading.W
        this.right -> Heading.E
        else -> throw IllegalArgumentException("Not adjacent")
    }

    fun headingFrom(other: Position) = headingTo(other).reverse()

    fun move(h: Heading) = when (h) {
        Heading.N -> up
        Heading.S -> down
        Heading.W -> left
        Heading.E -> right
    }

    fun distanceTo(other: Position) = abs(x - other.x) + abs((y - other.y))

    fun adjacentTo(other: Position) = abs(x - other.x) <= 1 && abs(y - other.y) <= 1
    fun notAdjacentTo(other: Position) = !adjacentTo(other)

    fun closest(a: Position, b: Position): Position {
        val distA = this.distanceTo(a)
        val distB = this.distanceTo(b)
        return if (distB < distA) b else a
    }


}

class Map2D<T>(
    input: List<String>,
    val wrapX: Boolean = false,
    val wrapY: Boolean = false,
    convert: (Char, Position) -> T
) {

    private val map: Array<Array<Any?>> = Array(input.size) { y ->
        (0 until input[y].length).map { convert(input[y][it], Position(it, y)) }.toTypedArray()
    }

    val height = input.size
    val width by lazy {
        map.maxOf { it.size }
    }

    val maxY = height - 1
    val maxX by lazy { width - 1 }

    fun print() = this.also {
        map.forEach { println(it.joinToString("")) }
    }

    fun contains(p: Position): Boolean {
        val containsX = wrapX || p.x in (0..maxX)
        val containsY = wrapY || p.y in (0..maxY)
        return containsX && containsY
    }

    operator fun get(x: Int, y: Int): T {
        return map[if (wrapY) y.rem(height) else y][if (wrapX) x.rem(width) else x] as T
    }

    operator fun get(p: Position) = this[p.x, p.y]

    fun row(index: Int) = sequence {
        (0 until width).forEach { yield(get(it, index)) }
    }

    fun rows(): Sequence<Sequence<T>> = sequence {
        (0 until height).forEach { yield(row(it)) }
    }

    fun column(index: Int) = sequence {
        (0 until height).forEach { yield(get(index, it)) }
    }

    fun columns(): Sequence<Sequence<T>> = sequence {
        (0 until width).forEach { yield(column(it)) }
    }

    fun positions(): Sequence<Position> =
        sequence { (0 until height).forEach { y -> (0 until width).forEach { x -> yield(Position(x, y)) } } }

    fun neighbors(p: Position, includeDiagonals: Boolean = false): Set<Position> {
        val adjacent = if (includeDiagonals) p.adjacentWithDiagonals else p.adjacent
        return adjacent.filterTo(HashSet()) { this.contains(it) }
    }

    fun find(value: T) = this.positions().filter { this[it] == value }.toSet()

}




operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(this.first + other.first, this.second + other.second)
operator fun Triple<Int, Int, Int>.plus(other: Triple<Int, Int, Int>) =
    Triple(this.first + other.first, this.second + other.second, this.third + other.third)

enum class Turn {
    LEFT, STRAIGHT, RIGHT, REVERSE
}

enum class Heading {
    N, S, E, W;

    companion object {
        fun from(s: String) = when (s.uppercase()) {
            "N" -> N
            "U" -> N
            "S" -> S
            "D" -> S
            "W" -> W
            "L" -> W
            "E" -> E
            "R" -> E
            else -> throw IllegalArgumentException()
        }
    }

    fun right(): Heading = when (this) {
        N -> E
        E -> S
        S -> W
        W -> N
    }

    fun left(): Heading = when (this) {
        N -> W
        W -> S
        S -> E
        E -> N
    }

    fun reverse(): Heading = when (this) {
        N -> S
        S -> N
        E -> W
        W -> E
    }
}

data class BoundingBox(val topLeft: Position, val bottomRight: Position) {
    val width = 1 + bottomRight.x - topLeft.x
    val height = 1 + topLeft.y - bottomRight.y

    fun render(block: (Position) -> Char) {
        println()
        (bottomRight.y..topLeft.y).map { y ->
            (topLeft.x..bottomRight.x).map { x ->
                block(Position(x, y))
            }.joinToString("").print()
        }
        println()
    }
}

fun Collection<Position>.boundingBox(): BoundingBox {
    val minX = this.minByOrNull { it.x }?.x!!
    val maxX = this.maxByOrNull { it.x }?.x!!
    val minY = this.minByOrNull { it.y }?.y!!
    val maxY = this.maxByOrNull { it.y }?.y!!
    return BoundingBox(Position(minX, maxY), Position(maxX, minY))
}


fun <T> Sequence<T>.infinite() = sequence {
    while (true) {
        yieldAll(this@infinite)
    }
}

fun IntRange.permute() = this.toList().permute()
fun IntRange.containsFully(other: IntRange) = this.contains(other.first) && this.contains(other.last)
fun IntRange.overlapsWith(other: IntRange) =
    this.contains(other.first) || this.contains(other.last) || other.contains(this.first) || other.contains(this.last)


fun <T> List<T>.permute(): List<List<T>> {
    if (this.size == 1) return listOf(this)
    val perms = mutableListOf<List<T>>()
    val toInsert = this[0]
    for (perm in this.drop(1).permute()) {
        for (i in 0..perm.size) {
            val newPerm = perm.toMutableList()
            newPerm.add(i, toInsert)
            perms.add(newPerm)
        }
    }
    return perms
}

tailrec fun gcd(a: Int, b: Int): Int {
    return if (b == 0) {
        a
    } else {
        gcd(b, a % b)
    }
}

tailrec fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) {
        a
    } else {
        gcd(b, a % b)
    }
}

