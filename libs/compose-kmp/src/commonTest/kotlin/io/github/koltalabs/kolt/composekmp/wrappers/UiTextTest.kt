package io.github.koltalabs.kolt.composekmp.wrappers

import kotlin.test.Test
import kotlin.test.assertEquals

class UiTextTest {

    @Test
    fun testDynamicString() {
        val uiText = UiText.DynamicString("Hello Kolt")
        assertEquals("Hello Kolt", uiText.value)
    }
}
