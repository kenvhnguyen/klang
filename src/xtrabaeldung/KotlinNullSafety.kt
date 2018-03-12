package xtrabaeldung

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class KotlinNullSafety {
    @Test fun `test KotlinNullSafety`() {
        // safe calls with ?
        var b: String? = "value"
        b = null
        //if (b != null) println(b.length) else assertNull(b)
        println(b?.length)

        // let()
        val firstName = "Tom"
        val lastName = "Michael"
        val names: List<String?> = listOf(firstName, null, lastName)

        var res = listOf<String?>()
        names.forEach {
            it?.let {
                res = res.plus(it) // let() doesn't work on null, so this is a way to filter out null value, otherwise res will have null also
            }
        }

        assertEquals(2, res.size)

        // also() // provides extra action to let()
        names.forEach {
            it?.let { res = res.plus(it); it }.also { println("processed value: $it") }
        }

        // run() operates on 'this'!
        for (item in names) {
            item?.run { res = res.plus(this) }
        }

        // Elvis operator ?:
        println(b?.length ?: -1) // instead of printing null, we can print out a reference

        // Nullable Unsafe Get or Asserted call
        assertFailsWith<NullPointerException> { b!!.length }
        b = "revalue"
        assertEquals(b!!.length, 7)

        // filtering null values
        val notNulls = names.filterNotNull()
        assertEquals(notNulls.size, 2)
    }
}