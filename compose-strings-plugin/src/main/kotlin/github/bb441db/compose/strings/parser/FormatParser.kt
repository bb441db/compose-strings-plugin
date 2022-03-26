package github.bb441db.compose.strings.parser

import github.bb441db.compose.strings.model.FormatString

object FormatParser {
    fun parse(format: String): List<FormatString> {
        return Formatter
            .parse(format)
            .filterIsInstance<Formatter.FormatSpecifier>()
            .mapNotNull {
                val type = getType(it.c) ?: return@mapNotNull null

                FormatString(
                    index = it.index(),
                    type = type,
                )
            }
    }

    private fun getType(character: Char): FormatString.Type? {
        return when (character) {
            Formatter.Conversion.BOOLEAN,
            Formatter.Conversion.BOOLEAN_UPPER -> FormatString.Type.Boolean

            Formatter.Conversion.DECIMAL_INTEGER,
            Formatter.Conversion.HEXADECIMAL_INTEGER,
            Formatter.Conversion.HEXADECIMAL_INTEGER_UPPER,
            Formatter.Conversion.OCTAL_INTEGER -> FormatString.Type.Integral

            Formatter.Conversion.SCIENTIFIC,
            Formatter.Conversion.SCIENTIFIC_UPPER,
            Formatter.Conversion.DECIMAL_FLOAT,
            Formatter.Conversion.HEXADECIMAL_FLOAT,
            Formatter.Conversion.HEXADECIMAL_FLOAT_UPPER -> FormatString.Type.FloatingPoint

            Formatter.Conversion.DATE_TIME,
            Formatter.Conversion.DATE_TIME_UPPER -> FormatString.Type.DateTime

            Formatter.Conversion.CHARACTER,
            Formatter.Conversion.CHARACTER_UPPER -> FormatString.Type.Character

            Formatter.Conversion.STRING,
            Formatter.Conversion.STRING_UPPER -> FormatString.Type.String

            Formatter.Conversion.GENERAL,
            Formatter.Conversion.GENERAL_UPPER -> FormatString.Type.General

            Formatter.Conversion.HASHCODE,
            Formatter.Conversion.HASHCODE_UPPER -> FormatString.Type.Hashcode

            else -> null
        }
    }
}