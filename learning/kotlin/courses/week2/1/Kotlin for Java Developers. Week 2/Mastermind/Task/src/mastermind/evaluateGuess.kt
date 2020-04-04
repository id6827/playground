package mastermind

import java.util.*

data class Evaluation(val rightPosition: Int, val wrongPosition: Int)

fun evaluateGuess(secret: String, guess: String): Evaluation {
    var r = 0
    var w = 0

    var targetArray = intArrayOf(-1, -1, -1, -1)
    var s = secret.toCharArray()
    var g = guess.toCharArray()
    s.forEachIndexed { i, c ->
        if (c == g[i]) {
            targetArray[i] = i
            println("완전 일치 char:$c index:$i ${Arrays.deepToString(g.toTypedArray())} ${Arrays.deepToString(s.toTypedArray())}")
            g[i] = 'G'
            s[i] = 'S'
        }
    }

    s.forEachIndexed { i, c ->
        if (g.indexOf(c) == -1) println(
            "없음 char:$c index:$i ${Arrays.deepToString(g.toTypedArray())} ${Arrays.deepToString(
                s.toTypedArray()
            )}"
        )
        else {
            targetArray[g.indexOf(c)] = i
            println("위치 다름 char:$c index:$i ${Arrays.deepToString(g.toTypedArray())} ${Arrays.deepToString(s.toTypedArray())}")
            g[g.indexOf(c)] = 'G'
            s[s.indexOf(c)] = 'S'
        }

    }
    targetArray.forEachIndexed { index, value ->
        when {
            value == index -> r++
            targetArray[index] == -1 -> print("")
            else -> w++
        }
    }
    println(targetArray.toList())
    println(Arrays.deepToString(targetArray.toTypedArray()))
    return Evaluation(r, w)
}
////g.toTypedArray().contentDeepToString()?? 70ms
////Arrays.deepToString(g.toTypedArray())?? 35ms