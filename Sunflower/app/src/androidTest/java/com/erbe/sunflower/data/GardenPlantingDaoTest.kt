package com.erbe.sunflower.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.platform.app.InstrumentationRegistry
import com.erbe.sunflower.utilities.testCalendar
import com.erbe.sunflower.utilities.testGardenPlanting
import com.erbe.sunflower.utilities.testPlant
import com.erbe.sunflower.utilities.testPlants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GardenPlantingDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var gardenPlantingDao: GardenPlantingDao
    private var testGardenPlantingId: Long = 0

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        gardenPlantingDao = database.gardenPlantingDao()

        database.plantDao().insertAll(testPlants)
        testGardenPlantingId = gardenPlantingDao.insertGardenPlanting(testGardenPlanting)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetGardenPlantings() = runBlocking {
        val gardenPlanting2 = GardenPlanting(
            testPlants[1].plantId,
            testCalendar,
            testCalendar
        ).also { it.gardenPlantingId = 2 }
        gardenPlantingDao.insertGardenPlanting(gardenPlanting2)
        assertThat(gardenPlantingDao.getGardenPlantings().first().size, equalTo(2))
    }

    @Test
    fun testDeleteGardenPlanting() = runBlocking {
        val gardenPlanting2 = GardenPlanting(
            testPlants[1].plantId,
            testCalendar,
            testCalendar
        ).also { it.gardenPlantingId = 2 }
        gardenPlantingDao.insertGardenPlanting(gardenPlanting2)
        assertThat(gardenPlantingDao.getGardenPlantings().first().size, equalTo(2))
        gardenPlantingDao.deleteGardenPlanting(gardenPlanting2)
        assertThat(gardenPlantingDao.getGardenPlantings().first().size, equalTo(1))
    }

    @Test
    fun testGetGardenPlantingForPlant() = runBlocking {
        assertTrue(gardenPlantingDao.isPlanted(testPlant.plantId).first())
    }

    @Test
    fun testGetGardenPlantingForPlant_notFound() = runBlocking {
        assertFalse(gardenPlantingDao.isPlanted(testPlants[2].plantId).first())
    }

    @Test
    fun testGetPlantAndGardenPlantings() = runBlocking {
        val plantAndGardenPlantings = gardenPlantingDao.getPlantedGardens().first()
        assertThat(plantAndGardenPlantings.size, equalTo(1))

        /**
         * Only the [testPlant] has been planted, and thus has an associated [GardenPlanting]
         */
        assertThat(plantAndGardenPlantings[0].plant, equalTo(testPlant))
        assertThat(plantAndGardenPlantings[0].gardenPlantings.size, equalTo(1))
        assertThat(plantAndGardenPlantings[0].gardenPlantings[0], equalTo(testGardenPlanting))
    }
}
