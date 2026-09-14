import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.tasks.BaseKtLintCheckTask

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidMultiplatformLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.6" apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    configure<DetektExtension> {
        buildUponDefaultConfig = true
        config.setFrom("$rootDir/config/detekt/detekt.yml")
    }

    configure<KtlintExtension> {
        filter {
            exclude("**/build/generated/**")
            exclude { element -> element.file.path.contains("resourceGenerator") }
        }
    }

    tasks.withType<BaseKtLintCheckTask>().configureEach {
        notCompatibleWithConfigurationCache("Exclusão manual de generated sources")
        val filteredFiles = source.files.filter { file ->
            !file.path.contains("resourceGenerator")
        }
        setSource(files(filteredFiles))
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        exclude("**/build/generated/**")
    }
}
