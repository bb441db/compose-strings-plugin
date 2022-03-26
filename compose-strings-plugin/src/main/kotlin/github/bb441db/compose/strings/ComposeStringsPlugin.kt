package github.bb441db.compose.strings

import com.android.build.gradle.internal.component.ComponentCreationConfig
import github.bb441db.compose.strings.helpers.android
import github.bb441db.compose.strings.helpers.androidComponents
import github.bb441db.compose.strings.helpers.create
import github.bb441db.compose.strings.tasks.AGPTaskConfiguration
import github.bb441db.compose.strings.tasks.GenerateStringsObjectTask
import github.bb441db.compose.strings.tasks.GenerateStringsTaskConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeStringsPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create<StringsExtension>("strings")
        val configurations = mutableListOf<AGPTaskConfiguration<*>>()
        val components = project.androidComponents
        components.finalizeDsl { common ->
            components.onVariants { variant ->
                val config = if (variant is ComponentCreationConfig) variant else return@onVariants
                val configuration = GenerateStringsTaskConfiguration(
                    project = project,
                    android = components,
                    variant = variant,
                    config = config,
                )

                configuration
                    .register()
                    .configure(configuration::configure)

                configuration.finalizeDsl(common)

                configurations.add(configuration)
            }
        }

        project.afterEvaluate {
            for (configuration in configurations) {
                configuration.afterEvaluate()
            }
        }
    }
}