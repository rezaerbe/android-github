package com.erbe.materialthemebuilder.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.erbe.materialthemebuilder.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A simple Modal Bottom Sheet.
 */
class BottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    companion object {
        const val FRAGMENT_TAG = "bottom_sheet_fragment_tag"
    }
}
