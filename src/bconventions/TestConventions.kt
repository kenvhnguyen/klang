package bconventions

import org.junit.Assert
import org.junit.Test

class TestConventions {
    @Test fun `test before date`() {
        val first = MyDate(2014, 5, 10)
        val second = MyDate(2014, 7, 11)
        Assert.assertTrue(first < second)
    }
    @Test fun `test after date`() {
        val first = MyDate(2014, 10, 20)
        val second = MyDate(2014, 7, 11)
        Assert.assertTrue(first > second)
    }

    // Test InRange.kt
    fun doTest1(date: MyDate, first: MyDate, last: MyDate, shouldBeInRange: Boolean) {
        val message = "${date} should${if (shouldBeInRange) "" else "n't"} be in ${DateRange(first, last)}"
        Assert.assertEquals(message, shouldBeInRange, checkInRange(date, first, last))
    }
    @Test fun `test if a date is in range`() {
        doTest1(MyDate(2018, 3, 22), MyDate(2018, 1, 1), MyDate(2019, 1, 1), shouldBeInRange = true)
    }
    @Test fun `test if a date is before a range`() {
        doTest1(MyDate(2017, 3, 22), MyDate(2018, 1, 1), MyDate(2019, 1, 1), shouldBeInRange = false)
    }
    @Test fun `test if a date is after a range`() {
        doTest1(MyDate(2019, 3, 22), MyDate(2018, 1, 1), MyDate(2019, 1, 1), shouldBeInRange = false)
    }

    // Test RangeTo.kt
    fun doTest(date: MyDate, first: MyDate, last: MyDate, shouldBeInRange: Boolean) {
        val message = ""
        Assert.assertEquals(message, shouldBeInRange, checkInRange(date, first, last))
    }
    @Test fun `test if a date should be in a range`() {
        doTest(MyDate(2014, 3, 22), MyDate(2014, 1, 1), MyDate(2015, 1,1), shouldBeInRange = true)
    }
}
