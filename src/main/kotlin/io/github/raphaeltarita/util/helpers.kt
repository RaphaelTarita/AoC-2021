package io.github.raphaeltarita.util

import kotlinx.datetime.LocalDate
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.EnumSet
import kotlin.io.path.Path
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.ulp

// ---------------------------------
// =========== AoC utils ===========
// ---------------------------------

//----------------------------------------------------------------------------------------------------------------------------------------------------
// maths
fun min(a: Byte, b: Byte) = if (a < b) a else b
fun min(a: Short, b: Short) = if (a < b) a else b

fun max(a: Byte, b: Byte) = if (a > b) a else b
fun max(a: Short, b: Short) = if (a > b) a else b

fun min(a: Byte, b: Byte, c: Byte): Byte = min(min(a, b), c)
fun min(a: Short, b: Short, c: Short): Short = min(min(a, b), c)
fun min(a: Int, b: Int, c: Int): Int = min(min(a, b), c)
fun min(a: Long, b: Long, c: Long): Long = min(min(a, b), c)
fun min(a: Float, b: Float, c: Float): Float = min(min(a, b), c)
fun min(a: Double, b: Double, c: Double): Double = min(min(a, b), c)

fun max(a: Byte, b: Byte, c: Byte) = max(max(a, b), c)
fun max(a: Short, b: Short, c: Short) = max(max(a, b), c)
fun max(a: Int, b: Int, c: Int): Int = max(max(a, b), c)
fun max(a: Long, b: Long, c: Long): Long = max(max(a, b), c)
fun max(a: Float, b: Float, c: Float): Float = max(max(a, b), c)
fun max(a: Double, b: Double, c: Double): Double = max(max(a, b), c)

fun sqr(x: Byte): Int = x * x
fun sqr(x: Short): Int = x * x
fun sqr(x: Int): Int = x * x
fun sqr(x: Long): Long = x * x
fun sqr(x: Float): Float = x * x
fun sqr(x: Double): Double = x * x

fun Byte.coerce(lower: Byte, upper: Byte): Byte = min(max(this, lower), upper)
fun Short.coerce(lower: Short, upper: Short): Short = min(max(this, lower), upper)
fun Int.coerce(lower: Int, upper: Int): Int = min(max(this, lower), upper)
fun Long.coerce(lower: Long, upper: Long): Long = min(max(this, lower), upper)
fun Float.coerce(lower: Float, upper: Float): Float = min(max(this, lower), upper)
fun Double.coerce(lower: Double, upper: Double): Double = min(max(this, lower), upper)

fun Short.coerceToByte(): Byte = coerce(Byte.MIN_VALUE.toShort(), Byte.MAX_VALUE.toShort()).toByte()
fun Int.coerceToByte(): Byte = coerce(Byte.MIN_VALUE.toInt(), Byte.MAX_VALUE.toInt()).toByte()
fun Int.coerceToShort(): Short = coerce(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
fun Long.coerceToByte(): Byte = coerce(Byte.MIN_VALUE.toLong(), Byte.MAX_VALUE.toLong()).toByte()
fun Long.coerceToShort(): Short = coerce(Short.MIN_VALUE.toLong(), Short.MAX_VALUE.toLong()).toShort()
fun Long.coerceToInt(): Int = coerce(Int.MIN_VALUE.toLong(), Int.MAX_VALUE.toLong()).toInt()
fun Double.coerceToFloat(): Float = coerce(Float.MIN_VALUE.toDouble(), Float.MAX_VALUE.toDouble()).toFloat()

fun sign(b: Byte): Int = if (b > 0) 1 else if (b < 0) -1 else 0
fun sign(s: Short): Int = if (s > 0) 1 else if (s < 0) -1 else 0
fun sign(i: Int): Int = if (i > 0) 1 else if (i < 0) -1 else 0
fun sign(l: Long): Int = if (l > 0) 1 else if (l < 0) -1 else 0
fun sign(f: Float): Int = if (f > 0) 1 else if (f < 0) -1 else 0
fun sign(d: Double): Int = if (d > 0) 1 else if (d < 0) -1 else 0

fun invertInsertionPoint(inverted: Int): Int = -(inverted + 1)

fun Int.pow(other: Double): Double = toDouble().pow(other)
fun Int.pow(other: Int): Double = toDouble().pow(other)
fun cbrt(a: Double): Double = Math.cbrt(a)
fun cbrt(a: Int): Double = Math.cbrt(a.toDouble())

infix fun Double.epsilonEq(other: Double): Boolean {
    val epsilon = max(ulp, other.ulp)
    return this > (other - epsilon) && this < (other + epsilon)
}

//----------------------------------------------------------------------------------------------------------------------------------------------------
// bit operations
private val HEX = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

fun ByteArray.toHexString(offset: Int = 0): String {
    val hexChars = CharArray(size * 2)
    for (i in indices) {
        val v = this[i + offset].toInt() and 0xFF
        hexChars[i * 2] = HEX[v ushr 4]
        hexChars[i * 2 + 1] = HEX[v and 0x0F]
    }
    return hexChars.joinToString("")
}

fun UShort.msb(): Byte = (this.toInt() ushr 8).toByte()
fun UShort.lsb(): Byte = toByte()

fun Short.toByteArray(): ByteArray {
    return byteArrayOf(
        (toInt() ushr 8).toByte(),
        toByte()
    )
}

fun Int.toByteArray(): ByteArray {
    return byteArrayOf(
        (this ushr 24).toByte(),
        (this ushr 16).toByte(),
        (this ushr 8).toByte(),
        toByte()
    )
}

fun Long.toByteArray(): ByteArray {
    return byteArrayOf(
        (this ushr 56).toByte(),
        (this ushr 48).toByte(),
        (this ushr 40).toByte(),
        (this ushr 32).toByte(),
        (this ushr 24).toByte(),
        (this ushr 16).toByte(),
        (this ushr 8).toByte(),
        toByte()
    )
}

fun Float.toByteArray(): ByteArray = toRawBits().toByteArray()
fun Double.toByteArray(): ByteArray = toRawBits().toByteArray()

//----------------------------------------------------------------------------------------------------------------------------------------------------
// collection operations
typealias MapJoin<K, V, U> = (left: Map<K, V>, right: Map<K, U>) -> Map<K, Pair<V?, U?>>

fun ByteArray.chunked(size: Int, offset: Int = 0, until: Int = this.size): List<ByteArray> {
    val thisSize = until - offset
    val resultCapacity = thisSize / size
    val result = ArrayList<ByteArray>(resultCapacity)
    for (i in 0 until resultCapacity) {
        result.add(sliceArray((offset + i * size) until (offset + (i + 1) * size)))
    }
    val remaining = thisSize % size
    if (remaining > 0) {
        val last = ByteArray(size)
        result.add(copyInto(last, startIndex = until - remaining, endIndex = until))
    }

    return result
}

fun <K, V, U> innerJoin(left: Map<K, V>, right: Map<K, U>): Map<K, Pair<V, U>> {
    val result = mutableMapOf<K, Pair<V, U>>()
    for ((key, firstValue) in left) {
        val rightValue = right[key]
        if (rightValue != null) {
            result[key] = firstValue to rightValue
        }
    }
    return result
}

fun <K, V, U> strictInnerJoin(left: Map<K, V>, right: Map<K, U>): Map<K, Pair<V, U>> {
    require(left.keys == right.keys) {
        "attempted to merge two maps with different key sets"
    }
    return innerJoin(left, right)
}

fun <K, V, U> leftOuterJoin(left: Map<K, V>, right: Map<K, U>): Map<K, Pair<V, U?>> {
    val result = mutableMapOf<K, Pair<V, U?>>()
    for ((key, leftValue) in left) {
        result[key] = leftValue to right[key]
    }
    return result
}

fun <K, V, U> rightOuterJoin(left: Map<K, V>, right: Map<K, U>): Map<K, Pair<V?, U>> {
    val result = mutableMapOf<K, Pair<V?, U>>()
    for ((key, rightValue) in right) {
        result[key] = left[key] to rightValue
    }
    return result
}

fun <K, V, U> fullOuterJoin(left: Map<K, V>, right: Map<K, U>): Map<K, Pair<V?, U?>> {
    val result = mutableMapOf<K, Pair<V?, U?>>()
    val rightMutable = right.toMutableMap()
    for ((key, value) in left) {
        result[key] = value to rightMutable.remove(key)
    }

    for ((key, value) in rightMutable) {
        result[key] = null to value
    }

    return result
}

fun <K, V, R> Map<K, V>.ifContainsKey(key: K, action: (Pair<K, V>) -> R): R? {
    return if (containsKey(key)) {
        action(key to this.getValue(key))
    } else {
        null
    }
}

fun <K, V, R> Map<K, V>.ifContainsValue(value: V, action: (Pair<K, V>) -> R): List<R> {
    return if (containsValue(value)) {
        val list = mutableListOf<R>()
        filterValues { it == value }.forEach {
            list.add(action(it.toPair()))
        }
        list
    } else {
        emptyList()
    }
}

fun <K, V> Iterable<Pair<K, V>>.toMap(onDuplicates: (Pair<K, V>) -> Unit): Map<K, V> {
    val res = LinkedHashMap<K, V>()
    for ((k, v) in this) {
        if (res.containsKey(k)) {
            onDuplicates(k to v)
        } else {
            res[k] = v
        }
    }
    return res
}

@Suppress("UNCHECKED_CAST")
internal fun <K, V> Map<K, V?>.filterNonNullValues(): Map<K, V> {
    return filterValues { it != null } as Map<K, V> // .mapValues { it.value!! }
}

internal fun <K, V> combineMaps(vararg maps: Map<K, V>): Map<K, V> {
    val destSize = maps.fold(0) { prev, cur -> prev + cur.size }
    val res = HashMap<K, V>(destSize)
    for (map in maps) {
        res.putAll(map)
    }
    return res
}

fun <T> Collection<T>.optimizeReadonlyCollection(): Collection<T> {
    return when (size) {
        0 -> emptyList()
        1 -> listOf(if (this is List) get(0) else iterator().next())
        else -> this
    }
}

fun <T, I : Iterable<T>> I.exactlyOrNull(n: Int): I? {
    return when (this) {
        is List<*> -> if (size == n) this else null
        else -> {
            val itr = iterator()
            repeat(n) {
                if (!itr.hasNext()) return@exactlyOrNull null
                itr.next()
            }
            if (itr.hasNext()) null else this
        }
    }
}

fun <T, I : Iterable<T>> I.exactly(n: Int): I {
    return exactlyOrNull(n) ?: throw IllegalArgumentException("Iterable does not have exactly $n elements")
}

fun <T> List<T>.withoutElementAtIndex(idx: Int): List<T> {
    val res = ArrayList(this)
    res.removeAt(idx)
    return res
}

infix fun <E : Enum<E>> EnumSet<E>.intersect(other: EnumSet<E>): EnumSet<E> {
    val res = EnumSet.copyOf(this)
    res.retainAll(other)
    return res
}

//----------------------------------------------------------------------------------------------------------------------------------------------------
// comparisons

sealed class NullableCompare<T> {
    data class RESULT<T>(val res: Int) : NullableCompare<T>()
    data class CONTINUE<T>(val o1: T, val o2: T) : NullableCompare<T>()
}

fun <T> compareNullable(n1: T?, n2: T?): NullableCompare<T> {
    return if (n1 == null) {
        if (n2 == null) NullableCompare.RESULT(0)
        else NullableCompare.RESULT(-1)
    } else {
        if (n2 == null) NullableCompare.RESULT(1)
        else NullableCompare.CONTINUE(n1, n2)
    }
}

fun <T, U> comparatorForNested(innerComparator: Comparator<U>, selector: (T) -> U): Comparator<T> {
    return Comparator { o1, o2 ->
        when (val ncomp = compareNullable(o1, o2)) {
            is NullableCompare.RESULT -> ncomp.res
            is NullableCompare.CONTINUE -> innerComparator.compare(selector(ncomp.o1), selector(ncomp.o2))
        }
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------------
// string operations
fun String.replaceMultiple(map: Map<String, String>, ignoreCase: Boolean = false): String {
    var ret = this
    for ((old, new) in map) {
        ret = ret.replace(old, new, ignoreCase)
    }
    return ret
}

fun String.replaceMultiple(vararg entries: Pair<String, String>, ignoreCase: Boolean = false): String {
    return replaceMultiple(mapOf(*entries), ignoreCase)
}

fun String.escape(vararg charsToEscape: Char, ignoreCase: Boolean = false): String {
    return replaceMultiple(charsToEscape.associate { it.toString() to "\\" + it }, ignoreCase)
}

fun String.unescape(vararg charsToUnescape: Char, ignoreCase: Boolean = false): String {
    return replaceMultiple(charsToUnescape.associate { "\\" + it to it.toString() }, ignoreCase)
}

fun String.truncate(maxLen: Int, replacement: String = "..."): String {
    val actualMaxLen = maxLen - replacement.length
    require(actualMaxLen >= 0) { "The truncation replacement has to be smaller than the length bound" }
    return if (length <= actualMaxLen) this else substring(0..actualMaxLen) + replacement
}

fun String.indexOf(regex: Regex, startIndex: Int = 0, notFound: Int = 0): Int {
    return regex.find(this.substring(startIndex))?.range?.start ?: notFound
}

fun String.indexOfFirst(offset: Int, until: Int = lastIndex, predicate: (Char) -> Boolean): Int {
    for (index in offset..until) {
        if (predicate(this[index])) {
            return index
        }
    }
    return -1
}

fun String.indexOfLast(offset: Int, until: Int = lastIndex, predicate: (Char) -> Boolean): Int {
    for (index in (offset..until).reversed()) {
        if (predicate(this[index])) {
            return index
        }
    }
    return -1
}

fun String.indexOfExcluding(
    char: Char,
    exclusionPrefix: String,
    startIndex: Int = 0,
    ignoreCase: Boolean = false,
    ignorePrefixCase: Boolean = false
): Int {
    var exclusionProgress = 0
    var skipNext = false
    for (index in startIndex.coerceAtLeast(0)..lastIndex) {
        if (skipNext) continue

        val current = get(index)

        if (exclusionPrefix[exclusionProgress].equals(current, ignorePrefixCase)) {
            if (++exclusionProgress >= exclusionPrefix.length) {
                skipNext = true
                exclusionProgress = 0
            }
        } else {
            exclusionProgress = 0
        }

        if (current.equals(char, ignoreCase)) return index
    }

    return -1
}

private fun matching(symbol: Char): Char = when (symbol) {
    '(' -> ')'
    '[' -> ']'
    '{' -> '}'
    '<' -> '>'
    else -> throw IllegalArgumentException("Character '$symbol' has no matching counterpart")
}

fun String.indexOfMatching(symbol: Char, offset: Int = 0, matching: Char = matching(symbol)): Pair<Int, Int> {
    val stack = mutableListOf<Unit>()
    var first = -1
    for (i in offset..this.length) {
        if (get(i) == symbol) {
            if (first == -1) first = i
            stack.add(Unit)
        }
        if (get(i) == matching) stack.removeLast()
        if (first != -1 && stack.isEmpty()) return first to i
    }
    return first to -1
}

fun String.splitTopLevel(startIndex: Int = 0, endIndex: Int = lastIndex, trim: Boolean = false): List<String> {
    val offsetInternal = if (trim) {
        indexOfFirst(startIndex.coerceAtLeast(0), endIndex.coerceAtMost(lastIndex)) { !it.isWhitespace() }
    } else {
        startIndex.coerceAtLeast(0)
    }
    val endInternal = if (trim) {
        indexOfLast(startIndex.coerceAtLeast(0), endIndex.coerceAtMost(lastIndex)) { !it.isWhitespace() }
    } else {
        endIndex.coerceAtMost(lastIndex)
    }
    val result = mutableListOf<String>()
    val accumulator = StringBuilder()

    var i = offsetInternal
    while (i <= endInternal) {
        val cur = get(i)

        val prev = i
        when (cur) {
            '{', '[' -> i = indexOfMatching(cur, i).second
            '\"', '\'' -> i = indexOfExcluding(cur, "\\", i + 1)
            ',' -> {
                result.add(accumulator.toString())
                accumulator.clear()
                i = indexOfFirst(i + 1, endInternal) { !it.isWhitespace() }
                continue
            }
        }

        accumulator.append(if (prev == i) cur else substring(prev..i))
        i++
    }
    result.add(accumulator.toString())
    return result
}

fun String.isNumeric(): Boolean {
    return toIntOrNull() != null
}

fun String.capitalizeFirst(): String {
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

fun Char.repeat(n: Int): String {
    return when (n) {
        0 -> ""
        1 -> this.toString()
        else -> String(CharArray(n) { this })
    }
}

//----------------------------------------------------------------------------------------------------------------------------------------------------
// Path & IO

fun composePath(root: Path, child: Path): Path = root.resolve(child)
fun composePath(root: Path, child: String): Path = root.resolve(child)
fun readFromPath(path: Path): String = Files.readAllBytes(path).decodeToString()
fun readFromPath(path: String): String = readFromPath(Paths.get(path))
fun readFromFile(location: File): String = readFromPath(location.toPath())

fun Path.renameTo(new: String): Path {
    ignoreError<FileAlreadyExistsException> {
        Files.move(this, resolveSibling(new))
    }
    return this
}

fun existsOrNull(file: File): File? = if (file.exists()) file else null
fun existsOrNull(file: Path): Path? = if (Files.exists(file)) file else null

fun pathOfDay(day: LocalDate): Path = Path("input/day${day.dayOfMonth}.txt")
fun pathOfDay(num: Int): Path = pathOfDay(day(num))
fun pathToday(): Path = pathOfDay(today())

fun fileOfDay(day: LocalDate): File = File("input/day${day.dayOfMonth}.txt")
fun fileOfDay(num: Int): File = fileOfDay(day(num))
fun fileToday(): File = fileOfDay(today())

fun outputPathOfDay(day: LocalDate): Path = Path("output/day${day.dayOfMonth}.txt")
fun outputPathOfDay(num: Int): Path = outputPathOfDay(day(num))
fun outputPathToday(): Path = outputPathOfDay(today())

fun outputFileOfDay(day: LocalDate): File = File("output/day${day.dayOfMonth}.txt")
fun outputFileOfDay(num: Int): File = outputFileOfDay(day(num))
fun outputFileToday(): File = outputFileOfDay(today())

//----------------------------------------------------------------------------------------------------------------------------------------------------
// generic type operations

typealias Twin<T> = Pair<T, T>

inline fun <reified E> ignoreError(block: () -> Unit) {
    try {
        block()
    } catch (exc: Throwable) {
        if (!E::class.isInstance(exc)) throw exc
    }
}

inline fun <reified E : Throwable, R> nullIfError(block: () -> R): R? {
    return try {
        block()
    } catch (exc: Throwable) {
        if (E::class.isInstance(exc)) null else throw exc
    }
}

fun hashCode(vararg vals: Any?, prime: Int = 31): Int {
    var res = 0
    for (v in vals) {
        res += v.hashCode()
        res *= prime
    }
    return res
}

inline fun <reified E : Enum<E>> Iterable<E>.negate(): Set<E> {
    return enumValues<E>().toSet() - if (this is Set) this else this.toSet()
}