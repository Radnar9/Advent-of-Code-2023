private const val AOC_DAY = "Day14"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY


/**
 * Calculates the load caused by the rounded rocks in the platform.
 * The load is calculated by multiplying the number of rounded rocks in a column by the distance from the bottom.
 * Since we want to know the total load on the north beam, by transposing the platform we only need to move the
 * rounded rocks to the left.
 * First, we split the platform by the cube-shaped rocks, then we sort the rounded rocks in each column and join them
 * back together. This way the cube-shaped rocks won't interfere with the sorting. In the end, we transpose the
 * platform back to its original state.
 */
private fun part1(input: List<String>): Int {
    val tiltedPlatform = input
        .transpose()
        .map { row -> row.split("#") }
        .map { row ->
            row.joinToString("#") { str ->
                str.toCharArray().sortedDescending().joinToString("")
            }
        }.transpose()
        .foldIndexed(0) { index, sum, row ->
            sum + row.count { it == 'O' } * (input.size - index)
        }

    return tiltedPlatform
}

/**
 * Calculates the cycle, and moves the rocks to their positions.
 * A cycle is composed by four 90 degree rotations.
 */
private fun cycle(platform: List<String>): List<String> {
    var newPlatform = platform
    repeat(4) {
        newPlatform = newPlatform
            .transpose()
            .map { row -> row.split("#") }
            .map { row ->
                row.joinToString("#") { str ->
                    str.toCharArray().sortedDescending().joinToString("")
                }
            }.map { row -> row.reversed() }
    }
    return newPlatform
}

/**
 * Finds the final platform after 1 billion rotations. Since the platform is cyclic, we can find the final platform
 * by finding the offset of the first cycle and then finding the offset of the final platform in the cycle.
 */
private fun part2(input: List<String>): Int {
    var lastPlatform = input
    val seenPlatforms = mutableSetOf<List<String>>()
    val orderedCycles = mutableListOf<List<String>>()

    while (true) {
        lastPlatform = cycle(lastPlatform)
        if (lastPlatform in seenPlatforms) {
            break
        }
        seenPlatforms.add(lastPlatform)
        orderedCycles.add(lastPlatform)
    }

    val firstCycleIdx = orderedCycles.indexOf(lastPlatform)
    // (number of cycles left) % (offset of a cycle) = initial platform + (offset of a cycle) = idx of the final plat
    val finalPlatform = orderedCycles[(1_000_000_000 - firstCycleIdx) % (orderedCycles.size - firstCycleIdx) + firstCycleIdx - 1]

    return finalPlatform.foldIndexed(0) { index, sum, row ->
        sum + row.count { it == 'O' } * (input.size - index)
    }
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 136
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 64
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 108889" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 104671" else ""}")
}
