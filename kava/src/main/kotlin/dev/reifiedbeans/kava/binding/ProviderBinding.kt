package dev.reifiedbeans.kava.binding

import com.google.inject.Provider
import kotlin.reflect.KClass

internal data class ProviderBinding<T : Any>(
    override val source: KClass<T>,
    val provider: Provider<out T>,
    override val singleton: Boolean,
    override val name: String?,
) : Binding<T>
