package com.erbe.materialthemebuilder.ui.themesummary

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * Adapter to display [Subsystem]s using their corresponding [SubsystemViewHolder].
 */
class SubsystemAdapter : ListAdapter<Subsystem, SubsystemViewHolder>(DIFF_CALLBACK) {

    override fun getItemViewType(position: Int): Int = getItem(position).ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubsystemViewHolder {
        return SubsystemViewHolder.create(parent, viewType)
    }

    override fun onBindViewHolder(holder: SubsystemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Subsystem>() {
            override fun areItemsTheSame(oldItem: Subsystem, newItem: Subsystem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Subsystem, newItem: Subsystem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
