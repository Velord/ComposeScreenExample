package com.velord.composescreenexample.ui.preview

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview

private const val GROUP_NAME = "devices"

@Preview(
    name = "Phone Landscape",
    group = GROUP_NAME,
    device = "spec:shape=Normal,width=891,height=311,unit=dp,dpi=420"
)
@Preview(
    name = "Phone Portrait",
    group = GROUP_NAME,
    device = Devices.PIXEL_4,
)
@Preview(
    name = "Tablet",
    group = GROUP_NAME,
    device = Devices.TABLET
)
annotation class PreviewDevice