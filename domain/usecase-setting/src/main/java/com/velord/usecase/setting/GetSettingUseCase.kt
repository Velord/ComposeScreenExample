package com.velord.usecase.setting

import com.velord.util.settings.ThemeConfig

interface GetSettingDataSource {
    fun getFlow(): ThemeConfig
}


class GetSettingUseCase(
    val dataSource: GetSettingDataSource
) {

}