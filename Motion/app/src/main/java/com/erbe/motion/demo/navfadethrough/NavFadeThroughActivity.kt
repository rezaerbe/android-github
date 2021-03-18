package com.erbe.motion.demo.navfadethrough

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erbe.motion.R
import com.erbe.motion.ui.EdgeToEdge

class NavFadeThroughActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nav_fade_through_activity)
        EdgeToEdge.setUpRoot(findViewById(R.id.nav_host))
    }
}
