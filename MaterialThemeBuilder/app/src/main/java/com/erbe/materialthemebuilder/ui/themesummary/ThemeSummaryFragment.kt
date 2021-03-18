package com.erbe.materialthemebuilder.ui.themesummary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erbe.materialthemebuilder.R

/**
 * Fragment to display a list of subsystems that show the values of this app's theme.
 */
class ThemeSummaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_theme_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceBundle: Bundle?) {
        super.onViewCreated(view, savedInstanceBundle)

        val adapter = SubsystemAdapter()
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.submitList(Subsystem.values().toList())
    }
}
