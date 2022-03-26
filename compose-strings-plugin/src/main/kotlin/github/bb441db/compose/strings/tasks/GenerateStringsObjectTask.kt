package github.bb441db.compose.strings.tasks

import com.squareup.kotlinpoet.FileSpec
import github.bb441db.compose.strings.StringsExtension
import github.bb441db.compose.strings.generators.GroupGenerator
import github.bb441db.compose.strings.helpers.GraphLoader
import github.bb441db.compose.strings.helpers.TypeMappingOptions
import github.bb441db.compose.strings.helpers.getByType
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class GenerateStringsObjectTask : DefaultTask() {
    @get:InputFiles
    abstract var inputFiles: FileTree

    @get:OutputDirectory
    abstract var outputFile: File

    @get:Input
    abstract var applicationId: Provider<String>

    @TaskAction
    fun process() {
        val extension = project.extensions.getByType<StringsExtension>()
        val typeMappingOptions = TypeMappingOptions(extension = extension)
        val loader = GraphLoader(extension.topLevelName, extension.separator)
        val graph = loader.load(inputFiles.files)

        val generator = GroupGenerator(
            packageName = applicationId.get(),
            typeMappingOptions = typeMappingOptions,
        )
        val fileSpec = FileSpec.builder(applicationId.get(), graph.name).apply {
            addType(generator.generate(graph))
        }.build()

        fileSpec.writeTo(outputFile)
    }
}