package com.erbe.motion.demo.sharedelement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erbe.motion.model.Cheese

class CheeseGridViewModel : ViewModel() {

    val cheeses: LiveData<List<Cheese>> = MutableLiveData(Cheese.ALL)

}
