package com.erbe.motion

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.commitNow
import com.erbe.motion.ui.EdgeToEdge
import com.erbe.motion.ui.demolist.DemoListFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Configure edge-to-edge display.
        EdgeToEdge.setUpRoot(findViewById(R.id.main))
        EdgeToEdge.setUpAppBar(findViewById(R.id.app_bar), toolbar)

        // Set up the fragment.
        if (savedInstanceState == null) {
            supportFragmentManager.commitNow {
                replace(R.id.container, DemoListFragment())
            }
        }
    }
}
