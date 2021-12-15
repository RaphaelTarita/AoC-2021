package io.github.raphaeltarita.util.ds.heap

interface Heap<E> : Collection<E> {
    fun minOrNull(): E?
    fun min(): E = minOrNull() ?: throw NoSuchElementException("Heap is empty")
}