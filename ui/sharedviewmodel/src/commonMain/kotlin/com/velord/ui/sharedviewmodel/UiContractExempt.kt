package com.velord.ui.sharedviewmodel

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class UiContractExempt(val reason: UiContractExemptionReason)

enum class UiContractExemptionReason {
    Base,
    SimpleFlow,
}
