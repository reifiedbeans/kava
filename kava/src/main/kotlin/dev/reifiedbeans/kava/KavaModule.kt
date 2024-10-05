package dev.reifiedbeans.kava

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.Provider
import com.google.inject.Singleton
import com.google.inject.binder.AnnotatedBindingBuilder
import com.google.inject.binder.LinkedBindingBuilder
import com.google.inject.binder.ScopedBindingBuilder
import com.google.inject.name.Names
import dev.reifiedbeans.kava.binding.Binding
import dev.reifiedbeans.kava.binding.ClassBinding
import dev.reifiedbeans.kava.binding.ProviderBinding

/**
 * A Guice Module with a built-in [Injector] for use in single-module applications.
 * You can create a [KavaModule] using the Kava DSL builder ([kava]).
 */
class KavaModule internal constructor(
    private val bindings: List<Binding<*>> = emptyList(),
    private val modules: List<Module> = emptyList(),
) : AbstractModule() {
    val injector: Injector by lazy { Guice.createInjector(this) }

    override fun configure() {
        modules.forEach(::install)

        for (binding in bindings) {
            var builder: ScopedBindingBuilder = bind(binding.source.java)

            if (binding.name != null) {
                builder = (builder as AnnotatedBindingBuilder<*>)
                    .annotatedWith(Names.named(binding.name))
            }

            @Suppress("UNCHECKED_CAST")
            builder = when (binding) {
                is ClassBinding<*> -> {
                    (builder as LinkedBindingBuilder<*>)
                        .to(binding.target.java as Class<out Nothing>)
                }

                is ProviderBinding<*> -> {
                    (builder as LinkedBindingBuilder<*>)
                        .toProvider(binding.provider as Provider<out Nothing>)
                }
            }

            if (binding.singleton) {
                builder.`in`(Singleton::class.java)
            }
        }
    }
}
