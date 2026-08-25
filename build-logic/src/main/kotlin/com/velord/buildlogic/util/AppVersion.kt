package com.velord.buildlogic.util

object AppVersion {
    // When app incompatible with previous version change this value
    const val globalVersion = 1
    // When you create huge feature(or many) release change this value
    const val majorVersion = 4
    // When you create feature release change this value
    const val minorVersion = 0
    // When you create fix change this value
    const val fixVersion = 0
    // When you create quick fix from master branch change this value
    const val hotfixVersion = 0

    // Based on current CI BUILD_NUMBER
    val buildNumber: Int
        get() = System.getenv("BUILD_NUMBER")?.toIntOrNull() ?: hotfixVersion

    // Doc says: max number is 2100000000
    // Do not use auto numeration when value beyond edge
    const val maxSafeVersionCode = 1000000000

    val calculatedVersionNumber: Int
        get() = globalVersion * 100000 +
                majorVersion * 10000 +
                minorVersion * 1000 +
                fixVersion * 100 +
                buildNumber

    // Don't use number greater than maxSafeVersionCode
    val isLessThanMax: Boolean get() = calculatedVersionNumber < maxSafeVersionCode

    val versionCode: Int get() = if (isLessThanMax) calculatedVersionNumber else 0

    val versionName: String get() = "$globalVersion.$majorVersion.$minorVersion"
}
