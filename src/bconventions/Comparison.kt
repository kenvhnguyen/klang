package bconventions

data class MyDate(val year: Int, val month: Int, val dayOfMonth: Int) : Comparable<MyDate> {
    /*override fun compareTo(other: MyDate): Int { // return can be lifted out of if else
        return if (!year.equals(other.year)) year - other.year
        else if (!month.equals(other.month)) month - other.month
        else dayOfMonth - dayOfMonth
    }*/

    override fun compareTo(other: MyDate): Int = when { // when is more beautiful than spaghetti if else
        year != other.year -> year - other.year
        month != other.month -> month - other.month
        else -> dayOfMonth - other.dayOfMonth
    }

}

fun compare(date1: MyDate, date2: MyDate) = date1 < date2 // what is this doing here?