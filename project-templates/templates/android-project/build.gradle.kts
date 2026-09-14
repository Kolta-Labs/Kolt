// Root build file — declare plugins apply false so submodules can apply them.
// Do NOT put dependencies or android {} here.

plugins {
    // Android + Kolt convention plugins
    alias(koltxlibs.plugins.kolt.application)       apply false
    alias(koltxlibs.plugins.kolt.library)           apply false
    alias(koltxlibs.plugins.kolt.library.compose)   apply false
    alias(koltxlibs.plugins.kolt.library.hilt)      apply false
    alias(koltxlibs.plugins.kolt.library.hilt.compose) apply false
    alias(koltxlibs.plugins.kolt.data)              apply false
}
