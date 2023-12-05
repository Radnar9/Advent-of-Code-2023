import kotlin.math.max
import kotlin.math.min

private const val AOC_DAY = "Day05"
private const val PART1_TEST_FILE = "${AOC_DAY}_test_part1"
private const val PART2_TEST_FILE = "${AOC_DAY}_test_part2"
private const val INPUT_FILE = AOC_DAY

// destination range start | source range start | range length
private data class Range(val destStart: Long, val sourceRange: LongRange)

/**
 * After parsing the input and having a range for each of its lines, check if each seed is contained in the range line.
 * If so, calculate the corresponding destination value, and add it to the seed list, so it can be evaluated in the
 * next range line, and so on.
 * In case the range does not contain a seed, we add it back to the list, since when there is no match, we should
 * continue with the initial/previous values.
 */
private fun part1(input: List<String>): Long {
    val (seeds, almanac) = parseInput(input)
    var seedsList = seeds

    for (list in almanac) {
        val auxList = mutableListOf<Long>()
        for ((i, seed) in seedsList.withIndex()) {
            for (range in list) {
                // If range does not contain seed, evaluate the next input line
                if (seed !in range.sourceRange) continue

                // Calculate the corresponding destination value
                auxList.add(seed - range.sourceRange.first + range.destStart)
                break
            }
            // If there is no mapping, add back the seed
            if (auxList.size == i) auxList.add(seed)
        }
        // Copy the resulting values to the seedsList to evaluate them through the next input lines
        seedsList = auxList
    }
    return seedsList.minOf { it }
}


/**
 * Parse the seeds in ranges, verify if they overlap with the next input line, if so calculate the destination value by
 * subtracting the source from the overlapping values and adding the destination values. Place the resulting ranges
 * inside the seedsList to evaluate the next line with these new ranges, and so on until reaching the location. If the
 * overlapped range is not the entire seed range, then we need to add the remaining back to the seedsList.
 * In case there is no overlapping in the current range lines, then add back the seed range to the seedsList, since
 * when there is no match, we should continue with the initial/previous values.
 */
private fun part2(input: List<String>): Long {
    val (seedsLine, almanac) = parseInput(input)

    val seeds = seedsLine.chunked(2).map { (start, len) -> start..<start + len }
    var seedsList = seeds.toMutableList()

    for (list in almanac) {
        val auxList = mutableListOf<LongRange>()
        while (seedsList.size > 0) {
            val seedRange = seedsList.removeFirst()
            for (range in list) {
                val overlapStart = max(seedRange.first, range.sourceRange.first)
                val overlapEnd = min(seedRange.last, range.sourceRange.last)

                // If there is no overlap, evaluate the next input line
                if (overlapStart >= overlapEnd) continue

                // Verify if there is a remaining in the [seedRange] before and after the overlap range
                if (overlapStart > seedRange.first) seedsList.add(seedRange.first..overlapStart)
                if (seedRange.last > overlapEnd) seedsList.add(overlapEnd..seedRange.last)

                // Calculate the corresponding destination values for the overlapping range: res - src + dst
                val destinationDiff = - range.sourceRange.first + range.destStart
                auxList.add(overlapStart + destinationDiff..overlapEnd + destinationDiff)
                break
            }
            // If there is no overlapping, add back the seedRange
            if (auxList.isEmpty()) auxList.add(seedRange)
        }
        // Copy the resulting ranges to the seedsList to evaluate them through the next input lines
        seedsList = auxList
    }
    return seedsList.minOf { it.first }
}

/**
 * Parses the input, resulting in a pair of the list of seeds and a list for each block which contains lists for each
 * line of the corresponding block. Each of these lines is represented as a [Range].
 */
private fun parseInput(input: List<String>): Pair<List<Long>, List<List<Range>>> {
    val seedsLine = input[0].removePrefix("seeds: ").split(" ").map { it.toLong() }
    val almanac = input
        .drop(2)
        .joinToString("\n")
        .split("\n\n")
        .map { it
            .split("\n")
            .drop(1)
            .map { range ->
                val (dst, src, len) = range.split(" ")
                Range(dst.toLong(), src.toLong()..<src.toLong() + len.toLong())
            }
        }
    return Pair(seedsLine, almanac)
}


fun main() {
    val part1ExpectedRes = 35
    val part1TestInput = readInputToList(PART1_TEST_FILE)
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(part1TestInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 46
    val part2TestInput = readInputToList(PART2_TEST_FILE)
    println("* PART 2:   ${part2(part2TestInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 51752125" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 12634632" else ""}")
}
