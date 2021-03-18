package com.erbe.navdonutcreator

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This class holds the data that we are tracking for each donut: its name, a description, and
 * a rating.
 */
@Entity
data class Donut(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val description: String = "",
    val rating: Int
)
