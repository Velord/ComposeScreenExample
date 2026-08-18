# Firebase Remote Localization — Implementation Plan

## Status

Implementation branch: `feature/firebase-remote-localization`

The common localization runtime is implemented for Android and Desktop. Firebase Remote Config delivery is implemented on Android with the official Firebase Android SDK. Desktop deliberately uses the bundled localization document because the currently available JVM backend does not provide functional Remote Config support.

## Fixed product decisions

- Languages exposed in Settings:
  - **Default** — follows the device locale.
  - **English**.
  - **Spanish**.
- Unsupported device locale falls back to **English**.
- Language selection changes the current UI immediately.
- The selected language preference is persisted in the existing app settings/DataStore.
- Remote localization fetched during a running session is used on the **next app start**, not injected into the current session.
- There is no custom localization cache, sync engine, timestamp, retry scheduler, version comparison, or downloaded resource-pack storage.
- Localization is no longer read from Compose `Res.string` resources.
- Compose `Res` remains valid for non-string resources such as drawables.

## Firebase project mapping

Firebase project: `ComposeScreenExample` (`true-artwork-239920`).

| Build environment | Firebase Android app | Application ID |
|---|---|---|
| develop | CSE Dev | `com.velord.composescreenexample.develop` |
| qa | CSE Dev | `com.velord.composescreenexample.develop` |
| stage | CSE Stage | `com.velord.composescreenexample.stage` |
| production | CSE Prod | `com.velord.composescreenexample` |

QA intentionally uses the same Firebase application/configuration as Develop. There is no separate QA Firebase app.

Remote Config is project-level. All variants therefore see the same default `localization` parameter unless Firebase conditions are introduced later.

## Canonical localization document

The source of truth is:

```text
core/core-resource/src/commonMain/composeResources/files/localization.json
```

Shape:

```json
{
  "schemaVersion": 1,
  "languages": {
    "en": {
      "settings": "Settings"
    },
    "es": {
      "settings": "Configuración"
    }
  }
}
```

The document contains the complete English and Spanish application string set.

Build-time validation rejects the bundled document when:

- `schemaVersion` is not `1`;
- English or Spanish is missing;
- a value is empty;
- language key sets differ;
- format placeholders differ between translations;
- a key cannot be emitted as an `AppString` member.

Runtime validation applies the remote document atomically. If the remote document is malformed, uses the wrong schema, has missing/extra language keys, has missing string keys, or has placeholder mismatches, the entire remote document is rejected and the bundled document is used.

There is no per-string mixing of remote and bundled documents.

## Generated API

`AppString` is generated from the canonical bundled JSON during the Gradle build. There is no manually maintained second list of keys.

Usage:

```kotlin
Text(
    text = stringResource(AppString.settings),
)
```

Formatted strings:

```kotlin
Text(
    text = stringResource(AppString.count, count),
)
```

A non-Compose `getString(AppString.xxx)` API is available for places such as Android framework code and Glance.

Supported positional placeholders currently include the formats already used by the application, such as `%1$s`, `%2$s`, and `%1$d`.

## Runtime lifecycle

Localization initialization is an application-start concern, not splash-screen or widget logic.

Application startup:

```text
start dependency injection
        ↓
InitializeLocalizationUC
        ↓
LocalizationGateway
        ↓
read bundled localization.json
        ↓
initialize platform Remote Config data source
        ↓
read the currently active/default localization value
        ↓
validate remote value against bundled schema
        ↓
freeze the accepted document for this application session
        ↓
start UI
        ↓
FetchLocalizationUC in the application-lifetime background scope
        ↓
Firebase fetches/activates according to its normal client fetch policy
        ↓
when a new value is fetched, it becomes eligible on the next application start
```

Use cases are named proxy boundaries only. Localization orchestration lives in `LocalizationGateway`; Koin declarations only connect use cases to gateway functions.

The in-memory localization document is not replaced when the background `fetchAndActivate()` completes. This guarantees that a value fetched during the current session is not injected into that session; it is read on the next application start. Firebase's normal client-side fetch interval and throttling policy remain in effect rather than forcing a network request on every launch.

Changing the language preference does not replace the document. It changes which language inside the already-frozen document is selected, so the UI updates immediately.

## Platform Remote Config implementation

### Android

Android uses the official Firebase Android Remote Config SDK under the repository's existing Firebase BOM.

The Android data source:

1. supplies the bundled JSON as the Remote Config default for `localization`;
2. calls `ensureInitialized()`;
3. reads the currently active/default `localization` value;
4. later calls `fetchAndActivate()` after the runtime session has been initialized.

If Firebase initialization/read/fetch fails, the localization gateway contains the failure and the bundled document remains usable.

### Desktop/JVM

Desktop uses the same public localization runtime and the same bundled `localization.json`, but does **not** execute an unsupported JVM Remote Config path.

GitLive advertises Desktop/JVM support broadly, but its current JVM Remote Config implementation reuses Android-side implementation sources, while the Java backend used for Desktop marks Remote Config as non-functional. Successful JVM compilation was therefore not accepted as proof of a working Desktop Remote Config runtime.

The Desktop platform data source intentionally returns no remote value and performs no remote fetch. This keeps Desktop deterministic and offline-safe instead of shipping an unsupported runtime path.

No custom REST/sync implementation is added, because that would violate the requirement to avoid an additional localization synchronization layer.

When a functional JVM Remote Config implementation is available, only the Desktop platform data-source implementation needs to change; the localization JSON, generated API, settings UI, validation, and runtime lifecycle remain unchanged.

Upstream references:

- `https://github.com/GitLiveApp/firebase-kotlin-sdk`
- `https://github.com/GitLiveApp/firebase-java-sdk`

## Domain/module placement

Localization setting use cases live in the existing module:

```text
:domain:usecase-setting
```

There is no separate `:domain:usecase-localization` module.

The localization use cases are intentionally thin named proxies:

```text
UI / application startup
        ↓
Use case
        ↓
Gateway
        ↓
Data sources / runtime state
```

Dependency-injection declarations do not contain localization business/orchestration logic.

## Language preference

The existing `AppSetting` model contains:

```kotlin
val language: LanguagePreference = LanguagePreference.DEFAULT
```

with:

```kotlin
enum class LanguagePreference {
    DEFAULT,
    ENGLISH,
    SPANISH,
}
```

Resolution rules:

```text
ENGLISH → en
SPANISH → es
DEFAULT + es-* → es
DEFAULT + en-* → en
DEFAULT + unsupported locale → en
```

Adding the field with a default value keeps existing serialized settings readable.

## Settings UI

Settings displays a radio/selectable language section in this order:

```text
Default
English
Spanish
```

The UI observes the persisted language preference. Selecting a different item updates the preference flow, changes the active language in `LocalizationRuntime`, and recomposes string consumers.

## Android framework and widgets

Compose string calls are migrated to `AppString`.

Android framework resources that are required by the manifest, AppWidget metadata, error layouts, or other framework-only XML remain Android resources. They are not Compose `Res.string` localization sources.

Android `Application.onCreate()` initializes localization before normal application UI. The same `Application` lifecycle also runs when the process is started for an AppWidget, so Glance widgets consume the already initialized runtime instead of duplicating localization bootstrap code.

## Firebase publishing workflow

Script:

```text
scripts/firebase/publish-localization.ps1
```

Validation without Firebase access:

```powershell
.\scripts\firebase\publish-localization.ps1 -ValidateOnly
```

Dry run against Firebase:

```powershell
.\scripts\firebase\publish-localization.ps1
```

Publish:

```powershell
.\scripts\firebase\publish-localization.ps1 -Publish
```

The script:

- validates the canonical JSON before contacting Firebase;
- fetches the current Remote Config template;
- preserves unrelated Remote Config parameters/conditions;
- replaces the default value of `parameters.localization` with the canonical JSON;
- keeps the parameter type as `JSON`;
- publishes only when `-Publish` is supplied.

Firebase CLI authentication is an external prerequisite for dry-run/publish operations. `-ValidateOnly` does not require Firebase CLI authentication.

## Remote Config parameter

Parameter key:

```text
localization
```

Type:

```text
JSON
```

The Firebase Console is not the normal localization editing workflow. Localization changes should be made in the repository JSON, validated in CI, then published with the script.

## CI/reviewer gates

`.github/workflows/localization-validation.yml` validates the feature branch by checking:

- no remaining Compose `Res.string` usages in Kotlin source;
- no remaining JetBrains Compose `stringResource` imports;
- the PowerShell publisher in `-ValidateOnly` mode;
- core localization Desktop tests;
- Desktop Firebase fallback test;
- Koin graph/Desktop tests;
- Desktop application compilation;
- Develop Android compilation;
- QA Android compilation;
- Stage Android compilation;
- Production Android compilation.

Each Android flavor that must be invoked separately is compiled in its own Gradle invocation because the repository build configuration intentionally permits only one flavor per Gradle invocation.

Core tests cover:

- Default locale resolution;
- unsupported-locale English fallback;
- explicit English/Spanish selection;
- immediate language switching within the frozen session document;
- valid remote-document use;
- rejection of incomplete remote JSON;
- rejection of placeholder mismatches;
- formatted positional arguments;
- prevention of mid-session document replacement.

## Definition of done

The implementation is ready for integration when:

1. localization validation CI is green;
2. no unintended Compose `Res.string` usages remain;
3. Android compiles with the official Firebase Remote Config data source;
4. Desktop compiles/tests using the explicit bundled fallback;
5. Develop, QA, Stage, and Production variants compile as designed;
6. the publisher validates the canonical document;
7. the Firebase `localization` parameter is present and its deployed document is integration-verified when Firebase CLI access is available;
8. an authenticated Firebase CLI publish can be performed when deployment verification is desired.

A live Android Remote Config fetch and an authenticated Firebase publish remain environment/integration checks; they are not reasons to introduce a custom synchronization layer.
