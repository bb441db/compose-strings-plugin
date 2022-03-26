package github.bb441db.compose.strings.helpers

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.xml.sax.Attributes
import org.xml.sax.Locator
import org.xml.sax.helpers.DefaultHandler
import java.util.*

class XmlLineHandler(private val document: Document) : DefaultHandler() {
    private var locator: Locator? = null
    private val stack = Stack<Element>()
    private val textBuffer = StringBuilder()

    override fun startElement(
        uri: String?,
        localName: String?,
        qName: String?,
        attributes: Attributes?
    ) {
        checkTextBuffer()
        val element = document.createElement(qName)
        if (attributes != null) {
            for (i in 0 until attributes.length) {
                element.setAttribute(attributes.getQName(i), attributes.getValue(i))
            }
        }
        val locator = this.locator
        if (locator != null) {
            element.setUserData("lineNumber", locator.lineNumber, null)
            element.setUserData("columnNumber", locator.columnNumber, null)
        }
        stack.push(element)
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {
        checkTextBuffer()
        val closed = stack.pop()
        if (stack.isEmpty()) {
            document.appendChild(closed)
        } else {
            val parent = stack.peek()
            parent.appendChild(closed)
        }
    }

    override fun setDocumentLocator(locator: Locator?) {
        this.locator = locator
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        textBuffer.append(ch, start, length)
    }

    private fun checkTextBuffer() {
        if (textBuffer.isNotEmpty()) {
            if (stack.isNotEmpty()) {
                val element = stack.peek()
                val textNode = document.createTextNode(textBuffer.toString())
                element.appendChild(textNode)
            } else {
                document.appendChild(document.createTextNode(textBuffer.toString()))
            }
            textBuffer.clear()
        }
    }
}