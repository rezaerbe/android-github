package com.erbe.sunflower.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.erbe.sunflower.data.GardenPlantingRepository
import com.erbe.sunflower.data.PlantAndGardenPlantings
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GardenPlantingListViewModel @Inject internal constructor(
    gardenPlantingRepository: GardenPlantingRepository
) : ViewModel() {
    val plantAndGardenPlantings: LiveData<List<PlantAndGardenPlantings>> =
        gardenPlantingRepository.getPlantedGardens().asLiveData()
}
