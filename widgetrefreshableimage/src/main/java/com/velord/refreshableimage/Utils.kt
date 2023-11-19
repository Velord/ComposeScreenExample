package com.velord.refreshableimage

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.FileProvider
import coil.imageLoader
import kotlin.random.Random

internal fun Context.getUriForFileThanGrantPermissionThanGetUriPath(url: String): String? {
    return imageLoader.diskCache?.get(url)?.use { snapshot ->
        val imageFile = snapshot.data.toFile()
        val contentUri = FileProvider.getUriForFile(
            this@getUriForFileThanGrantPermissionThanGetUriPath,
            createAuthorityForFile(),
            imageFile
        )
        // Find the current launcher everytime to ensure it has read permissions
        val intent = Intent(Intent.ACTION_MAIN).apply { addCategory(Intent.CATEGORY_HOME) }
        val resolveInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.packageManager.resolveActivity(
                intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
            )
        } else {
            this.packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
        }

        resolveInfo?.activityInfo?.packageName?.also {
            this.grantUriPermission(
                it,
                contentUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
            )
        }

        contentUri.toString()
    }
}

// See manifest for correct path
private fun Context.createAuthorityForFile(): String =
    "${applicationContext.packageName}.fileprovider"


private const val STRING_LENGTH = 6
private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
internal fun randomStringByKotlinRandom() = (1..STRING_LENGTH)
    .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
    .joinToString("")