# Implementation Plan — Firebase Remote Localization for ComposeScreenExample

## Fixed decisions

- Base branch: **`develop`**. Implementation branch: **`feature/firebase-remote-localization`** (already created).
- Settings languages:
  - **Default** — follows device locale.
  - **English**.
  - **Spanish**.
- If `Default` resolves to an unsupported locale, use **English**.
- Persist only the user's language choice in the existing app settings/DataStore.
- Firebase project: **ComposeScreenExample** (`true-artwork-239920`). Current Android apps inside that single Firebase project:
  - `develop` → CSE Dev → `com.velord.composescreenexample.develop`
  - `stage` → CSE Stage → `com.velord.composescreenexample.stage`
  - `production` → CSE Prod → `com.velord.composescreenexample`
  - `qa` → verify/add `com.velord.composescreenexample.qa` if the build really uses that application ID.
- Remote Config belongs to the Firebase **project**, not an individual Android app. The default `localization` value is therefore shared by all app variants unless app-specific Remote Config conditions are deliberately added later.
- Firebase client SDK: **GitLive Firebase Kotlin SDK**, dependency `dev.gitlive:firebase-config`, used from common KMP code for Android + Desktop/JVM.
- Use **one Firebase Remote Config parameter**, e.g. `localization`.
- That parameter contains **one JSON document with all languages**.
- `:core:core-resource/.../localization.json` is the canonical localization document and ships with the app as the built-in fallback. The publishing script pushes that same document into Firebase Remote Config; Firebase Console is not the normal editing surface.
- **Remove localization from `Res.string.*` completely.** Keep `Res` for drawables/images/etc.
- No Room/DataStore cache for localization JSON.
- No custom sync, timestamps, retry scheduler, version comparison, downloaded files, or extra persistence.
- **Firebase SDK alone owns fetch/cache/activation.**
- New Remote Config content is applied on the **next app start**, not during the current session.
- Preferred API:

```kotlin
Text(
    text = stringResource(AppString.settings),
)
```

and:

```kotlin
Text(
    text = stringResource(AppString.count, count),
)
```

---

## 1. Feature branch

The implementation branch already exists:

```text
develop
  ↓
feature/firebase-remote-localization
```

The Firebase publishing helper is already committed there at:

```text
scripts/firebase/publish-localization.ps1
```

Continue all implementation on this branch. Before each larger change, re-check the latest `develop`, repository rules, architecture, and dependency graph. Do not implement directly on `develop`.

---

## 2. Use GitLive Firebase Remote Config in common KMP code

Use the GitLive Firebase Kotlin SDK through the version catalog:

```text
dev.gitlive:firebase-config
```

This is no longer an architecture proof gate. GitLive explicitly documents Desktop support for the SDK, and its `firebase-config` module explicitly enables the JVM target. The common Remote Config API already exposes the operations required by this feature:

```text
setDefaults(...)
ensureInitialized()
getValue(...)
fetch(...)
activate()
fetchAndActivate()
settings { ... }
```

Therefore the intended implementation is:

```text
commonMain
   │
   └── GitLive Firebase.remoteConfig
          ├── Android
          └── Desktop/JVM
```

Do not create a separate Desktop HTTP client or custom Remote Config implementation.

Still run an Android + Desktop smoke test during implementation:

```text
initialize Firebase
set bundled localization as default
read localization
fetchAndActivate
restart
verify newly activated value is used
```

This is normal integration validation, not a reason to redesign the architecture in advance.

---

## 3. Introduce canonical `localization.json`

Create:

```text
core/core-resource/
└── src/commonMain/composeResources/files/
    └── localization.json
```

Initial structure:

```json
{
  "schemaVersion": 1,
  "languages": {
    "en": {
      "app_name": "ComposeScreenExample",
      "camera": "Camera",
      "settings": "Settings",
      "loading": "Loading",
      "count": "Count: %1$d"
    },
    "es": {
      "app_name": "ComposeScreenExample",
      "camera": "Cámara",
      "settings": "Configuración",
      "loading": "Cargando",
      "count": "Cantidad: %1$d"
    }
  }
}
```

Move **all existing values** from the current `strings.xml` into this JSON.

Rules:

```text
English key set == Spanish key set
English placeholders == Spanish placeholders
Firebase JSON structure == bundled localization.json structure
```

No partial Spanish translation should silently remove a string.

---

## 4. Make `localization.json` the source of truth for generated string identifiers

Do **not** manually maintain JSON keys plus a separate enum/list.

Add a Gradle generation task to `:core:core-resource`.

Input:

```text
localization.json
```

Generated output conceptually:

```kotlin
object AppString {
    val app_name = AppStringResource("app_name")
    val camera = AppStringResource("camera")
    val settings = AppStringResource("settings")
    val loading = AppStringResource("loading")
    val count = AppStringResource("count")
}
```

With:

```kotlin
@JvmInline
value class AppStringResource internal constructor(
    internal val key: String,
)
```

This gives the requested API:

```kotlin
stringResource(AppString.settings)
```

while preserving compile-time safety.

Adding a new key to JSON should generate the matching `AppString.*` member automatically.

---

## 5. Add the new `stringResource(...)`

Inside `:core:core-resource`, provide your own overload:

```kotlin
@Composable
fun stringResource(
    resource: AppStringResource,
    vararg formatArgs: Any,
): String
```

Usage:

```kotlin
stringResource(AppString.settings)
```

and:

```kotlin
stringResource(
    AppString.bottom_navigation_first_back_press,
    value,
)
```

It must support the formatting already used throughout the application:

```text
%1$s
%2$s
%1$d
\n
```

The implementation should be independent of JetBrains `StringResource`.

---

## 6. Create runtime localization state in `core-resource`

The application needs one immutable localization document for the current session.

Conceptually:

```text
LocalizationSession

document
selectedLanguage
resolvedLanguage
```

Resolution:

```text
AppString.settings
       ↓
"settings"
       ↓
current resolved language
       ↓
current session JSON
       ↓
"Configuración"
```

The state must cause normal Compose UI to recompose when the user changes:

```text
Default ↔ English ↔ Spanish
```

A **Firebase fetch must not update this state during the current session**.

The resource layer must also work for Android Glance/string consumers that are not underneath the normal application UI tree.

---

## 7. Add Firebase as a data source

Prefer a dedicated module:

```text
:data:firebase
```

Responsibilities:

```text
GitLive Firebase initialization/configuration
GitLive Remote Config initialization
setDefaults(localization JSON)
read localization parameter
fetchAndActivate()
Firebase-specific exceptions
```

Implement the Remote Config data source in `commonMain` using GitLive. Do not add Android/Desktop `expect/actual` data sources merely for Remote Config. Platform-specific code is allowed only where Firebase application configuration genuinely requires platform input.

No UI decisions.
No language selection logic.
No local cache.
No resource rendering.

Conceptual API:

```kotlin
interface FirebaseRemoteConfigDataSource {
    suspend fun initialize(defaultLocalization: String)
    fun getLocalization(): String
    suspend fun fetchAndActivate()
}
```

Exact API should follow existing project style after inspecting neighboring data sources.

---

## 8. Add localization Gateway + Domain use cases

Follow the project's architecture:

```text
UI
 ↓
UseCase
 ↓
Gateway
 ↓
Data sources
```

Add approximately:

```text
:data:gateway/localization/
    LocalizationGateway

:domain:usecase-localization/
    InitializeLocalizationUC
    GetLanguagePreferenceUC
    SetLanguagePreferenceUC
```

Do **not** introduce a Repository abstraction.

### `InitializeLocalizationUC`

Responsibilities:

```text
receive/load bundled JSON
        ↓
Firebase setDefaults(bundled JSON)
        ↓
Firebase ensureInitialized
        ↓
get currently activated/default "localization"
        ↓
validate + parse
        ↓
create current session localization
        ↓
start Firebase fetchAndActivate()
        ↓
ignore its value for current session
```

Important ordering:

```text
1. read current value
2. freeze current-session localization
3. fetchAndActivate
```

Not:

```text
fetchAndActivate
↓
read it
```

because that would apply newly published translations immediately.

---

## 9. Firebase owns synchronization

The application must **not** introduce:

```text
DataStore localization JSON
Room localization JSON
local JSON downloaded by the application
lastFetch timestamps
remote revision comparison
periodic WorkManager task
manual refresh scheduler
custom retries
custom expiry
custom cache invalidation
custom synchronization state
```

Runtime model:

```text
                bundled localization.json
                         │
                         ▼
Firebase Remote Config setDefaults
                         │
                         ▼
                   APP START
                         │
                getValue("localization")
                         │
              ┌──────────┴──────────┐
              │                     │
       activated Firebase       Firebase default
             exists             bundled JSON
              │                     │
              └──────────┬──────────┘
                         ▼
                  validate / parse
                         ▼
             current session resources
                         │
                         ▼
                        UI

afterwards:

fetchAndActivate()
       ↓
Firebase SDK handles everything
       ↓
new value available next app launch
```

If an activated remote JSON is malformed or violates the required schema, reject that document for the current startup and use the bundled JSON. That is **validation/fallback**, not synchronization.

---

## 10. Add language preference to existing `AppSetting`

Add:

```kotlin
enum class LanguagePreference {
    Default,
    English,
    Spanish,
}
```

Then extend `AppSetting`:

```kotlin
data class AppSetting(
    ...
    val language: LanguagePreference = LanguagePreference.Default,
)
```

Add a DataStore setter:

```text
setLanguagePreference(...)
```

and Gateway/domain access following the same pattern as theme configuration.

**This DataStore use is only for the selected language. It must never store localization JSON.**

---

## 11. Resolve `Default`

Add a small platform locale abstraction.

Resolution:

```text
LanguagePreference.Default
        ↓
device language
        │
        ├── es-* → es
        ├── en-* → en
        └── unsupported → en

LanguagePreference.English → en
LanguagePreference.Spanish → es
```

The JSON itself defines which localization data exists.

Later, adding another language should not require redesigning Firebase.

---

## 12. Initialize localization during Splash

Integrate localization initialization into application startup before the first normal screen renders.

Target:

```text
Splash starts
    │
    ├── initialize localization
    │
    └── existing splash timing/other startup requirements
             │
             ▼
        app ready
```

The first normal application screen must never render before a valid localization document exists.

If Firebase has never been fetched, the bundled JSON becomes the value through Remote Config defaults.

---

## 13. Add language selection to Settings

Add a Language section:

```text
Language

Default
English
Spanish
```

Prefer a radio/selectable control rather than three independent switches.

Selecting Spanish:

```text
Spanish
   ↓
persist LanguagePreference.Spanish
   ↓
resolve "es"
   ↓
switch current session to languages.es
   ↓
Compose recomposes
```

This is immediate because the complete English + Spanish document is already in memory.

**Changing the user's selected language may update the UI immediately.**

That is separate from the rule that **new Firebase content only takes effect next launch**.

---

## 14. Firebase project/app setup

The current Firebase layout is one project:

```text
Firebase project: ComposeScreenExample
projectId: true-artwork-239920

Android apps:
CSE Dev   → com.velord.composescreenexample.develop
CSE Stage → com.velord.composescreenexample.stage
CSE Prod  → com.velord.composescreenexample
```

For QA, first verify the build configuration. If QA really uses:

```text
com.velord.composescreenexample.qa
```

register that Android app in the same Firebase project and include its client configuration.

Important: **Remote Config is project-level.** CSE Dev / Stage / Prod are apps inside the same Firebase project, so the single default `localization` parameter is shared across them. That is acceptable for localization and avoids unnecessary environment duplication.

If environment-specific localization is ever required, use explicit Remote Config conditions based on app/environment. Do not create extra localization parameters or extra Firebase projects just for this feature.

For Desktop/JVM, initialize GitLive Firebase using the same Firebase project options selected through the project's existing environment/BuildKonfig mechanism. Do not hardcode environment branching in `Main.kt`.

---

## 15. Remote Config publishing workflow

Do not use the Firebase Console JSON editor as the normal localization workflow.

The repository contains:

```text
scripts/firebase/publish-localization.ps1
```

The script must:

```text
read bundled localization.json
validate EN/ES keys
validate formatting placeholders
fetch the current Firebase Remote Config template
replace only parameters.localization.defaultValue
preserve all other Remote Config parameters/metadata that should remain
publish only when -Publish is supplied
```

Normal usage:

```powershell
# dry run
.\scripts\firebase\publish-localization.ps1

# publish
.\scripts\firebase\publish-localization.ps1 -Publish
```

Firebase CLI authentication is a one-time local prerequisite:

```powershell
firebase login
```

During implementation, test the script against the current `localization` parameter before relying on it for routine updates.

---

## 16. Migrate all current strings

After the infrastructure works on one pilot screen:

```text
Search repository for:
Res.string.
org.jetbrains.compose.resources.stringResource
```

Convert:

```kotlin
stringResource(Res.string.settings)
```

to:

```kotlin
stringResource(AppString.settings)
```

Convert formatted calls similarly.

Migrate:

- CMP feature screens
- navigation labels
- dialogs
- camera UI
- movie UI
- bottom navigation
- toasts where applicable
- Android widgets/Glance
- every other current `Res.string` consumer

Do **one feature first**, validate it, then perform the mechanical repository-wide migration.

---

## 17. Remove the old localization resources

When:

```text
search "Res.string" → 0 usages
```

remove:

```text
core/core-resource/src/commonMain/composeResources/values/strings.xml
```

and obsolete generated string imports.

Do not remove:

```text
Res.drawable
Res.painter
other Compose resources
```

Only the string mechanism is replaced.

---

## 18. Validation rules

Add tests around the bundled JSON and Remote Config parser:

```text
JSON parses
schema version supported
"en" exists
"es" exists
same keys in all languages
no empty mandatory values
format arguments compatible
%1$d stays %1$d-compatible
%1$s stays %1$s-compatible
remote malformed → bundled document used
remote missing language/key → bundled document used
unsupported device locale → English
Default + Spanish device → Spanish
English selection overrides Spanish device
Spanish selection overrides English device
```

Prefer rejecting the **entire bad remote localization document** rather than producing a UI made from a mixture of remote and bundled strings.

---

## 19. Build/test matrix

Before calling the feature complete:

```text
Android develop debug
Android qa debug
Android stage
Android production release compile

Desktop develop debug
Desktop qa
Desktop stage
Desktop production package/compile
GitLive Remote Config Android smoke test
GitLive Remote Config Desktop/JVM smoke test

Default + English device
Default + Spanish device
Default + unsupported device locale
forced English
forced Spanish

first launch offline
first launch online
existing activated Firebase value
new Firebase value published during session
restart after fetch
malformed Firebase JSON

normal CMP screen
formatted string
multiline string
Settings
Glance widget
```

Also run the repository's existing Konsist/Detekt checks and any module-specific tests required by the project skills.

---

## 20. Definition of done

The feature is complete when this architecture is true:

```text
                         Firebase Remote Config
                       parameter: "localization"
                                  │
                                  │
                 Firebase SDK fetch/cache/activate
                                  │
                         application startup
                                  │
               ┌──────────────────┴──────────────────┐
               │                                     │
       activated remote JSON                 bundled JSON default
                                                     │
                           core-resource/localization.json
               │                                     │
               └──────────────────┬──────────────────┘
                                  ▼
                         LocalizationDocument
                     English + Spanish together
                                  │
                                  ▼
                     LanguagePreference
                 Default / English / Spanish
                                  │
                                  ▼
                         resolved language
                                  │
                                  ▼
                    stringResource(AppString.*)
                                  │
                                  ▼
                              UI
```

And:

```text
Res.string usages                         = 0
custom Firebase synchronization logic    = 0
localization JSON in DataStore/Room      = 0
bundled localization JSON files          = 1
Remote Config localization parameters    = 1
Remote Config client implementations      = 1 common GitLive path
```

---

## Verified SDK basis

Plan assumption verified against `GitLiveApp/firebase-kotlin-sdk`:

- The project README states that the Kotlin SDK supports **Desktop** in multiplatform projects.
- `firebase-config` enables the **JVM** target when `firebase-config.supportedTargets` contains `jvm`.
- The repository currently lists `firebase-config.supportedTargets=ios,macos,tvos,jvm,js,android`.
- The common Remote Config API provides the exact operations needed by this localization design, including defaults, initialization, value reads, fetch, activate, and `fetchAndActivate()`.

The upstream repository currently reports partial Remote Config API coverage, but the subset required by this feature is present.
