package com.erbe.viewbindingsample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erbe.viewbindingsample.databinding.FragmentBlankBinding

/**
 * View Binding example with a fragment that uses the traditional constructor and [onCreateView] for
 * inflation.
 */
class InflateFragment : Fragment() {

    // Scoped to the lifecycle of the fragment's view (between onCreateView and onDestroyView)
    private var fragmentBlankBinding: FragmentBlankBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentBlankBinding.inflate(inflater, container, false)
        fragmentBlankBinding = binding
        binding.textViewFragment.text = getString(R.string.hello_from_vb_inflatefragment)
        return binding.root
    }

    override fun onDestroyView() {
        // Consider not storing the binding instance in a field, if not needed.
        fragmentBlankBinding = null
        super.onDestroyView()
    }
}
