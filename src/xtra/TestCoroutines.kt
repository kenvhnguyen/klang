package xtra

// kotlinx-coroutines-core provides more useful co-routines such as runBlocking, launch
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals

class TestCoroutines {
    @Test fun `test if fibonacciSeq produces a fibonacci sequence`() {
        Assert.assertEquals(fibonacciSeq.take(5).toList(), listOf(1, 1, 2, 3, 5))
    }

    @Test fun `given asynchronous co-routine, when start, it then should execute in the asynchronous way`() {
        // given
        val res = mutableListOf<String>()

        // when
        runBlocking<Unit> {
            val promise = launch(CommonPool) {
                expensiveComputation(res) // launch is a co-routine that will execute the suspend function in a non-blcoking way - we need to pass a thread pool to it
            }
            println("Main thread will add 'Hello,' first")
            res.add("Hello,")
            promise.join() // launch() will return a Job instance on which we can call join() to wait for the results
        }

        //then
        assertEquals(res, listOf("Hello,", "word!")) // although launch() is triggered first, it is a delay computation, the main thread will add "Hello, " first before "word!" is added
        //assertEquals(res, listOf("word!", "Hello,")) // Test won't pass!
    }

    val test = "blab blah"

    // lightweight!
    @Test fun `given a huge amount of co-routine, when start, it should execute without Out Of Memory`() {
        runBlocking<Unit> {
            // given
            val counter = AtomicInteger(0)
            val numberOfCoroutines = 100_000 // perform 100000 operations asynchronously

            // when
            val jobs = List(numberOfCoroutines) {
                launch(CommonPool) {
                    println("Thread ${Thread.currentThread().id} started work: current value of counter = ${counter.get()}")
                    delay(1000L)
                    counter.incrementAndGet() // TODO: do some more complicated calculation! or try something with IO
                    println("Thread ${Thread.currentThread().id} finished work: current value of counter = ${counter.get()}")
                }
            }
            jobs.forEach { it.join() }

            // then
            println(counter.get())
            assertEquals(counter.get(), numberOfCoroutines)
        }
    }
}