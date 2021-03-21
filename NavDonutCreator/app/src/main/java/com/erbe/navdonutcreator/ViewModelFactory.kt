package com.erbe.navdonutcreator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.erbe.navdonutcreator.storage.DonutDao

class ViewModelFactory(private val donutDao: DonutDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DonutListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DonutListViewModel(donutDao) as T
        } else if (modelClass.isAssignableFrom(DonutEntryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DonutEntryViewModel(donutDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}