package github.bb441db.compose.strings.helpers

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.BaseExtension
import org.gradle.api.Project

val Project.android: BaseExtension get() = extensions.getByType()
val Project.androidComponents: AndroidComponentsExtension<*, *, *> get() = extensions.getByType()