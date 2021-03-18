package com.erbe.materialthemebuilder.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.erbe.materialthemebuilder.R

/**
 * Fragment to hold a list of all [Component]s.
 */
class ComponentFragment : Fragment(), ComponentAdapter.ComponentAdapterListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceBundle: Bundle?) {
        super.onViewCreated(view, savedInstanceBundle)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        val adapter = ComponentAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        adapter.submitList(Component.values().toList())
    }

    override fun onShowBottomSheetClicked() {
        BottomSheetFragment().show(requireFragmentManager(), BottomSheetFragment.FRAGMENT_TAG)
    }
}
