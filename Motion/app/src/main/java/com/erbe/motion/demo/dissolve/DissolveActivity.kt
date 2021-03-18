package com.erbe.motion.demo.dissolve

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.transition.TransitionManager
import com.erbe.motion.R
import com.erbe.motion.demo.FAST_OUT_SLOW_IN
import com.erbe.motion.ui.EdgeToEdge
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView

class DissolveActivity : AppCompatActivity() {

    private val viewModel: DissolveViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dissolve_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val image: ImageView = findViewById(R.id.image)
        val card: MaterialCardView = findViewById(R.id.card)
        val next: MaterialButton = findViewById(R.id.next)

        setSupportActionBar(toolbar)
        EdgeToEdge.setUpRoot(findViewById(R.id.root))
        EdgeToEdge.setUpAppBar(findViewById(R.id.app_bar), toolbar)
        EdgeToEdge.setUpScrollingContent(findViewById(R.id.content))

        // This is the transition we use for dissolve effect of the image view.
        val dissolve = Dissolve().apply {
            addTarget(image)
            duration = 200L
            interpolator = FAST_OUT_SLOW_IN
        }
        viewModel.image.observe(this) { resId ->
            // This delays the dissolve to be invoked at the next draw frame.
            TransitionManager.beginDelayedTransition(card, dissolve)
            // Here, we are simply changing the image shown on the image view. The animation is
            // handled by the transition API.
            image.setImageResource(resId)
        }

        card.setOnClickListener { viewModel.nextImage() }
        next.setOnClickListener { viewModel.nextImage() }
    }
}
