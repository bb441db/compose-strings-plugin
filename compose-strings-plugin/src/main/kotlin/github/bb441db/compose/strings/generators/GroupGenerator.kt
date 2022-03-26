package github.bb441db.compose.strings.generators

import com.squareup.kotlinpoet.TypeSpec
import github.bb441db.compose.strings.helpers.TypeMappingOptions
import github.bb441db.compose.strings.model.Graph

class GroupGenerator(packageName: String, typeMappingOptions: TypeMappingOptions) {
    private val entryGenerator = EntryGenerator(packageName, typeMappingOptions)

    fun generate(graph: Graph): TypeSpec {
        return TypeSpec.objectBuilder(graph.name).apply {
            for (child in graph.nested) {
                addType(generate(child))
            }

            if (graph.entries.isNotEmpty()) {
                for (entry in graph.entries) {
                    entryGenerator.append(to = this, entry = entry)
                }
            }
        }.build()
    }
}

