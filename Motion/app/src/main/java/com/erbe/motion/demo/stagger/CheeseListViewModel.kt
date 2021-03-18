package com.erbe.motion.demo.stagger

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erbe.motion.model.Cheese
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CheeseListViewModel : ViewModel() {

    private val _cheeses = MutableLiveData<List<Cheese>>()
    val cheeses: LiveData<List<Cheese>> = _cheeses

    init {
        refresh()
    }

    fun empty() {
        _cheeses.value = emptyList()
    }

    fun refresh() {
        viewModelScope.launch {
            // Simulate a loading delay of database, filesystem, etc.
            delay(300L)
            _cheeses.value = Cheese.ALL
        }
    }
}
