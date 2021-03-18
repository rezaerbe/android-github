package com.erbe.motion.demo.navfadethrough

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erbe.motion.model.Cheese

class CheeseCardViewModel : ViewModel() {

    val cheese: LiveData<Cheese> = MutableLiveData(Cheese.ALL.random())

}
