package github.bb441db.compose.strings.helpers

import github.bb441db.compose.strings.model.Argument
import github.bb441db.compose.strings.model.FormatString

fun List<FormatString>.matrix(options: TypeMappingOptions = TypeMappingOptions.Defaults): List<List<Argument>> {
    val mapper = TypeMapping(options)
    val formats = distinctBy { it.index }.sortedBy { it.index }
    var combinations = listOf(listOf<Argument>())

    for (index in formats.indices) {
        val tmp = mutableListOf<List<Argument>>()
        for (combination in combinations) {
            val format = formats[index]
            val types = mapper.map(formats[index].type)
            for (type in types) {
                tmp.add(combination + Argument(name = "p${format.index}", type = type))
            }
            combinations = tmp
        }
    }
    return combinations
}