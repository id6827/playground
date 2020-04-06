package nicestring

fun String.isNice(): Boolean {
//    이 문제는 string과 collection이 익숙해야 잘 풀수있음 / string은 immutable 존재다보니 비슷함 .
//    contains("bu") && contains("ba") 도 좋은데 contains는 a in this로 변환가능함
//    val con1 = "bu" !in this && "ba" !in this && "be" !in this
    val con1 = listOf("bu", "ba", "be").none { it in this }
    val con2 = 3 <= "aeiou".sumBy { a -> this.count { a == it } }
    val con3 = (0 until length - 1).map { this[it] to this[it + 1] }.any { it.first == it.second }
    return listOf(con1, con2, con3).count { it } >= 2
}