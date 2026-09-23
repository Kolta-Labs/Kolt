plugins {
    id("io.github.koltsystems.koltx.kmp.library")
    id("io.github.koltalabs.kolt.publish")
    alias(libs.plugins.dokka)
}

// Don't auto-add utils/logutils to this module (it IS logutils).
kmp {
    enableUtils.set(false)
    enableDesktop.set(true)
    enableIos.set(true)
}

android {
    namespace = "io.github.koltalabs.kolt.logutils"
}

dependencies {
    // App Startup powers the auto-gating LogInitializer on Android.
    "androidMainImplementation"("androidx.startup:startup-runtime:1.2.0")
}

mavenPublishing {
    coordinates(artifactId = "logutils")
    pom {
        name = "Kolt LogUtils"
        description = "Lightweight Kotlin Multiplatform logging with automatic debug/release gating on Android."
        url = "https://github.com/kolta-labs/Kolt"
    }
}
