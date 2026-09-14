plugins {
    `kotlin-dsl`
}

group = "io.github.koltsystems.koltx"

// This module deliberately depends ONLY on the vanniktech maven-publish plugin — NOT on KGP/AGP.
// Keeping its classpath lean means a module that applies io.github.koltsystems.koltx.publish (e.g. the
// BOM java-platform) does not pull Kotlin into its classloader scope, avoiding the
// KotlinNativeBundleBuildService classloader conflict in a whole-suite build.
dependencies {
    implementation(libs.vanniktech.maven.publish)
}

gradlePlugin {
    plugins {
        create("koltxPublish") {
            id = "io.github.koltsystems.koltx.publish"
            displayName = "KoltX Publish"
            description = "Applies the vanniktech maven-publish plugin from a shared, KGP-free classloader."
            implementationClass = "io.github.koltsystems.koltx.publish.KoltXPublishConventionPlugin"
        }
    }
}
