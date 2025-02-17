package com.velord.datastore.appSettings

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.velord.model.settings.AppSettings
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AppSettingsDataStoreSerializer : Serializer<AppSettings> {

    override val defaultValue: AppSettings = AppSettings.DEFAULT

    override suspend fun readFrom(input: InputStream): AppSettings = try {
        Json.decodeFromString(
            deserializer = AppSettings.serializer(),
            string = input.readBytes().decodeToString()
        )
    } catch (e: SerializationException) {
//         Possible variant
//        e.printStackTrace()
//        defaultValue
        throw CorruptionException("Unable to read ${AppSettings::class.simpleName}", e)
    }

    override suspend fun writeTo(t: AppSettings, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AppSettings.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}