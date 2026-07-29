# ComposeScreenExample — Desktop App (:app:desktop)

Compose Multiplatform (CMP) Desktop entry point and packaging module targeting the Desktop (JVM) environment.

---

## 💻 Desktop Library & Feature Compatibility

| Library / Feature | Desktop Support | Notes |
| :--- | :---: | :--- |
| **Compose UI & Foundation** | ✅ Supported | Shared cross-platform Compose UI |
| **Room Database** | ✅ Supported | KMP Room multiplatform runtime in `commonMain` |
| **Kamera (Camera Recording & Preview)** | ✅ Supported | KMP Kamera multiplatform library in `commonMain` |
| **Koin Dependency Injection** | ✅ Supported | `koin-core` and `koin-compose` in `desktopMain` |
| **Ktor HTTP Networking** | ✅ Supported | Shared HTTP backend calls |
| **DataStore Preferences** | ✅ Supported | Multiplatform preference storage |
| **Coil 3 Image Loader** | ✅ Supported | Cross-platform image loading |
| **Kermit Logging** | ✅ Supported | Multiplatform structured logging |
| **BuildKonfig** | ✅ Supported | Generated build configuration |
| **Voyager & Nav3 Navigation** | ✅ Supported | CMP `commonMain` navigation engines |
| **Compose Destinations / Jetpack Nav** | ❌ Android Only | Android-specific navigation frameworks |
| **Glance AppWidgets** | ❌ Android Only | Android home screen widget system |
| **Phone Number Hint / SMS Auth** | ❌ Android Only | Uses Google Play Services Credential Manager |

---

## 🚀 Running Desktop Variants

Every build environment (`Develop`, `QA`, `Stage`, `Production`) and build type (`Debug`, `Release`) is explicitly exposed as a Gradle task:

| Target Variant | Environment | Build Type | Command |
| :--- | :--- | :--- | :--- |
| **Develop Debug** | `Develop` | `Debug` | `.\gradlew.bat :app:desktop:runDevelopDebug` |
| **Develop Release** | `Develop` | `Release` | `.\gradlew.bat :app:desktop:runDevelopRelease` |
| **QA Debug** | `QA` | `Debug` | `.\gradlew.bat :app:desktop:runQaDebug` |
| **QA Release** | `QA` | `Release` | `.\gradlew.bat :app:desktop:runQaRelease` |
| **Stage Debug** | `Stage` | `Debug` | `.\gradlew.bat :app:desktop:runStageDebug` |
| **Stage Release** | `Stage` | `Release` | `.\gradlew.bat :app:desktop:runStageRelease` |
| **Production Debug** | `Production` | `Debug` | `.\gradlew.bat :app:desktop:runProductionDebug` |
| **Production Release** | `Production` | `Release` | `.\gradlew.bat :app:desktop:runProductionRelease` |

---

## 📦 Packaging & Native Installers

Generate platform-specific native installers (.msi / .dmg / .deb) or standalone executable Uber JARs:

| Target Package | Command | Output Artifact Location |
| :--- | :--- | :--- |
| **Develop Debug Package** | `.\gradlew.bat :app:desktop:packageDevelopDebug` | `build/compose/binaries/main/msi/` (or `.dmg` / `.deb`) |
| **Production Release Package** | `.\gradlew.bat :app:desktop:packageProductionRelease` | `build/compose/binaries/main/msi/` |
| **Native Installer (Current OS)** | `.\gradlew.bat :app:desktop:packageDistributionForCurrentOS` | `build/compose/binaries/main/` |
| **Obfuscated Release Installer** | `.\gradlew.bat :app:desktop:packageReleaseDistributionForCurrentOS` | `build/compose/binaries/main/` |
| **Standalone Uber JAR** | `.\gradlew.bat :app:desktop:packageUberJarForCurrentOS` | `build/compose/jars/` |

---

## 🛠️ Module Architecture

- **Main Entry Point**: `Main.kt` under `src/desktopMain/kotlin/com/velord/composescreenexample/desktop/Main.kt`.
- **Dynamic Navigation Resolution**: Consumes `GeneratedBuildConfigResolver().getNavigationLib()` to dynamically select the active CMP navigation engine (Voyager, Nav3) without hardcoded branches.
- **Obfuscation & ProGuard**: Configured via `buildTypes.release.proguard` using rules defined in `proguard-rules.pro`.
- **Threading**: Includes `kotlinx-coroutines-swing` to ensure coroutines dispatched to `Dispatchers.Main` bind to the Swing Event Dispatch Thread (EDT).
