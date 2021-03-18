package com.erbe.motion.demo.reorder

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.erbe.motion.model.Cheese

class ReorderViewModel : ViewModel() {

    private val _cheeses = MutableLiveData(Cheese.ALL.toMutableList())
    val cheeses = _cheeses.map { it.toList() }

    fun move(from: Int, to: Int) {
        _cheeses.value?.let { list ->
            val cheese = list.removeAt(from)
            list.add(to, cheese)
            _cheeses.value = list
        }
    }
}
