import dev.reifiedbeans.kava.getInstance
import dev.reifiedbeans.kava.kava
import jakarta.inject.Inject
import jakarta.inject.Named

private const val SPECIAL_MESSAGE = "SPECIAL_MESSAGE"

interface Messenger {
    fun sendMessage(message: String)
}

class StandardMessenger : Messenger {
    override fun sendMessage(message: String) {
        println(message)
    }
}

// Use regular Guice or Jakarta annotations
class SimpleApplication @Inject constructor(
    private val messenger: Messenger,
    @Named(SPECIAL_MESSAGE) private val message: String,
) {
    fun start() {
        messenger.sendMessage(message)
    }
}

fun main() {
    // Bind implementations and add providers using a concise DSL
    val module = kava {
        bind { Messenger::class to StandardMessenger::class }
        provide(String::class, name = SPECIAL_MESSAGE) { "Hello, world!" }
    }

    // Use the built-in Injector to get instances. Any Injector can be extended with Kava's getInstance function
    // to create instances using the Kotlin class reference syntax
    val app = module.injector.getInstance(SimpleApplication::class)
    app.start()
}
