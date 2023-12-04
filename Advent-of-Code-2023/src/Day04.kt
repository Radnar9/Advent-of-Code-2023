import kotlin.math.pow

private const val AOC_DAY = "Day04"
private const val PART1_TEST_FILE = "${AOC_DAY}_test_part1"
private const val PART2_TEST_FILE = "${AOC_DAY}_test_part2"
private const val INPUT_FILE = AOC_DAY

private const val DOUBLE_POINTS = 2.0


/**
 * Finds and counts the points of the numbers that match the winning numbers.
 * The first match makes the card worth one point and each match after the first doubles the point value of that card.
 * @return the sum of points obtained in all the cards.
 */
private fun part1(input: List<String>): Int {
    return splitWinningNumbersFromMine(input).sumOf { card ->
        val matches = getMatchesCount(card)
        DOUBLE_POINTS.pow(matches.toDouble() - 1).toInt()
    }
}

typealias CardId = Int
data class Card(val matches: Int, val instances: Int)
/**
 * Accumulates scratchcards equal to the number of winning numbers that were a match. If card 10 were to have
 * 5 matching numbers, it would result in one copy each of cards 11, 12, 13, 14, and 15.
 * Basically, after knowing the matches with the winning numbers, the instances of the next cards are incremented based
 * on how many instances the current card had, i.e., if card 2 had 2 matches and 2 instances, it would increment the
 * instances of card 3 and card 4 twice, since card 2 has 2 instances, or more specifically, two copies of itself.
 * @return the sum of all instances of the cards.
 */
private fun part2(input: List<String>): Int {
    val gameMap = mutableMapOf<CardId, Card>()

    splitWinningNumbersFromMine(input).forEachIndexed { currentCardId, card ->
        val matches = getMatchesCount(card)
        val currentInstances = (gameMap[currentCardId]?.instances ?: 0) + 1 // Add instance of the original card
        gameMap[currentCardId] = Card(matches, currentInstances)

        // Add instances of the cards copies
        (0..<matches).forEach { j ->
            val nextCardId = currentCardId + j + 1
            val nextCardInstances = gameMap[nextCardId]?.instances ?: 0
            gameMap[nextCardId] = Card(0, nextCardInstances + currentInstances)
        }
    }

    return gameMap.values.sumOf { it.instances }
}

/**
 * Splits the input by list of winning numbers and list of numbers to be matched.
 * @return a list of cards with each of one those split in a list of winning numbers and numbers to be matched.
 */
private fun splitWinningNumbersFromMine(input: List<String>): List<List<String>> {
    return input
        .map { line -> line.split(":  ", ": ")[1] } // ":  " to avoid creating an unnecessary entry with ""
        .map { line -> line.split(" |  ", " | ") }  // " |  2" & " | 22"
}

/**
 * Based on the card provided, counts the matches between the numbers that one have and the winning numbers.
 * @return the number of matches found.
 */
private fun getMatchesCount(card: List<String>): Int {
    val winningNumbersSet = card[0].split("  ", " ").toSet()
    val matches = card[1]
        .split("  ", " ")
        .sumOf {
            if (winningNumbersSet.contains(it)) 1 else 0 as Int
        }

    return matches
}



fun main() {
    val part1ExpectedRes = 13
    val part1TestInput = readInputToList(PART1_TEST_FILE)
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(part1TestInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 30
    val part2TestInput = readInputToList(PART2_TEST_FILE)
    println("* PART 2:   ${part2(part2TestInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t\t== 15205" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 6189740" else ""}")
}
