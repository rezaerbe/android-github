package com.erbe.materialthemebuilder.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.erbe.materialthemebuilder.App
import com.erbe.materialthemebuilder.R
import com.erbe.materialthemebuilder.ui.component.ComponentFragment
import com.erbe.materialthemebuilder.ui.instruction.InstructionsFragment
import com.erbe.materialthemebuilder.ui.themesummary.ThemeSummaryFragment
import com.google.android.material.tabs.TabLayout

/**
 * Single activity which contains the [MainViewPagerAdapter] that shows the [InstructionsFragment],
 * [ThemeSummaryFragment] and [ComponentFragment].
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tab_layout)

        tabLayout.setupWithViewPager(viewPager)
        val adapter = MainViewPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = adapter

        (application as App).preferenceRepository
            .nightModeLive.observe(this) { nightMode ->
                nightMode?.let { delegate.localNightMode = it }
            }
    }
}
