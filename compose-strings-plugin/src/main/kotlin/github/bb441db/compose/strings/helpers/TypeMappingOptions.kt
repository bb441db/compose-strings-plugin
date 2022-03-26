package github.bb441db.compose.strings.helpers

import github.bb441db.compose.strings.StringsExtension

data class TypeMappingOptions(
    val useByteIntegral: Boolean,
    val useShortIntegral: Boolean,
    val useBigInteger: Boolean,
    val useBigDecimal: Boolean,
    val useByteCharacter: Boolean,
    val useShortCharacter: Boolean,
    val useLongDate: Boolean,
    val useTemporalAccessor: Boolean,
) {
    companion object {
        val Defaults = TypeMappingOptions(
            useByteIntegral = false,
            useShortIntegral = false,
            useBigInteger = false,
            useBigDecimal = false,
            useByteCharacter = false,
            useShortCharacter = false,
            useLongDate = false,
            useTemporalAccessor = false,
        )

        operator fun invoke(extension: StringsExtension): TypeMappingOptions {
            return TypeMappingOptions(
                useByteIntegral = extension.useByteIntegral,
                useShortIntegral = extension.useShortIntegral,
                useBigInteger = extension.useBigInteger,
                useBigDecimal = extension.useBigDecimal,
                useByteCharacter = extension.useByteCharacter,
                useShortCharacter = extension.useShortCharacter,
                useLongDate = extension.useLongDate,
                useTemporalAccessor = extension.useTemporalAccessor
            )
        }
    }
}