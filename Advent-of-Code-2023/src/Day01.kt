private const val AOC_DAY = "Day01"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY


private fun part1(input: List<String>): Int {
    return input.sumOf { line ->
        line.first { it.isDigit() }.digitToInt() * 10 + line.last { it.isDigit() }.digitToInt()
    }
}



private val digitsLetters = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
private data class CalibrationValue(val index: Int, val value: Int)
private fun part2(input: List<String>): Int {
    return input.sumOf { line ->
        var firstVal = CalibrationValue(Int.MAX_VALUE, 0)
        var lastVal = CalibrationValue(Int.MIN_VALUE, 0)

        for ((i, digit) in digitsLetters.withIndex()) {
            if (digit in line) {
                val firstIdx = line.indexOf(digit)
                val lastIdx = line.lastIndexOf(digit)
                if (firstIdx < firstVal.index) {
                    firstVal = CalibrationValue(firstIdx, i + 1)
                }
                if (lastIdx > lastVal.index) {
                    lastVal = CalibrationValue(lastIdx, i + 1)
                }
            }
        }

        val firstIdx = line.indexOfFirst { it.isDigit() }
        val lastIdx = line.indexOfLast { it.isDigit() }
        if (firstIdx >= 0 && firstIdx < firstVal.index) {
            firstVal = CalibrationValue(firstIdx, line[firstIdx].digitToInt())
        }
        if (lastIdx >= 0 && lastIdx > lastVal.index) {
            lastVal = CalibrationValue(lastIdx, line[lastIdx].digitToInt())
        }

        firstVal.value * 10 + lastVal.value
    }
}


private fun getNumber(line: String, digitsLetters: List<String>): Int {
    val (letterIdx, letterValue) = line.findAnyOf(digitsLetters) ?: (Int.MAX_VALUE to "")
    val digitIdx = line.indexOfFirst { it.isDigit() }

    return if (digitIdx < 0 || letterIdx < digitIdx) {
        digitsLetters.indexOf(letterValue) + 1
    } else {
        line[digitIdx].digitToInt()
    }
}


/**
 * Instead of searching from the end of each line, by reversing the line and the digits' letters, it searches for
 * the last number in the same order but with the line reversed: eno, owt, eerht, ruof, ...
 */
private fun part2Improved(input: List<String>): Int {
    return input.sumOf { line ->
        getNumber(line, digitsLetters) * 10 + getNumber(line.reversed(), digitsLetters.map { it.reversed() })
    }
}


fun main() {
    val testInput = readInputToList(TEST_FILE)
    part2(testInput).println()

    val input = readInputToList(INPUT_FILE)
    part1(input).println()
    part2(input).println()
    part2Improved(input).println()
}
