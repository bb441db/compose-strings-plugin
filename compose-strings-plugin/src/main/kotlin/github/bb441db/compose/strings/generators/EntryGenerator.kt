package github.bb441db.compose.strings.generators

import com.squareup.kotlinpoet.*
import github.bb441db.compose.strings.helpers.TypeMappingOptions
import github.bb441db.compose.strings.helpers.matrix
import github.bb441db.compose.strings.model.Argument
import github.bb441db.compose.strings.model.Graph

class EntryGenerator(
    packageName: String,
    private val typeMappingOptions: TypeMappingOptions,
) {
    private val composableAnnotationClassName = ClassName("androidx.compose.runtime", "Composable")
    private val stringResourceMemberName = MemberName("androidx.compose.ui.res", "stringResource")
    private val rClassName = ClassName(packageName, "R")

    fun append(to: TypeSpec.Builder, entry: Graph.Entry) {
        if (entry.format.isEmpty()) {
            appendProperty(to = to, entry = entry)
        } else {
            appendFunction(to = to, entry = entry)
        }
    }

    private fun appendProperty(to: TypeSpec.Builder, entry: Graph.Entry) {
        to.addProperty(generateProperty(entry))
    }

    private fun generateProperty(entry: Graph.Entry): PropertySpec {
        return PropertySpec.builder(entry.name, String::class).apply {
                getter(
                    FunSpec.getterBuilder()
                    .addAnnotation(composableAnnotationClassName)
                    .addCode(
                        CodeBlock.builder()
                            .apply {
                                add("")
                                addStatement(
                                    "return %M(id = %T.%N.%N)",
                                    stringResourceMemberName,
                                    rClassName,
                                    "string",
                                    entry.identifier
                                )
                            }
                            .build()
                    )
                    .build())
        }.build()
    }

    private fun appendFunction(to: TypeSpec.Builder, entry: Graph.Entry) {
        for (arguments in entry.format.matrix(options = typeMappingOptions)) {
            to.addFunction(generateFunction(entry, arguments))
        }
    }

    private fun generateFunction(entry: Graph.Entry, arguments: List<Argument>): FunSpec {
        return FunSpec.builder(entry.name).apply {
                addAnnotation(composableAnnotationClassName)

                returns(String::class)

                for (argument in arguments) {
                    addParameter(argument.name, argument.type)
                }


                addCode(
                    CodeBlock.builder()
                        .apply {
                            add("")
                            addStatement(
                                "return %M(id = %T.%N.%N, ${arguments.joinToString(separator = ", ") { "%N" }})",
                                stringResourceMemberName,
                                rClassName,
                                "string",
                                entry.identifier,
                                *arguments.map { it.name }.toTypedArray()
                            )
                        }
                        .build()
                )
            }.build()
    }
}