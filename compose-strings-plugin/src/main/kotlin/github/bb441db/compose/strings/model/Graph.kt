package github.bb441db.compose.strings.model

import github.bb441db.compose.strings.helpers.computeEntryName
import github.bb441db.compose.strings.helpers.computeGroupName
import kotlin.reflect.KProperty1

data class Graph(
    val name: String,
    val nested: List<Graph> = listOf(),
    val entries: List<Entry> = listOf(),
) {

    fun put(
        xmlName: String,
        xmlValue: String,
        format: List<FormatString>,
        identifier: String,
        separator: String
    ): Graph {
        return put(
            parts = xmlName.split(separator),
            original = xmlName,
            value = xmlValue,
            format = format,
            identifier = identifier
        )
    }

    private fun put(
        parts: List<String>,
        original: String,
        value: String,
        format: List<FormatString>,
        identifier: String
    ): Graph {
        return when (parts.size) {
            0 -> throw RuntimeException("Cannot put empty entry")
            1 -> copy(
                entries = entries + Entry(
                    original = original,
                    name = computeEntryName(parts.first()),
                    value = value,
                    format = format,
                    identifier = identifier,
                ))
            else -> {
                val name = computeGroupName(parts.first())
                val updated = nested.replace(
                    key = Graph::name,
                    value = name,
                ) { it.put(parts.drop(1), original, value, format, identifier) }

                if (updated == nested) {
                    copy(nested = nested + Graph(
                        name = name,
                        parts = parts.drop(1),
                        original = original,
                        value = value,
                        format = format,
                        identifier = identifier,
                    ))
                } else {
                    copy(nested = updated)
                }
            }
        }
    }

    private fun<T: Any, V: Any> List<T>.replace(
        key: KProperty1<T, V>,
        value: V,
        replacer: (T) -> T,
    ): List<T> {
        return map { item ->
            if (key.get(item) == value) {
                replacer(item)
            } else {
                item
            }
        }
    }

    data class Entry(
        val original: String,
        val name: String,
        val value: String,
        val format: List<FormatString>,
        val identifier: String,
    )

    companion object {
        operator fun invoke(
            name: String,
            parts: List<String>,
            original: String,
            value: String,
            format: List<FormatString>,
            identifier: String
        ): Graph {
            return Graph(name)
                .put(parts, original, value, format, identifier)
        }
    }
}

