package com.erbe.sunflower.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.erbe.sunflower.data.AppDatabase
import com.erbe.sunflower.data.GardenPlantingRepository
import com.erbe.sunflower.data.PlantRepository
import com.erbe.sunflower.utilities.getValue
import com.erbe.sunflower.utilities.testPlant
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import javax.inject.Inject

@HiltAndroidTest
class PlantDetailViewModelTest {

    private lateinit var appDatabase: AppDatabase
    private lateinit var viewModel: PlantDetailViewModel
    private val hiltRule = HiltAndroidRule(this)
    private val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val rule = RuleChain
        .outerRule(hiltRule)
        .around(instantTaskExecutorRule)

    @Inject
    lateinit var plantRepository: PlantRepository

    @Inject
    lateinit var gardenPlantRepository: GardenPlantingRepository

    @Before
    fun setUp() {
        hiltRule.inject()

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()

        viewModel = PlantDetailViewModel(plantRepository, gardenPlantRepository, testPlant.plantId)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }

    @Test
    @Throws(InterruptedException::class)
    fun testDefaultValues() {
        assertFalse(getValue(viewModel.isPlanted))
    }
}
