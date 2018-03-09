package xtra

// kotlinx-coroutines-core library provides more useful co-routines such as launch(),
import kotlinx.coroutines.experimental.*
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.system.measureTimeMillis
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestCoroutines {
    @Test fun `test if fibonacciSeq produces a fibonacci sequence`() {
        Assert.assertEquals(fibonacciSeq.take(5).toList(), listOf(1, 1, 2, 3, 5))
    }

    // launch()
    @Test fun `given asynchronous co-routine, when start, it should then execute in the asynchronous way`() {
        // given
        val res = mutableListOf<String>()

        // when
        runBlocking<Unit> {
            val promise = launch(CommonPool) {
                expensiveComputation(res) // launch is a co-routine that will execute the suspend function in a non-blocking way - we need to pass a thread pool to it
            }
            println("Since there is no blocking, the main thread will continue and add 'Hello,'")
            res.add("Hello,")
            promise.join() // launch() will return a Job instance on which we can call join() to wait for the results
        } // runBlocking() allows all these non-blocking to become blocking again so the main thread will block until the co-routine inside completes.

        //then
        assertEquals(res, listOf("Hello,", "word!")) // although launch() is triggered first, "Hello, " were still added first before "word!" because
        // the suspend function only suspends the co-routine which in this case is the launch() but not suspends the calling thread so
        // the calling thread still continue and add "Hello," first.
        //assertEquals(res, listOf("word!", "Hello,")) // Test won't pass!
    }


    // lightweight!
    @Test fun `given a huge amount of co-routine, when start, it should execute without Out Of Memory`() {
        runBlocking<Unit> {
            // given
            val counter = AtomicInteger(0)
            val numberOfCoroutines = 100_000 // perform 100000 operations asynchronously

            // when
            val jobs = List(numberOfCoroutines) {
                launch(CommonPool) {
                    //println("Thread ${Thread.currentThread().id} started work: current value of counter = ${counter.get()}")
                    delay(1000L)
                    counter.incrementAndGet() // TODO: do some more complicated calculation! or try something with IO
                    //println("Thread ${Thread.currentThread().id} finished work: current value of counter = ${counter.get()}")
                }
            }
            jobs.forEach { it.join() }

            // then
            println(counter.get())
            assertEquals(counter.get(), numberOfCoroutines)
        }
    }

    // cancel()
    @Test fun `given cancellable Job, when request for cancel, then should quit`() {
        runBlocking<Unit> {
            // given
            val job = launch(CommonPool) {
                while (isActive) {
                    println("is working")
                }
            }
            delay(5000L)

            // when
            job.cancel()

            // then cancel successfully
            assertFalse { job.isActive }
        }
    }

    // withTimeout()
    @Test (expected = TimeoutCancellationException::class)
    fun `given a asynchronous action, when declare time out, then should finish when time out`() {
        runBlocking<Unit> {
            withTimeout(49999L) {
                repeat(100) {
                    println("Some expensive computation $it ...")
                    delay(500L)
                } // all these routines in the co-routine scope will takes 50_000, so timeout should be set lower!
            }
        }
    }

    // async() and await()
    @Test fun `given two ore more expensive actions, when execute them asynchronously, they should run concurrently`() {
        runBlocking<Unit> {
            val delay = 1000L
            val time = measureTimeMillis {
                // given
                val one = async(CommonPool) {
                    someExpensiveComputation(delay)
                }
                val two = async(CommonPool) {
                    someExpensiveComputation(delay)
                }

                // when
                runBlocking {
                    one.await() // waiting for result of both to be returned, the outer co-routine will resume
                    two.await()
                }
            }

            // then
            println("executing two task this way should take around $delay milliseconds: $time.")
            assertTrue(time < delay * 2)
        }
    }

    // async() with LAZINESS
    @Test fun `given two ore more expensive actions, when execute them lazy, they should NOT concurrently`() {
        runBlocking<Unit> {
            val delay = 1000L
            val time = measureTimeMillis {
                // given
                val one = async(CommonPool, CoroutineStart.LAZY) {
                    someExpensiveComputation(delay)
                } // LAZINESS causes the asynchronous scope below run in a synchronous manner,
                val two = async(CommonPool, CoroutineStart.LAZY) {
                    someExpensiveComputation(delay)
                }

                // when
                runBlocking {
                    one.await() // main thread will be blocked
                    two.await() // only task one finishes, task two will be triggered
                }
            }

            // then
            println("executing two task this way should take more than ${delay*2} milliseconds: $time.")
            assertTrue(time > delay * 2)
        }
    }
}