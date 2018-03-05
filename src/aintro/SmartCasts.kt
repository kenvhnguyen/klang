package aintro

interface Expr

fun eval(expr: Expr): Int =
        when (expr) {
            is Num -> expr.value
            is Sum -> eval(expr.left) + eval(expr.right) // Recursive!!!
            else -> throw IllegalArgumentException("Unknown expression")
        }

class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr