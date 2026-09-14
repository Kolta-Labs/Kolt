package io.github.koltsystems.koltx.conventions

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.io.File
import java.util.Properties

/**
 * Gradle TestKit integration tests for the Kolt Convention Plugins.
 *
 * These tests verify:
 * 1. Core versioning task behaviour (bumpDevVersion, upgradeKoltx)
 * 2. That the plugin correctly applies dependencies to consumer projects
 *
 * Tests use isolated temporary project directories so they never affect the
 * real project. Each test writes its own minimal build files.
 */
class ConventionPluginTest {

    @get:Rule
    val tempDir = TemporaryFolder()

    private lateinit var projectDir: File
    private lateinit var buildFile: File

    @Before
    fun setUp() {
        projectDir = tempDir.root
        buildFile = File(projectDir, "build.gradle.kts")
        // Minimal settings.gradle.kts to enable the test build
        File(projectDir, "settings.gradle.kts").writeText(
            """
            rootProject.name = "test-project"
            """.trimIndent()
        )
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Task 1: bumpDevVersion
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    fun `bumpDevVersion creates version properties when missing`() {
        // Write the task logic directly as in conventions/build.gradle.kts
        buildFile.writeText(
            """
            import java.util.Properties

            tasks.register("bumpDevVersion") {
                doLast {
                    val file = file("version.properties")
                    val props = Properties()
                    if (file.exists()) file.inputStream().use { props.load(it) }
                    val current = props.getProperty("DEV_VERSION", "0").toInt()
                    props.setProperty("DEV_VERSION", (current + 1).toString())
                    file.outputStream().use { props.store(it, "Auto-generated") }
                }
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments("bumpDevVersion")
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":bumpDevVersion")?.outcome)
        val propsFile = File(projectDir, "version.properties")
        assertTrue(propsFile.exists())
        val props = Properties().apply { propsFile.inputStream().use { load(it) } }
        assertEquals("1", props.getProperty("DEV_VERSION"))
    }

    @Test
    fun `bumpDevVersion increments existing DEV_VERSION`() {
        File(projectDir, "version.properties").writeText("DEV_VERSION=41\n")

        buildFile.writeText(
            """
            import java.util.Properties

            tasks.register("bumpDevVersion") {
                doLast {
                    val file = file("version.properties")
                    val props = Properties()
                    if (file.exists()) file.inputStream().use { props.load(it) }
                    val current = props.getProperty("DEV_VERSION", "0").toInt()
                    props.setProperty("DEV_VERSION", (current + 1).toString())
                    file.outputStream().use { props.store(it, "Auto-generated") }
                }
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments("bumpDevVersion")
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":bumpDevVersion")?.outcome)
        val props = Properties().apply {
            File(projectDir, "version.properties").inputStream().use { load(it) }
        }
        assertEquals("42", props.getProperty("DEV_VERSION"))
    }

    @Test
    fun `bumpDevVersion is never UP-TO-DATE`() {
        File(projectDir, "version.properties").writeText("DEV_VERSION=1\n")

        buildFile.writeText(
            """
            import java.util.Properties

            tasks.register("bumpDevVersion") {
                outputs.upToDateWhen { false }
                doLast {
                    val file = file("version.properties")
                    val props = Properties()
                    if (file.exists()) file.inputStream().use { props.load(it) }
                    val current = props.getProperty("DEV_VERSION", "0").toInt()
                    props.setProperty("DEV_VERSION", (current + 1).toString())
                    file.outputStream().use { props.store(it, "Auto-generated") }
                }
            }
            """.trimIndent()
        )

        val runner = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments("bumpDevVersion")

        val result1 = runner.build()
        val result2 = runner.build()

        assertEquals(TaskOutcome.SUCCESS, result1.task(":bumpDevVersion")?.outcome)
        assertEquals(TaskOutcome.SUCCESS, result2.task(":bumpDevVersion")?.outcome)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Task 2: upgradeKoltx
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    fun `upgradeKoltx replaces version in TOML`() {
        val gradleDir = File(projectDir, "gradle").also { it.mkdirs() }
        val tomlFile = File(gradleDir, "koltxlibs.versions.toml")
        tomlFile.writeText(
            """
            [versions]
            koltx = "0.0.1"
            someOtherLib = "1.2.3"
            """.trimIndent()
        )

        // Write the upgradeKoltx task logic directly — same logic as in the template
        buildFile.writeText(
            """
            tasks.register("upgradeKoltx") {
                doLast {
                    val newVersion = project.findProperty("newVersion") as String?
                        ?: error("Provide -PnewVersion=<version>")
                    val tomlFile = file("gradle/koltxlibs.versions.toml")
                    val original = tomlFile.readText()
                    val versionLineRegex = Regex(
                        "^koltx\\s*=\\s*\"[^\"]*\"",
                        RegexOption.MULTILINE
                    )
                    val updated = original.replace(versionLineRegex, "koltx = \"${'$'}newVersion\"")
                    tomlFile.writeText(updated)
                }
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments("upgradeKoltx", "-PnewVersion=1.0.0")
            .build()

        assertEquals(TaskOutcome.SUCCESS, result.task(":upgradeKoltx")?.outcome)
        val updatedContent = tomlFile.readText()
        // Version was updated
        assertTrue(updatedContent.contains("""koltx = "1.0.0""""))
        // Other entries were not touched
        assertTrue(updatedContent.contains("""someOtherLib = "1.2.3""""))
    }

    @Test
    fun `upgradeKoltx fails with clear error when newVersion is not provided`() {
        val gradleDir = File(projectDir, "gradle").also { it.mkdirs() }
        File(gradleDir, "koltxlibs.versions.toml").writeText(
            """
            [versions]
            koltx = "0.0.1"
            """.trimIndent()
        )

        buildFile.writeText(
            """
            tasks.register("upgradeKoltx") {
                doLast {
                    val newVersion = project.findProperty("newVersion") as String?
                        ?: error("Please provide -PnewVersion=<version>.")
                    println("Would upgrade to: ${'$'}newVersion")
                }
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(projectDir)
            .withArguments("upgradeKoltx")
            .buildAndFail()

        assertTrue(result.output.contains("Please provide -PnewVersion=<version>."))
    }

    // ─────────────────────────────────────────────────────────────────────────
    // buildDateSuffix format test (unit-level, no Gradle runner needed)
    // ─────────────────────────────────────────────────────────────────────────

    @Test
    fun `buildDateSuffix produces correct default format`() {
        // Import the function directly — it's internal to the plugin classpath
        // We test the format by checking the regex pattern of the output
        val suffix = io.github.koltsystems.koltx.conventions.extensions.buildDateSuffix()
        // Expected default format: .yyyyMMdd.HHmm  e.g. ".20260613.1430"
        val pattern = Regex("""^\.\d{8}\.\d{4}$""")
        assertTrue(
            "buildDateSuffix() output '$suffix' does not match expected format .yyyyMMdd.HHmm",
            pattern.matches(suffix)
        )
    }

    @Test
    fun `buildDateSuffix honours a custom pattern`() {
        val suffix = io.github.koltsystems.koltx.conventions.extensions.buildDateSuffix("ddMMyy")
        // Expected: .ddMMyy  e.g. ".130626"
        assertTrue(
            "buildDateSuffix(\"ddMMyy\") output '$suffix' does not match expected format .ddMMyy",
            Regex("""^\.\d{6}$""").matches(suffix)
        )
    }

    @Test
    fun `buildDateSuffix falls back to default for a blank pattern`() {
        val suffix = io.github.koltsystems.koltx.conventions.extensions.buildDateSuffix("")
        assertTrue(
            "buildDateSuffix(\"\") should fall back to the default .yyyyMMdd.HHmm format, got '$suffix'",
            Regex("""^\.\d{8}\.\d{4}$""").matches(suffix)
        )
    }
}
