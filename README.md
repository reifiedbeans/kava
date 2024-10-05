# Kava

[![maven central](https://img.shields.io/maven-central/v/dev.reifiedbeans/kava
)](https://central.sonatype.com/artifact/dev.reifiedbeans/kava)
[![license: MIT](https://img.shields.io/github/license/reifiedbeans/kava
)](https://github.com/reifiedbeans/kava/blob/main/LICENSE)

An easy-to-use Kotlin DSL for building modules with the Guice dependency injection framework.

```kotlin
import dev.reifiedbeans.kava.getInstance
import dev.reifiedbeans.kava.kava

data class Message(val content: String)

fun main() {
    val module = kava {
        provide(Message::class) { Message("Hello, world!") }
    }

    val message = module.injector.getInstance(Message::class)
    println(message.content)
}
```

## License

Licensed under the [MIT License](https://github.com/reifiedbeans/kava/blob/main/LICENSE).

[guice]: https://github.com/google/guice
