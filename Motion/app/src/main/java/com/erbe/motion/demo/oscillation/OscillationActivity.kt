package com.erbe.motion.demo.oscillation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.erbe.motion.R
import com.erbe.motion.ui.EdgeToEdge

class OscillationActivity : AppCompatActivity() {

    private val viewModel: OscillationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.oscillation_activity)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val list: RecyclerView = findViewById(R.id.list)
        setSupportActionBar(toolbar)

        EdgeToEdge.setUpRoot(findViewById(R.id.root))
        EdgeToEdge.setUpAppBar(findViewById(R.id.app_bar), toolbar)
        EdgeToEdge.setUpScrollingContent(list)

        val adapter = CheeseAdapter()
        list.adapter = adapter
        // The adapter knows how to animate its items while the list is scrolled.
        list.addOnScrollListener(adapter.onScrollListener)
        list.edgeEffectFactory = adapter.edgeEffectFactory

        viewModel.cheeses.observe(this) { cheeses ->
            adapter.submitList(cheeses)
        }
    }
}
