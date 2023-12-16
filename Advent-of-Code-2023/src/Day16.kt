private const val AOC_DAY = "Day16"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY

/**
 * Calculates the number of energized tiles starting from the initial position/movement.
 */
private fun energizedTiles(initialMovement: Movement, input: List<String>): Int {
    val seen = mutableSetOf<Movement>()
    val queue = ArrayDeque<Movement>().apply { add(initialMovement) }

    while (queue.isNotEmpty()) {
        val (currentPos, direction) = queue.removeFirst()
        val nextPos = Position(currentPos.row + direction.row, currentPos.col + direction.col)

        if (nextPos.row < 0 || nextPos.row >= input.size || nextPos.col < 0 || nextPos.col >= input[0].length) continue

        val nextChar = input[nextPos.row][nextPos.col]

        if (nextChar == '.' || (nextChar == '-' && direction.col != 0) || (nextChar == '|' && direction.row != 0)) {
            val mov = Movement(nextPos, direction)
            if (mov !in seen) {
                seen.add(mov)
                queue.add(mov)
            }
        } else if (nextChar == '/') {
            val newDir = Position(-direction.col, -direction.row)
            val mov = Movement(nextPos, newDir)
            if (mov !in seen) {
                seen.add(mov)
                queue.add(mov)
            }
        } else if (nextChar == '\\') {
            val newDir = Position(direction.col, direction.row)
            val mov = Movement(nextPos, newDir)
            if (mov !in seen) {
                seen.add(mov)
                queue.add(mov)
            }
        } else if (nextChar == '|') {
            val dirs = listOf(Position(1, 0), Position(-1, 0))
            for (dir in dirs) {
                val mov = Movement(nextPos, dir)
                if (mov !in seen) {
                    seen.add(mov)
                    queue.add(mov)
                }
            }
        } else { // nextChar = '-'
            val dirs = listOf(Position(0, 1), Position(0, -1))
            for (dir in dirs) {
                val mov = Movement(nextPos, dir)
                if (mov !in seen) {
                    seen.add(mov)
                    queue.add(mov)
                }
            }
        }
    }

    val uniquePos = seen.map { it.currentPos }.toSet()

    return uniquePos.size
}


private data class Movement(val currentPos: Position, val direction: Position)

/**
 * Finds the number of energized tiles starting from the required initial position and following movement.
 */
private fun part1(input: List<String>): Int {
    return energizedTiles(Movement(Position(0, -1), Position(0, 1)), input)
}

/**
 * Finds the starting position that allows to reach the maximum number of energized tiles.
 */
private fun part2(input: List<String>): Int {
    var maxEnergized = 0
    // Check the positions on the first and last columns where the tile goes right and left, respectively.
    for (row in input.indices) {
        maxEnergized = maxOf(maxEnergized, energizedTiles(Movement(Position(row, -1), Position(0, 1)), input))
        maxEnergized = maxOf(maxEnergized, energizedTiles(Movement(Position(row, input[0].length), Position(0, -1)), input))
    }
    // Check the downwards and upwards positions on top and bottom, respectively.
    for (col in input[0].indices) {
        maxEnergized = maxOf(maxEnergized, energizedTiles(Movement(Position(-1, col), Position(1, 0)), input))
        maxEnergized = maxOf(maxEnergized, energizedTiles(Movement(Position(input.size, col), Position(-1, 0)), input))
    }

    return maxEnergized
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 46
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 51
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = false
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 7392" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== ???" else ""}")
}
