private const val AOC_DAY = "Day19"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY


private data class Rule(val name: Char? = null, val signal: Char? = null, val comparingNum: Int? = null, val destiny: String)
private fun part1(input: List<String>): Int {
    val (part1, part2) = input.joinToString("\n").split("\n\n")

    val workflows = part1.split("\n").associate { flow ->
        val (partName, rulesToParse) = flow.split("{")
        val rules = rulesToParse.removeSuffix("}").split(",").map { part ->
            val ruleSplit = part.split(":")
            val parsedPart = if (ruleSplit.size == 2) {
                val comparingNum = ruleSplit[0].substring(1).split("<", ">")[1].toInt()
                Rule(ruleSplit[0].first(), ruleSplit[0][1], comparingNum, ruleSplit[1])
            } else {
                Rule(destiny = ruleSplit[0])
            }
            parsedPart
        }
        partName to rules
    }

    val ratings = part2.split("\n").map { rating ->
        val parts = rating.substring(1..<rating.length - 1).split(",").associate {
            val (partName, value) = it.split("=")
            partName.first() to value.toInt()
        }
        parts
    }


    fun isAccepted(workflowName: String, rating: Map<Char, Int>): Boolean {
        if (workflowName == "R") return false
        if (workflowName == "A") return true

        val rules = workflows[workflowName]!!
        for (rule in rules) {
            if (rule.name == null) return isAccepted(rule.destiny, rating)
            val partValue = rating[rule.name]!!
            if (rule.signal == '<' && partValue < rule.comparingNum!!) return isAccepted(rule.destiny, rating)
            else if (rule.signal == '>' && partValue > rule.comparingNum!!) return isAccepted(rule.destiny, rating)
        }
        return false
    }

    return ratings.sumOf { rating ->
        val sum = if (isAccepted("in", rating)) {
            rating.values.sum()
        } else 0
        sum
    }
}

private fun part2(input: List<String>): Long {
    val (part1, _) = input.joinToString("\n").split("\n\n")

    val workflows = part1.split("\n").associate { flow ->
        val (partName, rulesToParse) = flow.split("{")
        val rules = rulesToParse.removeSuffix("}").split(",").map { part ->
            val ruleSplit = part.split(":")
            val parsedPart = if (ruleSplit.size == 2) {
                val comparingNum = ruleSplit[0].substring(1).split("<", ">")[1].toInt()
                Rule(ruleSplit[0].first(), ruleSplit[0][1], comparingNum, ruleSplit[1])
            } else {
                Rule(destiny = ruleSplit[0])
            }
            parsedPart
        }
        partName to rules
    }

    fun countAccepted(workflowName: String, rating: MutableMap<Char, IntRange>): Long {
        if (workflowName == "R") return 0
        if (workflowName == "A") return rating.values.fold(1L) { acc, range ->
            acc * (range.last - range.first + 1)
        }

        val rules = workflows[workflowName]!!
        var total = 0L

        for (rule in rules.dropLast(1)) {
            val partRange = rating[rule.name]!!
            val (trueRange, falseRange) = if (rule.signal == '<') {
                Pair(partRange.first..<rule.comparingNum!!, rule.comparingNum..partRange.last)
            } else {
                Pair(rule.comparingNum!! + 1..partRange.last, partRange.first..rule.comparingNum)
            }

            if (trueRange.first <= trueRange.last) {
                val newRating = rating.toMutableMap()
                newRating[rule.name!!] = trueRange
                total += countAccepted(rule.destiny, newRating)
            }

            if (falseRange.first <= falseRange.last) {
                rating[rule.name!!] = falseRange
            } else {
                break
            }
        }
        total += countAccepted(rules.last().destiny, rating)
        return total
    }

    val ratings = mutableMapOf('x' to 1..4000, 'm' to 1..4000, 'a' to 1..4000, 's' to 1..4000)

    return countAccepted("in", ratings)
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
    val part1ExpectedRes = 19114
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 167409079868000L
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 377025" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 135506683246673" else ""}")
}
