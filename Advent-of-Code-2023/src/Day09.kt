private const val AOC_DAY = "Day09"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY


/**
 * Gets the next backwards or forwards value in the sequence. First is calculated the difference between each number in
 * the sequence, then the next value is calculated recursively.
 */
private fun getNextValue(diffs: List<Int>, backwards: Boolean = false): Int {
    if (diffs.all { it == 0 }) return 0
    val nextDiffs = diffs.zipWithNext().map { it.second - it.first }
    val diff = getNextValue(nextDiffs, backwards)
    return if (backwards) (diffs.first() - diff) else (diffs.last() + diff)
}

/**
 * Calculates the next value in the sequence by calculating the difference between each number, until all numbers are 0.
 * Afterward, the next value of each line of the differences is calculated by summing the last value of the previous
 * line with the resulting difference of the current line.
 */
private fun part1(input: List<String>): Int {
    val oasis = input.map { it.split(" ").map { num -> num.toInt() } }
    return oasis.sumOf { history -> getNextValue(history) }
}

/**
 * Instead calculates the previous value in the sequence.
 */
private fun part2(input: List<String>): Int {
    val oasis = input.map { it.split(" ").map { num -> num.toInt() } }
    return oasis.sumOf { history -> getNextValue(history, backwards = true) }
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 114
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 2
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 2101499000" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 1089" else ""}")
}
