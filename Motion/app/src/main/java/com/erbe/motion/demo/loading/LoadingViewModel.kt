package com.erbe.motion.demo.loading

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.erbe.motion.model.Cheese

class LoadingViewModel : ViewModel() {

    private var source: LiveData<PagedList<Cheese>>? = null
    private val _cheeses = MediatorLiveData<PagedList<Cheese>>()
    val cheeses: LiveData<PagedList<Cheese>> = _cheeses

    init {
        refresh()
    }

    fun refresh() {
        source?.let { _cheeses.removeSource(it) }
        val s = CheeseDataSource.toLiveData(pageSize = 15)
        source = s
        _cheeses.addSource(s) { _cheeses.postValue(it) }
    }
}
