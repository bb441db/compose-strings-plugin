package github.bb441db.compose.strings.helpers

private val illegalCharacterMatcher = "[^a-zA-Z0-9_]".toRegex()
private const val illegalCharacterReplacement = "_"

private fun String.replaceFirst(transform: (Char) -> Char): String {
    val char = firstOrNull() ?: return this
    return replaceRange(0 until 1, transform(char).toString())
}

fun computeGroupName(name: String): String {
    return name.replaceFirst { it.toUpperCase() }
}

fun computeEntryName(name: String): String {
    return computeJavaName(
        name
            .replaceFirst { it.toLowerCase() }
            .replace("^([0-9])".toRegex(), "_$1")
    )
}

fun computeJavaName(xmlName: String): String {
    return xmlName.replace(
        illegalCharacterMatcher,
        illegalCharacterReplacement,
    )
}