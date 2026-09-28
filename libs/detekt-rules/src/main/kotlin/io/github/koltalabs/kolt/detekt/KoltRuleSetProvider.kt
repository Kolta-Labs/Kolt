package io.github.koltalabs.kolt.detekt

import io.github.koltalabs.kolt.detekt.rules.NoHardcodedComposeTokens
import io.github.koltalabs.kolt.detekt.rules.NoMutableCollectionInState
import io.gitlab.arturbosch.detekt.api.Config
import io.gitlab.arturbosch.detekt.api.RuleSet
import io.gitlab.arturbosch.detekt.api.RuleSetProvider

/** Registered via META-INF/services — see Standards/steering/kmp/tooling.md#detekt-custom-rules. */
class KoltRuleSetProvider : RuleSetProvider {
    override val ruleSetId: String = "kolt"

    override fun instance(config: Config): RuleSet = RuleSet(
        ruleSetId,
        listOf(
            NoHardcodedComposeTokens(config),
            NoMutableCollectionInState(config),
        ),
    )
}
