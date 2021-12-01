package io.github.raphaeltarita.structure.fmt

interface Formatter<in T> {
    fun T.format(): String
}