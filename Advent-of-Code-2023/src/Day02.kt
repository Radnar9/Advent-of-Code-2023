private const val AOC_DAY = "Day02"
private const val PART1_TEST_FILE = "${AOC_DAY}_test_part1"
private const val PART2_TEST_FILE = "${AOC_DAY}_test_part2"
private const val INPUT_FILE = AOC_DAY

private const val MAX_COLORS = 3

private data class Cube(val value: Int, val color: String)

private val maxCubes = mapOf("red" to 12, "green" to 13, "blue" to 14)


/**
 * Finds the games that use a number of cubes from a specific color that is greater than the maximum cubes of that color
 * specified by the maxCubes map.
 * @return the gameID of the games that use a possible number of cubes for each color according to the maxCubes map.
 */
private fun part1(input: List<String>): Int {
    return input.sumOf { game ->
        val (gameId, cubes) = parseCubes(game)

        var isImpossible = false
        for (cube in cubes) {
            if (cube.value > maxCubes[cube.color]!!) {
                isImpossible = true
                break
            }
        }

        if (isImpossible) 0 else gameId
    }
}

/**
 * Finds the maximum number of cubes by color used in a set of a game, multiplies them, and sums the result with the
 * results of the other games.
 * @return the sum of the product of the number of cubes for each color in each game
 */
private fun part2(input: List<String>): Int {
    return input.sumOf { game ->
        val (_, cubes) = parseCubes(game)
        val sortedCubes = cubes.sortedByDescending { cube -> cube.value }

        val cubesValues = HashMap<String, Int>()
        for (cube in sortedCubes) {
            cubesValues.putIfAbsent(cube.color, cube.value)
            if (cubesValues.size == MAX_COLORS) break
        }

        cubesValues.values.reduce(Int::times)
    }
}


/**
 * Auxiliary function to parse the cubes from each game.
 * @return a pair with the game id and its cubes.
 */
private fun parseCubes(game: String): Pair<Int, List<Cube>> {
    val (gameInfo, sets) = game.split(":")
    return Pair(
        gameInfo.split(" ")[1].toInt(),
        sets
            .split(",", ";")
            .map {
                val cube = it.removePrefix(" ").split(" ")
                Cube(cube[0].toInt(), cube[1])
            })
}




fun main() {
    val part1ExpectedRes = 8
    val part1TestInput = readInputToList(PART1_TEST_FILE)
    println("---| TEST INPUT |---")
    println("* PART 1 RESULT:   ${part1(part1TestInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 2286
    val part2TestInput = readInputToList(PART2_TEST_FILE)
    println("* PART 2 RESULT:   ${part2(part2TestInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = false
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 2879" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 65122" else ""}")
}
