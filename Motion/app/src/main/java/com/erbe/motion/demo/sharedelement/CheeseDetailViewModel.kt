package com.erbe.motion.demo.sharedelement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erbe.motion.model.Cheese

class CheeseDetailViewModel : ViewModel() {

    private val _cheese = MutableLiveData<Cheese?>()
    val cheese: LiveData<Cheese?> = _cheese

    var cheeseId: Long?
        get() = _cheese.value?.id
        set(value) {
            _cheese.value = Cheese.ALL.find { it.id == value }
        }

}
