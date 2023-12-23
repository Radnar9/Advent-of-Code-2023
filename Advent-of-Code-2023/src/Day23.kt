private const val AOC_DAY = "Day23"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY



private fun part1(input: List<String>): Int {
    val start = Position(0, input.first().indexOf('.'))
    val end = Position(input.size - 1, input.last().indexOf('.'))

    val points = mutableListOf(start, end)

    for ((r, row) in input.withIndex()) {
        for ((c, char) in row.withIndex()) {
            if (char == '#') continue
            var neighbors = 0
            for ((nextRow, nextCol) in listOf(Pair(r - 1, c), Pair(r + 1, c), Pair(r, c - 1), Pair(r, c + 1))) {
                if (nextRow in input.indices && nextCol in input[0].indices && input[nextRow][nextCol] != '#') neighbors++
            }
            if (neighbors >= 3) points.add(Position(r, c))
        }
    }

    val graph = mutableMapOf<Position, MutableMap<Position, Int>>().apply { points.forEach { this[it] = mutableMapOf() } }

    val dirs = mutableMapOf(
        '^' to listOf(Pair(-1, 0)),
        'v' to listOf(Pair(1, 0)),
        '<' to listOf(Pair(0, -1)),
        '>' to listOf(Pair(0, 1)),
        '.' to listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
    )

    for (startingPos in points) {
        val stack = mutableListOf(Pair(0, Position(startingPos.row, startingPos.col)))
        val seen = mutableSetOf(Position(startingPos.row, startingPos.col))

        while (stack.isNotEmpty()) {
            val (n, pos) = stack.removeFirst()
            if (n != 0 && pos in points) {
                graph[startingPos]!![pos] = n
                continue
            }

            for ((dr, dc) in dirs[input[pos.row][pos.col]]!!) {
                val nextRow = pos.row + dr
                val nextCol = pos.col + dc
                val nextPos = Position(nextRow, nextCol)
                if (nextRow in input.indices && nextCol in input[0].indices && input[nextRow][nextCol] != '#' && nextPos !in seen) {
                    stack.add(Pair(n + 1, nextPos))
                    seen.add(nextPos)
                }
            }
        }
    }

    val seen = mutableSetOf<Position>()

    fun dfs(pos: Position): Int {
        if (pos == end) return 0
        var m = 0
        seen.add(pos)
        for (nx in graph[pos]!!) {
            if (nx.key !in seen) {
                m = maxOf(m, dfs(nx.key) + graph[pos]!![nx.key]!!)
            }
        }
        seen.remove(pos)
        return m
    }

    return dfs(start)
}

private fun part2(input: List<String>): Int {
    val start = Position(0, input.first().indexOf('.'))
    val end = Position(input.size - 1, input.last().indexOf('.'))

    val points = mutableListOf(start, end)

    for ((r, row) in input.withIndex()) {
        for ((c, char) in row.withIndex()) {
            if (char == '#') continue
            var neighbors = 0
            for ((nextRow, nextCol) in listOf(Pair(r - 1, c), Pair(r + 1, c), Pair(r, c - 1), Pair(r, c + 1))) {
                if (nextRow in input.indices && nextCol in input[0].indices && input[nextRow][nextCol] != '#') neighbors++
            }
            if (neighbors >= 3) points.add(Position(r, c))
        }
    }

    val graph = mutableMapOf<Position, MutableMap<Position, Int>>().apply { points.forEach { this[it] = mutableMapOf() } }

    for (startingPos in points) {
        val stack = mutableListOf(Pair(0, Position(startingPos.row, startingPos.col)))
        val seen = mutableSetOf(Position(startingPos.row, startingPos.col))

        while (stack.isNotEmpty()) {
            val (n, pos) = stack.removeFirst()
            if (n != 0 && pos in points) {
                graph[startingPos]!![pos] = n
                continue
            }

            for ((dr, dc) in listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))) {
                val nextRow = pos.row + dr
                val nextCol = pos.col + dc
                val nextPos = Position(nextRow, nextCol)
                if (nextRow in input.indices && nextCol in input[0].indices && input[nextRow][nextCol] != '#' && nextPos !in seen) {
                    stack.add(Pair(n + 1, nextPos))
                    seen.add(nextPos)
                }
            }
        }
    }

    val seen = mutableSetOf<Position>()

    fun dfs(pos: Position): Double {
        if (pos == end) return 0.0
        var m = Double.NEGATIVE_INFINITY
        seen.add(pos)
        for (nx in graph[pos]!!) {
            if (nx.key !in seen) {
                m = maxOf(m, dfs(nx.key) + graph[pos]!![nx.key]!!)
            }
        }
        seen.remove(pos)
        return m
    }

    return dfs(start).toInt()
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 94
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 154
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = false
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 2106" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== ???" else ""}")
}
