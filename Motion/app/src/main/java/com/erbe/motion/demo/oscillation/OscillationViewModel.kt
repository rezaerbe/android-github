package com.erbe.motion.demo.oscillation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erbe.motion.model.Cheese

class OscillationViewModel : ViewModel() {

    val cheeses: LiveData<List<Cheese>> = MutableLiveData(Cheese.ALL.filter {
        it.name.length < 10
    }.shuffled().take(15))
}
