package com.erbe.materialthemebuilder.ui.themesummary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.erbe.materialthemebuilder.R
import com.erbe.materialthemebuilder.ui.themesummary.Subsystem.*

/**
 * Sealed class to define all [RecyclerView.ViewHolder]s used to display [Subsystem]s.
 */
sealed class SubsystemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(subsystem: Subsystem) {
        // Override in subclass if needed.
    }

    class ColorSubsystemViewHolder(
        parent: ViewGroup
    ) : SubsystemViewHolder(inflate(parent, R.layout.subsystem_color))

    class TypeSubsystemViewHolder(
        parent: ViewGroup
    ) : SubsystemViewHolder(inflate(parent, R.layout.subsystem_type))

    class ShapeSubsystemViewHolder(
        parent: ViewGroup
    ) : SubsystemViewHolder(inflate(parent, R.layout.subsystem_shape))

    companion object {
        fun create(parent: ViewGroup, viewType: Int): SubsystemViewHolder {
            return when (Subsystem.values()[viewType]) {
                COLOR -> ColorSubsystemViewHolder(parent)
                TYPE -> TypeSubsystemViewHolder(parent)
                SHAPE -> ShapeSubsystemViewHolder(parent)
            }
        }

        private fun inflate(parent: ViewGroup, layout: Int): View {
            return LayoutInflater.from(parent.context).inflate(layout, parent, false)
        }
    }
}
