package com.velord.infrastructure.konsist.codestyle.controlflow

import org.jetbrains.kotlin.lexer.KtTokens
import org.jetbrains.kotlin.psi.KtBinaryExpression
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtEnumEntry
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtParenthesizedExpression
import org.jetbrains.kotlin.psi.psiUtil.collectDescendantsOfType
import org.jetbrains.kotlin.psi.psiUtil.parents

private val EQUALITY_TOKEN_ROSTER = setOf(
    KtTokens.EQEQ,
    KtTokens.EXCLEQ,
    KtTokens.EQEQEQ,
    KtTokens.EXCLEQEQEQ,
)

internal class EnumEntryCodeStyleRule(private val fileRoster: List<KtFile>) {

    private val entryOwnerByName = fileRoster.flatMap { it.enumEntryOwnerRoster() }.toMap()

    fun violationRoster(fileIndex: Int): List<String> {
        val file = fileRoster[fileIndex]
        return file.collectDescendantsOfType<KtBinaryExpression>()
            .filter { it.operationToken in EQUALITY_TOKEN_ROSTER }
            .mapNotNull { comparison ->
                val owner = fixedEntryOwner(comparison.left, file) ?:
                    fixedEntryOwner(comparison.right, file) ?: return@mapNotNull null
                val containingEnum = comparison.parents
                    .filterIsInstance<KtClass>()
                    .firstOrNull { it.isEnum() }
                    ?.fqName
                    ?.asString()
                if (containingEnum == owner) return@mapNotNull null

                val line = file.text.take(comparison.textOffset).count { it == '\n' } + 1
                "line $line: ${comparison.text}"
            }
    }

    private fun KtFile.enumEntryOwnerRoster(): List<Pair<String, String>> = this
        .collectDescendantsOfType<KtClass>()
        .filter { it.isEnum() }
        .flatMap { declaration ->
            val owner = declaration.fqName?.asString() ?: return@flatMap emptyList()
            declaration.declarations.filterIsInstance<KtEnumEntry>().map { entry ->
                "$owner.${entry.name}" to owner
            }
        }

    private fun fixedEntryOwner(expression: KtExpression?, file: KtFile): String? {
        val reference = expression?.referencePath() ?: return null
        val candidateRoster = buildList {
            add(reference)
            add("${file.packageFqName}.$reference")
            file.importDirectives.forEach { directive ->
                val path = directive.importedFqName?.asString() ?: return@forEach
                val importedName = directive.aliasName ?: path.substringAfterLast('.')
                when {
                    directive.isAllUnder -> add("$path.$reference")
                    reference == importedName -> add(path)
                    reference.startsWith("$importedName.") -> add(
                        path + reference.removePrefix(importedName),
                    )
                }
            }
        }

        return candidateRoster.firstNotNullOfOrNull { entryOwnerByName[it] }
    }

    private fun KtExpression.referencePath(): String? = when (this) {
        is KtNameReferenceExpression -> getReferencedName()
        is KtParenthesizedExpression -> expression?.referencePath()
        is KtDotQualifiedExpression -> {
            val receiver = receiverExpression.referencePath()
            val selector = selectorExpression?.referencePath()
            if (receiver != null && selector != null) "$receiver.$selector" else null
        }
        else -> null
    }
}
