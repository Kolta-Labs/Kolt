package io.github.koltalabs.kolt.detekt.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtClass

/**
 * Enforces presentation-mvi.md's steering non-negotiable 9: any `List`/`Map`/`Set`
 * in an MVI `State` is `ImmutableList`/`ImmutableMap`/`ImmutableSet`
 * (`kotlinx.collections.immutable`), never the plain `kotlin.collections` type —
 * Compose treats a plain collection as unstable and over-recomposes.
 */
class NoMutableCollectionInState(config: Config) : Rule(config) {

    override val issue = Issue(
        id = "NoMutableCollectionInState",
        severity = Severity.Defect,
        description = "A *State class must use ImmutableList/ImmutableMap/ImmutableSet, " +
            "not a plain kotlin.collections type.",
        debt = Debt.TEN_MINS,
    )

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (!klass.isData() || !klass.name.orEmpty().endsWith("State")) return

        klass.primaryConstructorParameters.forEach { param ->
            val typeText = param.typeReference?.text.orEmpty()
            val plainType = PLAIN_COLLECTION_TYPES.firstOrNull { typeText.startsWith(it) } ?: return@forEach
            report(
                CodeSmell(
                    issue,
                    Entity.from(param),
                    "'${param.name}' uses plain $plainType — use Immutable$plainType instead.",
                ),
            )
        }
    }

    private companion object {
        val PLAIN_COLLECTION_TYPES = listOf("List", "Map", "Set")
    }
}
