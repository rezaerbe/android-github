package com.erbe.motion.demo.navfadethrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.ViewGroupCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.transition.Fade
import com.erbe.motion.R
import com.erbe.motion.demo.FAST_OUT_LINEAR_IN
import com.erbe.motion.demo.LARGE_COLLAPSE_DURATION
import com.erbe.motion.demo.LARGE_EXPAND_DURATION
import com.erbe.motion.demo.LINEAR_OUT_SLOW_IN
import com.erbe.motion.demo.sharedelement.MirrorView
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.card.MaterialCardView

class CheeseCardFragment : Fragment() {

    private val viewModel: CheeseCardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Fade(Fade.OUT).apply {
            duration = LARGE_EXPAND_DURATION / 2
            interpolator = FAST_OUT_LINEAR_IN
        }
        reenterTransition = Fade(Fade.IN).apply {
            duration = LARGE_COLLAPSE_DURATION / 2
            startDelay = LARGE_COLLAPSE_DURATION / 2
            interpolator = LINEAR_OUT_SLOW_IN
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.cheese_card_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val toolbar: Toolbar = view.findViewById(R.id.toolbar)
        val content: FrameLayout = view.findViewById(R.id.content)
        val card: MaterialCardView = view.findViewById(R.id.card)
        val cardContent: ConstraintLayout = view.findViewById(R.id.card_content)
        val image: ImageView = view.findViewById(R.id.image)
        val name: TextView = view.findViewById(R.id.name)
        val mirror: MirrorView = view.findViewById(R.id.article_mirror)

        ViewCompat.setOnApplyWindowInsetsListener(view.parent as View) { _, insets ->
            toolbar.updateLayoutParams<AppBarLayout.LayoutParams> {
                topMargin = insets.systemWindowInsetTop
            }
            content.updatePadding(
                left = insets.systemWindowInsetLeft,
                right = insets.systemWindowInsetRight,
                bottom = insets.systemWindowInsetBottom
            )
            insets
        }

        ViewCompat.setTransitionName(card, "card")
        ViewCompat.setTransitionName(cardContent, "card_content")
        ViewCompat.setTransitionName(mirror, "article")
        ViewGroupCompat.setTransitionGroup(cardContent, true)

        viewModel.cheese.observe(viewLifecycleOwner) { cheese ->
            name.text = cheese.name
            image.setImageResource(cheese.image)
        }

        card.setOnClickListener { v ->
            val cheese = viewModel.cheese.value ?: return@setOnClickListener
            v.findNavController().navigate(
                CheeseCardFragmentDirections.actionArticle(cheese.id),
                FragmentNavigatorExtras(
                    card to CheeseArticleFragment.TRANSITION_NAME_BACKGROUND,
                    cardContent to CheeseArticleFragment.TRANSITION_NAME_CARD_CONTENT,
                    mirror to CheeseArticleFragment.TRANSITION_NAME_ARTICLE_CONTENT
                )
            )
        }
    }
}
