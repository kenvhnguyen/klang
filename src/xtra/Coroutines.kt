package xtra

import kotlinx.coroutines.experimental.delay
import kotlin.coroutines.experimental.buildSequence

// A co-routine allows us to call a suspend function without compilation error.
// buildSequence is the main building block of every co-routine and it is a co-routine that comes in with the standard library
val fibonacciSeq = buildSequence {
    var a = 0
    var b = 1

    yield(1) // suspend keyword means that this function can be blocking a thus can only be call inside a coroutine like buildSequence

    while(true) {
        yield(a + b)

        val tmp = a + b
        a = b
        b = tmp
    }
}

val fibonacci = buildSequence {
    var terms = Pair(0, 1)
    while(true) {
        yield(terms.first)
        terms = Pair(terms.second, terms.first + terms.second)
    }
}


/**
 * mocking some long-running operation (network IO, file IO, CPU or GPU intensive work, etc)
 * */
suspend fun expensiveComputation(res: MutableList<String>) {
    delay(10000L) // delay() is a suspend function that will block, this could mimic any long-running operation
    res.add("word!")
}

suspend fun someExpensiveComputation(delay: Long) {
    println("Thread ${Thread.currentThread().id} is running...")
    delay(delay)
}

fun main(args: Array<String>) {
    println(fibonacci.take(7).toList())
    println(fibonacciSeq.take(15).toList())

    //println(expensiveComputation()) // Compile error: only a co-routine can accommodate a suspend or blocking function.
}

