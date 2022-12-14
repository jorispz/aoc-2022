import java.nio.file.Paths
import kotlin.system.measureNanoTime


suspend fun main(args: Array<String>) {
    val repeat = args[0].toInt()
    val day = args[1].toInt()
    run(repeat, day, "JVM")
}

actual inline fun measureNanos(block: () -> Unit) = measureNanoTime(block)

actual fun readInput(name: String) = Paths.get(System.getenv("AOC_INPUT_DIR"), name).toFile().readText()