package bconventions

/**
 * In order to create a range with the operator ..
 * the Comparable class 'MyDate' needs to implement the utility rangeTo()
 */
operator fun MyDate.rangeTo(other: MyDate) = DateRange(this, other)

class DateRange(override val start: MyDate, override val endInclusive: MyDate): ClosedRange<MyDate>

fun checkInRange(date: MyDate, first: MyDate, last: MyDate): Boolean {
    return date in first..last
}