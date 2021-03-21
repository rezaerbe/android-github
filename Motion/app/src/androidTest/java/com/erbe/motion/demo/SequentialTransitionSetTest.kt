package com.erbe.motion.demo

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.transition.Fade
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SequentialTransitionSetTest {

    @Test
    fun setDuration_before() {
        val set = transitionSequential {
            duration = 100L
            this += Fade(Fade.OUT)
            this += Fade(Fade.IN)
        }

        assertThat(set[0].duration).isEqualTo(50L)
        assertThat(set[1].duration).isEqualTo(50L)
    }

    @Test
    fun setDuration_after() {
        val set = transitionSequential {
            this += Fade(Fade.OUT)
            this += Fade(Fade.IN)
            duration = 100L
        }

        assertThat(set[0].duration).isEqualTo(50L)
        assertThat(set[1].duration).isEqualTo(50L)
    }

    @Test
    fun setInterpolator_before() {
        val set = transitionSequential {
            interpolator = FAST_OUT_SLOW_IN
            this += Fade(Fade.OUT)
            this += Fade(Fade.IN)
        }

        set[0].interpolator!!.let { interpolator ->
            val si = interpolator as SegmentInterpolator
            assertThat(si.base).isSameInstanceAs(set.interpolator)
            assertThat(si.start).isEqualTo(0f)
            assertThat(si.end).isEqualTo(0.5f)
        }
        set[1].interpolator!!.let { interpolator ->
            val si = interpolator as SegmentInterpolator
            assertThat(si.base).isSameInstanceAs(set.interpolator)
            assertThat(si.start).isEqualTo(0.5f)
            assertThat(si.end).isEqualTo(1f)
        }
    }

    @Test
    fun setInterpolator_after() {
        val set = transitionSequential {
            this += Fade(Fade.OUT)
            this += Fade(Fade.IN)
            interpolator = FAST_OUT_SLOW_IN
        }

        set[0].interpolator!!.let { interpolator ->
            val si = interpolator as SegmentInterpolator
            assertThat(si.base).isSameInstanceAs(set.interpolator)
            assertThat(si.start).isEqualTo(0f)
            assertThat(si.end).isEqualTo(0.5f)
        }
        set[1].interpolator!!.let { interpolator ->
            val si = interpolator as SegmentInterpolator
            assertThat(si.base).isSameInstanceAs(set.interpolator)
            assertThat(si.start).isEqualTo(0.5f)
            assertThat(si.end).isEqualTo(1f)
        }
    }

    @Test
    fun weight() {
        val set = transitionSequential {
            addTransition(Fade(Fade.OUT), 2f)
            addTransition(Fade(Fade.OUT), 3f)
            interpolator = FAST_OUT_SLOW_IN
        }

        set[0].interpolator!!.let { interpolator ->
            val si = interpolator as SegmentInterpolator
            assertThat(si.base).isSameInstanceAs(set.interpolator)
            assertThat(si.start).isEqualTo(0f)
            assertThat(si.end).isEqualTo(0.4f)
            assertThat(si.getInterpolation(0f)).isWithin(0.01f).of(0f)
            assertThat(si.getInterpolation(1f)).isWithin(0.01f).of(1f)
        }
        set[1].interpolator!!.let { interpolator ->
            val si = interpolator as SegmentInterpolator
            assertThat(si.base).isSameInstanceAs(set.interpolator)
            assertThat(si.start).isEqualTo(0.4f)
            assertThat(si.end).isEqualTo(1f)
            assertThat(si.getInterpolation(0f)).isWithin(0.01f).of(0f)
            assertThat(si.getInterpolation(1f)).isWithin(0.01f).of(1f)
        }
    }
}
