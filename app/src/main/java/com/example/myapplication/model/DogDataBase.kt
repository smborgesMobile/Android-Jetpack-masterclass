package com.example.myapplication.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DogBreed::class], version = 1)
abstract class DogDataBase : RoomDatabase() {
    abstract fun dogDao(): DogDao

    companion object {
        @Volatile
        private var instance: DogDataBase? = null
        private val LOCK = Any()

        open operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        //Create a dog data base.
        private fun buildDatabase(context: Context): DogDataBase {
            return Room.databaseBuilder(
                context.applicationContext,
                DogDataBase::class.java,
                "dogdatabase"
            ).build()
        }
    }
}