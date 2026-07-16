package com.velord.data.os.share

interface ShareDataSource {
    suspend fun share(text: String)
}
