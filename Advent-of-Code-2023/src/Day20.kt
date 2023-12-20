private const val AOC_DAY = "Day20"
private const val TEST_FILE = "${AOC_DAY}_test"
private const val INPUT_FILE = AOC_DAY


private data class Module(val name: String, val type: String, val memory: MutableMap<String, String>, val outputs: List<String>) {
    init {
        if (type == "%") memory["%"] = "off"
    }
}
private fun part1(input: List<String>): Int {
    val modules = mutableMapOf<String, Module>()
    var broadcastTargets = listOf<String>()

    input.forEach { line ->
        val (left, right) = line.split(" -> ")
        val outputs = right.split(", ")
        if (left == "broadcaster") {
            broadcastTargets = outputs
        } else {
            val type = left.first().toString()
            val name = left.drop(1)
            modules[name] = Module(name, type, mutableMapOf(), outputs)
        }
    }

    modules.values.forEach { module ->
        module.outputs.forEach { output ->
            if (output in modules && modules[output]!!.type == "&") {
                modules[output]!!.memory[module.name] = "low"
            }
        }
    }

    var low = 0
    var high = 0

    repeat(1000) {
        low++
        val pulsesQueue = ArrayDeque<Triple<String, String, String>>() // origin, target, pulse
        for (target in broadcastTargets) {
            pulsesQueue.add(Triple("broadcaster", target, "low"))
        }

        while (pulsesQueue.isNotEmpty()) {
            val (origin, target, pulse) = pulsesQueue.removeFirst()
            if (pulse == "low") low++
            else high++

            if (target !in modules) continue

            val module = modules[target]!!

            if (module.type == "%") {
                if (pulse == "low") {
                    module.memory["%"] = if (module.memory["%"] == "off") "on" else "off"
                    val outgoingPulse = if (module.memory["%"] == "on") "high" else "low"
                    module.outputs.forEach { output ->
                        pulsesQueue.add(Triple(module.name, output, outgoingPulse))
                    }
                }
            } else {
                module.memory[origin] = pulse
                val outgoingPulse = if (module.memory.values.all { it == "high" }) "low" else "high"
                module.outputs.forEach { output ->
                    pulsesQueue.add(Triple(module.name, output, outgoingPulse))
                }
            }
        }
    }

    return low * high
}

private fun part2(input: List<String>): Long {
    val modules = mutableMapOf<String, Module>()
    var broadcastTargets = listOf<String>()

    input.forEach { line ->
        val (left, right) = line.split(" -> ")
        val outputs = right.split(", ")
        if (left == "broadcaster") {
            broadcastTargets = outputs
        } else {
            val type = left.first().toString()
            val name = left.drop(1)
            modules[name] = Module(name, type, mutableMapOf(), outputs)
        }
    }

    modules.values.forEach { module ->
        module.outputs.forEach { output ->
            if (output in modules && modules[output]!!.type == "&") {
                modules[output]!!.memory[module.name] = "low"
            }
        }
    }

    val finalModule = modules.filter { "rx" in it.value.outputs }.keys.first()
    val cycleLengths = mutableMapOf<String, Long>()
    val seen = modules.filter { finalModule in it.value.outputs }.keys.associateWith { 0L }.toMutableMap()
    var buttonPresses = 0L

    while (true) {
        buttonPresses++
        val pulsesQueue = ArrayDeque<Triple<String, String, String>>() // origin, target, pulse
        for (target in broadcastTargets) {
            pulsesQueue.add(Triple("broadcaster", target, "low"))
        }

        while (pulsesQueue.isNotEmpty()) {
            val (origin, target, pulse) = pulsesQueue.removeFirst()

            if (target !in modules) continue

            val module = modules[target]!!

            // Assumption: everytime we see the final module with a high pulse, it marks the end of a cycle
            if (module.name == finalModule && pulse == "high") {
                seen[origin] = seen[origin]!! + 1
                if (origin !in cycleLengths) {
                    cycleLengths[origin] = buttonPresses
                } else {
                    assert(buttonPresses == seen[origin]!! * cycleLengths[origin]!!)
                }

                if (seen.values.all { it >= 1 }) {
                    return lcm(cycleLengths.values.toList())
                }
            }

            if (module.type == "%") {
                if (pulse == "low") {
                    module.memory["%"] = if (module.memory["%"] == "off") "on" else "off"
                    val outgoingPulse = if (module.memory["%"] == "on") "high" else "low"
                    module.outputs.forEach { output ->
                        pulsesQueue.add(Triple(module.name, output, outgoingPulse))
                    }
                }
            } else {
                module.memory[origin] = pulse
                val outgoingPulse = if (module.memory.values.all { it == "high" }) "low" else "high"
                module.outputs.forEach { output ->
                    pulsesQueue.add(Triple(module.name, output, outgoingPulse))
                }
            }
        }
    }
}



fun main() {
    createTestFiles(AOC_DAY)

    val testInput = readInputToList(TEST_FILE)
//    val part1ExpectedRes = 32000000
    val part1ExpectedRes = 11687500
    println("---| TEST INPUT |---")
    println("* PART 1:   ${part1(testInput)}\t== $part1ExpectedRes")

//    val part2ExpectedRes = -1
//    println("* PART 2:   ${part2(testInput)}\t== $part2ExpectedRes\n")


    val input = readInputToList(INPUT_FILE)
    val improving = true
    println("---| FINAL INPUT |---")
    println("* PART 1: ${part1(input)}${if (improving) "\t\t\t== 712543680" else ""}")
    println("* PART 2: ${part2(input)}${if (improving) "\t== 238920142622879" else ""}")
}
