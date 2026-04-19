package com.velord.datastore.appSetting

import androidx.datastore.core.okio.OkioSerializer
import com.velord.model.setting.AppSetting
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import okio.BufferedSink
import okio.BufferedSource

object AppSettingDataStoreSerializer : OkioSerializer<AppSetting> {

    override val defaultValue: AppSetting = AppSetting.DEFAULT

    override suspend fun readFrom(source: BufferedSource): AppSetting = try {
        Json.decodeFromString(
            deserializer = AppSetting.serializer(),
            string = source.readUtf8(),
        )
    } catch (exception: SerializationException) {
        defaultValue
    }

    override suspend fun writeTo(t: AppSetting, sink: BufferedSink) {
        sink.writeUtf8(
            Json.encodeToString(
                serializer = AppSetting.serializer(),
                value = t,
            )
        )
    }
}
