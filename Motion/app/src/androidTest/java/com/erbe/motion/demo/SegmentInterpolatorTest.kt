package com.erbe.motion.demo

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.google.common.truth.Truth.assertWithMessage
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SegmentInterpolatorTest {

    @Test
    fun matchWithBase() {
        val base = FAST_OUT_SLOW_IN
        val first = SegmentInterpolator(base, 0f, 0.5f)
        val second = SegmentInterpolator(base, 0.5f, 1f)
        val count = 100

        // Start and end of an interpolator are supposed to be 0 and 1
        assertThat(first.getInterpolation(0f)).isWithin(0.01f).of(0f)
        assertThat(first.getInterpolation(1f)).isWithin(0.01f).of(1f)
        assertThat(second.getInterpolation(0f)).isWithin(0.01f).of(0f)
        assertThat(second.getInterpolation(1f)).isWithin(0.01f).of(1f)

        // Make sure that the segments match with the base when combined.
        val baseMiddle = base.getInterpolation(0.5f)
        for (i in 0..count) {
            val baseInput = 1f * i / count
            if (baseInput < 0.5f) {
                val firstInput = baseInput / 0.5f
                assertWithMessage("Mismatch at iteration $i input $baseInput ($firstInput)")
                    .that(first.getInterpolation(firstInput))
                    .isWithin(0.01f)
                    .of(base.getInterpolation(baseInput) / baseMiddle)
            } else {
                val secondInput = (baseInput - 0.5f) / 0.5f

                assertWithMessage("Mismatch at iteration $i, input $baseInput ($secondInput)")
                    .that(second.getInterpolation(secondInput))
                    .isWithin(0.01f)
                    .of((base.getInterpolation(baseInput) - baseMiddle) / (1 - baseMiddle))
            }
        }
    }
}
