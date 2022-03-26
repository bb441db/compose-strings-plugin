package github.bb441db.compose.strings.tasks

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.api.variant.Variant
import com.android.build.gradle.internal.component.ComponentCreationConfig
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider

abstract class AGPTaskConfiguration<T: Task>(
    protected val project: Project,
    protected val android: AndroidComponentsExtension<*, *, *>,
    protected val variant: Variant,
    protected val config: ComponentCreationConfig,
) {
    protected val scope = config.variantScope
    protected val data = config.variantData

    abstract fun configure(task: T)
    abstract fun register(): TaskProvider<T>
    abstract fun afterEvaluate()
    abstract fun finalizeDsl(common: CommonExtension<*, *, *, *>)
}