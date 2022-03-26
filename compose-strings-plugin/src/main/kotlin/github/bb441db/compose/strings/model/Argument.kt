package github.bb441db.compose.strings.model

import kotlin.reflect.KClass

data class Argument(
    val name: String,
    val type: KClass<*>,
)