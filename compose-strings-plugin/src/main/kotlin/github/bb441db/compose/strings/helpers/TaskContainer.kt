package github.bb441db.compose.strings.helpers

import org.gradle.api.Action
import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider

inline fun<reified T: Task> TaskContainer.register(name: String, configure: Action<T>): TaskProvider<T> {
    return register(name, T::class.java, configure)
}

inline fun<reified T: Task> TaskContainer.register(name: String, vararg args: Any): TaskProvider<T> {
    return register(name, T::class.java, *args)
}