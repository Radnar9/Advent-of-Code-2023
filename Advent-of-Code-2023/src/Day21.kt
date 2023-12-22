import kotlin.math.pow

private const val AOC_DAY = "Day21"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY


private fun findPlotsFrom(row: Int, col: Int, maxSteps: Int, input: List<String>): Long {
    val startingPos = Position(row, col)
    val gardenPlotsReached = mutableSetOf<Position>()
    val seen = mutableSetOf<Position>().apply { add(startingPos) }
    val queue = ArrayDeque<Pair<Position, Int>>().apply { addLast(Pair(startingPos, maxSteps)) }

    while (queue.isNotEmpty()) {
        val (pos, currSteps) = queue.removeFirst()
        if (currSteps % 2 == 0) gardenPlotsReached.add(pos)
        if (currSteps == 0) continue

        for ((row, col) in listOf(Pair(pos.row + 1, pos.col), Pair(pos.row - 1, pos.col), Pair(pos.row, pos.col + 1), Pair(pos.row, pos.col - 1))) {
            val newPos = Position(row, col)
            if (row < 0 || row >= input.size || col < 0 || col >= input[0].length || input[row][col] == '#' || newPos in seen) continue
            seen.add(newPos)
            queue.addLast(Pair(newPos, currSteps - 1))
        }
    }
    return gardenPlotsReached.size.toLong()
}

private fun part1(input: List<String>, maxSteps: Int): Long {
    /*val startingPos = input.mapIndexed { row, line ->
        line.mapIndexed { col, char -> Pair(row, col) to char }
    }
        .flatten()
        .find { it.second == 'S' }!!
        .first
        .let { Position(it.first, it.second) }*/

    var startingPos = Position(0, 0)
    for ((row, line) in input.withIndex()) {
        for ((col, char) in line.withIndex()) {
            if (char != 'S') continue
            startingPos = Position(row, col)
            break
        }
    }

    return findPlotsFrom(startingPos.row, startingPos.col, maxSteps, input)
}

private fun part2(input: List<String>): Long {
    val steps = 26501365
    var initialPos = Position(0, 0)
    for ((row, line) in input.withIndex()) {
        for ((col, char) in line.withIndex()) {
            if (char != 'S') continue
            initialPos = Position(row, col)
            break
        }
    }

    val (sr, sc) = initialPos
    val size = input.size
    val gridWidth = steps / size - 1

    val odd = (gridWidth / 2 * 2 + 1.0).pow(2)
    val even = ((gridWidth + 1) / 2 * 2.0).pow(2)

    val oddPoints = findPlotsFrom(sr, sc, size * 2 + 1, input)
    val evenPoints = findPlotsFrom(sr, sc, size * 2, input)

    val cornerT = findPlotsFrom(size - 1, sc, size - 1, input)
    val cornerR = findPlotsFrom(sr, 0, size - 1, input)
    val cornerB = findPlotsFrom(0, sc, size - 1, input)
    val cornerL = findPlotsFrom(sr, size - 1, size - 1, input)

    val smallTr = findPlotsFrom(size - 1, 0, size / 2 - 1, input)
    val smallTl = findPlotsFrom(size - 1, size - 1, size / 2 - 1, input)
    val smallBr = findPlotsFrom(0, 0, size / 2 - 1, input)
    val smallBl = findPlotsFrom(0, size - 1, size / 2 - 1, input)

    val largeTr = findPlotsFrom(size - 1, 0, size * 3 / 2 - 1, input)
    val largeTl = findPlotsFrom(size - 1, size - 1, size * 3 / 2 - 1, input)
    val largeBr = findPlotsFrom(0, 0, size * 3 / 2 - 1, input)
    val largeBl = findPlotsFrom(0, size - 1, size * 3 / 2 - 1, input)

    return ((odd * oddPoints) +
            (even * evenPoints) +
            cornerT + cornerR + cornerB + cornerL +
            ((gridWidth + 1) * (smallTr + smallTl + smallBr + smallBl)) +
            (gridWidth * (largeTr + largeTl + largeBr + largeBl))).toLong()
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 16
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput, 6)}\t== $part1ExpectedRes")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input, 64)}${if (improving) "\t== 3814" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 632257949158206" else ""}")
}
