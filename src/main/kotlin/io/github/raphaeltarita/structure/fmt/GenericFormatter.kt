package io.github.raphaeltarita.structure.fmt

object GenericFormatter : Formatter<Any?> {
    override fun Any?.format(): String = toString()
}