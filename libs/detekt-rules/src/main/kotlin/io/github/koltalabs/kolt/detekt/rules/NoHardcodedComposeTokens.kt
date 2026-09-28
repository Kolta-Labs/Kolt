package io.github.koltalabs.kolt.detekt.rules

import io.gitlab.arturbosch.detekt.api.CodeSmell
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.Debt
import io.gitlab.arturbosch.detekt.api.Entity
import io.gitlab.arturbosch.detekt.api.Issue
import io.gitlab.arturbosch.detekt.api.Rule
import io.gitlab.arturbosch.detekt.api.Severity
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtConstantExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtSimpleNameExpression

/**
 * Enforces theming.md's no-hardcoded-token rule (steering non-negotiable 11):
 * `Color(...)`, `.dp`, `.sp` literals belong in the theme module only —
 * everywhere else references `Kolt.colors`/`Kolt.sizes`.
 */
class NoHardcodedComposeTokens(config: Config) : Rule(config) {

    override val issue = Issue(
        id = "NoHardcodedComposeTokens",
        severity = Severity.Defect,
        description = "Color(...)/.dp/.sp literals must live in the theme module; " +
            "reference Kolt.colors/Kolt.sizes elsewhere.",
        debt = Debt.TEN_MINS,
    )

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        if (isInThemeModule(expression)) return
        if (expression.calleeExpression?.text == "Color") {
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    "Hardcoded Color(...) outside the theme module.",
                ),
            )
        }
    }

    override fun visitDotQualifiedExpression(expression: KtDotQualifiedExpression) {
        super.visitDotQualifiedExpression(expression)
        if (isInThemeModule(expression)) return
        val selectorName = (expression.selectorExpression as? KtSimpleNameExpression)?.getReferencedName()
        val receiverIsLiteral = expression.receiverExpression is KtConstantExpression
        if (receiverIsLiteral && selectorName in DIMENSION_UNITS) {
            report(
                CodeSmell(
                    issue,
                    Entity.from(expression),
                    "Hardcoded .$selectorName literal outside the theme module.",
                ),
            )
        }
    }

    private fun isInThemeModule(element: KtElement): Boolean =
        element.containingKtFile.packageFqName.asString().contains(THEME_PACKAGE_MARKER)

    private companion object {
        val DIMENSION_UNITS = setOf("dp", "sp")
        const val THEME_PACKAGE_MARKER = "theme"
    }
}
