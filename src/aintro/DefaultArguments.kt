package aintro

fun foo(name: String, number: Int = 42, toUpperCase: Boolean = false) =
        (if (toUpperCase) name.toUpperCase() else name).plus(number)


// With default arguments, Kotlin combines all possible Java overloads into just one function above!
fun useFoo() = listOf(
        foo("a"),
        foo("b", number = 1),
        foo("c", toUpperCase = true),
        foo( name = "d", number = 2, toUpperCase = true)
)