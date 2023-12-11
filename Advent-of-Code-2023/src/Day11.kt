import kotlin.math.max
import kotlin.math.min

private const val AOC_DAY = "Day11"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY


private const val EXPANDED_TWICE = 2
private const val EXPANDED_MILLION = 1_000_000
private data class Galaxy(val row: Int, val col: Int)

/**
 * Finds the shortest path between every different pair of galaxies and sums them up.
 * In case of empty rows or columns, the path is expanded by the given [expansion] factor.
 */
private fun calculateSumOfPaths(grid: List<String>, expansion: Int): Long {
    val emptyRows = grid.indices.filter { y -> grid[0].indices.all { x -> grid[y][x] == '.' } }.toSet()
    val emptyCols = grid[0].indices.filter { x -> grid.indices.all { y -> grid[y][x] == '.' } }.toSet()

    val galaxies = buildList {
        for (row in grid.indices) {
            for (col in grid[row].indices) {
                if (grid[row][col] == '#') add(Galaxy(row, col))
            }
        }
    }

    var sum = 0L
    for (i in galaxies.indices) {
        val galaxy1 = galaxies[i]
        for (j in i + 1..<galaxies.size) {
            val galaxy2 = galaxies[j]
            for (row in min(galaxy1.row, galaxy2.row)..<max(galaxy1.row, galaxy2.row)) {
                sum += if (row in emptyRows) expansion else 1
            }
            for (col in min(galaxy1.col, galaxy2.col)..<max(galaxy1.col, galaxy2.col)) {
                sum += if (col in emptyCols) expansion else 1
            }
        }
    }

    return sum
}

private fun part1(input: List<String>): Long {
    return calculateSumOfPaths(input, EXPANDED_TWICE)
}

private fun part2(input: List<String>): Long {
    return calculateSumOfPaths(input, EXPANDED_MILLION)
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 374
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 82000210
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 10276166" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 598693078798" else ""}")
}
