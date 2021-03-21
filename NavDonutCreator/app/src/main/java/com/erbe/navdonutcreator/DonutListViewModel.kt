package com.erbe.navdonutcreator

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erbe.navdonutcreator.storage.DonutDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This ViewModel is used to access the underlying data and to observe changes to it.
 */
class DonutListViewModel(private val donutDao: DonutDao) : ViewModel() {

    // Users of this ViewModel will observe changes to its donuts list to know when
    // to redisplay those changes
    val donuts: LiveData<List<Donut>> = donutDao.getAll()

    fun delete(donut: Donut) = viewModelScope.launch(Dispatchers.IO) {
        donutDao.delete(donut)
    }
}