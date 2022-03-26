package github.bb441db.compose.strings.model

data class FormatString(
    val index: Int,
    val type: Type,
) {
    enum class Type {
        String,
        Character,
        Boolean,
        Integral,
        FloatingPoint,
        DateTime,
        Hashcode,
        General;
    }
}