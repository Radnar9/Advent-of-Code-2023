import java.util.PriorityQueue

private const val AOC_DAY = "Day17"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY


private data class Block(val row: Int, val col: Int, val dirRow: Int, val dirCol: Int, val heatLoss: Int, val dirCounter: Int, val path: List<Position> = listOf(Position(0, 0)))
private fun Block.withoutHeatLoss() = Block(row, col, dirRow, dirCol, 0, dirCounter)

/**
 * Finds the shortest path from the top-left corner to the bottom-right corner of the city using Dijkstra's algorithm
 * and the established rules.
 */
private fun part1(input: List<String>): Int {
    val city = input.map { it.map { num -> num.digitToInt() } }
    val pQueue = PriorityQueue<Block>(compareBy { it.heatLoss }).apply { add(Block(0, 0, 0, 0, 0, 0)) }
    val seen = mutableSetOf<Block>()

    var result = Block(0, 0, 0, 0, 0, 0)
    while (pQueue.isNotEmpty()) {
        val block = pQueue.poll()

        if  (block.row == city.size - 1 && block.col == city[0].size - 1) {
            result = block
            break
        }

        if (block.withoutHeatLoss() in seen) continue
        seen.add(block.withoutHeatLoss())

        // If the maximum of 3 consecutive blocks in the same direction is not reached, continue in the same direction
        if (block.dirCounter < 3 && Pair(block.dirRow, block.dirCol) != Pair(0, 0)) {
            val nextRow = block.row + block.dirRow
            val nextCol = block.col + block.dirCol
            if (nextRow in city.indices && nextCol in city[0].indices) {
                val nextHeatLoss = block.heatLoss + city[nextRow][nextCol]
                val path = mutableListOf<Position>().apply { addAll(block.path); add(Position(nextRow, nextCol)) }
                pQueue.add(Block(nextRow, nextCol, block.dirRow, block.dirCol, nextHeatLoss, block.dirCounter + 1, path))
            }
        }

        // Test all directions, unless the direction that goes to the back position and the one that's the same as the current one
        for (dir in listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))) {
            if (dir == Pair(-block.dirRow, -block.dirCol) || dir == Pair(block.dirRow, block.dirCol)) continue
            val nextRow = block.row + dir.first
            val nextCol = block.col + dir.second
            if (nextRow in city.indices && nextCol in city[0].indices) {
                val nextHeatLoss = block.heatLoss + city[nextRow][nextCol]
                val path = mutableListOf<Position>().apply { addAll(block.path); add(Position(nextRow, nextCol)) }
                pQueue.add(Block(nextRow, nextCol, dir.first, dir.second, nextHeatLoss, 1, path))
            }
        }
    }

    // Debug
    if (false) {
        val observePath = input.toMutableList()
        for (pos in result.path) {
            observePath[pos.row] = observePath[pos.row].replaceRange(pos.col, pos.col + 1, "#")
        }
        observePath.forEach { println(it) }
    }

    return result.heatLoss
}

/**
 * Same as part1, but now it needs to move a minimum of four blocks in the same direction before it can turn
 * (or even before it can stop at the end), and can move in the same direction for a maximum of 10 consecutive blocks.
 */
private fun part2(input: List<String>): Int {
    val city = input.map { it.map { num -> num.digitToInt() } }
    val pQueue = PriorityQueue<Block>(compareBy { it.heatLoss }).apply { add(Block(0, 0, 0, 0, 0, 0)) }
    val seen = mutableSetOf<Block>()

    while (pQueue.isNotEmpty()) {
        val block = pQueue.poll()

        if  (block.row == city.size - 1 && block.col == city[0].size - 1 && block.dirCounter >= 4) {
            return block.heatLoss
        }

        if (block.withoutHeatLoss() in seen) continue
        seen.add(block.withoutHeatLoss())

        if (block.dirCounter < 10 && Pair(block.dirRow, block.dirCol) != Pair(0, 0)) {
            val nextRow = block.row + block.dirRow
            val nextCol = block.col + block.dirCol
            if (nextRow in city.indices && nextCol in city[0].indices) {
                val nextHeatLoss = block.heatLoss + city[nextRow][nextCol]
                pQueue.add(Block(nextRow, nextCol, block.dirRow, block.dirCol, nextHeatLoss, block.dirCounter + 1))
            }
        }

        if (block.dirCounter < 4 && Pair(block.dirRow, block.dirCol) != Pair(0, 0)) continue
        for (dir in listOf(Pair(1, 0), Pair(-1, 0), Pair(0, 1), Pair(0, -1))) {
            if (dir == Pair(-block.dirRow, -block.dirCol) || dir == Pair(block.dirRow, block.dirCol)) continue
            val nextRow = block.row + dir.first
            val nextCol = block.col + dir.second
            if (nextRow in city.indices && nextCol in city[0].indices) {
                val nextHeatLoss = block.heatLoss + city[nextRow][nextCol]
                pQueue.add(Block(nextRow, nextCol, dir.first, dir.second, nextHeatLoss, 1))
            }
        }
    }

    return 0
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 102
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 94
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 767" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 904" else ""}")
}
