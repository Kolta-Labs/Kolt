plugins {
    `kotlin-dsl`
}

group = "io.github.koltalabs.kolt"

// This module deliberately depends ONLY on the vanniktech maven-publish plugin — NOT on KGP/AGP.
// Keeping its classpath lean means a module that applies io.github.koltalabs.kolt.publish (e.g. the
// BOM java-platform) does not pull Kotlin into its classloader scope, avoiding the
// KotlinNativeBundleBuildService classloader conflict in a whole-suite build.
dependencies {
    implementation(libs.vanniktech.maven.publish)
}

gradlePlugin {
    plugins {
        create("koltPublish") {
            id = "io.github.koltalabs.kolt.publish"
            displayName = "Kolt Publish"
            description = "Applies the vanniktech maven-publish plugin from a shared, KGP-free classloader."
            implementationClass = "io.github.koltalabs.kolt.publish.KoltPublishConventionPlugin"
        }
    }
}
