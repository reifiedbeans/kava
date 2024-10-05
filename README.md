# Kava

[![maven central](https://img.shields.io/maven-central/v/dev.reifiedbeans/kava
)](https://central.sonatype.com/artifact/dev.reifiedbeans/kava)
[![docs](https://img.shields.io/badge/dynamic/json?url=https%3A%2F%2Fjavadoc.io%2Ftypeahead%2Fversion_ids%3FgroupId%3Ddev.reifiedbeans%26artifactId%3Dkava&query=%24.%5B-1%3A%5D&prefix=v&label=dokka&color=%23835bf7)](https://javadoc.io/doc/dev.reifiedbeans/kava)
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
