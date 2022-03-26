package github.bb441db.compose.strings.helpers

import org.gradle.api.plugins.ExtensionContainer

inline fun<reified T> ExtensionContainer.getByType(): T {
    return getByType(T::class.java)
}

inline fun<reified T> ExtensionContainer.create(name: String, vararg args: Any): T {
    return create(name, T::class.java, *args)
}