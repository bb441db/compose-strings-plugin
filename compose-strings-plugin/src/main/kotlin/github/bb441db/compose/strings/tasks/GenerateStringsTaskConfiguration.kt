package github.bb441db.compose.strings.tasks

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.android.build.gradle.internal.component.ComponentCreationConfig
import github.bb441db.compose.strings.StringsExtension
import github.bb441db.compose.strings.helpers.getByType
import github.bb441db.compose.strings.helpers.register
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.TaskProvider
import java.nio.file.Paths

class GenerateStringsTaskConfiguration(
    project: Project,
    android: AndroidComponentsExtension<*, *, *>,
    variant: Variant,
    config: ComponentCreationConfig,
) : AGPTaskConfiguration<GenerateStringsObjectTask>(project, android, variant, config) {

    private val name = config.computeTaskName("generate", "ComposeStrings")
    private val outputFile = Paths.get(
        project.buildDir.absolutePath,
        "generated",
        "source",
        "composeStrings",
        config.name
    ).toFile()

    private val inputFiles = data
        .androidResources
        .values
        .fold(project.files() as FileCollection) { acc, collection -> acc.plus(collection) }
        .asFileTree
        .matching {
            it.include("values/*.xml")
        }

    override fun configure(task: GenerateStringsObjectTask) {
        task.outputFile = outputFile
        task.inputFiles = inputFiles
        task.applicationId = config.applicationId
    }

    override fun register(): TaskProvider<GenerateStringsObjectTask> {
        return project.tasks.register<GenerateStringsObjectTask>(name)
    }

    override fun afterEvaluate() {
        // Setup dependencies
        val compileKotlin = project
            .tasks
            .getByName(config.computeTaskName("compile", "Kotlin"))
        val task = project
            .tasks
            .getByName(name)

        task.outputs.dir(outputFile)

        compileKotlin
            .dependsOn(task)
            .inputs
            .dir(outputFile)

        // Register generated source directories
        data.registerJavaGeneratingTask(task, listOf(outputFile))
    }

    override fun finalizeDsl(common: CommonExtension<*, *, *, *>) {
        common.sourceSets {
            getByName(config.name) { sourceSet ->
                sourceSet.kotlin.srcDirs(outputFile)
            }
        }
    }
}