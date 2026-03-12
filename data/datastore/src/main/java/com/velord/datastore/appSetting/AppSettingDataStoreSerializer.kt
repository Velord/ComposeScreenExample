package com.velord.datastore.appSetting

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.velord.model.setting.AppSetting
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AppSettingDataStoreSerializer : Serializer<AppSetting> {

    override val defaultValue: AppSetting = AppSetting.DEFAULT

    override suspend fun readFrom(input: InputStream): AppSetting = try {
        Json.decodeFromString(
            deserializer = AppSetting.serializer(),
            string = input.readBytes().decodeToString()
        )
    } catch (e: SerializationException) {
//         Possible variant
//        e.printStackTrace()
//        defaultValue
        throw CorruptionException("Unable to read ${AppSetting::class.simpleName}", e)
    }

    override suspend fun writeTo(t: AppSetting, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AppSetting.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}