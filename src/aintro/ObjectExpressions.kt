package aintro

import java.util.*
import kotlin.Comparator

fun getList(): List<Int> {
    val arrayList = arrayListOf(1, 5, 2)
    Collections.sort(arrayList, object : Comparator<Int> { // Comparator is a SAM interface, one with a Single Access Method
        override fun compare(o1: Int, o2: Int): Int {
            return o2 - o1
        }
    } // thus you can replace this object expression with a lamda as following:
            // { (o2, o1) -> o2 - o1 }
    )
    return arrayList
}