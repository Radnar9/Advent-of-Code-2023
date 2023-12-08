private const val AOC_DAY = "Day07"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY

private data class Hand(val cards: String, val bid: Int, val typeStrength: Int)

private fun relativeStrengthMap(withJokers: Boolean = false) = mapOf(
    'A' to 13,
    'K' to 12,
    'Q' to 11,
    if (withJokers) 'J' to 0 else 'J' to 10,
    'T' to 9,
    '9' to 8,
    '8' to 7,
    '7' to 6,
    '6' to 5,
    '5' to 4,
    '4' to 3,
    '3' to 2,
    '2' to 1
)

/**
 * Counts the existent jokers and evaluates the type of the hand.
 * @return the type strength of the hand.
 */
private fun getTypeStrength(hand: String, withJokers: Boolean): Int {
    // Occurrences of each card in the hand
    val handMap = mutableMapOf<Char, Int>()
    var jokers = 0
    hand.forEach { c ->
        if (c == 'J') jokers++
        if (handMap.containsKey(c)) handMap[c] = handMap[c]!! + 1
        else handMap[c] = 1
    }
    jokers = if (withJokers) jokers else 0

    val typeStrength = when (handMap.size) {
        5 -> if (jokers == 1) 2 else 1                              // one-pair: 2 | high-card: 1
        4 -> if (jokers == 1 || jokers == 2) 4 else 2               // three of a kind: 4 | one-pair: 2
        3 -> if (handMap.none { it.value == 3 })                    // two-pair: 3 | three of a kind: 4
                (if (jokers == 2) 6 else if (jokers == 1) 5 else 3) // four of a kind: 6 | full-house: 5 | two-pair: 3
            else
                (if (jokers == 1 || jokers == 3) 6 else 4)          // four of a kind: 6 | three of a kind: 4
        2 -> if (handMap.any { it.value == 3 })                     // full-house: 5 | four of a kind: 6
                (if (jokers > 0) 7 else 5)                          // five of a kind: 7 | full-house: 5
            else
                (if (jokers == 1 || jokers == 4) 7 else 6)          // five of a kind: 7 | four of a kind: 6
        else -> 7                                                   // five of a kind: 7
    }
    return typeStrength
}

/**
 * Parses the input into a list of hands.
 * @return a list of hands.
 */
private fun parseHands(input: List<String>, withJokers: Boolean = false): List<Hand> {
    return input.joinToString("\n").split("\n").map {
        val (hand, bid) = it.split(" ")
        Hand(hand, bid.toInt(), getTypeStrength(hand, withJokers))
    }
}

/**
 * Compares two hands by their type strength and then by their relative strength.
 */
private fun strengthComparator(relativeStrengthMap: Map<Char, Int>) = Comparator<Hand> { hand1, hand2 ->
    if (hand1.typeStrength != hand2.typeStrength) return@Comparator hand1.typeStrength - hand2.typeStrength
    for ((i, card1) in hand1.cards.withIndex()) {
        if (card1 == hand2.cards[i]) continue
        return@Comparator relativeStrengthMap[card1]!! - relativeStrengthMap[hand2.cards[i]]!!
    }
    0
}

/**
 * Calculates the total winnings of the hands by multiplying the bid with the rank of each hand. The rank is determined
 * by the strength of the hand, it is first evaluated by the type strength and then by the relative strength.
 * @return the total winnings of the hands.
 */
private fun part1(input: List<String>): Int {
    val hands = parseHands(input)

    val rankedHands = hands.sortedWith(strengthComparator(relativeStrengthMap()))

    return rankedHands.foldIndexed(0) { i, totalWinnings, hand -> totalWinnings + hand.bid * (i + 1) }
}

/**
 * The objective is the same of part 1, but now the jokers are considered as the lowest card, and act like whatever
 * card would make the hand the strongest type possible.
 * @return the total winnings of the hands.
 */
private fun part2(input: List<String>): Int {
    val hands = parseHands(input, withJokers = true)

    val rankedHands = hands.sortedWith(strengthComparator(relativeStrengthMap(withJokers = true)))

    return rankedHands.foldIndexed(0) { i, totalWinnings, hand -> totalWinnings + hand.bid * (i + 1) }
}


fun main() {
    val testInput = readInputToList(TEST_FILE)

    val part1ExpectedRes = 6440
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

    val part2ExpectedRes = 5905
    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t== 248113761" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 246285222" else ""}")
}
