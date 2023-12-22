private const val AOC_DAY = "Day22"
private const val INPUT_FILE = AOC_DAY

private data class Brick(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int, val minZ: Int, val maxZ: Int) {

    fun down(): Brick = Brick(minX, maxX, minY, maxY, minZ - 1, maxZ - 1)

    fun supports(b: Brick) =
        minX <= b.maxX && maxX >= b.minX && minY <= b.maxY && maxY >= b.minY && minZ <= b.maxZ - 1 && maxZ >= b.minZ - 1

    fun isUnsupported(stack: List<Brick>, disintegrated: Set<Brick> = emptySet()) =
        minZ > 1 && stack.none { it.supports(this) && it !== this && it !in disintegrated }


    companion object {
        operator fun <T> List<T>.component6() = this[5]
        fun parse(s: String): Brick {
            val (x1, y1, z1, x2, y2, z2) = s.split(',', '~').map { it.toInt() }
            return Brick(minOf(x1, x2), maxOf(x1, x2), minOf(y1, y2), maxOf(y1, y2), minOf(z1, z2), maxOf(z1, z2))
        }
    }
}

private class BrickStack(input: List<String>) {

    val bricks = buildList<Brick> {
        for (brick in input.map { Brick.parse(it) }.sortedBy { it.minZ }) {
            var pos = brick
            while (pos.isUnsupported(this))
                pos = pos.down()
            add(pos)
        }

        sortBy { it.minZ }
    }

    fun canDisintegrate(disintegratedBrick: Brick): Boolean {
        val disintegrated = setOf(disintegratedBrick)
        return bricks.none { it.isUnsupported(bricks, disintegrated) }
    }

    fun countFalls(initial: Brick): Int {
        val work = mutableListOf(initial)
        val disintegrated = mutableSetOf(initial)

        while (work.isNotEmpty()) {
            val current = work.removeLast()

            for (brick in bricks) {
                // If brick is below other, it can't possibly fall
                if (brick.maxZ < current.minZ)
                    continue

                // Brick can't be directly supported by any remaining since the stack is sorted by Z
                if (brick.minZ > current.maxZ + 1)
                    break

                if (brick !in disintegrated && brick.isUnsupported(bricks, disintegrated)) {
                    disintegrated.add(brick)
                    work.add(brick)
                }
            }
        }

        return disintegrated.size - 1
    }
}

fun main() {

    fun part1(input: List<String>): Int {
        val stack = BrickStack(input)
        return stack.bricks.count { stack.canDisintegrate(it) }
    }

    fun part2(input: List<String>): Int {
        val stack = BrickStack(input)
        return stack.bricks.sumOf { stack.countFalls(it) }
    }

    val input = readInputToList(INPUT_FILE)
    part1(input).println()
    part2(input).println()
}

/* // Python solution
from collections import deque

bricks = [list(map(int, line.replace("~", ",").split(","))) for line in open(0)]
bricks.sort(key=lambda brick: brick[2])

def overlaps(a, b):
    return max(a[0], b[0]) <= min(a[3], b[3]) and max(a[1], b[1]) <= min(a[4], b[4])

for index, brick in enumerate(bricks):
    max_z = 1
    for check in bricks[:index]:
        if overlaps(brick, check):
            max_z = max(max_z, check[5] + 1)
    brick[5] -= brick[2] - max_z
    brick[2] = max_z

bricks.sort(key=lambda brick: brick[2])

k_supports_v = {i: set() for i in range(len(bricks))}
v_supports_k = {i: set() for i in range(len(bricks))}

for j, upper in enumerate(bricks):
    for i, lower in enumerate(bricks[:j]):
        if overlaps(lower, upper) and upper[2] == lower[5] + 1:
            k_supports_v[i].add(j)
            v_supports_k[j].add(i)

total = 0

# Part 1
for i in range(len(bricks)):
    if all(len(v_supports_k[j]) >= 2 for j in k_supports_v[i]):
        total += 1

# Part 2
for i in range(len(bricks)):
    q = deque(j for j in k_supports_v[i] if len(v_supports_k[j]) == 1)
    falling = set(q)
    falling.add(i)

    while q:
        j = q.popleft()
        for k in k_supports_v[j] - falling:
            if v_supports_k[k] <= falling:
                q.append(k)
                falling.add(k)

    total += len(falling) - 1

print(total)
*/