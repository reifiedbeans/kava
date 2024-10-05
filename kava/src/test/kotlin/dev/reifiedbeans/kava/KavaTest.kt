package dev.reifiedbeans.kava

import com.google.inject.Binder
import com.google.inject.Guice
import com.google.inject.Injector
import com.google.inject.Provider
import com.google.inject.Singleton
import com.google.inject.name.Names
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class KavaTest {
    @Nested inner class InjectorTest {
        @Test fun `it returns an injector from Guice`() {
            val mockInjector = mockk<Injector>()
            mockkStatic(Guice::class)
            every { Guice.createInjector(any<KavaModule>()) } returns mockInjector

            val module = kava { }
            assertEquals(mockInjector, module.injector)
        }

        @Test fun `it converts KClass instances to Class instances`() {
            val injector = spyk(Guice.createInjector())
            injector.getInstance(Any::class)
            verify { injector.getInstance(Object::class.java) }
        }
    }

    @Nested inner class ConfigureTest {
        @Test fun `it installs provided modules`() {
            val binder = mockk<Binder>(relaxed = true)
            val childModules = listOf(KavaModule(), KavaModule())
            val rootModule = kava {
                childModules.forEach(::install)
            }

            rootModule.configure(binder)
            childModules.forEach {
                verify { binder.install(it) }
            }
        }

        @Test fun `it adds bindings for ClassBindings`() {
            val binder = mockk<Binder>(relaxed = true)
            val module = kava {
                bind { Any::class to String::class }
                bind { Exception::class to IllegalArgumentException::class }
            }

            module.configure(binder)
            verify { binder.bind(Any::class.java).to(String::class.java) }
            verify { binder.bind(Exception::class.java).to(IllegalArgumentException::class.java) }
        }

        @Test fun `it adds bindings for ProviderBindings`() {
            val binder = mockk<Binder>(relaxed = true)
            val stringProvider = Provider { "Hello, world!" }
            val exceptionProvider = Provider { IllegalArgumentException() }
            val module = kava {
                provide(String::class, provider = stringProvider)
                provide(Exception::class, provider = exceptionProvider)
            }

            module.configure(binder)
            verify { binder.bind(String::class.java).toProvider(stringProvider) }
            verify { binder.bind(Exception::class.java).toProvider(exceptionProvider) }
        }

        @Test fun `it annotates bindings with their given name`() {
            val binder = mockk<Binder>(relaxed = true)
            val module = kava {
                provide(String::class, name = "Message") { "Hello, world!" }
            }

            module.configure(binder)
            verify {
                binder.bind(String::class.java)
                    .annotatedWith(Names.named("Message"))
                    .toProvider(any<Provider<String>>())
            }
        }

        @Test fun `it adds the Singleton scope to singleton bindings`() {
            val binder = mockk<Binder>(relaxed = true)
            val module = kava {
                bind(singleton = true) { Exception::class to IllegalArgumentException::class }
            }

            module.configure(binder)
            verify {
                binder.bind(Exception::class.java)
                    .to(IllegalArgumentException::class.java).`in`(Singleton::class.java)
            }
        }
    }
}
