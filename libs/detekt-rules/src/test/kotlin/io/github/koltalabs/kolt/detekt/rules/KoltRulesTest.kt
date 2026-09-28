package io.github.koltalabs.kolt.detekt.rules

import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.test.compileAndLint
import kotlin.test.Test
import kotlin.test.assertEquals

class KoltRulesTest {

    @Test
    fun `flags hardcoded Color and dp literals outside the theme package`() {
        val code = """
            package com.example.feature
            import androidx.compose.ui.graphics.Color
            val brand = Color(0xFF112233)
            val padding = 16.dp
        """.trimIndent()

        assertEquals(2, NoHardcodedComposeTokens(Config.empty).compileAndLint(code).size)
    }

    @Test
    fun `does not flag Color and dp literals inside the theme package`() {
        val code = """
            package com.example.theme
            import androidx.compose.ui.graphics.Color
            val brand = Color(0xFF112233)
            val padding = 16.dp
        """.trimIndent()

        assertEquals(0, NoHardcodedComposeTokens(Config.empty).compileAndLint(code).size)
    }

    @Test
    fun `flags a plain List in a State data class`() {
        val code = """
            package com.example.feature
            data class OrdersState(val orders: List<String> = emptyList())
        """.trimIndent()

        assertEquals(1, NoMutableCollectionInState(Config.empty).compileAndLint(code).size)
    }

    @Test
    fun `does not flag an ImmutableList in a State data class`() {
        val code = """
            package com.example.feature
            import kotlinx.collections.immutable.ImmutableList
            data class OrdersState(val orders: ImmutableList<String>)
        """.trimIndent()

        assertEquals(0, NoMutableCollectionInState(Config.empty).compileAndLint(code).size)
    }
}
