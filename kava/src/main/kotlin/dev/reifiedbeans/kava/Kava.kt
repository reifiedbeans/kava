package dev.reifiedbeans.kava

/**
 * Creates a new Kava module using the Kava DSL.
 */
fun kava(init: KavaDsl.() -> Unit): KavaModule {
    val dsl = KavaDsl().apply(init)
    return KavaModule(dsl.bindings, dsl.modules)
}
