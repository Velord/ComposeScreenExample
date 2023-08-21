package com.example.widgetnewimage

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import coil.imageLoader

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
            Log.d("RefreshableImageWidget", "getRandomImage: launcherName: $it")
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