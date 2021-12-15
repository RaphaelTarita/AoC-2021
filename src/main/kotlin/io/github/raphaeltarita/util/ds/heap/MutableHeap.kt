package io.github.raphaeltarita.util.ds.heap

interface MutableHeap<E> : MutableCollection<E>, Heap<E> {
    fun popMin(): E
}