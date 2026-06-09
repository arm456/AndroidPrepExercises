package com.interviewprep.exercises.core

/**
 * ImmutableList — a lightweight Kotlin replacement for Guava's ImmutableList.
 *
 * ─── Why we dropped Guava ────────────────────────────────────────────────────
 *
 * Guava triggers AGP's constraint alignment handler during dependency
 * resolution, which attempts to mutate debugCompileClasspath after it has
 * already been resolved — causing a build failure in processDebugResources.
 * This is a known incompatibility between guava:32.x and AGP 8.x.
 *
 * The fix: implement the same ImmutableList contract ourselves using Kotlin's
 * standard library. The behavioral guarantee is identical:
 *   - Contents cannot be mutated after construction
 *   - Adding an element always produces a new reference
 *   - LiveData observers fire correctly because the reference changes
 *
 * ─── Interview note (Milestone 4 Q2 answer still holds) ─────────────────────
 *
 * The core concept is unchanged: LiveData uses reference equality. Any list
 * that guarantees a new reference on "add" satisfies the pattern. Guava's
 * ImmutableList, this class, or even `listOf()` all work — what matters is
 * that you never mutate the list in-place.
 *
 * ─────────────────────────────────────────────────────────────────────────────
 */
class ImmutableList<T> private constructor(
    private val backing: List<T>
) : List<T> by backing {

    companion object {
        /** Create an empty ImmutableList. */
        fun <T> of(): ImmutableList<T> = ImmutableList(emptyList())

        /** Create an ImmutableList from varargs. */
        fun <T> of(vararg elements: T): ImmutableList<T> = ImmutableList(elements.toList())

        /** Create an ImmutableList from any Iterable. */
        fun <T> copyOf(elements: Iterable<T>): ImmutableList<T> =
            ImmutableList(elements.toList())

        /** Builder pattern for compatibility with Guava-style call sites. */
        fun <T> builder(): Builder<T> = Builder()
    }

    class Builder<T> {
        private val items = mutableListOf<T>()

        fun add(item: T): Builder<T> = apply { items.add(item) }

        fun addAll(elements: Iterable<T>): Builder<T> = apply { items.addAll(elements) }

        fun build(): ImmutableList<T> = ImmutableList(items.toList())
    }

    override fun toString(): String = backing.toString()
    override fun equals(other: Any?): Boolean = backing == other
    override fun hashCode(): Int = backing.hashCode()
}
