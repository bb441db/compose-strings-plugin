package github.bb441db.compose.strings.helpers

import github.bb441db.compose.strings.model.FormatString
import java.math.BigDecimal
import java.math.BigInteger
import java.time.temporal.TemporalAccessor
import java.util.*
import kotlin.reflect.KClass

class TypeMapping(private val options: TypeMappingOptions) {
    private fun build(block: MutableList<KClass<*>>.() -> Unit): Array<KClass<*>> {
        val list = mutableListOf<KClass<*>>()
        block(list)
        return list.toTypedArray()
    }

    private fun MutableList<KClass<*>>.add(type: KClass<*>, condition: Boolean) {
        if (condition) {
            add(type)
        }
    }

    fun map(type: FormatString.Type): Array<KClass<*>> {
        return when (type) {
            FormatString.Type.String -> arrayOf(String::class)
            FormatString.Type.Character -> build {
                add(Char::class)
                add(Byte::class, condition = options.useByteCharacter)
                add(Short::class, condition = options.useShortCharacter)
            }
            FormatString.Type.Boolean -> arrayOf(Boolean::class)
            FormatString.Type.Integral -> build {
                add(Int::class)
                add(Long::class)
                add(Byte::class, condition = options.useByteIntegral)
                add(Short::class, condition = options.useShortIntegral)
                add(BigInteger::class, condition = options.useBigInteger)
            }
            FormatString.Type.FloatingPoint -> build {
                add(Float::class)
                add(Double::class)
                add(BigDecimal::class, condition = options.useBigDecimal)
            }
            FormatString.Type.DateTime -> build {
                add(Date::class)
                add(Calendar::class)
                add(TemporalAccessor::class, condition = options.useTemporalAccessor)
                add(Long::class, condition = options.useLongDate)
            }
            FormatString.Type.Hashcode -> arrayOf(Any::class)
            FormatString.Type.General -> arrayOf(Any::class)
        }
    }
}