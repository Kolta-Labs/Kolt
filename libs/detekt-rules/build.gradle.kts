// Kolt's shared Detekt rule set — see Standards/steering/kmp/tooling.md and
// Standards/steering/android/kolt-libs.md#external-tooling-coordinates.
//
// Plain Kotlin/JVM, not KMP: Detekt rules compile against detekt-api's PSI-based
// Rule type, which only runs on the JVM. Consumers add it via `detektPlugins(...)`.

plugins {
    kotlin("jvm")
    id("io.github.koltalabs.kolt.publish")
    alias(libs.plugins.dokka)
}

val detektVersion = "1.23.7"

dependencies {
    // compileOnly: the Detekt Gradle plugin supplies detekt-api on the plugin
    // classloader at runtime — bundling it here would duplicate/version-clash.
    compileOnly("io.gitlab.arturbosch.detekt:detekt-api:$detektVersion")
    testImplementation("io.gitlab.arturbosch.detekt:detekt-test:$detektVersion")
    testImplementation(kotlin("test"))
}

mavenPublishing {
    coordinates(artifactId = "detekt-rules")
    pom {
        name = "Kolt Detekt Rules"
        description = "Custom Detekt rule set enforcing Kolt steering non-negotiables: " +
            "no hardcoded Color(...)/.dp/.sp outside the theme module, no plain " +
            "List/Map/Set in an MVI State class."
        url = "https://github.com/kolta-labs/Kolt"
    }
}
