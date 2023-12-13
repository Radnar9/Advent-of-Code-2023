private const val AOC_DAY = "Day13"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY


/**
 * Finds the reflection line of a pattern by comparing the last line of the first part reversed with the first line
 * of the second part.
 */
fun findReflectionLine(grid: List<String>): Int {
    for (row in 1..<grid.size) {
        val above = grid.subList(0, row).reversed() // Flip the first part so the line matches with the below part.
        val below = grid.subList(row, grid.size)

        val minSize = minOf(above.size, below.size)
        val aboveTrimmed = above.subList(0, minSize)
        val belowTrimmed = below.subList(0, minSize)

        if (aboveTrimmed == belowTrimmed) {
            return row
        }
    }
    return 0
}

/**
 * Finds a new reflection line of a pattern, by fixing the smudge. When the sum of differences is equal to 1 in the
 * entire pattern, it means that the smudge found corresponds the reflection line.
 */
fun findDifferentReflectionLine(grid: List<String>): Int {
    for (row in 1..<grid.size) {
        val above = grid.subList(0, row).reversed()
        val below = grid.subList(row, grid.size)

        // Zip the first line of above with the first of below, then zip the first char of each line and compare them.
        if (above.zip(below).sumOf { pair -> pair.first.zip(pair.second).count { (c1, c2) -> c1 != c2 } } == 1) {
            return row
        }
    }
    return 0
}

/**
 * Finds the reflection line of a pattern, either horizontally or vertically.
 */
private fun part1(input: List<String>): Int {
    val patterns = input.joinToString("\n").split("\n\n").map { it.split("\n") }

    val horizontalSum = patterns.sumOf { findReflectionLine(it) * 100 }
    val verticalSum = patterns.innerTranspose().sumOf { findReflectionLine(it) }

    return horizontalSum + verticalSum
}

/**
 * Finds a new reflection line based on a smudge.
 */
private fun part2(input: List<String>): Int {
    val patterns = input.joinToString("\n").split("\n\n").map { it.split("\n") }

    val horizontalSum = patterns.sumOf { findDifferentReflectionLine(it) * 100 }
    val verticalSum = patterns.innerTranspose().sumOf { findDifferentReflectionLine(it) }

    return horizontalSum + verticalSum
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 405
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 400
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 37025" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 32854" else ""}")
}
