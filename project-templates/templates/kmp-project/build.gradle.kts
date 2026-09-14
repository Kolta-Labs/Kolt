// Root build file — declare plugins apply false so submodules can apply them.

plugins {
    // KMP Kolt convention plugins
    alias(kmplibs.plugins.kmp.application)          apply false
    alias(kmplibs.plugins.kmp.library)              apply false
    alias(kmplibs.plugins.kmp.library.compose)      apply false
    alias(kmplibs.plugins.kmp.library.koin)         apply false
    alias(kmplibs.plugins.kmp.library.koin.compose) apply false
    alias(kmplibs.plugins.kmp.data)                 apply false

    // Android-only convention plugins (for pure-Android modules if any)
    alias(koltxlibs.plugins.kolt.library) apply false
}
