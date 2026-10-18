# Horizon Bloom Demo Integration

This module is the ComposeScreenExample UI feature boundary for Horizon Bloom.
Keep :ui:feature-demo-horizon as a distinct Kotlin Multiplatform module.

- The only local UI entry point is HorizonDemoScreen(onBackClick).
- HorizonDemoScreen delegates to HorizonDemoApp from the published
  io.github.velord.horizonbloom:demo artifact.
- The module owns its published demo dependency; navigation depends on
  projects.ui.featureDemoHorizon, not on the Maven artifact directly.
- Keep previews alongside any dedicated composable source files.
- Do not reintroduce local copies of the renderer, editor controls,
  ViewModel, color helpers, or any other implementation belonging to HorizonBloom.
- Maintain all four navigation implementations (Voyager, Navigation Compose,
  Navigation 3, and Compose Destinations).

Horizon Bloom source and rendering documentation live in:
https://github.com/Velord/HorizonBloom
