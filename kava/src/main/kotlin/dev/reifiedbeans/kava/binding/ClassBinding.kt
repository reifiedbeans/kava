package dev.reifiedbeans.kava.binding

import kotlin.reflect.KClass

internal data class ClassBinding<T : Any>(
    override val source: KClass<T>,
    val target: KClass<out T>,
    override val singleton: Boolean,
    override val name: String?,
) : Binding<T>
