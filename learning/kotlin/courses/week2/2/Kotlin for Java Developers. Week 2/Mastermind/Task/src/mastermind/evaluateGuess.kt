package mastermind

import kotlin.math.min

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    fun evaluateRight(secret: String, guess: String): Int { // 원래 접근가능하지만 투박하게
        fun calculate(secret: String, guess: String, index: Int): Int {
            // 공집합에서 k -> k+1
            // A,B,C,D -> 0 더는 측정할수가없을 때 공집합.
            // a b  c  d
            //   ||
            // b b  b  c
            return if (index in secret.indices) {
                val matchCount = if (secret[index] == guess[index]) {
                    1
                } else {
                    0
                }
                matchCount + calculate(secret, guess, index + 1)
            } else {
                0
            }
        }

        return calculate(secret, guess, 0)
    }

    fun evaluateWrong(secret: String, guess: String): Int {
        // ABCA
        // A???
        // 공집합부터 생각, A가 매칭되면 A는 무시해도 됨 !!
        fun stripRight(secret: String, guess: String, index: Int = 0): Pair<String, String> {
            return if (index in secret.indices) {
                val unmatchChar = if (secret[index] == guess[index]) {
                    Pair("", "")
                } else {
                    Pair(secret[index].toString(), guess[index].toString())
                }
                val tail = stripRight(secret, guess, index + 1)
                unmatchChar.first + tail.first to unmatchChar.second + tail.second
            } else {
                Pair("", "")
            }
        }

        // 이제 남은애들은 진짜 위치만 다른애들임...
        fun String.toCountMap(): Map<Char, Int> {
            fun toCountMap(str: String, index: Int = 0): Map<Char, Int> {
                // 모든 자료구조는 똑같음, 결국 고차함수 숙제내준게 이거때문임
                return if (index in str.indices) {
                    val char = str[index]
                    val tail = toCountMap(str, index + 1)
                    tail + mapOf(char to tail.getOrDefault(char, 0) + 1)
                } else {
                    emptyMap()
                }
            }
            return toCountMap(this)
        }
        val (s, g) = stripRight(secret, guess)
        val sMap = s.toCountMap()
        val gMap = g.toCountMap()

        return sMap.map { sEntry -> min(sEntry.value, gMap.getOrDefault(sEntry.key, 0)) }.sum()
    }

    return Evaluation(evaluateRight(secret, guess), evaluateWrong(secret, guess))
}
