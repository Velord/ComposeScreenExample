package com.velord.core.ui.compose.preview

import androidx.compose.ui.tooling.preview.Preview

private const val GROUP_NAME = "devices"

@Preview(
    name = "Phone Landscape",
    group = GROUP_NAME,
    widthDp = 891,
    heightDp = 311,
)
@Preview(
    name = "Phone Portrait",
    group = GROUP_NAME,
    widthDp = 411,
    heightDp = 891,
)
@Preview(
    name = "Tablet",
    group = GROUP_NAME,
    widthDp = 800,
    heightDp = 1280,
)
annotation class PreviewDevice