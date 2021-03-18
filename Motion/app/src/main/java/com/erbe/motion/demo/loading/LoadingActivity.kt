package com.erbe.motion.demo.loading

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionListenerAdapter
import androidx.transition.TransitionManager
import com.erbe.motion.R
import com.erbe.motion.demo.FAST_OUT_SLOW_IN
import com.erbe.motion.demo.LARGE_EXPAND_DURATION
import com.erbe.motion.demo.plusAssign
import com.erbe.motion.demo.transitionSequential
import com.erbe.motion.ui.EdgeToEdge

/**
 * Shows a list of cheeses. We use the Paging Library to load the list.
 */
class LoadingActivity : AppCompatActivity() {

    private val viewModel: LoadingViewModel by viewModels()

    private lateinit var list: RecyclerView
    private val fade = transitionSequential {
        duration = LARGE_EXPAND_DURATION
        interpolator = FAST_OUT_SLOW_IN
        this += Fade(Fade.OUT)
        this += Fade(Fade.IN)
        addListener(object : TransitionListenerAdapter() {
            override fun onTransitionEnd(transition: Transition) {
                if (savedItemAnimator != null) {
                    list.itemAnimator = savedItemAnimator
                }
            }
        })
    }

    private val placeholderAdapter = PlaceholderAdapter()
    private val cheeseAdapter = CheeseAdapter()

    private var savedItemAnimator: RecyclerView.ItemAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        list = findViewById(R.id.list)
        setSupportActionBar(toolbar)
        EdgeToEdge.setUpRoot(findViewById(R.id.coordinator))
        EdgeToEdge.setUpAppBar(findViewById(R.id.app_bar), toolbar)
        EdgeToEdge.setUpScrollingContent(list)

        // Show the initial placeholders.
        // See the ViewHolder implementation for how to create the loading animation.
        list.adapter = placeholderAdapter
        viewModel.cheeses.observe(this) { cheeses ->
            if (list.adapter != cheeseAdapter) {
                list.adapter = cheeseAdapter
                savedItemAnimator = list.itemAnimator
                list.itemAnimator = null
                TransitionManager.beginDelayedTransition(list, fade)
            }
            cheeseAdapter.submitList(cheeses)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.loading, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                TransitionManager.beginDelayedTransition(list, fade)
                list.adapter = placeholderAdapter
                viewModel.refresh()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
