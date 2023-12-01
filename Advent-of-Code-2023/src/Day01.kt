fun main() {
    fun part1(input: List<String>): Int {
        "boas".println()
        return input.size
    }

    fun part2(input: List<String>): Int {
        "boas".println()
        return input.size
    }

    val testInput = readInputToList("Day01_test")
    check(part1(testInput) == 1)

    val input = readInputToList("Day01")
    part1(input).println()
    part2(input).println()
}