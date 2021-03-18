package com.erbe.sunflower

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.erbe.sunflower.databinding.ActivityGardenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GardenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView<ActivityGardenBinding>(this, R.layout.activity_garden)
    }
}
