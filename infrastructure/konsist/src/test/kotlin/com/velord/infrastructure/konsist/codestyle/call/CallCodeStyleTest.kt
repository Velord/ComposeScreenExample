package com.velord.infrastructure.konsist.codestyle.call

import com.lemonappdev.konsist.api.verify.assertTrue
import com.velord.infrastructure.konsist.codestyle.projectFileRoster
import kotlin.test.Test
import kotlin.test.assertTrue

class CallCodeStyleTest {
    @Test
    fun `one or two argument calls should stay on one line when they fit`() {
        val projectComposeCallNameRoster = projectFileRoster
            .flatMap { file -> composeCallNameRoster(file.text) }
            .toSet()
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitOneOrTwoArgumentCall(
                    composeCallNameRoster = projectComposeCallNameRoster,
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster.getOrNull(lineIndex + 1),
                    thirdLine = lineRoster.getOrNull(lineIndex + 2),
                    fourthLine = lineRoster.getOrNull(lineIndex + 3),
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "One or two argument call at line ${violation + 1} should stay on one line."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `object mapping calls with several arguments should use multiline arguments`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = lineRoster.withIndex().firstOrNull { (_, line) ->
                isInlineExpressionBodyObjectMapping(line)
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Object mapping call at line ${violation.index + 1} " +
                    "should use multiline arguments."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `single collection calls should stay with short receivers when they fit`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isSplitSingleCollectionCallAfterShortReceiver(
                    currentLine = lineRoster[lineIndex],
                    nextLine = lineRoster[lineIndex + 1],
                    thirdLine = lineRoster.getOrNull(lineIndex + 2),
                )
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Single collection call at line ${violation + 1} " +
                    "must stay with its short receiver."
                println(msg)
            }

            violation == null
        }
    }

    @Test
    fun `explicit lambda parameter collection calls may keep multiline bodies`() {
        val isViolation = isSplitSingleCollectionCallAfterShortReceiver(
            currentLine = "    val colorStops = gradientColorAndPosition",
            nextLine = "        .map { colorAndPosition ->",
            thirdLine = "            colorAndPosition.second to colorAndPosition.first",
        )

        assertTrue(isViolation.not())
    }

    @Test
    fun `aliased compose imports should remain compose calls`() {
        val fileText = """
            import com.kashif.cameraK.ui.CameraPreviewView as KameraPreviewView

            @Composable
            fun Content() = Unit
        """.trimIndent()
        val composeCallNameRoster = composeCallNameRoster(fileText)

        assertTrue(isComposeCallOpening(composeCallNameRoster, "KameraPreviewView("))
    }

    @Test
    fun `call chains should use one-line form or one call per wrapped line`() {
        projectFileRoster.assertTrue { file ->
            val lineRoster = file.text.lines()
            val violation = (0 until lineRoster.lastIndex).firstOrNull { lineIndex ->
                isCallChainViolation(lineRoster = lineRoster, lineIndex = lineIndex)
            }

            if (violation != null) {
                val msg = "Name: ${file.name}. FAILED. " +
                    "Call chain at line ${violation + 1} " +
                    "must be one line or one call per wrapped line."
                println(msg)
            }

            violation == null
        }
    }
}
