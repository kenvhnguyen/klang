package xtradp24Go4.structural

/**
 * A Flyweight is an object that minimizes memory usage by sharing as much data as possible
 * e.g: To print out 'Hello' you should actually use only 4 object H, e, l, o
 * */
fun main(args: Array<String>) {
    val cf = GraphicCharFactory()
    val text = ArrayList<GraphicChar>()
    text.add(cf.get('H', "Arial"))
    text.add(cf.get('e', "Arial"))
    text.add(cf.get('l', "Arial"))
    text.add(cf.get('l', "Arial"))
    text.add(cf.get('o', "Times"))

    println("Character factory created ${cf.getNum()} object(s) for ${text.size} characters(s).\n")

    /**
     * the character and the font which is part of the object are seen as intrinsic
     * external data such as position (extrinsic)
     * */
    var x = 0
    val y = 0
    text.forEach({it.printAtPosition(x++, y)})
}

class GraphicChar(private val c: Char, private val fontFace: String) {
    fun printAtPosition(x: Int, y: Int) {
        println("Printing $c in $fontFace at position $x:$y")
    }
}

class GraphicCharFactory {
    private val pool = HashMap<String, GraphicChar>() // the Flyweights
    fun getNum(): Int{ return pool.size }
    fun get(c: Char, fontFace: String): GraphicChar {
        val graphicChar: GraphicChar
        val key = c.toString() + fontFace
        if (!pool.containsKey(key)) {
            graphicChar = GraphicChar(c, fontFace)
            pool[key] = graphicChar
        }
        return pool[key]!!
    }
}
