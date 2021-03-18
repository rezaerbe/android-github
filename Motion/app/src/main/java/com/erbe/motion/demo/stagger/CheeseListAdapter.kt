package com.erbe.motion.demo.stagger

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewGroupCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.erbe.motion.R
import com.erbe.motion.model.Cheese

class CheeseListAdapter : ListAdapter<Cheese, CheeseViewHolder>(Cheese.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseViewHolder {
        return CheeseViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CheeseViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater
        .from(parent.context)
        .inflate(R.layout.cheese_list_item, parent, false)
) {

    private val image: ImageView = itemView.findViewById(R.id.image)
    private val name: TextView = itemView.findViewById(R.id.name)

    init {
        ViewGroupCompat.setTransitionGroup(itemView as ViewGroup, true)
    }

    fun bind(cheese: Cheese) {
        Glide.with(image).load(cheese.image).transform(CircleCrop()).into(image)
        name.text = cheese.name
    }
}
