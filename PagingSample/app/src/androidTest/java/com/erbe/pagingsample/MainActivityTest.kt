package com.erbe.pagingsample

import android.content.Intent
import androidx.annotation.UiThread
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Simply sanity test to ensure that activity launches without any issues and shows some data.
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Test
    @UiThread
    fun showSomeResults() {
        val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        ActivityScenario.launch<MainActivity>(intent)
        onView(withId(R.id.cheeseList)).check { view, noViewFoundException ->
            if (noViewFoundException != null) {
                throw noViewFoundException
            }

            val recyclerView = view as RecyclerView
            assertThat(recyclerView.adapter).isNotNull()
            assertThat(recyclerView.adapter!!.itemCount).isGreaterThan(0)
        }
    }
}
