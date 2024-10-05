package dev.reifiedbeans.kava.binding

import kotlin.reflect.KClass

internal sealed interface Binding<T : Any> {
    val source: KClass<T>
    val singleton: Boolean
    val name: String?
}
