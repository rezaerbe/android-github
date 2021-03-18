package com.erbe.motion.demo.dissolve

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.erbe.motion.model.Cheese

class DissolveViewModel : ViewModel() {

    private val position = MutableLiveData(0)

    val image = position.map { p -> Cheese.IMAGES[p % Cheese.IMAGES.size] }

    fun nextImage() {
        position.value?.let { position.value = it + 1 }
    }
}
