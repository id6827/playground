package mastermind

import kotlin.math.min


data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    fun evaluateRight(secret: String, guess: String): Int {
//        secret.zip(guess).count{p -> p.first == p.second }
        return secret.zip(guess).count { it.first == it.second }
    }

    fun evaluateWrong(secret: String, guess: String): Int {
        fun String.toCountMap(): Map<Char, Int> {
            return this.groupBy { it }.map { it.key to it.value.size }.toMap()
        }
        val (s, g) = secret.zip(guess).filter { p -> p.first != p.second }.map { it.first }.joinToString("") to secret.zip(
            guess
        ).filter { p -> p.first != p.second }.map { it.second }.joinToString("")
        val sMap = s.toCountMap()
        val gMap = g.toCountMap()
        return sMap.map { sEntry -> min(sEntry.value, gMap.getOrDefault(sEntry.key, 0)) }.sum()
    }

    return Evaluation(evaluateRight(secret, guess), evaluateWrong(secret, guess))
}
