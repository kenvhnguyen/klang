package ebuilders

import org.junit.Assert
import org.junit.Test

class TestHtmlBuilders {
    @Test
    fun productTableIsFilled() {
        val result = renderProductTable()
        println(result)
        Assert.assertTrue("Product table should contain the corresponding data", result.contains("cactus"))
    }

    @Test fun productTableIsColored() {
        val result = renderProductTable()
        Assert.assertTrue("Product table should be colored", result.contains("bgcolor"))
    }
}