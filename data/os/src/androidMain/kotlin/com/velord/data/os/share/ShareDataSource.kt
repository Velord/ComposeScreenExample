package com.velord.data.os.share

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import org.koin.core.scope.Scope

@Module
actual class SharePlatformModule {
    @Single
    actual fun provideShareDataSource(scope: Scope): ShareDataSource =
        AndroidShareDataSource(scope.get())
}

private class AndroidShareDataSource(private val context: Context) : ShareDataSource {

    override suspend fun share(text: String) {
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, text)
            setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        withContext(Dispatchers.Main) {
            context.startActivity(shareIntent)
        }
    }
}
