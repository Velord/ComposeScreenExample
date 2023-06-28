package com.example.modifierdemo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velord.uicore.utils.modifier.HangingDefaults
import com.velord.uicore.utils.modifier.HangingPivotPoint
import com.velord.uicore.utils.modifier.hanging

@Composable
internal fun ColumnScope.HangingDemo() {
    Title(text = "Modifier.hanging")
    Bell()
    Default()
    SmallLeft()
    FullRotation()
    HangingOnRightSide()
    QuickHangingOnLeftSide()
    EpilepsyAtBottom()
    HangingOnCenter()
    Spacer(modifier = Modifier.height(50.dp))
}

@Composable
private fun ColumnScope.Bell() {
    Icon(
        imageVector = Icons.Filled.Notifications,
        contentDescription = null,
        modifier = Modifier
            .padding(8.dp)
            .align(Alignment.CenterHorizontally)
            .hanging(),
        tint = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun Default() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .hanging(),
    ) {
        Text(
            text = "Default Hanging",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun SmallLeft() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .hanging(
                shift = HangingDefaults.shift(
                    startRotationAngle = 5,
                    endRotationAngle = -20
                ),
                animation = HangingDefaults.animation(duration = 2000)
            )
    ) {
        Text(
            text = "Small Left",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun FullRotation() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .hanging(
                shift = HangingDefaults.shift(startRotationAngle = 180),
                animation = HangingDefaults.animation(3000)
            )
    ) {
        Text(
            text = "Full Rotation",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Composable
private fun HangingOnRightSide() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .hanging(pivotPoint = HangingPivotPoint.Right),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    ) {
        Text(
            text = "Hanging On Right Side",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun QuickHangingOnLeftSide() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
            .hanging(
                shift = HangingDefaults.shift(10),
                animation = HangingDefaults.animation(300),
                pivotPoint = HangingPivotPoint.Left
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    ) {
        Text(
            text = "Quick Hanging On Left Side",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun EpilepsyAtBottom() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .hanging(
                shift = HangingDefaults.shift(
                    startRotationAngle = 5,
                    endRotationAngle = -10
                ),
                animation = HangingDefaults.animation(duration = 80),
                pivotPoint = HangingPivotPoint.Bottom
            ),
    ) {
        Text(
            text = "Epilepsy At Bottom",
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun HangingOnCenter() {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .hanging(
                animation = HangingDefaults.animation(duration = 4300),
                pivotPoint = HangingDefaults.pivot(
                    pivotFractionX = 0.5f,
                    pivotFractionY = 0.5f
                )
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        Text(
            text = "Slow Hanging On Center",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
private fun HangingDemoPreview() {
    Column {
        HangingDemo()
    }
}