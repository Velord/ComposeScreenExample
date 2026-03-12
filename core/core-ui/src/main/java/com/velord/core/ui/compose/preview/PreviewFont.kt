<<<<<<<< HEAD:core-ui/src/main/java/com/velord/uicore/compose/preview/PreviewFont.kt
package com.velord.uicore.compose.preview
========
package com.velord.core.ui.compose.preview
>>>>>>>> 76f71e457c730912e8e00a3beb96f602e7765555:core/core-ui/src/main/java/com/velord/core/ui/compose/preview/PreviewFont.kt

import androidx.compose.ui.tooling.preview.Preview

private const val GROUP_NAME = "font scales"

@Preview(
    name = "small font",
    group = GROUP_NAME,
    fontScale = 0.5f
)
@Preview(
    name = "normal font",
    group = GROUP_NAME,
    fontScale = 1f
)
@Preview(
    name = "large font",
    group = GROUP_NAME,
    fontScale = 1.5f
)
annotation class PreviewFontScale
