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
}
