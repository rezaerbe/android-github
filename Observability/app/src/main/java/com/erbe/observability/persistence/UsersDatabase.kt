package com.erbe.observability.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The Room database that contains the Users table
 */
@Database(entities = [User::class], version = 1)
abstract class UsersDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

    companion object {

        @Volatile
        private var INSTANCE: UsersDatabase? = null

        fun getInstance(context: Context): UsersDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                UsersDatabase::class.java, "Sample.db"
            )
                .build()
    }
}