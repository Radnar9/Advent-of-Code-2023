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

        for (rule in rules) {
            val partRange = rating[rule.name]!!
            val rangesPair = if (rule.signal == '<' && rule.comparingNum!! in partRange) {
                Pair(partRange.first..<rule.comparingNum, rule.comparingNum..partRange.last)
            } else {
                Pair(rule.comparingNum!! + 1..partRange.last, partRange.first..rule.comparingNum)
            }

            if (rangesPair.first.first <= rangesPair.first.last) {
                rating[rule.name!!] = rangesPair.first
                total += countAccepted(rule.destiny, rating)
            }

            if (rangesPair.second.first <= rangesPair.second.last) {
                rating[rule.name!!] = rangesPair.second
            } else {
                break
            }

            /*if (rule.name == null) return countAccepted(rule.destiny, rating)
//            val partRange = rating[rule.name]!!
            if (rule.signal == '<' && rule.comparingNum!! in partRange) {
                rating[rule.name] = partRange.first..<rule.comparingNum
                return countAccepted(rule.destiny, rating)
            } else if (rule.signal == '>' && rule.comparingNum!! in partRange) {
                rating[rule.name] = rule.comparingNum..partRange.last
                return countAccepted(rule.destiny, rating)
            }*/
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


//    val input = readInputToList(INPUT_FILE)
//    val improving = false
//    println("---| FINAL INPUT |---")
//    println("* PART 1: ${part1(input)}${if (improving) "\t== ???" else ""}")
//    println("* PART 2: ${part2(input)}${if (improving) "\t== ???" else ""}")
}
