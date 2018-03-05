package bconventions

/**
 * Any collection or another entity e.g DateRange below that defines the 'contains' method
 * can use the 'in' keyword as an infix operator to check if a value belongs to a range.
 * */
class DateRange(val start: MyDate, val endInclusive: MyDate) {
    operator fun contains(d: MyDate): Boolean {
        return d > start && d < endInclusive
    }
}

fun checkInRange(date: MyDate, first: MyDate, last: MyDate): Boolean {
    return date in DateRange(first, last)
}