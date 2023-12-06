private const val AOC_DAY = "Day06"
private const val PART1_TEST_FILE = "${AOC_DAY}_test_part1"
private const val PART2_TEST_FILE = "${AOC_DAY}_test_part2"
private const val INPUT_FILE = AOC_DAY


/**
 * Finds the minimum time required to go further than the record distance, and then, since the results are symmetric,
 * we can also get the maximum time required. Then, we store the count of numbers between the min and max.
 * @return the product of the possibilities for each time of getting further than the record distance.
 */
private fun part1(input: List<String>): Int {
    val parsedInput = parseInput(input)
    val times = parsedInput.first.map { it.toInt() }
    val distances = parsedInput.second.map { it.toInt() }
    val waysToWin = mutableListOf<Int>()

    for ((time, distance) in times.zip(distances)) {
        for (j in 1..<time / 2) {
            if (j * (time - j) <= distance) continue
            waysToWin.add(time - j - j + 1)
            break
        }
    }
    return waysToWin.reduce(Int::times)
}

/**
 * The same objective as in part1, however here we only have one time and a corresponding record distance, since the
 * numbers in the sheet of paper are supposed to be interpreted as a single number.
 * @return the number of possibilities of getting further than the record distance.
 */
private fun part2(input: List<String>): Long {
    val (times, distances) = parseInput(input)
    val time = times.joinToString("").toLong()
    val distance = distances.joinToString("").toLong()

    for (j in 1..<time / 2) {
        if (j * (time - j) <= distance) continue
        return time - j - j + 1
    }
    return 0
}

typealias Times = List<String>
typealias Distances = List<String>
private fun parseInput(input: List<String>): Pair<Times, Distances> {
    val spacesRegex = Regex("\\s+")
    val times = input.first().removePrefix("Time:").trim().split(spacesRegex)
    val distances = input.last().removePrefix("Distance:").trim().split(spacesRegex)
    return Pair(times, distances)
}


fun main() {
    val part1ExpectedRes = 288
    val part1TestInput = readInputToList(PART1_TEST_FILE)
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(part1TestInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 71503
    val part2TestInput = readInputToList(PART2_TEST_FILE)
    println("* PART 2:   ${part2(part2TestInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 393120" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 36872656" else ""}")
}
