package com.erbe.motion.demo

import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionSet

/**
 * Creates a transition like [androidx.transition.AutoTransition], but customized to be more
 * true to Material Design.
 *
 * Fade through involves one element fading out completely before a new one fades in. These
 * transitions can be applied to text, icons, and other elements that don't perfectly overlap.
 * This technique lets the background show through during a transition, and it can provide
 * continuity between screens when paired with a shared transformation.
 *
 * See
 * [Expressing continuity](https://material.io/design/motion/understanding-motion.html#expressing-continuity)
 * for the detail of fade through.
 */
fun fadeThrough(): Transition {
    return transitionTogether {
        interpolator = FAST_OUT_SLOW_IN
        this += ChangeBounds()
        this += transitionSequential {
            addTransition(Fade(Fade.OUT))
            addTransition(Fade(Fade.IN))
        }
    }
}

inline fun transitionTogether(crossinline body: TransitionSet.() -> Unit): TransitionSet {
    return TransitionSet().apply {
        ordering = TransitionSet.ORDERING_TOGETHER
        body()
    }
}

inline fun transitionSequential(
    crossinline body: SequentialTransitionSet.() -> Unit
): SequentialTransitionSet {
    return SequentialTransitionSet().apply(body)
}

inline fun TransitionSet.forEach(action: (transition: Transition) -> Unit) {
    for (i in 0 until transitionCount) {
        action(getTransitionAt(i) ?: throw IndexOutOfBoundsException())
    }
}

inline fun TransitionSet.forEachIndexed(action: (index: Int, transition: Transition) -> Unit) {
    for (i in 0 until transitionCount) {
        action(i, getTransitionAt(i) ?: throw IndexOutOfBoundsException())
    }
}

operator fun TransitionSet.iterator() = object : MutableIterator<Transition> {

    private var index = 0

    override fun hasNext() = index < transitionCount

    override fun next() =
        getTransitionAt(index++) ?: throw IndexOutOfBoundsException()

    override fun remove() {
        removeTransition(getTransitionAt(--index) ?: throw IndexOutOfBoundsException())
    }
}

operator fun TransitionSet.plusAssign(transition: Transition) {
    addTransition(transition)
}

operator fun TransitionSet.get(i: Int): Transition {
    return getTransitionAt(i) ?: throw IndexOutOfBoundsException()
}
