package com.erbe.motion.demo.reorder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.erbe.motion.R
import com.erbe.motion.model.Cheese

class CheeseGridAdapter(
    private val onItemLongClick: (holder: RecyclerView.ViewHolder) -> Unit
) : ListAdapter<Cheese, CheeseViewHolder>(Cheese.DIFF_CALLBACK) {

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return getItem(position).id
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheeseViewHolder {
        return CheeseViewHolder(parent).apply {
            itemView.setOnLongClickListener {
                onItemLongClick(this)
                true
            }
            itemView.setOnClickListener { v ->
                val cheese = getItem(adapterPosition)
                val context = v.context
                Toast.makeText(
                    context,
                    context.getString(R.string.drag_hint, cheese.name),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onBindViewHolder(holder: CheeseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class CheeseViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.cheese_staggered_grid_item, parent, false)
) {

    private val constraintLayout: ConstraintLayout = itemView.findViewById(R.id.cheese)
    private val image: ImageView = itemView.findViewById(R.id.image)
    private val name: TextView = itemView.findViewById(R.id.name)
    private val constraintSet = ConstraintSet().apply { clone(constraintLayout) }

    fun bind(cheese: Cheese) {
        // The image loaded asynchronously, but the aspect ratio should be set synchronously.
        constraintSet.setDimensionRatio(R.id.image, "H,${cheese.imageWidth}:${cheese.imageHeight}")
        constraintSet.applyTo(constraintLayout)

        // Load the image.
        Glide.with(image).load(cheese.image).into(image)
        name.text = cheese.name
    }
}
