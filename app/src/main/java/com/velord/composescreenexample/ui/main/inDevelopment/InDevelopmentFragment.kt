package com.velord.composescreenexample.ui.main.inDevelopment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.velord.uicore.compose.PervasiveArcFromBottomShape
import com.velord.uicore.utils.setContentWithTheme
import com.velord.util.fragment.activityNavController
import com.velord.util.fragment.viewLifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.velord.uicore.R as RString

@AndroidEntryPoint
class InDevelopmentFragment : Fragment() {

    private val viewModel by viewModels<InDevelopmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        InDevelopmentScreen(viewModel)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserving()
    }

    private fun initObserving() {
        viewLifecycleScope.launch {
            launch {
                viewModel.navigationEvent.collect {
                    activityNavController()?.navigate(it.id)
                }
            }
            launch {
                viewModel.backEvent.collect {
                    findNavController().popBackStack()
                }
            }
        }
    }
}

@Composable
private fun InDevelopmentScreen(viewModel: InDevelopmentViewModel) {
    val time = remember { System.currentTimeMillis().toString() }
    Content(
        text = time,
        onOpenNew = viewModel::onOpenNew
    )
}

@Composable
private fun Content(
    text: String,
    onOpenNew: () -> Unit
) {
    Box(Modifier.fillMaxSize()) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.Center)
        )
        Button(
            onClick = onOpenNew,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        ) {
            Text(text = stringResource(id = RString.string.open_new))
        }

        VisByGenericShape(
            isVisible = isVisibleState.value,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 82.dp)
        )
    }
}

@Composable
private fun VisByGenericShape(
    isVisible: Boolean,
    modifier: Modifier = Modifier
) {
    val animationProgressState by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
    )
    val isEdgeState = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = isVisible) {
        if (isVisible.not()) {
            isEdgeState.value = false
        }
    }

    val currentShape: Shape = when {
        isEdgeState.value -> CutCornerShape(10.dp)
        animationProgressState == 1f -> CutCornerShape(10.dp)
        else -> {
            val progress = (animationProgressState * 100).toInt()
            PervasiveArcFromBottomShape(progress) {
                isEdgeState.value = true
            }
        }
    }

    var count = 0
    if (isVisible) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .graphicsLayer {
                    shape = currentShape
                    clip = true
                }
                .background(color = MaterialTheme.colorScheme.secondary)
        ) {
            Row2(count = count++)
            Row2(count = count++)
            Row2(count = count++)
        }
    }
}

@Composable
private fun Row2(count: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        Text(
            text = "Item: ${count}",
            fontSize = 20.sp,
            modifier = Modifier
                .border(1.dp, color = MaterialTheme.colorScheme.onError),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "Item: ${count + 1}",
            fontSize = 20.sp,
            modifier = Modifier
                .border(1.dp, color = MaterialTheme.colorScheme.onError),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = "Item: ${count + 2}",
            fontSize = 20.sp,
            modifier = Modifier
                .border(1.dp, color = MaterialTheme.colorScheme.onError),
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Preview
@Composable
private fun InDevelopmentPreview() {
    Content(
        text = "Now",
        onOpenNew = {}
    )
}