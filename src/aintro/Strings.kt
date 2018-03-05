package aintro

val month = "(JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEV)"

fun getPattern(): String = """\d{2} $month \d{4}"""