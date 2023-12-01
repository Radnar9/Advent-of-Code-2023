import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file into a list of strings.
 */
fun readInputToList(name: String) = Path("src/$name.txt").readLines() //.readText()

/**
 * Reads lines from the given input txt file into a single string.
 */
fun readInputToString(name: String) = Path("src/$name.txt").readText()

/**
 * Converts string to md5 hash.
 * - It's an example of how to create extension functions
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)