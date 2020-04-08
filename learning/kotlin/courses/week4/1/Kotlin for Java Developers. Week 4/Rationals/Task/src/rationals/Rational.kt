package rationals

import java.math.BigInteger

class Rational(val n: BigInteger, val d: BigInteger) {
    operator fun plus(other: Rational): Rational {
        val result = Rational((this.n * other.d) + (other.n * this.d), (this.d * other.d))
        return result.reduction()
    }

    private fun gcd(): BigInteger {
        return this.n.gcd(d)
    }

    private fun reduction(): Rational {
        return when (val gcd = this.gcd()) {
            BigInteger.ONE -> this
            else -> Rational(this.n / gcd, this.d / gcd).reduction()
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Rational) return false
        val a = this.toString()
        val b = other.toString()
//        return (a.n == b.n) && (a.d == b.d)
        return a == b
    }

    operator fun minus(other: Rational): Rational {
        return this.plus(Rational((other.n * -BigInteger.ONE), other.d))
    }

    operator fun times(other: Rational): Rational {
        return Rational((this.n * other.n), (this.d * other.d))
    }

    operator fun div(other: Rational): Rational {
        return this.times(Rational(other.d, other.n))
    }

    operator fun unaryMinus(): Rational {
        return Rational((this.n * -BigInteger.ONE), this.d)
    }

    override fun toString(): String {
        val reduction = this.reduction()
        return when {
            BigInteger.ONE == reduction.d -> {
                "${reduction.n}"
            }
            BigInteger.ZERO == reduction.d -> {
                throw throw IllegalArgumentException("Denominator cannot be zero.")
            }
            BigInteger.ZERO > reduction.d -> {
                "${reduction.n * -BigInteger.ONE}/${reduction.d * -BigInteger.ONE}"
            }
            else -> "${reduction.n}/${reduction.d}"
        }
    }

    operator fun compareTo(other: Rational): Int {
        val a = this.times(Rational(other.d, other.d))
        val b = other.times(Rational(this.d, this.d))
        return when {
            a.n < b.n -> {
                -1
            }
            a.n == b.n -> {
                0
            }
            else -> {
                1
            }
        }
    }

    operator fun rangeTo(other: Rational): RationalRange {
        return RationalRange(this, other)
    }

    class RationalRange(private val start: Rational, private val endInclusive: Rational) {
        operator fun contains(other: Rational): Boolean {
            return (start.n * endInclusive.d * other.d) <= (other.n * start.d * endInclusive.d) &&
                    (other.n * start.d * endInclusive.d) <= (endInclusive.n * start.d * other.d)
        }
    }

}

infix fun Number.divBy(other: Number): Rational {
    val n = BigInteger.valueOf(this.toLong())
    val d = BigInteger.valueOf(other.toLong())

    return when {
        BigInteger.ZERO == d -> {
            throw IllegalArgumentException("Denominator cannot be zero.")
        }
        BigInteger.ZERO > d -> {
            Rational(-n, -d)
        }
        else -> {
            Rational(n, d)
        }
    }
}

fun String.toRational(): Rational {
    return if (this.contains('/')) {
        val e = this.split('/')
        Rational(BigInteger(e[0]), BigInteger(e[1]))
    } else {
        Rational(BigInteger(this), BigInteger.ONE)
    }
}

fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val sum: Rational = half + third
    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
    println((-2 divBy 4).toString() == "-1/2")
    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)

    println(half in third..twoThirds)

    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println(
        "912016490186296920119201192141970416029".toBigInteger() divBy
                "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2
    )
}