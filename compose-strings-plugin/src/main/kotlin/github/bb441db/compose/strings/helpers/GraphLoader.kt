package github.bb441db.compose.strings.helpers

import github.bb441db.compose.strings.model.Graph
import github.bb441db.compose.strings.parser.FormatParser
import org.gradle.api.GradleScriptException
import org.gradle.api.file.FileCollection
import java.io.File
import java.util.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.SAXParserFactory

class GraphLoader(
    private val name: String,
    private val separator: String,
) {
    fun load(files: Set<File>): Graph {
        return files
            .fold(initial = Graph(name)) { graph, file ->
                load(file, graph)
            }
    }

    fun load(file: File, graph: Graph = Graph(name)): Graph {
        val document = documentBuilder.newDocument()
        val handler = XmlLineHandler(document)
        parser.parse(file, handler)

        return document
            .getElementsByTagName("string")
            .iterable()
            .fold(graph) { acc, node ->
                val lineNumber = node.getUserData("lineNumber") as Int
                val columnNumber = node.getUserData("columnNumber") as Int
                val xmlName = requireNotNull(node.attributes["name"])
                val xmlValue = node.textContent
                val format = try {
                    FormatParser.parse(xmlValue)
                } catch (e: IllegalFormatException) {
                    throw GradleScriptException(
                        "${e.javaClass.simpleName} at ${file.absolutePath}: ($lineNumber, $columnNumber):",
                        e
                    )
                }

                acc.put(
                    xmlName = xmlName,
                    xmlValue = xmlValue,
                    format = format,
                    identifier = computeJavaName(xmlName),
                    separator = separator
                )
            }
    }

    private companion object {
        private val parser = SAXParserFactory
            .newInstance()
            .newSAXParser()
        private val documentBuilder = DocumentBuilderFactory
            .newInstance()
            .newDocumentBuilder()
    }
}