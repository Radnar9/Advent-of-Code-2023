private const val AOC_DAY = "Day10"
private const val PART1_TEST_FILE = "${AOC_DAY}_test_part1"
private const val PART2_TEST_FILE = "${AOC_DAY}_test_part2"
private const val INPUT_FILE = AOC_DAY

private data class Point(val x: Int, val y: Int)
private data class Path(val prev: Point, val next: Point)
private val pipes = mapOf(
//    | is a vertical pipe connecting north and south.
    '|' to listOf(Path(Point(0, -1), Point(0, 1)), Path(Point(0, 1), Point(0, -1))),
//    - is a horizontal pipe connecting east and west.
    '-' to listOf(Path(Point(-1, 0), Point(1, 0)), Path(Point(1, 0), Point(-1, 0))),
//    L is a 90-degree bend connecting north and east.
    'L' to listOf(Path(Point(0, -1), Point(1, 0)), Path(Point(1, 0), Point(0, -1))),
//    J is a 90-degree bend connecting north and west.
    'J' to listOf(Path(Point(0, -1), Point(-1, 0)), Path(Point(-1, 0), Point(0, -1))),
//    7 is a 90-degree bend connecting south and west.
    '7' to listOf(Path(Point(-1, 0), Point(0, 1)), Path(Point(0, 1), Point(-1, 0))),
//    F is a 90-degree bend connecting south and east.
    'F' to listOf(Path(Point(0, 1), Point(1, 0)), Path(Point(1, 0), Point(0, 1))),
)
private const val GROUND = '.'    //    . is ground; there is no pipe in this tile.
private const val START = 'S'     //    S is the starting position of the animal; there is a pipe on this tile.

private val possibleDirections = listOf(Point(0, 1), Point(1, 0), Point(-1, 0), Point(0, -1))

private fun getPipeAtPoint(point: Point, diagram: List<CharArray>): Char {
    return diagram[point.y][point.x]
}

private fun Point.isStart(diagram: List<CharArray>): Boolean {
    return getPipeAtPoint(this, diagram) == START
}

private fun Point.isGround(diagram: List<CharArray>): Boolean {
    return getPipeAtPoint(this, diagram) == GROUND
}

private fun Point.sum(other: Point): Point {
    return Point(x + other.x, y + other.y)
}

private fun Point.minus(other: Point): Point {
    return Point(x - other.x, y - other.y)
}

private fun Point.isValid(diagram: List<CharArray>): Boolean {
    return x >= 0 && y >= 0 && y < diagram.size && x < diagram[y].size
}

private fun Point.isValidPath(diagram: List<CharArray>): Boolean {
    return isValid(diagram) && !isGround(diagram)
}

private fun tryPath(point: Point, startingPoint: Point, diagram: List<CharArray>): List<Point> {
    val path = mutableListOf(startingPoint)
    var currentPoint = point
    var prevPoint = startingPoint
    while (!currentPoint.isStart(diagram)) {
        val nextPoint = pipes[getPipeAtPoint(currentPoint, diagram)]!!.find {
            val prev = currentPoint.sum(it.prev)
            val next = currentPoint.sum(it.next)
            prev == prevPoint && next.isValidPath(diagram)
        }?.let { currentPoint.sum(it.next) } ?: return emptyList()

        path.add(currentPoint)
        prevPoint = currentPoint
        currentPoint = nextPoint
    }

    return path
}

private fun tryPathBFS(queue: ArrayDeque<Path>, startingPoint: Point, diagram: List<CharArray>): List<Point> {
    val path = mutableSetOf(startingPoint)
    do {
        val currentPath = queue.removeFirst()

        val nextPoint = pipes[getPipeAtPoint(currentPath.next, diagram)]!!.find {
            val prev = currentPath.next.sum(it.prev)
            val next = currentPath.next.sum(it.next)
            prev == currentPath.prev && next.isValid(diagram) && !next.isGround(diagram) /*(next == startingPoint || currentPath.next.isValidPath(prev, diagram))*/
        }?.let { currentPath.next.sum(it.next) } ?: continue

        if (path.contains(nextPoint)) continue
        path.add(currentPath.next)
        queue.add(Path(currentPath.next, nextPoint))
    } while (queue.isNotEmpty())

    return path.toList()
}

/**
 * Counts the steps along the loop until the starting point is reached again.
 * @return the steps needed to get to the farthest point from the starting point
 */
private fun part1(input: List<String>): Int {
    val diagram = input.map { it.toCharArray() }

    val startingPoint: Point = diagram.find { it.contains(START) }?.let {
        Point(it.indexOf(START), diagram.indexOf(it))
    } ?: throw Exception("No starting point found")

    for (direction in possibleDirections) {
        val nextPoint = startingPoint.sum(direction)
        if (!nextPoint.isValidPath(diagram)) continue
        val path = tryPath(nextPoint, startingPoint, diagram)
        if (path.isNotEmpty()) return path.size / 2
    }
    return -1
}

/**
 * Breadth-first search version, i.e., don't start by verifying the full path, but rather by verifying the first step in
 * all possible directions, then the second step in all possible directions, etc.
 */
private fun part1BFS(input: List<String>): Int {
    val diagram = input.map { it.toCharArray() }

    val startingPoint: Point = diagram.find { it.contains(START) }?.let {
        Point(it.indexOf(START), diagram.indexOf(it))
    } ?: throw Exception("No starting point found")

    val queue = ArrayDeque<Path>()
    for (direction in possibleDirections) {
        val nextPoint = startingPoint.sum(direction)
        if (!nextPoint.isValidPath(diagram)) continue
        queue.add(Path(startingPoint, nextPoint))
    }

    val path = tryPathBFS(queue, startingPoint, diagram)

    return (path.size + 1) / 2
}

/**
 * Finds the loop, then replaces the starting point with the corresponding pipe, and finally counts the number of tiles
 * enclosed by the loop.
 */
private fun part2(input: List<String>): Int {
    val diagram = input.map { it.toCharArray() }

    val startingPoint: Point = diagram.find { it.contains(START) }?.let {
        Point(it.indexOf(START), diagram.indexOf(it))
    } ?: throw Exception("No starting point found")

    var path = listOf<Point>()
    for (direction in possibleDirections) {
        val nextPoint = startingPoint.sum(direction)
        if (!nextPoint.isValidPath(diagram)) continue
        path = tryPath(nextPoint, startingPoint, diagram)
        if (path.isNotEmpty()) break
    }

    // Find the starting pipe and clear the diagram
    val point1 = path[1].minus(startingPoint)
    val point2 = path.last().minus(startingPoint)
    val pipePaths = listOf(Path(point2, point1), Path(point1, point2))
    val startingPipe = pipes.filter { it.value.all { path -> pipePaths.contains(path) } }.keys.first()
    val cleanedDiagram = diagram.mapIndexed { yIdx, yLine ->
        yLine.mapIndexed { xIdx, c ->
            if (c == START) startingPipe else if (path.contains(Point(xIdx, yIdx))) c else GROUND
        }
    }

    val outside = mutableSetOf<Point>()
    cleanedDiagram.forEachIndexed { yIdx, yLine ->
        var within = false
        var up = false
        yLine.forEachIndexed { xIdx, c ->
            when (c) {
                '|' -> within = !within
                '-' -> assert(!up)
                'L', 'F' -> up = (c == 'L')
                '7', 'J' -> {
                    if (c != (if (up) 'J' else '7')) within = !within
                    up = false
                }
                '.' -> ""
                else -> throw Exception("Unexpected character $c")
            }
            if (!within) outside.add(Point(xIdx, yIdx))
        }
    }

    return diagram.size * diagram[0].size - (outside.size + path.count { !outside.contains(it) })
}


fun main() {
    createTestFiles(AOC_DAY)

    var testInput = readInputToList(PART1_TEST_FILE)
    val part1ExpectedRes = 8
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")
    println("* PART 1:   ${part1BFS(testInput)}\t== $part1ExpectedRes (BFS)")

    testInput = readInputToList(PART2_TEST_FILE)
    val part2ExpectedRes = 10
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 7102" else ""}")
    println("* PART 1: ${part1BFS(input)}${if (improving) "\t== 7102" else ""} (BFS)")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 363" else ""}")
}
