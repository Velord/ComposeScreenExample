# Compose Desktop ProGuard rules
-keep class com.velord.composescreenexample.desktop.MainKt { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable *;
}
