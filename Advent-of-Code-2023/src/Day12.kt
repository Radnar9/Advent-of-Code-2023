private const val AOC_DAY = "Day12"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY

/**
 * Hashmap used for memoization, to avoid recalculating the same values.
 */
private val cache = hashMapOf<Pair<String, List<Int>>, Long>()
fun count(springs: String, springsSize: List<Int>): Long {
    if (springs == "") return if (springsSize.isEmpty()) 1 else 0
    if (springsSize.isEmpty()) return if (springs.contains("#")) 0 else 1

    val pair = Pair(springs, springsSize)
    if (pair in cache) return cache[pair]!!

    var count = 0L
    if (springs[0] in ".?") {
        count += count(springs.substring(1), springsSize)
    }
    if (springs[0] in "#?") {
        if (springsSize[0] <= springs.length && "." !in springs.substring(0, springsSize[0]) &&
            (springsSize[0] == springs.length || springs[springsSize[0]] != '#'))
        {
            val subSpring = if (springsSize[0] + 1 >= springs.length) "" else springs.substring(springsSize[0] + 1)
            count += count(subSpring, springsSize.drop(1))
        }
    }
    cache[pair] = count
    return count
}

private fun part1(input: List<String>): Long {
    val parsedInput = input.map { it.split(" ") }
    val springs = parsedInput.map { it.first() }
    val springsSize = parsedInput.map { it.last().split(",").map { num -> num.toInt() } }

    return springsSize.zip(springs).sumOf { (size, spring) -> count(spring, size) }
}

private fun part2(input: List<String>): Long {
    val parsedInput = input.map { it.split(" ") }
    val springs = parsedInput.map { "${it.first()}?".repeat(5) }.map { it.dropLast(1) }
    val springsSize = parsedInput.map { "${it.last()},".repeat(5).split(",").dropLast(1).map { num -> num.toInt() } }

    return springsSize.zip(springs).sumOf { (size, spring) -> count(spring, size) }
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 21
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 525152
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 7771" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 10861030975833" else ""}")
}
