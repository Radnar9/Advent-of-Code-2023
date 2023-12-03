private const val AOC_DAY = "Day03"
private const val PART1_TEST_FILE = "${AOC_DAY}_test_part1"
private const val PART2_TEST_FILE = "${AOC_DAY}_test_part2"
private const val INPUT_FILE = AOC_DAY


/**
 * Find and sum up the numbers adjacent, including diagonal ones, to any symbol different from '.'.
 * Starts by finding the symbols and then verifies the numbers that are adjacent.
 * @return the sum of the adjacent numbers.
 */
private fun part1(input: List<String>): Int {
    var sum = 0

    for ((i, line) in input.withIndex()) {
        for ((j, c) in line.withIndex()) {
            if (!c.isSymbolNotDot()) continue

            val possibleLines = getSubList(i, input)
            val positions = getNumbersPositions(j, if (i > 0) 1 else i, possibleLines)

            sum += getSumOrRatio(positions, possibleLines, isRatio = false)
        }
    }

    return sum
}

/**
 * Builds a sub list containing the possible lines where it can be found a number adjacent to a character of desired
 * line, represented by mainLineIndex
 */
private fun getSubList(mainLineIndex: Int, list: List<String>): List<String> {
    val initialIdx = if (mainLineIndex > 0) mainLineIndex - 1 else mainLineIndex
    val lastIdx = if (mainLineIndex < list.size - 1) mainLineIndex + 1 else mainLineIndex
    return list.subList(initialIdx, lastIdx + 1)
}


/**
 * Find and multiply the existent gears, i.e., the '*' symbol that is adjacent to exactly two numbers.
 * @return the sum of the gear ratios, i.e., the product of the numbers that constitute each gear.
 */
private fun part2(input: List<String>): Int {
    var sum = 0

    for ((i, line) in input.withIndex()) {
        for ((j, c) in line.withIndex()) {
            if (!c.isAsterisk()) continue

            val possibleLines = getSubList(i, input)
            val positions = getNumbersPositions(j, if (i > 0) 1 else i, possibleLines)
            if (positions.size != 2) continue

            sum += getSumOrRatio(positions, possibleLines, isRatio = true)
        }
    }

    return sum
}

data class Position(val row: Int, val col: Int)

/**
 * Obtains the positions of the numbers that are adjacent (including diagonally) to a symbol
 * @return an array list of the numbers positions
 */
private fun getNumbersPositions(symbolCol: Int, symbolRow: Int, possibleLines: List<String>): ArrayList<Position> {
    val positions = ArrayList<Position>()

    // Tips
    if (symbolCol > 0 && possibleLines[symbolRow][symbolCol - 1].isDigit()) {
        positions.add(Position(symbolRow, symbolCol - 1))
    }
    if (symbolCol < possibleLines[symbolRow].length - 1 && possibleLines[symbolRow][symbolCol + 1].isDigit()) {
        positions.add(Position(symbolRow, symbolCol + 1))
    }

    // Top
    if (symbolRow > 0) {
        if (possibleLines[symbolRow - 1][symbolCol].isDigit()) {
            positions.add(Position(symbolRow - 1, symbolCol))
        } else { // To not count the same number twice
            // Diagonal
            if (symbolCol > 0 && possibleLines[symbolRow - 1][symbolCol - 1].isDigit()) {
                positions.add(Position(symbolRow - 1, symbolCol - 1))
            }
            if (symbolCol < possibleLines[symbolRow].length - 1 && possibleLines[symbolRow - 1][symbolCol + 1].isDigit()) {
                positions.add(Position(symbolRow - 1, symbolCol + 1))
            }
        }
    }
    // Bottom
    if (symbolRow < possibleLines.size - 1) {
        if (possibleLines[symbolRow + 1][symbolCol].isDigit()) {
            positions.add(Position(symbolRow + 1, symbolCol))
        } else {
            // Diagonal
            if (symbolCol > 0 && possibleLines[symbolRow + 1][symbolCol - 1].isDigit()) {
                positions.add(Position(symbolRow + 1, symbolCol - 1))
            }
            if (symbolCol < possibleLines[symbolRow].length - 1 && possibleLines[symbolRow + 1][symbolCol + 1].isDigit()) {
                positions.add(Position(symbolRow + 1, symbolCol + 1))
            }
        }
    }

    return positions
}

/**
 * Finds the numbers that constitute an adjacency with a symbol by starting on the provided index of the numbers.
 * Searches the left and right side of that index until the char is not a number.
 * This approach avoids starting from the start or end of the line.
 * @return the sum of the found numbers or the ratio of both numbers, i.e., their product.
 */
private fun getSumOrRatio(numberPositions: ArrayList<Position>, possibleLines: List<String>, isRatio: Boolean): Int {
    var result = if (isRatio) 1 else 0  // If it is the sum functionality start as 0
    for (pos in numberPositions) {
        val line = possibleLines[pos.row]

        var startIdx = -1
        var lastIdx = -1

        // Find number starting index
        for (i in pos.col downTo 0) {
            if (line[i].isDigit() && i == 0) startIdx = i
            if (line[i].isDigit()) continue
            startIdx = i + 1
            break
        }

        // Find number last index
        for (i in pos.col ..< line.length) {
            if (line[i].isDigit() && i == line.length - 1) lastIdx = i
            if (line[i].isDigit()) continue
            lastIdx = i - 1
            break
        }

        val number = line.substring(startIdx .. lastIdx).toInt()
        result = if (isRatio) result * number else result + number
    }

    return result
}


fun main() {
    val part1ExpectedRes = 4361
    val part1TestInput = readInputToList(PART1_TEST_FILE)
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(part1TestInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 467835
    val part2TestInput = readInputToList(PART2_TEST_FILE)
    println("* PART 2:   ${part2(part2TestInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 543867" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 79613331" else ""}")
}
