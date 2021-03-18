package com.erbe.materialthemebuilder

import android.app.Application
import android.content.Context
import com.erbe.materialthemebuilder.data.PreferenceRepository

class App : Application() {

    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        super.onCreate()
        preferenceRepository = PreferenceRepository(
          getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)
        )
    }

    companion object {
        const val DEFAULT_PREFERENCES = "default_preferences"
    }
}
