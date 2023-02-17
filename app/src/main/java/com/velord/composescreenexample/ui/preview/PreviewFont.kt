package com.velord.composescreenexample.ui.preview

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
