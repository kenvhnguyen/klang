package ebuilders

fun usage() {
    val map1: Map<Int, String> = buildMap {
        put(0, "0")
        for (i in 1..10) {
            put(i, "$i")
        }
    }

    val map2: Map<String, String> = buildMap {
        put("Ken", "4545454")
        put("Alisa", "4545454")
        put("Frans", "343434")
    }

    val string1: String = "Hello".doCertainThingToAnything {
        toString() + toString()
    }
}

// build is a function literal which could be doing anything to a map
fun <K, V> buildMap(doSomething: HashMap<K, V>.() -> Unit): Map<K, V> {
    val map = hashMapOf<K, V>()
    map.doSomething() // or you can just do: map.apply(doSomething)
    return map
}


fun <T> T.doCertainThingToAnything(f: T.() -> Unit): T { return apply(f) }