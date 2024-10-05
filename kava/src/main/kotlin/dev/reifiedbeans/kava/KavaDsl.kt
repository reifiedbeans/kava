package dev.reifiedbeans.kava

import com.google.inject.Module
import com.google.inject.Provider
import dev.reifiedbeans.kava.binding.Binding
import dev.reifiedbeans.kava.binding.ClassBinding
import dev.reifiedbeans.kava.binding.ProviderBinding
import kotlin.reflect.KClass

/**
 * The DSL for building a Kava module.
 */
class KavaDsl {
    internal val bindings = mutableListOf<Binding<*>>()
    internal val modules = mutableListOf<Module>()

    fun <T : Any> bind(
        singleton: Boolean = false,
        name: String? = null,
        binding: () -> Pair<KClass<T>, KClass<out T>>,
    ) {
        val (source, target) = binding()
        bindings.add(ClassBinding(source, target, singleton, name))
    }

    fun <T : Any> provide(
        type: KClass<T>,
        singleton: Boolean = false,
        name: String? = null,
        provider: Provider<out T>,
    ) {
        bindings.add(ProviderBinding(type, provider, singleton, name))
    }

    fun install(module: Module) {
        modules.add(module)
    }
}
