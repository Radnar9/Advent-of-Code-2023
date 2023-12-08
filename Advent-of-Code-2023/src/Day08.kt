private const val AOC_DAY = "Day08"
private const val PART1_TEST_FILE = "${AOC_DAY}_test_part1"
private const val PART2_TEST_FILE = "${AOC_DAY}_test_part2"
private const val INPUT_FILE = AOC_DAY

private data class Direction(val left: String, val right: String)

/**
 * Counts the number of steps until finding a position ending with "ZZZ".
 */
private fun part1(input: List<String>): Int {
    var instructions = input.first()
    val map = input.drop(2).associate {
        val line = it.split(" = ")
        val dir = line[1].removePrefix("(").removeSuffix(")").split(", ")
        line[0] to Direction(dir[0], dir[1])
    }

    var currentDir = "AAA"
    var steps = 0
    while (currentDir != "ZZZ") {
        currentDir = if (instructions[0] == 'R') map[currentDir]!!.right else map[currentDir]!!.left
        instructions = instructions.substring(1) + instructions[0]
        steps++
    }

    return steps
}

private const val STARTING_CHAR = 'A'
private const val ENDING_CHAR = 'Z'

/**
 * After finding that the numbers of steps until finding a position ending with 'Z' are always the same for each initial
 * position, I just need to find each cycle length and calculate the Least Common Multiple of all of them.
 */
private fun part2(input: List<String>): Long {
    var instructions = input.first()
    val startingNodes = mutableListOf<String>()
    val map = input.drop(2).associate {
        val line = it.split(" = ")
        if (line[0].last() == STARTING_CHAR) startingNodes.add(line[0])
        val dir = line[1].removePrefix("(").removeSuffix(")").split(", ")
        line[0] to Direction(dir[0], dir[1])
    }

    val cycles = mutableListOf<Long>()
    for (i in startingNodes.indices) {
        var steps = 0L
        while (startingNodes[i].last() != ENDING_CHAR) {
            val node = startingNodes[i]
            startingNodes[i] = if (instructions[0] == 'R') map[node]!!.right else map[node]!!.left
            instructions = instructions.substring(1) + instructions[0]
            steps++
        }
        cycles.add(steps)
    }
    return lcm(cycles)
}



fun main() {
    val part1TestInput = readInputToList(PART1_TEST_FILE)
    val part1ExpectedRes = 6
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(part1TestInput)}\t== $part1ExpectedRes")

    val part2TestInput = readInputToList(PART2_TEST_FILE)
    val part2ExpectedRes = 6
    println("* PART 2:   ${part2(part2TestInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 11567" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 9858474970153" else ""}")
}
