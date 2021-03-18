package com.erbe.materialthemebuilder.ui

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.erbe.materialthemebuilder.R
import com.erbe.materialthemebuilder.ui.component.ComponentFragment
import com.erbe.materialthemebuilder.ui.instruction.InstructionsFragment
import com.erbe.materialthemebuilder.ui.themesummary.ThemeSummaryFragment

/**
 * View pager to show all tabbed destinations - Instructions, Theme Summary and Components.
 */
class MainViewPagerAdapter(
    private val context: Context,
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    enum class MainFragments(val titleRes: Int) {
        INSTRUCTIONS(R.string.tab_title_instructions),
        THEME_SUMMARY(R.string.tab_title_theme_summary),
        COMPONENTS(R.string.tab_title_components)
    }

    override fun getCount(): Int = MainFragments.values().size

    private fun getItemType(position: Int): MainFragments {
        return MainFragments.values()[position]
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.getString(getItemType(position).titleRes)
    }

    override fun getItem(position: Int): Fragment {
        return when (getItemType(position)) {
            MainFragments.INSTRUCTIONS -> InstructionsFragment()
            MainFragments.THEME_SUMMARY -> ThemeSummaryFragment()
            MainFragments.COMPONENTS -> ComponentFragment()
        }
    }
}
