package com.velord.data.os.file

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.provider.DocumentsContract
import java.io.File

private const val DOCUMENT_AUTHORITY = "com.android.externalstorage.documents"
private const val PRIMARY_STORAGE = "primary"

class AndroidFileDataSource(private val context: Context) : BaseFileDataSource() {

    override val appFolderName: String
        get() = context.applicationInfo.loadLabel(context.packageManager).toString()

    override fun createDirectory(path: String): String = File(path)
        .apply { mkdirs() }
        .absolutePath

    override fun openDirectory(path: String): Boolean = try {
        context.startActivity(createOpenDirectoryIntent(path))
        true
    } catch (_: Exception) {
        false
    }

    private fun createOpenDirectoryIntent(
        path: String
    ): Intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE).apply {
        path.toPrimaryTreeUri()?.let { uri ->
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
        }
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    private fun String.toPrimaryTreeUri() = toPrimaryTreeId()?.let { treeId ->
        DocumentsContract.buildTreeDocumentUri(DOCUMENT_AUTHORITY, treeId)
    }

    private fun String.toPrimaryTreeId(): String? {
        val rootPath = Environment.getExternalStorageDirectory().absolutePath
        val targetPath = File(this).absolutePath
        if (targetPath.startsWith(rootPath).not()) return null

        val relativePath = targetPath
            .removePrefix(rootPath)
            .trimStart(File.separatorChar)
            .replace(File.separatorChar, '/')
        return "$PRIMARY_STORAGE:$relativePath"
    }

    override fun getRootDirectoryPath(
        directory: FileDirectory,
        storageScope: FileStorageScope,
    ): String = when (storageScope) {
        FileStorageScope.Public -> directory.getPublicRootDirectoryPath()
        FileStorageScope.AppPrivate -> directory.getAppPrivateRootDirectoryPath()
    }

    private fun FileDirectory.getPublicRootDirectoryPath(): String = when (this) {
        FileDirectory.Video -> Environment
            .getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
            .absolutePath
    }

    private fun FileDirectory.getAppPrivateRootDirectoryPath(): String = when (this) {
        FileDirectory.Video -> context
            .getExternalFilesDir(Environment.DIRECTORY_MOVIES)
            ?.absolutePath
            ?: File(context.filesDir, Environment.DIRECTORY_MOVIES).absolutePath
    }
}
