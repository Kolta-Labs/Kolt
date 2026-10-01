# Preparing for Release

Run this top to bottom on a **release branch** (`release/x.y.z`) cut from a
green `main`. Nothing here is speculative — each step exists because skipping
it ships a bug users see. Anything the tooling can check is a Gradle task;
don't hand-wave "looks fine".

## 1. Freeze & clean

- [ ] Cut `release/x.y.z`. Only release-blocking fixes land after this.
- [ ] `./gradlew clean check` passes (Konsist/Detekt/ktlint + unit tests — see [tooling.md](tooling.md)) **and** `./gradlew allTests` — `commonTest` must be green on Android/JVM **and** iOS (`iosSimulatorArm64Test`), not just one target.
- [ ] No `TODO`/`FIXME`/`println`/`Log.d`/`NSLog` left in code shipped to users; no debug-only screens reachable from release (`BuildConfig.DEBUG` guards or `debug` source set only).
- [ ] Dependencies: no `-SNAPSHOT`, `-alpha`/`-beta` you didn't consciously accept, no `mavenLocal()`, no `includeBuild` pointing outside the repo (a release must build from a clean checkout / CI).
- [ ] Deprecation warnings triaged; `allWarningsAsErrors` not silently disabled to get green.

## 2. Version bump

- [ ] `versionName` = SemVer `MAJOR.MINOR.PATCH` (breaking/redesign · feature · fix). Pre-release suffixes (`-rc1`) only for internal tracks, never the store listing.
- [ ] `versionCode` strictly increases for **every** upload, including re-uploads of a rejected build — Play refuses duplicates and never lets you go down. Keep it a single monotonic integer in one place (the version catalog / app `defaultConfig`), never computed from wall-clock in a release build.
- [ ] Bump in the **same commit** as the changelog / release notes, tagged `vX.Y.Z` after the build is uploaded — not before.
- [ ] iOS: `CFBundleShortVersionString` = `versionName`, `CFBundleVersion` (build number) strictly increases per App Store Connect upload. **One source of truth** — feed both platforms from the same catalog/Gradle property (e.g. an `.xcconfig` written by Gradle, or BuildKonfig) so Android and iOS never drift.
- [ ] Shared-module release: if `shared` is also published as a library/XCFramework, its version is bumped and tagged separately from the apps.
- [ ] Debug builds keep their `-debug` suffix/timestamp (see the `kolt.application` extension) — release must not.
- [ ] Bump `targetSdk` deliberately, not incidentally: Play enforces a minimum target each August; do it in its own PR with behavior-change testing, not in the release commit.

## 3. Release build configuration

- [ ] `release` build type: `isMinifyEnabled = true`, `isShrinkResources = true`, `isDebuggable = false`, real `signingConfig` (from CI secrets / `keystore.properties` git-ignored — never a committed keystore or password).
- [ ] R8 keep rules exist for everything reflected on: `kotlinx.serialization` (usually automatic), Retrofit/Ktor models, Room, JNI, anything loaded by name. **Test the minified build** — a crash that only appears with R8 is the classic release-day bug.
- [ ] iOS: Release scheme builds the Kotlin framework in `Release` config (`embedAndSignAppleFrameworkForXcode` uses `CONFIGURATION`; a Debug-linked framework in a Release archive is slow and huge), bitcode n/a, `dSYM` for the Kotlin framework generated (`-Xadd-light-debug=enable` / `binaryOption`s as configured) and uploaded to the crash reporter.
- [ ] Build an **AAB** (`bundleRelease`) for Play; APK only for side-loading/QA.
- [ ] Play App Signing enrolled; upload key ≠ app-signing key; keystore + passwords backed up in the team secret manager (losing the upload key is recoverable, losing an unenrolled signing key is not).
- [ ] Network: no cleartext (`usesCleartextTraffic="false"`), no dev/staging base URL baked into release (`BuildConfig`/BuildKonfig per build type, checked by grep in step 8), no logging interceptor / Chucker in release.
- [ ] iOS: `Info.plist` audit — every `NS*UsageDescription` is present, accurate and only for APIs actually used; ATS exceptions none; `PrivacyInfo.xcprivacy` (required-reason APIs + SDK manifests) current; entitlements minimal; background modes only those used.
- [ ] Manifest audit (`./gradlew :app:processReleaseManifest`, read the merged manifest): every `exported` component is intentional, no unused permissions pulled in by libraries, `allowBackup`/`dataExtractionRules` decided (default to excluding tokens/DB from backup).
- [ ] Crash + analytics SDKs point at **production** projects; mapping file upload (Crashlytics/Sentry) is wired into the release task so stack traces de-obfuscate.

## 4. One round of launch optimization

One measured pass, not an open-ended perf project. Measure first on a **release-like** build (`benchmark` build type: release + `debuggable=false`, signed with debug key) on a **mid/low-end physical device** — never an emulator, never a debug build.

1. **Baseline**: Macrobenchmark `StartupBenchmark` (`StartupTimingMetric`, `CompilationMode.None()` and `Partial()`), cold start, ≥10 iterations. Record `timeToInitialDisplayMs` and `timeToFullDisplayMs` (call `reportFullyDrawn()` when the first screen's real content is on screen).
2. **Baseline Profile** (the biggest single win, typically 20–30% faster cold start):
   - Add a `:baselineprofile` module with the `androidx.baselineprofile` plugin (producer) and apply the consumer plugin in `:app`.
   - `BaselineProfileGenerator` must walk the **critical user journeys** — cold start → first screen → the 2–3 flows users hit most (scroll the main list, open detail, sign-in) — not just "launch app". Whatever isn't exercised isn't precompiled.
   - Generate on a rooted/API 33+ emulator or device: `./gradlew :app:generateReleaseBaselineProfile`. Commit the produced `baseline-prof.txt`; regenerate whenever navigation or a main screen changes materially.
   - Also ship the **startup profile** (`startup-prof.txt`) so R8 lays out startup classes contiguously in the DEX.
   - Libraries ship their own profiles via `ProfileInstaller` — keep `androidx.profileinstaller` on the classpath.
3. **iOS launch** (same round, Instruments → *App Launch* template on a release build, real device): cold-launch to first frame. Kotlin/Native has no baseline-profile equivalent; the levers are: initialize Koin/SDKs lazily off the first frame, keep `main`/`application(_:didFinishLaunching:)` free of heavy shared-module init, avoid eager top-level `val` singletons in `commonMain` (K/N initializes them on first access — put them behind `lazy`), and check framework binary size (`isStatic = true`, `-Xbinary=…` size options, strip unused exports). MetricKit/Xcode Organizer for field data.
4. **Cut startup work** (Android `Application`/first-Activity `onCreate`; shared init both platforms call): find it in a Perfetto/`Trace.beginSection` capture, then
   - lazy-init or `androidx.startup` deferred every SDK not needed for the first frame (analytics, ads, remote config, crash init after first frame where the SDK allows);
   - DI: `lazy`/`by inject()` at the edges; no eager singleton graph building (Hilt: `Lazy<T>`/`Provider<T>`);
   - no disk/network/DB on the main thread before first frame (StrictMode in debug catches this — leave it on in `debug`);
   - a proper `SplashScreen` API theme, not a blocking splash Activity; don't hold the splash on a network call.
5. **Compose**: first screen stable state (immutable collections — non-negotiable 9), no heavy work in composition, `LazyColumn` items keyed, no eager image decode of off-screen items.
6. **Re-measure** with the profile installed (`CompilationMode.Partial(BaselineProfileMode.Require)`). Record before/after in the release PR. Regressions >10% vs. the previous release's numbers block the release; otherwise ship and file follow-ups — **one round only**.
7. Also glance at APK/AAB size (`bundletool` / Play Console size report) and jank (`FrameTimingMetric` on the main scroll) while the benchmark harness is up.

## 5. Whole-journey tests

Unit tests already ran in step 1. Here you test **real flows on a real build**, not isolated screens.

- [ ] Automated: journey tests written **once** against Compose Multiplatform's `runComposeUiTest` (`compose.uiTest`, `commonTest`/`desktopTest`/`androidInstrumentedTest`) driving the real `NavDisplay` + fake-backed DI, plus Android instrumented (`createAndroidComposeRule`, UI Automator for permission/cross-app flows) and iOS `XCUITest` for the true-native shell (launch, permissions, deep link, share sheet). Run against a **fake/stubbed backend** (MockWebServer / `MockEngine`-backed DI override) — deterministic, no prod data. One test per critical journey: first launch/onboarding → sign-in → the core task → sign-out; purchase/subscription (Play test tracks + license testers) if applicable; deep link → correct screen with back stack intact.
- [ ] Run them against the **minified release-like variant** (`benchmark`/`release` with test-only signing), not just `debug`.
- [ ] Manual smoke checklist on ≥2 physical devices (one low-end, one recent) and the oldest and newest supported API levels:
  - fresh install · upgrade over the **previous production version** (data migration, DB schema/Room migration, DataStore keys — the upgrade path is the one automation skips)
  - process death & restore mid-flow (`adb shell am kill`; Developer Options → "Don't keep activities")
  - rotation, split-screen, dark mode, **font scale 200%**, RTL/long-locale strings, TalkBack on the main path (non-negotiable 12)
  - airplane mode / flaky network on every screen with a load; token expiry mid-session
  - permissions: deny, deny-forever, grant-then-revoke in Settings
  - background → foreground after hours; notification tap cold/warm
  - iOS extras: iOS 26 + oldest supported iOS, small (SE) + large device, Dynamic Type max, Reduce Motion, VoiceOver, swipe-back gesture vs. our back stack, app-switcher kill & relaunch, TestFlight build (not just Xcode-run)
- [ ] Crash-free: run the Play pre-launch report (Firebase Test Lab robo test) on the internal track and fix anything red.

## 6. Store & compliance

- [ ] Release notes (per-locale, user-facing, not commit log) and screenshots current with the UI.
- [ ] App Store Connect: privacy nutrition labels, App Privacy manifest, export-compliance (encryption) answer, review notes + demo account for the reviewer, age rating, screenshots per required device size.
- [ ] Play Console: Data Safety form matches the SDKs actually shipped (re-check after any dependency added this cycle), privacy-policy URL live, content rating, target-audience/ads declarations, required permission declarations (background location, exact alarms, foreground-service type, photo picker vs. `READ_MEDIA_*`).
- [ ] Third-party license notices regenerated (`oss-licenses`/`licensee`) if you show them.
- [ ] Feature flags/remote config default to the **safe** value for release; kill switches for anything risky verified working.
- [ ] Backend compatibility: the new build works against the backend versions currently in prod **and** the API version older installed apps still use — mobile can't roll back, so don't remove endpoints until the old app's traffic is ~0. Set/verify a minimum-supported-version gate (force-update path tested via `update-utils`, see [kolt-libs.md](kolt-libs.md)).

## 7. Staged rollout

1. Upload AAB → **internal testing** track and IPA → **TestFlight** internal group (team + license testers) → step 5 smoke on the Play-delivered build (Play re-signs and splits — it differs from your local APK).
2. **Closed/open testing / external TestFlight** if the change is big; otherwise straight to production at **1–5%** staged rollout (iOS: *phased release* over 7 days, pausable).
3. Watch for 24–48h at each step: Play Console **Android vitals** and Xcode Organizer (crash rate, ANR rate vs. bad-behavior thresholds), Crashlytics/Sentry new issues, startup-time vitals, review sentiment, key funnel metrics.
4. Halt (Play Console → *Halt rollout*; App Store Connect → *Pause phased release*) on: crash-free users drop >0.5pp vs. previous release, any new top-3 crash, ANR spike, funnel drop. Fix forward with a **new versionCode**; never try to re-upload the same one.
5. 5% → 20% → 50% → 100%, one step per day if metrics hold.

## 8. Tag & close

- [ ] Grep the release build for leaks: `./gradlew :app:assembleRelease` then `unzip -p app-release.apk 'classes*.dex' | strings | grep -iE 'staging|localhost|10\.0\.2\.2|api[_-]?key|BEGIN (RSA|PRIVATE)'` (should be empty, or only known public values).
- [ ] Archive per release: signed AAB + IPA/`.xcarchive`, R8 `mapping.txt`, Android native + iOS Kotlin `dSYM`s, the baseline profile, and benchmark numbers (CI artifact or release attachment). Without `mapping.txt` you can't read production crashes.
- [ ] `git tag vX.Y.Z` on the exact released commit; merge `release/x.y.z` back to `main`; open next-version bump (`versionName` to next `-SNAPSHOT`/dev if you use one; `versionCode` continues from the last uploaded).
- [ ] Post-release: retro note for anything the checklist missed → add it here.

## Agent rules

- "Prepare for release" means work this list, checking off what's verifiable from the repo (build files, manifest, rules, versions) and **listing what needs a human/device** (physical-device runs, Play Console, store metadata) — never claim those done.
- Never invent signing credentials, commit a keystore, or bump `versionCode` downward. Never upload/publish; produce the artifacts and stop.
- If a benchmark can't run (no device attached), say so; don't estimate numbers.
