package com.erbe.motion.ui.demolist

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.erbe.motion.R
import com.erbe.motion.model.Demo

internal class DemoListAdapter(
    private val onDemoSelected: (demo: Demo) -> Unit
) : ListAdapter<Demo, DemoViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        return DemoViewHolder(parent).apply {
            itemView.setOnClickListener {
                onDemoSelected(getItem(adapterPosition))
            }
        }
    }

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Demo>() {

    override fun areItemsTheSame(oldItem: Demo, newItem: Demo): Boolean {
        return oldItem.packageName == newItem.packageName &&
                oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Demo, newItem: Demo): Boolean {
        return oldItem == newItem
    }
}

internal class DemoViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.demo_item, parent, false)
) {

    private val label: TextView = itemView.findViewById(R.id.label)
    private val description: TextView = itemView.findViewById(R.id.description)
    private val apis: List<TextView> = listOf(
        itemView.findViewById(R.id.api_1),
        itemView.findViewById(R.id.api_2),
        itemView.findViewById(R.id.api_3),
        itemView.findViewById(R.id.api_4),
        itemView.findViewById(R.id.api_5)
    )

    fun bind(demo: Demo) {
        label.text = demo.label
        description.text = demo.description
        description.isVisible = demo.description != null
        for (i in apis.indices) {
            if (demo.apis.size > i) {
                apis[i].run {
                    text = demo.apis[i]
                    isVisible = true
                }
            } else {
                apis[i].isVisible = false
            }
        }
    }
}
