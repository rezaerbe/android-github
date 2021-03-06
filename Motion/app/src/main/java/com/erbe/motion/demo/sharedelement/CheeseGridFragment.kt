package com.erbe.motion.demo.sharedelement

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Explode
import androidx.transition.Slide
import com.erbe.motion.R
import com.erbe.motion.demo.*
import com.erbe.motion.widget.SpaceDecoration
import com.google.android.material.appbar.AppBarLayout
import java.util.concurrent.TimeUnit

/**
 * Shows a grid list of cheeses.
 */
class CheeseGridFragment : Fragment() {

    private val viewModel: CheeseGridViewModel by viewModels()

    private val adapter = CheeseGridAdapter(onReadyToTransition = {
        startPostponedEnterTransition()
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is the transition to be used for non-shared elements when we are opening the detail
        // screen.
        exitTransition = transitionTogether {
            duration = LARGE_EXPAND_DURATION / 2
            interpolator = FAST_OUT_LINEAR_IN
            // The app bar.
            this += Slide(Gravity.TOP).apply {
                mode = Slide.MODE_OUT
                addTarget(R.id.app_bar)
            }
            // The grid items.
            this += Explode().apply {
                mode = Explode.MODE_OUT
                excludeTarget(R.id.app_bar, true)
            }
        }

        // This is the transition to be used for non-shared elements when we are return back from
        // the detail screen.
        reenterTransition = transitionTogether {
            duration = LARGE_COLLAPSE_DURATION / 2
            interpolator = LINEAR_OUT_SLOW_IN
            // The app bar.
            this += Slide(Gravity.TOP).apply {
                mode = Slide.MODE_IN
                addTarget(R.id.app_bar)
            }
            // The grid items.
            this += Explode().apply {
                // The grid items should start imploding after the app bar is in.
                startDelay = LARGE_COLLAPSE_DURATION / 2
                mode = Explode.MODE_IN
                excludeTarget(R.id.app_bar, true)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cheese_grid_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            adapter.restoreInstanceState(savedInstanceState)
        }
        if (adapter.expectsTransition) {
            // We are transitioning back from CheeseDetailFragment.
            // Postpone the transition animation until the destination item is ready.
            postponeEnterTransition(500L, TimeUnit.MILLISECONDS)
        }

        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val grid: RecyclerView = view.findViewById(R.id.grid)

        // Adjust the edge-to-edge display.
        val gridPadding = resources.getDimensionPixelSize(R.dimen.spacing_tiny)
        ViewCompat.setOnApplyWindowInsetsListener(view.parent as View) { _, insets ->
            toolbar.updateLayoutParams<AppBarLayout.LayoutParams> {
                topMargin = insets.systemWindowInsetTop
            }
            grid.updatePadding(
                left = gridPadding + insets.systemWindowInsetLeft,
                right = gridPadding + insets.systemWindowInsetRight,
                bottom = gridPadding + insets.systemWindowInsetBottom
            )
            insets
        }

        grid.addItemDecoration(
            SpaceDecoration(resources.getDimensionPixelSize(R.dimen.spacing_tiny))
        )

        grid.adapter = adapter
        viewModel.cheeses.observe(viewLifecycleOwner) { cheeses ->
            adapter.submitList(cheeses)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        adapter.saveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }
}
