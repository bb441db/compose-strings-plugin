package github.bb441db.compose.strings

import github.bb441db.compose.strings.helpers.TypeMappingOptions

open class StringsExtension(
    var separator: String = ".",
    var topLevelName: String = "Strings",
    var useByteIntegral: Boolean = TypeMappingOptions.Defaults.useByteIntegral,
    var useShortIntegral: Boolean = TypeMappingOptions.Defaults.useShortIntegral,
    var useBigInteger: Boolean = TypeMappingOptions.Defaults.useBigInteger,
    var useBigDecimal: Boolean = TypeMappingOptions.Defaults.useBigDecimal,
    var useByteCharacter: Boolean = TypeMappingOptions.Defaults.useByteCharacter,
    var useShortCharacter: Boolean = TypeMappingOptions.Defaults.useShortCharacter,
    var useLongDate: Boolean = TypeMappingOptions.Defaults.useLongDate,
    var useTemporalAccessor: Boolean = TypeMappingOptions.Defaults.useTemporalAccessor,
)