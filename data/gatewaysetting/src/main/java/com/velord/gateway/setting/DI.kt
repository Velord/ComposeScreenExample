package com.velord.gateway.setting

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

//@Module(includes = [SettingGatewayModule::class])
//@ComponentScan("com.velord.usecase.setting")
//class SettingUseCaseModule

@Module
@ComponentScan("com.velord.gateway.setting")
class SettingGatewayModule