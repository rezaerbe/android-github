package com.erbe.navdonutcreator.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.erbe.navdonutcreator.Donut

/**
 * The underlying database where information about the donuts is stored.
 */
@Database(entities = arrayOf(Donut::class), version = 1)
abstract class DonutDatabase : RoomDatabase() {

    abstract fun donutDao(): DonutDao

    companion object {
        @Volatile
        private var INSTANCE: DonutDatabase? = null

        fun getDatabase(context: Context): DonutDatabase {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    DonutDatabase::class.java,
                    "donut_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
