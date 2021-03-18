package com.erbe.materialthemebuilder.ui.component

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

/**
 * An adapter to display all [Component]s using their corresponding [ComponentViewHolder].
 */
class ComponentAdapter(
    private val listener: ComponentAdapterListener
) : ListAdapter<Component, ComponentViewHolder>(DIFF_CALLBACK) {

    interface ComponentAdapterListener {
        fun onShowBottomSheetClicked()
    }

    override fun getItemViewType(position: Int): Int = getItem(position).ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComponentViewHolder {
        return ComponentViewHolder.create(parent, viewType, listener)
    }

    override fun onBindViewHolder(holder: ComponentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Component>() {
            override fun areItemsTheSame(oldItem: Component, newItem: Component): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Component, newItem: Component): Boolean {
                return oldItem == newItem
            }
        }
    }
}
