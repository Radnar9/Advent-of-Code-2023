private const val AOC_DAY = "Day15"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY

/**
 * Hash function.
 */
private fun hash(message: String): Int {
    return message.fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }
}

/**
 * Calculates the hash of each step and sums them up.
 */
private fun part1(input: List<String>): Int {
    val steps = input.flatMap { it.split(",") }
    return steps.sumOf { step -> hash(step) }
}

private typealias Label = String
private typealias FocalLength = Int

/**
 * Places each label inside the box corresponding to its hash. And calculates the focusing power.
 */
private fun part2(input: List<String>): Int {
    val steps = input.flatMap { it.split(",") }
    val boxes = ArrayList<MutableList<Label>>().apply { repeat(256) { add(mutableListOf()) } }
    val focalLengths = hashMapOf<Label, FocalLength>()

    for (step in steps) {
        if ("=" in step) {
            val (label, focalLength) = step.split("=")
            val index = hash(label)
            if (label !in boxes[index]) {
                boxes[index].add(label)
            }
            focalLengths[label] = focalLength.toInt()
        } else {
            val label = step.removeSuffix("-")
            val index = hash(label)
            if (label in boxes[index]) {
                boxes[index].remove(label)
            }
        }
    }

    var sum = 0
    for ((boxNum, box) in boxes.withIndex()) {
        for ((slot, label) in box.withIndex()) {
            sum += (boxNum + 1) * (slot + 1) * focalLengths[label]!!
        }
    }

    return sum
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 1320
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 145
    println("* PART 2:   ${part2(testInput)}\t\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 504449" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 262044" else ""}")
}
