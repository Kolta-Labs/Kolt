# Changelog

All notable changes to `compose` (`io.github.koltalabs.kolt:compose`) are documented
here, newest first. Format loosely follows
[Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [0.2.1.dev-01] - 2026-09-23
- Resolved Material 3 deprecations, matching compose-kmp: `KoltTopBar` uses `TopAppBarDefaults.topAppBarColors`, `KoltDropdown` uses `menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled)`.
- Parameterized the private `KoltDropdownCore` with `<T>`, removing an unchecked cast. No public API change.

## [0.2.1.dev-00] - 2026-08-08
- Changelog tracking starts here — see the git log for prior history.
