package com.velord.ui.sharedviewmodel

import com.velord.core.resource.AppStringResource
import com.velord.usecase.setting.GetLocalizationStateUC
import org.koin.core.context.GlobalContext
import com.velord.core.resource.getString as resolveString

@UiContractExempt(UiContractExemptionReason.Base)
open class LocalizationVM(
    getLocalizationStateUC: Lazy<GetLocalizationStateUC> = lazy { GlobalContext.get().get() }
) : CoroutineScopeVM() {

    val localizationStateFlow by lazy { getLocalizationStateUC.value() }

    protected fun getString(resource: AppStringResource, vararg formatArgs: Any): String =
        resolveString(localizationStateFlow.value, resource, *formatArgs)
}
