package ebuilders

fun renderProductTable(): String {
    return html {
        table {
            tr {
                // this refers to a receiver parameter TR of the function literal
                this.td {
                    text("Product")
                }
                td {
                    text("Price")
                } // this td block is a function literal (or "lamda") thus this here is a function invocation!
                td {
                    text("Popularity")
                }
            }
            val products = getProducts()
            products.forEach {
                var i = 1
                tr(color = getTitleColor()) {
                    td(color = getCellColor(i, 0)) {
                        text(it.description)
                    } // color is argument name
                    td(color = getCellColor(i, 1)) {
                        text(it.price)
                    }
                    td(color = getCellColor(i, 2)) {
                        text(it.popularity)
                    }
                }
                i++
            }
        }
    }.toString()
}

fun getTitleColor() = "#b9c9f3"
fun getCellColor(row: Int, column: Int) = if ((row + column) %2 == 0) "#dce4ff" else "#eff2ff"