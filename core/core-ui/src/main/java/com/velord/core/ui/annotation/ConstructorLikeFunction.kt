package com.velord.core.ui.annotation

/**
 * Marks a function that acts like a constructor — returning a new instance of a type.
 * PascalCase naming is intentional and follows Kotlin convention for factory functions.
 * Detekt's FunctionNaming rule is configured to ignore functions with this annotation.
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION)
annotation class ConstructorLikeFunction
