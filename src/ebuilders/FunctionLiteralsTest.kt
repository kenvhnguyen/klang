package ebuilders

import org.junit.Assert
import org.junit.Test

class FunctionLiteralsTest {
    @Test fun `test if all function literals work`() {
        Assert.assertEquals("The function 'isOdd' and 'isEven' should be implemented correctly", listOf(false, true, true, 3435), task())
    }
}