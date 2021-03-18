package com.erbe.motion.demo.fabtransformation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erbe.motion.model.Cheese

class FabTransformationViewModel : ViewModel() {

    val cheeses: LiveData<List<Cheese>> = MutableLiveData(Cheese.ALL.shuffled().take(4))
}
