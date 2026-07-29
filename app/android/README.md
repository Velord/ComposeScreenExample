# ComposeScreenExample — Android App (:app:android)

Android application entry point module providing the single-activity architecture, Android-specific Koin setup, and AGP build variants.

---

## 🚀 Building & Running Android Variants

All combinations of `BuildEnvironment` (`develop`, `qa`, `stage`, `production`) and `BuildType` (`debug`, `release`) are available:

| Target Variant | Environment | Build Type | Command |
| :--- | :--- | :--- | :--- |
| **Develop Debug** | `Develop` | `Debug` | `.\gradlew.bat :app:android:assembleDevelopDebug` |
| **Develop Release** | `Develop` | `Release` | `.\gradlew.bat :app:android:assembleDevelopRelease` |
| **QA Debug** | `QA` | `Debug` | `.\gradlew.bat :app:android:assembleQaDebug` |
| **Stage Release** | `Stage` | `Release` | `.\gradlew.bat :app:android:assembleStageRelease` |
| **Production Release** | `Production` | `Release` | `.\gradlew.bat :app:android:assembleProductionRelease` |

---

## 🛠️ Module Architecture

- **Application Subclass**: `App.kt` initializes Koin DI, Kermit logging, and StrictMode performance policies.
- **Single Activity**: `MainActivity.kt` sets up the window decor, system bars, and root navigation container.
- **Dynamic Versioning**: Version code and version name are computed centrally via `AppVersion` in `build-logic`.
- **ProGuard / R8**: Release optimization configured via `proguard-rules.pro`.
