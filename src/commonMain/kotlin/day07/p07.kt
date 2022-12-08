package day07

import print
import readInput

sealed interface FSItem {

    val name: String
    val size: Long

    data class Directory(override val name: String, val items: MutableMap<String, FSItem> = mutableMapOf()) : FSItem {
        override val size: Long
            get() = items.values.sumOf { it.size }

        fun mkDir(name: String): Directory = items.getOrPut(name) { Directory(name) } as Directory

        fun mkFile(name: String, size: Long) = items.put(name, File(name, size))

        fun searchDirectories(selector: (Directory) -> Boolean): List<Directory> {
            val result = mutableListOf<Directory>()
            if (selector(this)) result.add(this)
            result.addAll(items.values.filterIsInstance<Directory>().flatMap { it.searchDirectories(selector) })
            return result
        }

    }

    data class File(override val name: String, override val size: Long) : FSItem
}

// Part 1: 1648397
// Part 2: 1815525

val p07 = suspend {
    val root = FSItem.Directory("/")
    val current: MutableList<FSItem.Directory> = mutableListOf(root)

    val input = readInput("07.txt").lines()
    input.forEach { line ->
        if (line.startsWith("$")) {
            val command = line.substring(2)
            if (command.startsWith("cd")) {
                when (val navTarget = command.substring(3)) {
                    "/" -> current.clear().also { current.add(root) }
                    ".." -> current.removeLast()
                    else -> current.add(current.last().mkDir(navTarget))
                }
            }
        } else {
            if (line.startsWith("dir")) {
                val name = line.substring(4)
                current.last().mkDir(name)
            } else {
                val size = line.substringBefore(" ").toLong()
                val name = line.substringAfter(" ")
                current.last().mkFile(name, size)
            }
        }
    }
    root.searchDirectories { it.size <= 100000 }.sumOf { it.size }.print { "Part 1: $it" }

    val totalSpace = 70000000L
    val currentSpace = root.size
    val unusedSpace = totalSpace - currentSpace
    val toDelete = 30000000L - unusedSpace

    root.searchDirectories { it.size >= toDelete }.minBy { it.size }.print { "Part 2: ${it.size}" }

}