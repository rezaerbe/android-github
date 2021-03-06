package com.erbe.observability.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.erbe.observability.persistence.UserDao

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val dataSource: UserDao) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}