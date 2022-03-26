package github.bb441db.compose.strings.helpers

import org.w3c.dom.NamedNodeMap
import org.w3c.dom.Node
import org.w3c.dom.NodeList

operator fun NamedNodeMap.get(index: String): String? {
    return this.getNamedItem(index)?.nodeValue
}

fun NodeList.iterator(): Iterator<Node> {
    return object : Iterator<Node> {
        private var index = 0

        override fun hasNext(): Boolean {
            return index < length
        }

        override fun next(): Node {
            val node = item(index)
            index += 1
            return node
        }
    }
}

fun NodeList.iterable(): Iterable<Node> {
    return object : Iterable<Node> {
        override fun iterator(): Iterator<Node> {
            return this@iterable.iterator()
        }
    }
}