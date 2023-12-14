import kotlin.io.path.Path
import kotlin.io.path.createFile
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Creates the input test files for the given day.
 */
fun createTestFiles(day: String) {
    try {
        Path("src/$day.txt").createFile()
        Path("src/${day}_test.txt").createFile()
        Path("src/${day}_test_part1.txt").createFile()
        Path("src/${day}_test_part2.txt").createFile()
    } catch (e: Exception) {
//        println("Test files already exist!")
    }
}

/**
 * Reads lines from the given input txt file into a list of strings.
 */
fun readInputToList(name: String) = Path("src/$name.txt").readLines()

/**
 * Reads lines from the given input txt file into a single string.
 */
fun readInputToString(name: String) = Path("src/$name.txt").readText()

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/**
 * Verifies if the [Char] is a symbol that is not a dot.
 */
fun Char.isSymbolNotDot(): Boolean {
    return !this.isLetterOrDigit() && this != '.'
}

/**
 * Verifies if the [Char] is an asterisk.
 */
fun Char.isAsterisk(): Boolean {
    return this == '*'
}

/**
 * Calculates the Greatest Common Divisor of two [Long] numbers.
 */
fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

/**
 * Calculates the Least Common Multiple of a list of [Long] numbers.
 */
fun lcm(numbers: List<Long>): Long {
    var lcm = numbers.first()
    for (i in 1..<numbers.size) {
        lcm = lcm * numbers[i] / gcd(lcm, numbers[i])
    }
    return lcm
}

/**
 * Given a list of lists, transpose the inner lists.
 */
fun List<List<String>>.innerTranspose(): List<List<String>> {
    return this.map { patterns ->
        patterns[0].indices.map { x ->
            patterns.indices.joinToString("") { y ->
                patterns[y][x].toString()
            }
        }
    }
}

/**
 * Transpose a list.
 */
fun List<String>.transpose(): List<String> {
    return this[0].indices.map { x ->
        this.indices.joinToString("") { y ->
            this[y][x].toString()
        }
    }
}