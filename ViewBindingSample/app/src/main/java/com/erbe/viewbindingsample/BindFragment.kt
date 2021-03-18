package com.erbe.viewbindingsample

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.erbe.viewbindingsample.R.string
import com.erbe.viewbindingsample.databinding.FragmentBlankBinding

/**
 * View Binding example with a fragment that uses the alternate constructor for inflation and
 * [onViewCreated] for binding.
 */
class BindFragment : Fragment(R.layout.fragment_blank) {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var fragmentBlankBinding: FragmentBlankBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentBlankBinding.bind(view)
        fragmentBlankBinding = binding
        binding.textViewFragment.text = getString(string.hello_from_vb_bindfragment)
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        fragmentBlankBinding = null
        super.onDestroyView()
    }
}
