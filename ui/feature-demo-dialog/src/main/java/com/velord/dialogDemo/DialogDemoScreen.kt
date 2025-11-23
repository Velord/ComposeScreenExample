package com.velord.dialogDemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.velord.core.resource.R
import com.velord.dialogDemo.component.DialogActions
import com.velord.dialogDemo.component.DialogAnimations
import com.velord.dialogDemo.component.DialogPredefinedAnimation
import com.velord.dialogDemo.component.OneButtonDialog
import com.velord.dialogDemo.component.TwoButtonDialog
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.suspendCoroutine
import kotlin.math.max
import kotlin.random.Random

class GeminiRequestProcessor {

    fun proceed(request: String, callback: (ResponseGemini) -> Unit) {
        Thread.sleep(1000)
        val length = Random.nextInt(max(request.length * 3, 9)) + 1
        val answer = generateAnswer(length).replaceFirstChar { it.uppercase() }
        callback(ResponseGemini(answer))
    }

    private val words = listOf(
        "lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
        "adipiscing", "elit", "sed", "do", "eiusmod", "tempor",
        "incididunt", "ut", "labore", "et", "dolore", "magna", "aliqua"
    )

    private fun generateAnswer(wordCount: Int): String {
        return (1..wordCount)
            .map { words.random() }
            .joinToString(" ")
    }

    data class ResponseGemini(val answer: String)
}

class ChatgptRequestProcessor {

    fun proceed(request: String): ResponseChatGpt {
        Thread.sleep(2000)
        val length = Random.nextInt(max(request.length * 3, 9)) + 1
        val answer = generateAnswer(length).replaceFirstChar { it.uppercase() }
        return ResponseChatGpt(answer)
    }

    private val words = listOf(
        "galaxy", "throw", "travel", "drive", "melon", "swim", "read", "dance",
        "black", "peach", "write", "cat", "jump", "apple", "couch", "blackhole",
        "run", "chair", "elephant", "truck", "to",
    )

    private fun generateAnswer(wordCount: Int): String {
        return (1..wordCount)
            .map { words.random() }
            .joinToString(" ")
    }

    data class ResponseChatGpt(val answer: String)
}

class AlphaRequestProcessor {

    fun proceed(request: String) = flow {
        val length = Random.nextInt(max(request.length * 3, 9)) + 1
        (1..length).map {
            emit(words.random())
            Thread.sleep(100)
        }
    }

    private val words = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
}

data class ResponseProcessor(
    val answerGemini: String? = null,
    val answerChatGpt: String? = null,
    val answerAlpha: String? = null
)

suspend fun wrapper(proc: GeminiRequestProcessor): String = suspendCoroutine {  cont ->
    proc.proceed("request") {
        cont.resumeWith(Result.success(it.answer))
    }
}

interface A {
    suspend fun proceed(request: String): String
}

class Processor(
    val fds: List<String>
) : CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

    val flows = MutableStateFlow<List<ResponseProcessor>>(emptyList())

    fun process(request: String) {
        launch {
//            fds.map {
//                async {
//                    it.proceed(request)
//                }
//            }.awaitAll().joinToString()
        }
    }
}

//fun main2() = runBlocking {
//    val sdf = Processor(
//        gemini = GeminiRequestProcessor(),
//        chatgpt = ChatgptRequestProcessor(),
//        alpha = AlphaRequestProcessor()
//    )
//
//    launch {
//        sdf.process("What is the meaning of life?")
//    }
//
//    sdf.flows.collect { responses ->
//        println("Collected responses: $responses")
//    }
//
//    Unit
//}

@Composable
fun DialogDemoScreen(viewModel: DialogDemoViewModel) {
    val uiState = viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val isVisibleTwoButtonDialogState = rememberUpdatedState(uiState.value.isVisibleTwoButtonDialog)
    val isVisibleOneButtonDialogState = rememberUpdatedState(uiState.value.isVisibleOneButtonDialog)

    Content(onAction = viewModel::onAction)

    TwoButtonDialog(
        isVisibleState = isVisibleTwoButtonDialogState,
        title = "Two Button Dialog",
        action = DialogActions.TwoButton {
            viewModel.onAction(DialogDemoUiAction.TwoButtonDialogDismiss)
        }
    )

    OneButtonDialog(
        isVisibleState = isVisibleOneButtonDialogState,
        title = "One Button Dialog",
        action = DialogActions.OneButton {
            viewModel.onAction(DialogDemoUiAction.OneButtonDialogDismiss)
        },
        animation = DialogAnimations.Default.copy(
            mainBox = DialogPredefinedAnimation.Default()
        )
    )


}

@Composable
private fun Content(onAction: (DialogDemoUiAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OpenButton(
            text = stringResource(R.string.show_two_buttons_dialog),
            onClick = { onAction(DialogDemoUiAction.OpenTwoButtonDialogClick) }
        )
        OpenButton(
            text = stringResource(R.string.show_one_button_dialog),
            onClick = { onAction(DialogDemoUiAction.OpenOneButtonDialogClick) },
        )
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

@Preview
@Composable
private fun ShapeDemoPreview() {
    Content(
        onAction = {}
    )
}

private val handler = CoroutineExceptionHandler { _, throwable ->
    println("CoroutineExceptionHandler: ${throwable.message}")
}
private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default + handler)

fun main2() = runBlocking {
    scope.async(handler) {
        error("Top level async error")
    }.await()

//    scope.launch {
//        launch {
//            launch {
//                launch {
//                    launch {
//                        launch(handler + Job()) {
//                            delay(100L)
//                            error("This is an error in nested launch block")
//                        }
//                    }
//                }
//            }
//        }
//
//        async(Job() + handler) { // Even Job creates new coroutine scope, handler is ignored by async
//            error("This is an error in async block")
//        }
//    }
//
//    scope.launch(handler) {
//        coroutineScope {
//            async {
//                error("coroutineScope This is an error in async block outside of launch")
//            }
//        }
//    }
//    scope.launch {
//        launch {
//            launch(handler) {
//                launch {
//                    supervisorScope {
//                        launch {
//                            error("supervisorScope launch outside of main scope")
//                        }
//                        launch {
//                            launch {
//                                async {
//                                    delay(100L)
//                                    error("supervisorScope async outside of main scope")
//                                }.await()
//                            }
//                        }
//
//                        launch {
//                            withContext(handler + Dispatchers.IO) {
//                                error("withContext error in block outside of launch")
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        withContext(handler + Dispatchers.IO) {
//            //error("withContext !!!!!!! error in block outside of launch")
//        }
//    }

    delay(10000L)
    Unit
}

fun main() {
    V.bar()
}

class V(val x: Int) {

    constructor() : this(42) {
        println("Secondary constructor initialized with x = $x")
    }

    init {
        println("First init Initialized with x = $x")
    }

    init {
        println("Second init initialized with x = $x")
    }

    fun foo() {
        println("Function foo called with x = $x")
    }

    companion object {

        init {
            println("Companion object initialized")
        }

        operator fun invoke(): V {
            Unit
            println("Companion object invoke called")
            return V()
        }

        fun bar() {
            println("Companion object function bar called")
        }
    }
}
open class Extension

abstract sealed class FFF : Extension() {
    data class A(val x: Int) : FFF()
    data class B(val y: String) : FFF()
}

sealed class SSS : FFF() {
    object A : SSS()
    object B : SSS()
}

enum class S {
    X, Y, Z
}

data class Bbb(val x: Int, val y: String) : Extension()
