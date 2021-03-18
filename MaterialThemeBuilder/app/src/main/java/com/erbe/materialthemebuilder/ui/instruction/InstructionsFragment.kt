package com.erbe.materialthemebuilder.ui.instruction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erbe.materialthemebuilder.App
import com.erbe.materialthemebuilder.R
import com.google.android.material.switchmaterial.SwitchMaterial

/**
 * Fragment to display static instructions text and in-app theming options.
 */
class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_instructions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val darkThemeSwitch: SwitchMaterial = view.findViewById(R.id.dark_theme_switch)
        val preferenceRepository = (requireActivity().application as App).preferenceRepository

        preferenceRepository.isDarkThemeLive.observe(this) { isDarkTheme ->
            isDarkTheme?.let { darkThemeSwitch.isChecked = it }
        }

        darkThemeSwitch.setOnCheckedChangeListener { _, checked ->
            preferenceRepository.isDarkTheme = checked
        }
    }
}
