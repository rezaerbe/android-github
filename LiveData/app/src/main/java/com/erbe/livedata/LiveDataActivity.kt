package com.erbe.livedata

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.erbe.livedata.databinding.ActivityLivedataBinding

class LiveDataActivity : AppCompatActivity() {

    // Obtain ViewModel
    private val viewmodel: LiveDataViewModel by viewModels { LiveDataVMFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Obtain binding object using the Data Binding library
        val binding = DataBindingUtil.setContentView<ActivityLivedataBinding>(
            this, R.layout.activity_livedata
        )

        // Set the LifecycleOwner to be able to observe LiveData objects
        binding.lifecycleOwner = this

        // Bind ViewModel
        binding.viewmodel = viewmodel
    }
}
