package com.erbe.motion.demo.navfadethrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewGroupCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Transition
import com.erbe.motion.R
import com.erbe.motion.demo.*
import com.erbe.motion.demo.sharedelement.MirrorView
import com.erbe.motion.demo.sharedelement.SharedFade
import com.google.android.material.appbar.CollapsingToolbarLayout

class CheeseArticleFragment : Fragment() {

    companion object {
        const val TRANSITION_NAME_BACKGROUND = "background"
        const val TRANSITION_NAME_CARD_CONTENT = "card_content"
        const val TRANSITION_NAME_ARTICLE_CONTENT = "article_content"
    }

    private val args: CheeseArticleFragmentArgs by navArgs()

    private val viewModel: CheeseArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // These are the shared element transitions.
        sharedElementEnterTransition =
            createSharedElementTransition(LARGE_EXPAND_DURATION, R.id.article_mirror)
        sharedElementReturnTransition =
            createSharedElementTransition(LARGE_COLLAPSE_DURATION, R.id.card_mirror)

        viewModel.cheeseId = args.cheeseId
    }

    private fun createSharedElementTransition(duration: Long, @IdRes noTransform: Int): Transition {
        return transitionTogether {
            this.duration = duration
            interpolator = FAST_OUT_SLOW_IN
            this += SharedFade()
            this += ChangeBounds()
            this += ChangeTransform()
                // The content is already transformed along with the parent. Exclude it.
                .excludeTarget(noTransform, true)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cheese_article_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val name: TextView = view.findViewById(R.id.name)
        val image: ImageView = view.findViewById(R.id.image)
        val scroll: NestedScrollView = view.findViewById(R.id.scroll)
        val content: LinearLayout = view.findViewById(R.id.content)

        val background: FrameLayout = view.findViewById(R.id.background)
        val coordinator: CoordinatorLayout = view.findViewById(R.id.coordinator)
        val mirror: MirrorView = view.findViewById(R.id.card_mirror)

        ViewCompat.setTransitionName(background, TRANSITION_NAME_BACKGROUND)
        ViewCompat.setTransitionName(coordinator, TRANSITION_NAME_ARTICLE_CONTENT)
        ViewCompat.setTransitionName(mirror, TRANSITION_NAME_CARD_CONTENT)
        ViewGroupCompat.setTransitionGroup(coordinator, true)

        // Adjust the edge-to-edge display.
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            toolbar.updateLayoutParams<CollapsingToolbarLayout.LayoutParams> {
                topMargin = insets.systemWindowInsetTop
            }
            // The collapsed app bar gets taller by the toolbar's top margin. The CoordinatorLayout
            // has to have a bottom margin of the same amount so that the scrolling content is
            // completely visible.
            scroll.updateLayoutParams<CoordinatorLayout.LayoutParams> {
                bottomMargin = insets.systemWindowInsetTop
            }
            content.updatePadding(
                left = insets.systemWindowInsetLeft,
                right = insets.systemWindowInsetRight,
                bottom = insets.systemWindowInsetBottom
            )
            insets
        }

        viewModel.cheese.observe(viewLifecycleOwner) { cheese ->
            if (cheese != null) {
                name.text = cheese.name
                image.setImageResource(cheese.image)
            }
        }

        toolbar.setNavigationOnClickListener { v ->
            v.findNavController().popBackStack()
        }
    }
}
