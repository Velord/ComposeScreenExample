package com.velord.feature.demo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.velord.bottomnavigation.screen.jetpack.addTestCallback
import com.velord.bottomnavigation.viewmodel.BottomNavigationJetpackVM
import com.velord.resource.R
import com.velord.uicore.compose.preview.PreviewCombined
import com.velord.uicore.utils.ObserveSharedFlow
import com.velord.uicore.utils.setContentWithTheme
import com.velord.util.fragment.viewLifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.androidx.viewmodel.ext.android.viewModel

class DemoFragment : Fragment() {

    private val viewModel by viewModels<DemoViewModel>()
    private val viewModelBottom by viewModel<BottomNavigationJetpackVM>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = setContentWithTheme {
        DemoScreen(viewModel) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTestCallback("Demo graph", viewModelBottom)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserving()
    }

    private fun initObserving() {
        viewLifecycleScope.launch {
            viewModel.navigationEventJetpack.collect {
                findNavController().navigate(it.id)
            }
        }
    }
}

@Composable
fun DemoScreen(
    viewModel: DemoViewModel,
    onNavigationEvent: (DemoDestinationNavigationEvent) -> Unit
) {
    ObserveSharedFlow(flow = viewModel.navigationEventDestination) {
        onNavigationEvent(it)
    }

    Content(onAction = viewModel::onAction)
}

@Composable
private fun Content(onAction: (DemoUiAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OpenButton(text = stringResource(
            id = R.string.open_shape_demo),
            onClick = { onAction(DemoUiAction.OpenShapeClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_modifier_demo),
            onClick = { onAction(DemoUiAction.OpenModifierClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_flow_summator),
            onClick = { onAction(DemoUiAction.OpenSummatorClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_morph_demo),
            onClick = { onAction(DemoUiAction.OpenMorphClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_hint_phone_number),
            onClick = { onAction(DemoUiAction.OpenHintPhoneNumberClick) }
        )
        OpenButton(
            text = stringResource(id = R.string.open_movie),
            onClick = { onAction(DemoUiAction.OpenMovieClick) }
        )
        Foo()
    }
}

@Composable
private fun Foo() {
    val isVisibleLabel by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(top = 32.dp)
            .fillMaxWidth()
            .background(Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .background(Color.Red)
                .padding(20.dp)
                .size(100.dp)
                .border(2.dp, Color.Green)
                .background(Color.Blue)
        )
        //InfiniteRecompositionDemo()
    }
}

@Composable
private fun InfiniteRecompositionDemo() {
    val counter = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            // Recompose every 100 milliseconds (adjust as needed)
            delay(100)
            counter.intValue++
        }
    }

    Text(text = "Counter: ${counter.value}")
}

enum class Operation {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE
}

// Overflow prevention int -> long
fun calculate(a: Int, b: Int, op: Operation): Long  = when (op) {
    Operation.ADD -> a.toLong() + b.toLong()
    Operation.SUBTRACT -> a.toLong() - b.toLong()
    Operation.MULTIPLY -> a.toLong() * b.toLong()
    Operation.DIVIDE -> {
        if (b == 0) {
            throw IllegalArgumentException("Cannot divide by zero")
        }
        a.toLong() / b.toLong()
    }
}

@Composable
private fun OpenButton(
    text: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(top = 32.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        )
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.displaySmall.copy(fontSize = 22.sp)
        )
    }
}

@PreviewCombined
@Composable
private fun DemoPreview() {
    Content(onAction = {})
}

fun main() = runBlocking {
    val stateFlow = MutableStateFlow(0)
    val sharedFlow = MutableSharedFlow<Int>(1)
    sharedFlow.emit(1)

    launch {
        stateFlow.collect { println("$it") }
        println("Done stateFlow")
    }

    launch {
        sharedFlow.collect { println("$it") }
        println("Done sharedFlow")
    }
    delay(100)
    println("Done")
}
