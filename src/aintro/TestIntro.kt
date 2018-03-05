package aintro

import org.junit.Assert
import org.junit.Test

class TestIntro {
    @Test fun `test if start is OK`() {
        Assert.assertEquals("OK", start(true))
    }

    @Test fun `test if start is Not OK`() {
        Assert.assertEquals("not OK", start(false))
    }

    @Test fun `test if function with named parameters returns a list in JSON format`() {
        Assert.assertEquals("[a,b,c]", joinOptions(listOf("a", "b", "c")))
    }

    @Test fun `test default and named parameters optimizing Java overloads`() {
        Assert.assertEquals(listOf("a42", "b1", "C42", "D2"), useFoo())
    }

    @Test fun `test valid month`() {
        testMatch("24 AUG 1999")
    }

    @Test fun `test invalid month`() {
        testMisMatch("11 XYZ 2002")
    }

    @Test fun `test send email when all info is provided`() {
        testSendMessageToClient(Client(PersonalInfo(
                "bob@somewhere.com")),
                "Hi Bob, How are you?...",
                "bob_group@somemail.com",
                true)
    }

    @Test fun `test send email when there is no client`() {
        testSendMessageToClient(
                null,
                ""
        )
    }

    @Test fun `test send mail when there is no message`() {
        testSendMessageToClient(
                Client(PersonalInfo("bob@somemail.com")),
                null
        )
    }

    @Test fun `test send mail when client doesn't have personal info`() {
        testSendMessageToClient(
                Client(null),
                "Hi Bob, How are you?..."
        )
    }

    @Test fun `test send mail when there is no email`() {
        testSendMessageToClient(
                Client(PersonalInfo(null)),
                "Hi Bob, How are you?..."
        )
    }

    // test SmartCasts.kt
    @Test fun testNum() {
        Assert.assertEquals("'eval' on Num should work:", 2, eval(Num(2)))
    }
    @Test fun testSum() {
        Assert.assertEquals("'eval' on Sum should work: ", 3, eval(Sum(Num(2), Num(1))))
    }
    @Test fun testRecusrion() {
        Assert.assertEquals("'eval' should work: ", 6,  eval(Sum(Sum(Num(2), Num(1)), Num(3))))
    }

    // test ExtensionFunctions.kt
    @Test fun `test if the extended method r() works for class Int`() {
        Assert.assertEquals("Rational number creation error: ", RationalNumber(4, 1), 4.r())
    }

    @Test fun `test if the extended method r() works for class Pair`() {
        Assert.assertEquals("Rational number creation error: ", RationalNumber(2,3), Pair(2,3).r())
    }

    // test ObjectExpressions.kt
    @Test fun `test if the list is sorted correctly`() {
        Assert.assertEquals("getList", listOf(5, 2, 1), getList())
    }

    // test ExtensionsOnCollections
    @Test fun `test if the standard Kotlin extended function on Java list collection works`() {
        Assert.assertEquals(listOf(5, 2, 1), getSortedList())
    }
}

private fun testMatch(date: String) = Assert.assertTrue("The pattern should match $date", date.matches(getPattern().toRegex()))

private fun testMisMatch(date: String) = Assert.assertFalse("The pattern shouldn't match $date", date.matches(getPattern().toRegex()))

private fun testSendMessageToClient(
        client: Client?,
        message: String?,
        expectedEmail: String? = null,
        shouldBeInvoked: Boolean = false
) {
    var invoked = false
    val expectedMessage = message
    sendMessageToClient(client, message, object : Mailer { // an object expression play the same role as anonymous classes!
        override fun sendMessage(email: String, message: String) {
            invoked = true
            Assert.assertEquals("The message is not as expected: ", expectedMessage, message)
            Assert.assertEquals("The email is not as expected: ", expectedEmail, email)
        }

    })
    Assert.assertEquals(
            "The function 'sendMessage' should${if (shouldBeInvoked) "" else "n't"} be invoked",
            shouldBeInvoked,
            invoked
    )
}