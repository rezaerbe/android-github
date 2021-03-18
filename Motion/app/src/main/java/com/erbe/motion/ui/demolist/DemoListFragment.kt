package com.erbe.motion.ui.demolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.erbe.motion.R
import com.erbe.motion.ui.EdgeToEdge

class DemoListFragment : Fragment() {

    private val viewModel: DemoListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.demo_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val demoList: RecyclerView = view.findViewById(R.id.demo_list)
        EdgeToEdge.setUpScrollingContent(demoList)

        val adapter = DemoListAdapter { demo ->
            requireActivity().startActivity(demo.toIntent())
        }
        demoList.adapter = adapter
        viewModel.demos.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }
}
