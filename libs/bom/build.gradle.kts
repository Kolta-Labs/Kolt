// ──────────────────────────────────────────────────────────────────────────────
// KoltX BOM (Bill of Materials)
//
// A Maven platform POM that constrains all KoltX library versions to a
// known-compatible set. Consumers add this once:
//
//   implementation(platform("io.github.koltsystems.koltx:koltx-bom:<version>"))
//
// After which all `io.github.koltsystems.koltx:*` artifacts can be declared without
// explicit versions. The convention plugins automatically inject this BOM, so
// most consumer projects never need to add it manually.
//
// Version: set by the root build.gradle.kts from BOM_VERSION in version.properties.
// Format: YYYY.MM.PATCH  (calendar-based, like the AndroidX BOM)
// ──────────────────────────────────────────────────────────────────────────────

plugins {
    `java-platform`
    id("io.github.koltsystems.koltx.publish")
}

// Lib versions are exposed as extra properties by the root build.gradle.kts,
// which reads them from version.properties. This avoids re-parsing the file here.
val utilsVersion:        String by rootProject.extra
val logutilsVersion:     String by rootProject.extra
val composeUtilsVersion: String by rootProject.extra
val composeKmpVersion:   String by rootProject.extra
val updateUtilsVersion:  String by rootProject.extra
val locationVersion:     String by rootProject.extra

dependencies {
    constraints {
        api("io.github.koltsystems.koltx:utils:$utilsVersion")
        api("io.github.koltsystems.koltx:logutils:$logutilsVersion")
        api("io.github.koltsystems.koltx:compose:$composeUtilsVersion")
        api("io.github.koltsystems.koltx:compose-kmp:$composeKmpVersion")
        api("io.github.koltsystems.koltx:update-utils:$updateUtilsVersion")
        api("io.github.koltsystems.koltx:location:$locationVersion")
    }
}

mavenPublishing {
    coordinates(artifactId = "koltx-bom")
    pom {
        name = "KoltX BOM"
        description = "Bill of Materials for the KoltX library suite — " +
            "constrains utils, logutils, compose, compose-kmp, and update-utils to a " +
            "known-compatible version set."
        url = "https://github.com/kolt-systems/KoltX"
        licenses {
            license {
                name = "Apache-2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0"
            }
        }
        developers {
            developer {
                id = "kolt-systems"
                name = "Kolt Systems"
                url = "https://github.com/kolt-systems"
            }
        }
        scm {
            connection = "scm:git:git://github.com/kolt-systems/KoltX.git"
            developerConnection = "scm:git:ssh://github.com/kolt-systems/KoltX.git"
            url = "https://github.com/kolt-systems/KoltX"
        }
    }
}
