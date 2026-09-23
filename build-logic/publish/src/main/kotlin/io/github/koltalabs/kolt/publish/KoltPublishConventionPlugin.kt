package io.github.koltalabs.kolt.publish

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Applies the vanniktech maven-publish plugin from this lean `:publish` included-build module,
 * whose classpath contains vanniktech but NOT KGP/AGP.
 *
 * Sourcing vanniktech from a KGP-free module gives every module one shared vanniktech classloader
 * without forcing Kotlin onto non-Kotlin modules. Each module declares its own
 * `mavenPublishing { }` block for coordinates/POM.
 *
 * Publishing targets:
 * - Local dev:   `./gradlew :libs:<name>:publishToMavenLocal`  (no credentials needed)
 * - Release:     `./gradlew :libs:<name>:publishAllPublicationsToMavenCentralRepository -PisRelease`
 *
 * Credentials are read by vanniktech from Gradle properties (set in ~/.gradle/gradle.properties
 * locally, or via ORG_GRADLE_PROJECT_* environment variables in CI — never hardcoded):
 *   mavenCentralUsername / mavenCentralPassword — Central Portal user token
 *   signingInMemoryKey / signingInMemoryKeyPassword — GPG private key (base64-armored)
 */
class KoltPublishConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.pluginManager.apply("com.vanniktech.maven.publish")

        target.extensions.configure(MavenPublishBaseExtension::class.java) {
            if (!target.pluginManager.hasPlugin("java-platform")) {
                configureBasedOnAppliedPlugins(javadocJar = false)
            }

            // Wire up Central Portal publishing and signing only for release builds.
            // Gated on -PisRelease so that `publishToMavenLocal` works without any credentials.
            if (target.providers.gradleProperty("isRelease").isPresent) {
                publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
                signAllPublications()
            }
        }
    }
}
