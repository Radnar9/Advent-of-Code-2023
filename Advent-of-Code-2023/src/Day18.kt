import kotlin.math.abs

private const val AOC_DAY = "Day18"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY

private val directions = mapOf(
    "U" to Position(-1, 0),
    "D" to Position(1, 0),
    "L" to Position(0, -1),
    "R" to Position(0, 1)
)

private fun part1(input: List<String>): Int {
    val points = mutableListOf(Position(0, 0))

    var perimeter = 0
    input.forEach { line ->
        val (direction, meters, _) = line.split(" ")
        val (dirRow, dirCol) = directions[direction]!!
        val num = meters.toInt()
        perimeter += num
        val (row, col) = points.last()
        points.add(Position(row + dirRow * num, col + dirCol * num))
    }

    // Using Shoelace formula to calculate the area of the polygon
    val area = abs(points.indices.sumOf {
        points[it].row * (points[if (it - 1 < 0) points.size - 1 else it - 1].col - points[(it + 1) % points.size].col)
    }) / 2

    // Using Pick's theorem to calculate the number of points inside the polygon
    val i = area - perimeter / 2 + 1

    return i + perimeter
}

private fun part2(input: List<String>): Long {
    val directionDigits = listOf("R", "D", "L", "U")
    val points = mutableListOf(Position(0, 0))

    var perimeter = 0L
    input.forEach { line ->
        val (_, _, instruction) = line.split(" ")
        val (dirRow, dirCol) = directions[directionDigits[instruction[instruction.length - 2].digitToInt()]]!!
        val num = instruction.substring(instruction.length - 7, instruction.length - 2).toInt(radix = 16)
        perimeter += num
        val (row, col) = points.last()
        points.add(Position(row + dirRow * num, col + dirCol * num))
    }

    val area = abs(points.indices.sumOf {
        points[it].row * (points[if (it - 1 < 0) points.size - 1 else it - 1].col - points[(it + 1) % points.size].col).toLong()
    }) / 2L
    val i = area - perimeter / 2 + 1

    return i + perimeter
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 62
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 952408144115L
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 56678" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 79088855654037" else ""}")
}
