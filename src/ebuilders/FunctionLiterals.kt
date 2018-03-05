package ebuilders

fun task(): List<Any> {
    val isEven = fun Int.(): Boolean = this % 2 == 0
    //val isEven: Int.() -> Boolean = { this % 2 == 0 }

    val isOdd = fun Int.(): Boolean = this % 2 == 1
    //val isOdd: Int.() -> Boolean = { this % 2 == 1 }

    val sum = fun Int.(another: Int): Int =  this + another
    //val sum: Int.(another: Int) -> Int = { this + another }
    //fun Int.sum(another: Int): Int { return this + another }

    return listOf(42.isOdd(), 236.isEven(), 324783748.isEven(), 3434.sum(1))
}