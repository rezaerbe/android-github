package com.erbe.motion.demo.sharedelement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erbe.motion.R
import com.erbe.motion.ui.EdgeToEdge

class SharedElementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.shared_element_activity)
        EdgeToEdge.setUpRoot(findViewById(R.id.nav_host))
    }
}
